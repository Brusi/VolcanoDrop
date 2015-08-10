package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.WorldRenderer;

public class FlameEffect extends Effect {

	public FlameEffect(Vector2 position) {
		super(Assets.flamethrowerFlame.size * WorldRenderer.FRAME_TIME, position);
	}
	
	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
}
