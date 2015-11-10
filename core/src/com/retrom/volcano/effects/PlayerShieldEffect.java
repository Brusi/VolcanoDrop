package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;

public class PlayerShieldEffect extends Effect {
	
	private static final float INF_DURATION = 600f;
	
	public enum ShieldState {
		START,
		MIDDLE,
		HIT,
		DIE,
	}
	
	private ShieldState shieldState_ = ShieldState.START;

	public PlayerShieldEffect(Vector2 position) {
		super(INF_DURATION, position);
	}

	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}

	public ShieldState shieldState() {
		return shieldState_;
	}

	private void setShieldState(ShieldState shieldState) {
		this.shieldState_ = shieldState;
		stateTime_ = 0;
	}
	
	public void hit() {
		if (shieldState_ != ShieldState.DIE) {
			setShieldState(ShieldState.HIT);
		}
	}
	
	public void die() {
		if (shieldState_ != ShieldState.DIE) {
			setShieldState(ShieldState.DIE);
		}
	}

	public void renew() {
		if (shieldState_ == ShieldState.DIE) {
			setShieldState(ShieldState.START);
		}
	}
}
