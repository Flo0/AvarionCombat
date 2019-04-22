package com.avarioncraft.AvarionCombat.util;

public class RoundUtil {
	
	public static float unsafeRound(float number, int points) {
		double multi = Math.pow(10, points);
		return (float) (((int)(number * multi)) / multi);
	}
	
	public static double unsafeRound(double number, int points) {
		double multi = Math.pow(10, points);
		return (((int)(number * multi)) / multi);
	}
	
}
