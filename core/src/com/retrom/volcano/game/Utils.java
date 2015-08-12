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
}
