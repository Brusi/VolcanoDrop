package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.WorldRenderer;

public class PlayerExplodeEffect extends Effect {
	
	private static final float DURATION = Assets.playerExplode.size * WorldRenderer.FRAME_TIME;

	public PlayerExplodeEffect(Vector2 position) {
		super(DURATION, position);
	}

	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
}
