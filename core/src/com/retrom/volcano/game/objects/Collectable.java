package com.retrom.volcano.game.objects;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.game.World;


public class Collectable extends DynamicGameObject {
	
	public static final float SIZE = 30f;
	private static final float GRAVITY_RATIO = 0.7f;
	private static final float MAGNETIZED_FORCE = 1200f;
	
	public static final int STATUS_FALLING = 1;
	public static final int STATUS_IDLE = 2;
	public static final int STATUS_CRUSHED = 3;
	public static final int STATUS_TAKEN = 4;
	public static final int STATUS_MAGNETIZED = 5;
	private static final float MAGNETIZED_FRICTION = 0.54716f;
	
	// temporary vector, defined static to prevent allocations.
	private static Vector2 magnetDir = new Vector2();  
	
	private int state_;
	private float stateTime_;
	
	public enum Type {
		COIN_1_1, COIN_1_2,
		COIN_2_1, COIN_2_2, COIN_2_3, 
		COIN_3_1, COIN_3_2, COIN_3_3, 
		COIN_4_1, COIN_4_2, COIN_4_3, 
		COIN_5_1, COIN_5_2, COIN_5_3, COIN_5_4, 
		
		POWERUP_MAGNET;
	}
	
	public final Type type;
	private List<Rectangle> obstacles_;

	public Collectable(float x, float y, Type type) {
		super(x, y, SIZE, SIZE);
		this.type = type;
		setState(STATUS_FALLING);
	}
	
	public void setObstacles(List<Rectangle> rects) {
		this.obstacles_ = rects;
	}
	
	public void update(float deltaTime) {
		stateTime_ += deltaTime;
		
		if (state() == STATUS_FALLING) {
			velocity.add(0, World.gravity.y * deltaTime * GRAVITY_RATIO);
		}
		if (state() == STATUS_MAGNETIZED) {
			// If touched by both top and bottom => is crushed.
			boolean topTouched = false, bottomTouched = false;
			bounds.y += velocity.y * deltaTime;
			for (Rectangle rect : obstacles_) {
				if (bounds.overlaps(rect)) {
					if (bounds.y + bounds.height / 2 > rect.y + rect.height / 2) {
						bounds.y = rect.y + rect.height;
						bottomTouched = true;
					} else {
						bounds.y = rect.y - bounds.height;
						topTouched = true;
					}
					velocity.y = 0;
				}
			}
			if (topTouched && bottomTouched) {
				setState(STATUS_CRUSHED);
				System.out.println("crushed while magnetized");
				return;
			}
			
			bounds.x += velocity.x * deltaTime;
			for (Rectangle rect : obstacles_) {
				if (bounds.overlaps(rect)) {
					if (bounds.x + bounds.width / 2 > rect.x + rect.width / 2) {
						bounds.x = rect.x + rect.width;
					} else
						bounds.x = rect.x - bounds.width;
					velocity.x = 0;
				}
			}
		} else {
			bounds.x += velocity.x * deltaTime;
			bounds.y += velocity.y * deltaTime;
		}
		
		bounds.getCenter(position);
	}

	public void magnetTo(Vector2 playerPos, float deltaTime) {
		setState(STATUS_MAGNETIZED);
		magnetDir.x = playerPos.x - position.x;
		magnetDir.y = playerPos.y - position.y;
		magnetDir.nor();
		velocity.x += magnetDir.x * MAGNETIZED_FORCE * deltaTime;
		velocity.y += magnetDir.y * MAGNETIZED_FORCE * deltaTime;
		
		velocity.x *= Math.pow(MAGNETIZED_FRICTION, deltaTime);
		velocity.y *= Math.pow(MAGNETIZED_FRICTION, deltaTime);
	}

	public int state() {
		return state_;
	}

	public void setState(int state) {
		if (state_ == state) {
			return;
		}
		state_ = state;
		stateTime_ = 0f;
	}

	public float stateTime() {
		return stateTime_;
	}

	public boolean isPowerup() {
		return type == Type.POWERUP_MAGNET; // TODO handle other types.
	}
}
