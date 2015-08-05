package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;

public abstract class ScoreNumEffect extends Effect {
	
	private static final Vector2 SCORE_NUM_VEL = new Vector2(0, 100f);
	private static final float DURATION = 1f;

	ScoreNumEffect(Vector2 position) {
		super(DURATION, position, SCORE_NUM_VEL);
	}
}
