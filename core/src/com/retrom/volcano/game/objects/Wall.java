package com.retrom.volcano.game.objects;

import java.util.Random;

import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.World;
import com.retrom.volcano.game.WorldRenderer;

public class Wall extends DynamicGameObject {
	
	public static final float SIZE = 80f;
	protected static final int NUM_COLS = 6;
	private static final float GRAVITY_RATIO = 0.4f;
	
	public static int STATUS_ACTIVE = 1;
	public static int STATUS_INACTIVE = 2;
	public static int STATUS_GONE = 3;
	
	private int status_;
	private float stateTime_;
	
	public final int graphic_;
	
	private final int col_;
	
	protected static final Random rand = new Random();
	
	public Wall(float x, float y, float width, float height, int col, int graphic) {
		super(x, y, width, height);
		col_ = col;
		graphic_ = graphic;
		setStatus(STATUS_ACTIVE);
	}
	public void updateStateTime(float deltaTime) {
		stateTime_ += deltaTime;
	}

	public void update(float deltaTime) {
		if (status() == STATUS_INACTIVE) {
			return;
		}
		velocity.add(0, World.gravity.y * deltaTime * GRAVITY_RATIO);
		bounds.y += velocity.y * deltaTime;
		bounds.getCenter(position);
	}

	public int col() {
		return col_;
	}

	public int status() {
		return status_;
	}

	public void setStatus(int status) {
		if (status == status_) {
			return;
		}
		status_ = status;
		stateTime_ = 0;
	}

	public float stateTime() {
		return stateTime_;
	}
	public boolean isDual() {
		return false;
	}
}
