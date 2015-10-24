package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;

public class PlayerShieldEffect extends Effect {
	
	private static final float INF_DURATION = 600f;

	public PlayerShieldEffect(Vector2 position) {
		super(INF_DURATION, position);
	}

	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
}
