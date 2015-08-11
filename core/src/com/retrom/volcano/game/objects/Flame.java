package com.retrom.volcano.game.objects;

public class Flame extends Enemy {
	
	private static final float duration = 3f;

	public Flame(float x, float y) {
		super(x, y, 30, 30);
		System.out.println("Flame created");
	}
	
	@Override
	protected void childSpecificUpdating() {
		if (stateTime() >= duration) {
			state_ = Enemy.STATE_DONE;
		}
	}

	@Override
	public <T> T accept(Visitor<T> v) {
		return v.visit(this);
	}
}
