package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;

public abstract class Effect {
	public static final int STATE_ACTIVE = 1;
	public static final int STATE_DONE = 2;
	
	public Vector2 position_;
	public Vector2 velocity_;
	
	private float stateTime_ = 0;
	protected int state_;
	
	private final float duration_;
	
	Effect(float duration, Vector2 position) {
		this(duration, position, Vector2.Zero);
	}
	
	Effect(float duration, Vector2 position, Vector2 velocity) {
		duration_ = duration;
		position_ = position;
		velocity_ = velocity;
		state_ = STATE_ACTIVE;
	}
	
	public void update(float deltaTime) {
		position_.x += velocity_.x * deltaTime;
		position_.y += velocity_.y * deltaTime;
		
		stateTime_ += deltaTime;
		if (stateTime_ > duration_) {
			state_ = STATE_DONE;
		}
		
		childSpecificUpdating(deltaTime);
	}

	protected void childSpecificUpdating(float deltaTime) {
		// Will be overriden by children.
	}
	
	abstract public <T> T accept(EffectVisitor<T> v);

	public float stateTime() {
		return stateTime_;
	}

	public int state() {
		return state_;
	}
	
	public float duration() {
		return duration_;
	}
	
	public float getRotation() {
		return 0;
	}

	public float getScale() {
		return 1f;
	}
	
	public void destroy() {
		state_ = STATE_DONE;
	}
}