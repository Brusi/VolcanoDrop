package com.retrom.volcano.game.objects;

public class TopFireball extends Enemy {
	
	private static final float SIZE = 30f;
	private static final float FIREBALL_SPEED = -1200f;
	public static final float DISTANCE_FROM_TOP = 100f;
	public static final float PREPARATION_DELAY = 2.6f;
	
	/**
	 * Get the x value of the wall which fits the given column.
	 */
	private static float xOfCol(int col) {
		return (col - Wall.NUM_COLS/2) * Wall.SIZE + Wall.SIZE/2; 
	}
	

	public TopFireball(int col, float y) {
		super(xOfCol(col), y, SIZE, SIZE);
		float x = xOfCol(col);
		System.out.println("x="+x);
		velocity.y = FIREBALL_SPEED;
	}

	@Override
	public <T> T accept(Visitor<T> v) {
		return v.visit(this);
	}


	public void explode() {
		if (state() == Enemy.STATE_ACTIVE) {
			setState(Enemy.STATE_DONE);
		}
	}
}
