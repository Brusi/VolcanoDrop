package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;

public abstract class ScoreNumEffect extends Effect {
	
	private static final Vector2 SCORE_NUM_VEL = new Vector2(0, 100f);
	private static final float DURATION = 1f;

	ScoreNumEffect(Vector2 position) {
		super(DURATION, position, SCORE_NUM_VEL);
	}
	
	public float getAlpha() {
		return Math.min(1, 3 * (1f - stateTime() / duration()));
//		return 1f - stateTime() / duration();
	}
	
	public float getScale() {
		if (stateTime() < duration() / 8) {
			return 8 * stateTime() / duration() * 1.2f;
		}
		if (stateTime() < duration() / 4) {
			return (1 - (stateTime() - duration() / 8) / (duration() / 8)) * 0.2f + 1f; 
		}
		return 1f;
	}
}
