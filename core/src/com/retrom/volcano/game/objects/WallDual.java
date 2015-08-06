package com.retrom.volcano.game.objects;

import com.retrom.volcano.assets.Assets;

public class WallDual extends Wall {
	
	/**
	 * Get the x value of the wall which fits the given column.
	 */
	private static float xOfColDual(int col) {
		return (col - NUM_COLS/2) * SIZE + SIZE; 
	}

	public WallDual(int col, float y) {
		super(xOfColDual(col), y, Wall.SIZE*2, Wall.SIZE, col, rand.nextInt(Assets.walls2.size));
	}
	
	@Override
	public boolean isDual() {
		return true;
	}
}
