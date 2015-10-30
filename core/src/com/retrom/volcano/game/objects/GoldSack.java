package com.retrom.volcano.game.objects;

import com.retrom.volcano.game.World;

public class GoldSack extends DynamicGameObject {
	
	public static final int STATE_FALLING = 1;
	public static final int STATE_GROUND = 2;
	public static final int STATE_HOVER = 3;
	public static final int STATE_DONE = 4;
	
	public static float WIDTH = 50;
	public static float HEIGHT = 32;
	
	private static final float GRAVITY_RATIO = 0.3f;
	
	private int state_;
	private float stateTime_;

	public GoldSack(float x, float y) {
		super(x, y, WIDTH, HEIGHT);
		setState(STATE_FALLING);
	}


	public void setState(int state) {
		state_ = state;
		stateTime_ = 0;
	}


	public void update(float deltaTime) {
		stateTime_ += deltaTime;
		if (state_ == STATE_FALLING) {
			velocity.add(0, World.gravity.y * deltaTime * GRAVITY_RATIO);
			bounds.y += velocity.y * deltaTime;
			bounds.getCenter(position);
			return;
		}
	}
	
	public int state() {
		return state_;
	}
	
	public float stateTime() {
		return stateTime_;
	}
}
