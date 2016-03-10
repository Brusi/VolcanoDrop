package com.retrom.volcano.game.objects;

import com.retrom.volcano.assets.SoundAssets;
import com.retrom.volcano.game.World;

public class GoldSack extends DynamicGameObject {
	
	public static final int STATE_FALLING = 1;
	public static final int STATE_GROUND = 2;
	public static final int STATE_PUMP = 3;
	public static final int STATE_EMPTY = 4;
	public static final int STATE_DONE = 5;
	
	public static final float MIN_TIME_BETWEEN_PUMPS = 0.6f;
	
	public static final float EMPTY_ANIMATION_TIME = 4.8f;
	public static float WIDTH = 50;
	public static float HEIGHT = 32;
	
	private static final float GRAVITY_RATIO = 0.3f;
	
	private int state_;
	private float stateTime_;
	
	private int coinsLeft;
	
	private boolean shouldFlare_ = false;
	private float timeToLastFlare = 0;

	public GoldSack(float x, float y, int numCoins) {
		super(x, y, WIDTH, HEIGHT);
		setState(STATE_FALLING);
		coinsLeft = numCoins;
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
		
		if (state_ == STATE_PUMP && stateTime_ > MIN_TIME_BETWEEN_PUMPS) {
			state_ = STATE_GROUND;
			stateTime_ = 100f;
		}
		
		if (state_ == STATE_GROUND && coinsLeft <= 0) {
			setState(STATE_EMPTY);
			SoundAssets.playSound(SoundAssets.coinSackEnd);
			return;
		}
		
		if (state_ == STATE_GROUND  && stateTime() > 0.8f || state_ == STATE_PUMP) {
			timeToLastFlare -= deltaTime;
			if (timeToLastFlare < 0) {
				shouldFlare_ = true;
				timeToLastFlare = (float)Math.random() * 0.4f + 0.8f;
			}
		}
	}
	
	public int state() {
		return state_;
	}
	
	public float stateTime() {
		return stateTime_;
	}

	public void pump() {
		setState(STATE_PUMP);
		coinsLeft--;
	}


	public boolean shouldFlare() {
		if (shouldFlare_) {
			shouldFlare_ = false;
			return true;
		}
		return false;
	}

	public boolean hasCoinsLeft() {
		return coinsLeft > 0;
	}
	
	// TODO: replace with a better implementation and/or change loot chances.
	static public Collectable.Type randomSackCoin() {
		return Collectable.Type.values()[(int) Math.floor(Math.random() * Collectable.Type.DIAMOND_GREEN.ordinal())]; 
	}
}
