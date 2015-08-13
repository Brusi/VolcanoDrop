package com.retrom.volcano.game.objects;

public class SideFireball extends Enemy {
	
	private static final float SIZE = 15f;
	private static final float FIREBALL_SPEED = -1200f;
	public static final float DISTANCE_FROM_TOP = 100f;
	public static final float PREPARATION_DELAY = 2.6f;
	
	private boolean side_;
	
	public SideFireball(float x, float y, boolean side) {
		super(x, y, SIZE, SIZE);
		side_ = side;
		velocity.x = FIREBALL_SPEED * (side == Spitter.LEFT ? -1 : 1);
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

	public boolean side() {
		return side_;
	}
}
