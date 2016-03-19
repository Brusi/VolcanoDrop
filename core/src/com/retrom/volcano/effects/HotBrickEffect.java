package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;

public class HotBrickEffect extends OneFrameEffect {
	
	private static final float DURATION = 2;

	public HotBrickEffect(Vector2 position) {
		super(Assets.hotBrick, DURATION, position);
	}
	
	@Override
	public float getTint() {
		return Math.min(1, Math.max(0, DURATION - stateTime_));
	}
}
