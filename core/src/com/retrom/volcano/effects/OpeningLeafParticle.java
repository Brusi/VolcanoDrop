package com.retrom.volcano.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.game.World;

public class OpeningLeafParticle extends Particle {
	
	private static final float SIDE_AMP = 100;
	private static final float BOTTOM_AMP = 100;

	private static float DURATION = 3f;
	
	private Vector2 base_pos_ = new Vector2();
	private float phase_ = Utils.random2Range((float) Math.PI);
	private float theta_amp_ = (float) (Math.PI * Utils.randomRange(0.25f, 0.1f));;
	private float theta_;
	private float rotation_;
	private float rotationSpeed_;
	private final float scale_;
	private final boolean flip_;
	
	public OpeningLeafParticle(Vector2 position) {
		super(getParticleSprite(), DURATION, position.cpy(), getInitVel());
		base_pos_ = position.cpy();
		rotation_ = (float) (Math.random() * 360);
		rotationSpeed_ = 0;//Utils.randomRange(400f, 1000f) * (Utils.randomBool() ? 1 : -1);
		scale_ = Utils.randomRange(0.5f, 1f);
		flip_ = Utils.randomBool();
		velocity_.x /= scale_;
		velocity_.y /= scale_;
		rotationSpeed_ /= scale_;
	}

	private static Sprite getParticleSprite() {
		return Assets.openingRootsParticles.get(Utils.randomInt(2)+3);
	}

	private static Vector2 getInitVel() {
		Vector2 vec = new Vector2(0, Utils.randomRange(-20, -50));
		return vec;
	}
	
	@Override
	protected void childSpecificUpdating(float deltaTime) {
		theta_ = (float) (Math.cos(stateTime_ * 2+phase_) * theta_amp_);
		
//		velocity_.y += World.gravity.y * deltaTime * 0.5;
		rotation_ += rotationSpeed_ * deltaTime;
		base_pos_.y += velocity_.y * deltaTime;
		
		position_.x = (float) (base_pos_.x + Math.sin(theta_) * SIDE_AMP);
		position_.y = (float) (base_pos_.y - Math.cos(theta_) * BOTTOM_AMP) + BOTTOM_AMP;
	}
	
	@Override
	public float getRotation() {
		return rotation_ + Utils.radToDeg(theta_);
	}
	
	@Override
	public float getScale() {
		return scale_;
	}
	
	@Override
	public boolean getFlip() {
		return flip_;
	}
	
	@Override
	public float getAlpha() {
		return Math.min(stateTime_ * 2, Math.min(1, Math.max(0, DURATION - stateTime_)));
	}
}
