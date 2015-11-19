package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;

public class PlayerMagnetGlow extends OneFrameEffect {

	private float tint_;

	public PlayerMagnetGlow(Vector2 position) {
		super(Assets.playerMagnetGlow, 6000f, position);
	}
	
	@Override
	public float getTint() {
		return Math.min(1, stateTime() * 4);
	}
}
