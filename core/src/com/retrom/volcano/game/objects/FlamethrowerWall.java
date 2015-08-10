package com.retrom.volcano.game.objects;

public class FlamethrowerWall extends Wall {
	
	public boolean flameAdded = false;

	
	private static float xOfCol(int col) {
		return (col - Wall.NUM_COLS/2) * SIZE + SIZE/2; 
	}
	
	public FlamethrowerWall(int col, float y) {
		super(xOfCol(col), y, Wall.SIZE, Wall.SIZE, col, -1);
	}
}
