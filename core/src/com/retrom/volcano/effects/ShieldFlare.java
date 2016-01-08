package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;

public class ShieldFlare extends OneFrameEffect {

	private final static float DURATION = 200f;
	
	private PlayerShieldEffect shieldEffect;
	
	public ShieldFlare(PlayerShieldEffect shieldEffect) {
		super(Assets.shieldFlare, DURATION, new Vector2());
		this.shieldEffect = shieldEffect;
	}
	
	@Override
	protected void childSpecificUpdating(float deltaTime) {
		if (shieldEffect.state() == Effect.STATE_DONE) {
			this.state_ = Effect.STATE_DONE;
		}
		
		position_.x = shieldEffect.position_.x + 25;
		position_.y = shieldEffect.position_.y + 50;
	}
	
	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
	
	@Override
	public float getTint() {
		switch(shieldEffect.shieldState()) {
		case DIE:
			return Math.min(1, 1 - shieldEffect.stateTime() * 2.5f);
		case START:
			if (stateTime_ <= 0.4f) { 
				return Math.min(1, shieldEffect.stateTime() * 7.5f);
			}
		case HIT:
		case MIDDLE:
			return (float) ((Math.sin(this.stateTime() * 4) + 1) / 2 * 0.3 + 0.7);
		default:
			return 1;
		}
	}
	
	
}