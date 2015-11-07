package com.retrom.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;

public class Hub {
	
	private static final float PADDING = 20;

	float stateTime_ = 0;
	
	private int score_;

	public Hub() {
	}
	
	public void update(float deltaTime) {
		stateTime_ += deltaTime;
	}
	
	public void setScore(int score) {
		if (score == score_) {
			return;
		}
		stateTime_ = 0;
		score_ = score;
	}

	public void render(SpriteBatch batch, Camera cam, boolean isPaused) {
		float COIN_X_POS = - cam.viewportWidth / 2 + PADDING;
		float COIN_Y_POS = cam.viewportHeight / 2 - Assets.scoreIcon.getHeight() - PADDING; 
		batch.draw(Assets.scoreIcon, COIN_X_POS, COIN_Y_POS);
		
		float PAUSE_X_POS = cam.viewportWidth / 2 - Assets.pauseButton.getWidth();
		float PAUSE_Y_POS = cam.viewportHeight / 2 - Assets.pauseButton.getHeight();
		Sprite buttonSprite = isPaused ? Assets.pauseButtonClicked : Assets.pauseButton; 
		batch.draw(buttonSprite, PAUSE_X_POS, PAUSE_Y_POS);
	}
}
