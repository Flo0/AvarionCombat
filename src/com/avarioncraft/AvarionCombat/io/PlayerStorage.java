package com.avarioncraft.AvarionCombat.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import com.avarioncraft.AvarionCombat.buffs.CombatBuff;
import com.avarioncraft.AvarionCombat.buffs.StatBuff;
import com.avarioncraft.AvarionCombat.core.AvarionCombat;
import com.avarioncraft.AvarionCombat.data.CombatPlayer;
import com.avarioncraft.AvarionCombat.skills.Skill;
import com.avarioncraft.AvarionCombat.skills.SkillRegistration;
import com.avarioncraft.AvarionCombat.skills.TriggerType;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Sets;

import net.crytec.shaded.hikaricp.hikari.HikariConfig;
import net.crytec.shaded.hikaricp.hikari.HikariDataSource;
import net.crytec.shaded.org.apache.lang3.EnumUtils;
import net.crytec.shaded.org.apache.lang3.StringUtils;
import net.crytec.taskchain.TaskChain;

public class PlayerStorage {
	
	private final AvarionCombat plugin;
	private HikariDataSource dataSource;
	
	private final HashBiMap<Class<? extends Skill>, Integer> skillIDs = HashBiMap.create(SkillRegistration.values().length);
	private final HashBiMap<TriggerType, Integer> triggerIDs = HashBiMap.create(TriggerType.values().length);
	
	public PlayerStorage(AvarionCombat plugin) {
		this.plugin = plugin;
		
		plugin.getLogger().info("Connecting to database...");
		this.setupPool();
	}
	
	private void setupPool() {
		HikariConfig config = new HikariConfig();
		config.setPoolName("AvarionCombat");
		config.setDriverClassName("org.sqlite.JDBC");
		config.setJdbcUrl("jdbc:sqlite:plugins/" + plugin.getDataFolder().getName() + "/players.db");
		config.setConnectionTestQuery("SELECT 1");
		config.setMaxLifetime(60000);
		config.setIdleTimeout(45000);
		config.setMaximumPoolSize(5);
		dataSource = new HikariDataSource(config);

		TaskChain<?> chain = plugin.getTaskFactory().newChain();
		
		chain.asyncFirst( () -> {
			try {
				Statement stmt = this.getConnection().createStatement();
				plugin.getLogger().info("Creating database...");
				stmt.executeUpdate(this.readFile("skillIndex"));
				stmt.executeUpdate(this.readFile("triggerIndex"));
				stmt.executeUpdate(this.readFile("players"));
				stmt.executeBatch();
				return true;
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		})
		.sync( () -> Bukkit.getLogger().info("Before Registration"))
		.async( () -> initializeSkillRegistry())
		.async( () -> initializeTriggerRegistry())
		.sync( () -> handleReload())
		.execute( () -> plugin.getLogger().info("Database Setup complete!"));
	}
	
	private void handleReload() {
		for (Player cur : Bukkit.getOnlinePlayers()) {
			CombatPlayer.init(cur);

			CombatPlayer cp = CombatPlayer.of(cur);
			cp.calculateFull();
			AvarionCombat.getPlugin().getStorage().loadPlayer(cp);
			
			StatBuff.statBuffs.put(cur, Sets.newHashSet());
			CombatBuff.combatBuffs.put(cur, Sets.newHashSet());

			cur.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100.0);
			cur.setHealthScale(20);
			cur.setHealthScaled(true);
		}
	}
	
	/**
	 * This closes a previously used connection from getConnection() and returns
	 * it into the connection pool.
	 * 
	 * @param conn
	 *            - The connection, can be null
	 * @param ps
	 *            - The PreparedStatement, can be null
	 * @param res
	 *            - The ResultSet, can be null
	 */
	public void close(Connection conn, PreparedStatement ps, ResultSet res) {
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException ignored) {
			}
		if (ps != null)
			try {
				ps.close();
			} catch (SQLException ignored) {
			}
		if (res != null)
			try {
				res.close();
			} catch (SQLException ignored) {
			}
	}

	/**
	 * Gets a free connection from the pool.
	 * 
	 * @return A new {@link Connection}
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	/**
	 * Closes the connection pool and prevents further connections to the
	 * database.
	 */
	public void closePool() {
		if (dataSource != null && !dataSource.isClosed()) {
			dataSource.close();
		}
	}

	/**
	 * Read a plain SQL file from the resources / SQL folder inside the .jar
	 * <br>
	 * and returns the SQL Query as a single string.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private String readFile(String fileName) throws IOException {
		InputStream in = plugin.getResource("sql/" + fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
			in.close();
		}
	}
	
	/**
	 * Initialize skill registry, stores every registered skill into the
	 * database an assigns each skill a unique ID (Integer)
	 */
	private void initializeSkillRegistry() {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = this.getConnection();
			String sql = "INSERT INTO skills(skill) VALUES(?) ON CONFLICT(skill) DO UPDATE SET skill = ?; ";

			for (SkillRegistration skill : SkillRegistration.values()) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, skill.toString());
				stmt.setString(2, skill.toString());
				stmt.executeUpdate();
				stmt.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(conn, stmt, null);
			this.loadSkillIDs();
		}
	}
	
	/**
	 * Initialize skill registry, stores every registered skill into the
	 * database an assigns each skill a unique ID (Integer)
	 */
	private void initializeTriggerRegistry() {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = this.getConnection();
			String sql = "INSERT INTO trigger(trigger) VALUES(?) ON CONFLICT(trigger) DO UPDATE SET trigger = ?; ";

			for (TriggerType trigger : TriggerType.values()) {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, trigger.toString());
				stmt.setString(2, trigger.toString());
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(conn, stmt, null);
			this.loadTriggerIDs();
		}
	}

	/**
	 * Load all registered skills with their unique IDs from the database and
	 * cache them into a map
	 */
	private void loadSkillIDs() {
		Connection conn = null;
		ResultSet res = null;
		
		try {
			conn = this.getConnection();
			String sql = "SELECT * FROM skills";
			Statement stmt = conn.createStatement();
			
			res = stmt.executeQuery(sql);

			while (res.next()) {
				int id = res.getInt("id");
				String s = res.getString("skill");
				
				if (EnumUtils.isValidEnum(SkillRegistration.class, s)) {
					this.skillIDs.put(SkillRegistration.valueOf(s).getClazz(), id);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(conn, null, res);
		}
	}
	
	private void loadTriggerIDs() {
		Connection conn = null;
		ResultSet res = null;
		
		try {
			conn = this.getConnection();
			String sql = "SELECT * FROM trigger";
			Statement stmt = conn.createStatement();
			
			res = stmt.executeQuery(sql);

			while (res.next()) {
				int id = res.getInt("id");
				String s = res.getString("trigger");
				
				if (EnumUtils.isValidEnum(TriggerType.class, s)) {
					this.triggerIDs.put(TriggerType.valueOf(s), id);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close(conn, null, res);
		}
	}
	
	
	/**
	 * Load a {@link CombatPlayer} player and restore all settings.
	 * 
	 * @param player
	 */
	public void loadPlayer(CombatPlayer player) {
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
			
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet res = null;
			
			try {
				conn = this.getConnection();
				String sql = "SELECT * FROM players WHERE uuid = ?;";
				
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, player.getPlayer().getUniqueId().toString());
				
				res = stmt.executeQuery();
				
				if (res.next()) {
					
					String[] skills = res.getString("skills").split(";");
					
					for (String serialized : skills) {
						String[] data = serialized.split("=");
						Class<? extends Skill> skill = this.skillIDs.inverse().get(Integer.valueOf(data[0]));
						if (skill == null) continue;
						
						player.getSkillEquip().getSkillOfClass(skill).addLevel(Integer.valueOf(data[1]));
					}
					
					String[] equipedSkills = res.getString("equip").split(";");
					
					for (String serialized : equipedSkills) {
						String[] data = serialized.split("=");
						
						TriggerType type = this.triggerIDs.inverse().get(Integer.valueOf(data[0]));
						
						int skillID = Integer.valueOf(data[1]);
						Skill skill = (skillID == -1) ? null : player.getSkillEquip().getSkillOfClass(this.skillIDs.inverse().get(skillID));
						player.getSkillEquip().getPlayerSkills().put(type, skill);
					}
					
					
					
					player.getLevelSet().addSkillPoints(res.getInt("levelpoints"));
					player.getLevelSet().serializeExp(res.getLong("xp"));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.close(conn, null, res);
			}
		});
	}
	
	/**
	 * Save a {@link CombatPlayer} into the database.
	 * 
	 * @param CombatPlayer
	 */
	public void savePlayer(CombatPlayer player) {
			String serialized = player.getSkillEquip().getSkillSet().values().stream()
					.map(skill -> skillIDs.get(skill.getClass()).toString() + "=" + skill.getLevel())
					.collect(Collectors.joining(";"));
			
			StringBuilder builder = new StringBuilder();
			
			player.getSkillEquip().getPlayerSkills().forEach( (t,s) -> {
				
				builder.append(this.triggerIDs.get(t));
				builder.append("=");
				if (s != null) {
				builder.append(this.skillIDs.get(s.getClass()));
				} else {
					builder.append("-1");
				}
				builder.append(";");
				
			});
			
			String serializedEquipment = StringUtils.chop(builder.toString());
			
			Connection conn = null;
			PreparedStatement stmt = null;
			try {
				conn = this.getConnection();
				String sql = "INSERT INTO players(uuid, skills, xp, equip, levelpoints) "
						+ "VALUES(?, ?, ?, ?, ?) "
						+ "ON CONFLICT(uuid) DO UPDATE SET skills = ?, xp = ?, equip = ?, levelpoints = ? WHERE uuid = ? ;";

				stmt = conn.prepareStatement(sql);
				stmt.setString(1, player.getPlayer().getUniqueId().toString());
				stmt.setString(2, serialized);
				stmt.setLong(3, player.getLevelSet().getExp());
				stmt.setString(4, serializedEquipment);
				stmt.setInt(5, player.getLevelSet().getSkillPoints());

				stmt.setString(6, serialized);
				stmt.setLong(7, player.getLevelSet().getExp());
				stmt.setString(8, serializedEquipment);
				stmt.setInt(9, player.getLevelSet().getSkillPoints());
				stmt.setString(10, player.getPlayer().getUniqueId().toString());

				stmt.executeUpdate();
				stmt.close();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.close(conn, stmt, null);
			}
			
	}
}