package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;

public class FireballStartEffect extends FiniteAnimationEffect {
	
	public final float originalY; 

	public FireballStartEffect(Vector2 position) {
		super(Assets.fireballStartEffect, position);
		originalY = position.y;
	}
	
	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
}
