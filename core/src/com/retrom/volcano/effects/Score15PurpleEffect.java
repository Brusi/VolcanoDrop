package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;

public class Score15PurpleEffect extends ScoreNumEffect {

	public Score15PurpleEffect(Vector2 position) {
		super(position);
	}

	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
}
