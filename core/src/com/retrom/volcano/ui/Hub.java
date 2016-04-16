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
	private float time_;

	private Label score_text_;
	private Label time_text_;
	
	private final TouchToPoint ttp = TouchToPoint.create();

	private final Rectangle pauseRect;

	public Hub() {
		score_text_ = new Label("0", new LabelStyle(Assets.scoreFont, Color.WHITE));
		time_text_ = new Label("0:00", new LabelStyle(Assets.timeFont, Color.WHITE));
		
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
	
	public void setTime(float time) {
		time_ = time;
	}

	public void render(SpriteBatch batch, Camera cam, boolean isPaused) {
		float COIN_X_POS = - cam.viewportWidth / 2 + PADDING;
		float COIN_Y_POS = cam.viewportHeight / 2 - Assets.scoreIcon.getHeight() - PADDING - 50; 
		batch.draw(Assets.scoreIcon, COIN_X_POS, COIN_Y_POS);
		
		score_text_.setPosition(COIN_X_POS + 50, COIN_Y_POS + 8);
		time_text_.setPosition(COIN_X_POS, COIN_Y_POS + 58);
		
		
        // TODO convert to real menu button.
		Sprite buttonSprite = isPaused ? Assets.pauseButtonClicked : Assets.pauseButton;
		if (!isPaused)
			batch.draw(buttonSprite, pauseRect.x, pauseRect.y);
		
		updateScoreText();
		updateTimeText();
		
		score_text_.draw(batch, 1);
		time_text_.draw(batch, 1);
		
	}
	
	private void updateTimeText() {
		int t = (int)time_;
		String text = "" + t/60 + ":" + ((t%60 < 10) ? "0" : "") + t%60;
		time_text_.setText(text);
		// TODO Auto-generated method stub
		
	}

	private void updateScoreText() {
		score_text_.setText("" + score_);
		float scale = 1f;
		if (stateTime_ < POP_TIME) {
			scale = 1f + POP_ADDED_SCALE * (POP_TIME - stateTime_) / POP_TIME;
		}
		
		Assets.scoreFont.getData().setScale(scale);
		LabelStyle style = new LabelStyle(Assets.scoreFont, Color.WHITE);
		score_text_.setStyle(style);
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
