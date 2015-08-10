package com.retrom.volcano.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;

public class FlameGlowEffect extends OneFrameEffect {
	
	private static float DURATION = 3.5f;

	public FlameGlowEffect(Vector2 position) {
		super(Assets.burningWallGlow, DURATION, position);
	}
	
	@Override
	public Sprite sprite() {
		Sprite $ = super.sprite();
		setTint($, getGlowAlpha());
		return $;
	}

	private void setTint(Sprite $, float glowAlpha) {
		$.setColor(glowAlpha, glowAlpha, glowAlpha, glowAlpha);
	}
	
	@Override
	public float getScale() {
		return 0.75f;
	}

	private float getGlowAlpha() {
		if (stateTime() < 0.4f) {
			return stateTime() / 0.4f;
		}
		if (stateTime() < 2.7f) {
			return 1f;
		}
		return Math.max(0, 1 - (stateTime() - 2.7f)*3);
	}
}
