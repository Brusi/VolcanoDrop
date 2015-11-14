package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;

public class PlayerMagnetEffect extends Effect {
	
	private static final float INF_DURATION = 6000f;
	
	public PlayerMagnetEffect(Vector2 position) {
		super(INF_DURATION, position);
	}

	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
}
