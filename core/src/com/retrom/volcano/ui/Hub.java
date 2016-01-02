package com.retrom.volcano.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.utils.TouchToPoint;

public class Hub {
	
	private static final float PADDING = 20;
	
	private static final float POP_TIME = 0.2f;
	private static final float POP_ADDED_SCALE = 0.3f;

	float stateTime_ = 0;
	
	private int score_;

	private Label _score_text;
	
	private final TouchToPoint ttp = TouchToPoint.create();

	final private Rectangle pauseRect;

	public Hub() {
		_score_text = new Label("0", new LabelStyle(Assets.scoreFont, Color.WHITE));
		
		float PAUSE_X_POS = WorldRenderer.FRUSTUM_WIDTH / 2 - Assets.pauseButton.getWidth();
		float PAUSE_Y_POS = WorldRenderer.FRUSTUM_HEIGHT / 2 - Assets.pauseButton.getHeight();
		pauseRect = new Rectangle(PAUSE_X_POS, PAUSE_Y_POS, Assets.pauseButton.getWidth(), Assets.pauseButton.getHeight());
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
		
		
		 
		Sprite buttonSprite = isPaused ? Assets.pauseButtonClicked : Assets.pauseButton; 
		batch.draw(buttonSprite, pauseRect.x, pauseRect.y);
		
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
	
	public boolean isPauseAreaTouched() {
		if (!Gdx.input.justTouched()) {
			return false;
		}
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		
		Vector2 pnt = ttp.toPoint(x, y);
		return pauseRect.contains(pnt);
//		return x > Gdx.graphics.getWidth() * 3f / 4f && y < Gdx.graphics.getWidth() / 4f;  
	}
}
