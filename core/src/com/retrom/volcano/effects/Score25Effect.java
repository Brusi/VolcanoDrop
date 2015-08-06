package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;

public class Score25Effect extends ScoreNumEffect {

	public Score25Effect(Vector2 position) {
		super(position);
	}

	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
}
