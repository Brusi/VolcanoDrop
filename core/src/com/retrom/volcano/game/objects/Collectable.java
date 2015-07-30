package com.retrom.volcano.game.objects;

import com.retrom.volcano.game.World;


public class Collectable extends DynamicGameObject {
	
	public interface Handler {
		public void handle();		
	}
	
	public static final float SIZE = 30f;
	private static final float GRAVITY_RATIO = 0.7f;
	
	public static final int STATUS_FALLING = 1;
	public static final int STATUS_IDLE = 2;
	public static final int STATUS_CRUSHED = 3;
	public static final int STATUS_TAKEN = 4;
	
	public int status;
	private final Handler handler_;
	
	

	public Collectable(float x, float y, Handler handler) {
		super(x, y, SIZE, SIZE);
		handler_ = handler;
		status = STATUS_FALLING;
	}
	
	public void update(float deltaTime) {
		if (status == STATUS_FALLING) {
			velocity.add(0, World.gravity.y * deltaTime * GRAVITY_RATIO);
			bounds.y += velocity.y * deltaTime;
			bounds.getCenter(position);
		}
	}

	public void handle() {
		status = STATUS_TAKEN;
		handler_.handle();
	}
}
