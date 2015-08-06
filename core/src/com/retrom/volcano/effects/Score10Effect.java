package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;

public class Score10Effect extends ScoreNumEffect {

	public Score10Effect(Vector2 position) {
		super(position);
	}

	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
}
