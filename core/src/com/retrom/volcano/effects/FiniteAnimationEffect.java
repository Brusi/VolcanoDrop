package com.retrom.volcano.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.game.WorldRenderer;

public class FiniteAnimationEffect extends Effect {
	
	private Array<Sprite> animation_;

	public FiniteAnimationEffect(Array<Sprite> animation_, Vector2 position) {
		super(getDuration(animation_), position);
		this.animation_ = animation_;
	}

	private static float getDuration(Array<Sprite> animation) {
		return animation.size * WorldRenderer.FRAME_TIME;
	}

	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
	
	public Array<Sprite> getAnimation() {
		return animation_;
	}

}
