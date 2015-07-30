package com.retrom.volcano.game.objects;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.game.World;


public class Collectable extends DynamicGameObject {
	
	public static final float SIZE = 30f;
	private static final float GRAVITY_RATIO = 0.7f;
	private static final float MAGNETIZED_VELOCITY = 400f;
	
	public static final int STATUS_FALLING = 1;
	public static final int STATUS_IDLE = 2;
	public static final int STATUS_CRUSHED = 3;
	public static final int STATUS_TAKEN = 4;
	public static final int STATUS_MAGNETIZED = 5;
	
	public int status;
	
	public enum Type {
		COIN3_1, COIN5_4, POWERUP_MAGNET
	}
	
	public final Type type;
	private List<Rectangle> obstacles_;

	public Collectable(float x, float y, Type type) {
		super(x, y, SIZE, SIZE);
		this.type = type;
		status = STATUS_FALLING;
	}
	
	public void setObstacles(List<Rectangle> rects) {
		this.obstacles_ = rects;
	}
	
	public void update(float deltaTime) {
		if (status == STATUS_FALLING) {
			velocity.add(0, World.gravity.y * deltaTime * GRAVITY_RATIO);
		}
		if (status == STATUS_MAGNETIZED) {
			bounds.y += velocity.y * deltaTime;
			for (Rectangle rect : obstacles_) {
				if (bounds.overlaps(rect)) {
					if (bounds.y + bounds.height / 2 > rect.y + rect.height / 2) {
						bounds.y = rect.y + rect.height;
					} else {
						bounds.y = rect.y - bounds.height;
					}
					velocity.y = 0;
				}
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

	public void magnetTo(Vector2 playerPos) {
		status = STATUS_MAGNETIZED;
		velocity.x = playerPos.x - position.x;
		velocity.y = playerPos.y - position.y;
		velocity.nor();
		velocity.x *= MAGNETIZED_VELOCITY;
		velocity.y *= MAGNETIZED_VELOCITY;
	}
}
