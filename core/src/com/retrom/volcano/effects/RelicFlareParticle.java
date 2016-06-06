package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.game.World;

public class RelicFlareParticle extends Particle {
	
	private static float SPEED = 150f;
	private static float MAX_ROTATION = 1600f;

	private float rotation_;
	private float rotationSpeed_;
	
	private final float gravity_ratio = Utils.randomRange(0.25f, 0.4f);
	
	private static float getDuration() {
		return Utils.randomRange(0.25f, 1f);
	}
	
	private final float scale_ = Utils.randomRange(0.7f, 1f);
	private final float original_y;
	
	public RelicFlareParticle(Vector2 position) {
		super(Assets.sackFlare, getDuration(), position.cpy(), getInitVel());
		original_y = position.y - 70;
		rotation_ = (float) (Math.random() * 360);
		rotationSpeed_ = (float) (Math.random() * MAX_ROTATION - MAX_ROTATION / 2);
		this.position_.x += Utils.random2Range(5);
	}
	
	private static Vector2 getInitVel() {
		Vector2 vec = Utils.randomDir45Up();
		float scaleSpeed = Utils.randomRange(0.3f, 1);
		vec.x *= SPEED * scaleSpeed;
		vec.y *= SPEED * scaleSpeed * 2;
		return vec;
	}
	
	@Override
	protected void childSpecificUpdating(float deltaTime) {
		velocity_.y += World.gravity.y * deltaTime * gravity_ratio;
		rotation_ += rotationSpeed_ * deltaTime;
	}
	
	@Override
	public float getRotation() {
		return rotation_;
	}
	
	@Override
	public float getScale() {
//		if (stateTime() < 0.2) {
//			return (stateTime() * 5);
//		}
		if (stateTime_ > duration() - 0.2f) {
			float scale = (duration() - stateTime_) / 0.2f; 
			return Math.min(1, Math.max(0, scale)) * scale_;
		}
		return 1 * scale_;
	}

}
