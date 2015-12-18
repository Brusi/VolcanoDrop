package com.retrom.volcano.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.game.World;

public class WallBreakParticle extends Particle {
	
	private static float DURATION = 3f;
	
	private static float SPEED = 600f;
	private static float MAX_ROTATION = 1600f;

	private float rotation_;
	private float rotationSpeed_;
	private final float scale_;
	private final boolean flip_;
	
	public WallBreakParticle(Array<Sprite> sprites, Vector2 position) {
		super(sprites.random(), DURATION, position.cpy(), getInitVel());
		rotation_ = (float) (Math.random() * 360);
		rotationSpeed_ = Utils.randomRange(400f, 1000f) * (Utils.randomBool() ? 1 : -1);
		scale_ = Utils.randomRange(0.5f, 1f);
		flip_ = Utils.randomBool();
		velocity_.x /= scale_;
		velocity_.y /= scale_;
		rotationSpeed_ /= scale_;
	}

	private static Vector2 getInitVel() {
		Vector2 vec = Utils.randomDirOnlyUp();
		vec.x *= SPEED * Utils.randomRange(0.5f, 1f);
		vec.y *= SPEED * Utils.randomRange(0.5f, 1f);
		return vec;
	}
	
	@Override
	protected void childSpecificUpdating(float deltaTime) {
		velocity_.y += World.gravity.y * deltaTime * 0.5;
		rotation_ += rotationSpeed_ * deltaTime;
		
		if (position_.x > 240) {
			velocity_.x = -Math.abs(velocity_.x);
		} else if (position_.x < -240) {
			velocity_.x = Math.abs(velocity_.x);
		}
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
