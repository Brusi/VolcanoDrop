package com.retrom.volcano.game.objects;

public class Spitter extends Enemy {
	
	public static boolean LEFT = false;
	public static boolean RIGHT = true;
	
	private static float DURATION = 8f;
	private boolean side_;

	public Spitter(float x, float y, boolean side) {
		super(x, y, 1, 1);
		this.side_ = side;
	}

	@Override
	public <T> T accept(Visitor<T> v) {
		return v.visit(this);
	}
	
	@Override
	protected void childSpecificUpdating() {
		if (stateTime() > DURATION) {
			this.state_ = Enemy.STATE_DONE;
		}
	}

	public boolean side() {
		return side_;
	}

}
