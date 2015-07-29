package com.retrom.volcano.game.objects;

import com.retrom.volcano.game.World;


public class Coin extends DynamicGameObject {
	
	public static final float SIZE = 30f;
	private static final float GRAVITY_RATIO = 0.7f;
	
	public static final int STATUS_FALLING = 1;
	public static final int STATUS_IDLE = 2;
	public static final int STATUS_CRUSHED = 3;
	public static final int STATUS_TAKEN = 4;
	public int status;
	

	public Coin(float x, float y) {
		super(x, y, SIZE, SIZE);
		status = STATUS_FALLING;
	}
	
	public void update(float deltaTime) {
		if (status == STATUS_FALLING) {
			velocity.add(0, World.gravity.y * deltaTime * GRAVITY_RATIO);
			bounds.y += velocity.y * deltaTime;
			bounds.getCenter(position);
		}
	}
}
