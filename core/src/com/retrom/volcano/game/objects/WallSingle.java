package com.retrom.volcano.game.objects;

import com.retrom.volcano.assets.Assets;

public class WallSingle extends Wall {

	/**
	 * Get the x value of the wall which fits the given column.
	 */
	private static float xOfCol(int col) {
		return (col - Wall.NUM_COLS/2) * SIZE + SIZE/2; 
	}
	
	public WallSingle(int col, float y) {
		super(xOfCol(col), y, Wall.SIZE, Wall.SIZE, col, rand.nextInt(Assets.walls1.size));
	}
}
