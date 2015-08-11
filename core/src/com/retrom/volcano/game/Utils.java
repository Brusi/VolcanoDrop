package com.retrom.volcano.game;

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
}
