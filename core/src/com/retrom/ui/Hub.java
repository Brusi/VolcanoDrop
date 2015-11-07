package com.retrom.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.retrom.volcano.assets.Assets;

public class Hub {
	
	private static final float PADDING = 20;
	
	private static final float POP_TIME = 0.2f;
	private static final float POP_ADDED_SCALE = 0.3f;

	float stateTime_ = 0;
	
	private int score_;

	private Label _score_text;


	public Hub() {
		_score_text = new Label("0", new LabelStyle(Assets.scoreFont, Color.WHITE));
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
		
		_score_text.setPosition(COIN_X_POS + 50, COIN_Y_POS + 8);
		
		float PAUSE_X_POS = cam.viewportWidth / 2 - Assets.pauseButton.getWidth();
		float PAUSE_Y_POS = cam.viewportHeight / 2 - Assets.pauseButton.getHeight();
		Sprite buttonSprite = isPaused ? Assets.pauseButtonClicked : Assets.pauseButton; 
		batch.draw(buttonSprite, PAUSE_X_POS, PAUSE_Y_POS);
		
		updateScoreText();
		
		_score_text.draw(batch, 1);
		
	}
	
	private void updateScoreText() {
		_score_text.setText("" + score_);
		float scale = 1f;
		if (stateTime_ < POP_TIME) {
			scale = 1f + POP_ADDED_SCALE * (POP_TIME - stateTime_) / POP_TIME;
		}
		
		Assets.scoreFont.getData().setScale(scale);
		LabelStyle style = new LabelStyle(Assets.scoreFont, Color.WHITE);
		_score_text.setStyle(style);
	}
}
