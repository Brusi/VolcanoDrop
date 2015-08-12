package com.retrom.volcano.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.game.World;

public class CoinBreakParticle extends Particle {
	
	private static float DURATION = 3f;
	
	private static float SPEED = 600f;
	private static float MAX_ROTATION = 1600f;

	private float rotation_;
	private float rotationSpeed_;
	
	private float scaele;
	

	public CoinBreakParticle(Array<Sprite> sprites, Vector2 position) {
		super(sprites.random(), DURATION, position.cpy(), getInitVel());
		rotation_ = (float) (Math.random() * 360);
		rotationSpeed_ = (float) (Math.random() * MAX_ROTATION - MAX_ROTATION / 2);
	}

	private static Vector2 getInitVel() {
		Vector2 vec = Utils.randomDirOnlyUp();
		vec.x *= SPEED * Math.random();
		vec.y *= SPEED * Math.random();
		return vec;
	}
	
	@Override
	protected void childSpecificUpdating(float deltaTime) {
		velocity_.y += World.gravity.y * deltaTime * 0.5;
		rotation_ += rotationSpeed_ * deltaTime;
	}
	
	@Override
	public float getRotation() {
		return rotation_;
	}
	
	@Override
	public float getScale() {
		if (stateTime() < 0.2) {
			return (stateTime() * 5);
		}
		return Math.min(1, Math.max(0, 1.2f - stateTime() * 1.5f));
	}

}
