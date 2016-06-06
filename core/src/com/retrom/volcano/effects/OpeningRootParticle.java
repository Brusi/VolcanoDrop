package com.retrom.volcano.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.game.World;

public class OpeningRootParticle extends Particle {
	
	private static float DURATION = 3f;
	
	private float rotation_;
	private float rotationSpeed_;
	private final float scale_;
	private final boolean flip_;

	private boolean floorHit_ = false;
	
	public OpeningRootParticle(Vector2 position, float speed) {
		super(getParticleSprite(), DURATION, position.cpy(), getInitVel(speed));
		rotation_ = (float) (Math.random() * 360);
		rotationSpeed_ = Utils.randomRange(400f, 1000f) * (Utils.randomBool() ? 1 : -1);
		scale_ = Utils.randomRange(0.5f, 1f);
		flip_ = Utils.randomBool();
		velocity_.x /= scale_;
		velocity_.y /= scale_;
		rotationSpeed_ /= scale_;
	}

	private static Sprite getParticleSprite() {
		return Assets.openingRootsParticles.get(Utils.randomInt(3));
	}

	private static Vector2 getInitVel(float speed) {
		Vector2 vec = Utils.randomDirOnlyUp();
		vec.x *= speed * Utils.randomRange(0.5f, 1f);
		vec.y *= speed * Utils.randomRange(0.5f, 1f);
		return vec;
	}
	
	@Override
	protected void childSpecificUpdating(float deltaTime) {
		velocity_.y += World.gravity.y * deltaTime * 0.5;
		rotation_ += rotationSpeed_ * deltaTime;
		
		checkFloorHit(deltaTime);
	}
	
	private void checkFloorHit(float deltaTime) {
		if (floorHit_) {
			return;
		}
		if (this.position_.y <= 0) {
			hitFloor(deltaTime);
			return;
		}
	}
	
	private void hitFloor(float deltaTime) {
		this.position_.y -= this.velocity_.y * deltaTime / 2;
		floorHit_ = true;
		this.velocity_.y = -this.velocity_.y / 3;
		
	}

	@Override
	public float getRotation() {
		return rotation_;
	}
	
	@Override
	public float getScale() {
		return scale_;
	}
	
	@Override
	public boolean getFlip() {
		return flip_;
	}
}
