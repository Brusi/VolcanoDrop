package com.retrom.volcano.effects;

import com.retrom.volcano.game.objects.Enemy;

public class FireballAnimationEffect extends Effect {
	
	private static float DURATION = 5f;
	private final Enemy fireball_;
	
	public static final int DOWN = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	private int direction_;

	public FireballAnimationEffect(Enemy fireball, int direction) {
		super(DURATION, fireball.position);
		this.fireball_ = fireball;
		this.direction_ = direction;
	}
	
	@Override
	protected void childSpecificUpdating(float deltaTime) {
		if (fireball_.state() == Enemy.STATE_DONE) {
			state_ = Effect.STATE_DONE;
		}
	}
	
	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
	
	@Override
	public float getRotation() {
		switch(direction_) {
		case DOWN:
			return 0;
		case LEFT:
			return 90f;
		case RIGHT:
			return -90f;
		}
		return 45;
	}
	
	@Override
	public float getScale() {
		switch(direction_) {
		case DOWN:
			return 1;
		case LEFT:
		case RIGHT:
			return 0.65f;
		}
		return 45;
	}
}
