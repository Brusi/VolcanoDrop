package com.retrom.volcano.game;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.game.objects.Wall;

public class Utils {

	/**
	 * Get the x value of the wall which fits the given column.
	 */
	public static float xOfCol(int col) {
		return (col - Wall.NUM_COLS / 2) * Wall.SIZE + Wall.SIZE / 2;
	}

	public static float dualXOfCol(int col) {
		return (col - Wall.NUM_COLS/2) * Wall.SIZE + Wall.SIZE;
	}
	
	public static Vector2 randomDir() {
		double angle = Math.random() * 2 * Math.PI;
		return new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
	}

	public static Vector2 randomDirOnlyUp() {
		Vector2 vec = randomDir();
		vec.y = Math.abs(vec.y);
		return vec;
	}
	
	public static Vector2 randomDir45Up() {
		double angle = Utils.randomRange((float) Math.PI / 4, 3 * (float) Math.PI / 4);
		return new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
	}
	
	// Returns a random float between -limit and +limit.
	public static float random2Range(float limit) {
		return randomRange(-limit, limit); 
	}
	
	// Returns a random float between bottom and top.
	public static float randomRange(float bottom, float top) {
		return (float) (bottom + Math.random() * (top - bottom)); 
	}
	
	// Returns a random boolean.
	public static boolean randomBool() {
		return Math.random() > 0.5;
	}
	
	public static int randomInt(int x) {
		return (int) Math.floor(Math.random() * x);
	}
	
	public static void shuffleArr(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			int j = randomInt(arr.length);
			{
				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}
	}
	
	// Returns an array { 0, 1, 2, ..., length }
	public static int[] rangeArr(int length) {
		int[] arr = new int[length];
		for (int i=0; i < arr.length; i++) {
			arr[i] = i;
		}
		return arr;
	}
	
	public static int[] shuffledRangeArr(int length) {
		int[] arr = rangeArr(length);
		shuffleArr(arr);
		return arr;
	}
}
