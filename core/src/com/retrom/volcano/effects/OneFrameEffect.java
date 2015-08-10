package com.retrom.volcano.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class OneFrameEffect extends Effect {
	
	private final Sprite sprite_;

	public OneFrameEffect(Sprite sprite, float duration, Vector2 position) {
		super(duration, position);
		sprite_ = sprite;
	}

	public OneFrameEffect(Sprite sprite, float duration, Vector2 position, Vector2 velocity) {
		super(duration, position, velocity);
		sprite_ = sprite;
	}

	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}

	public Sprite sprite() {
		return sprite_;
	}

}
