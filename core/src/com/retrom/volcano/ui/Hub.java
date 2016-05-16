package com.retrom.volcano.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.menus.MenuButton;
import com.retrom.volcano.menus.SimpleMenuButton;

public class Hub {
	
	private static final float PADDING = 20;
	
	private static final float POP_TIME = 0.2f;
	private static final float POP_ADDED_SCALE = 0.3f;

	float stateTime_ = 0;
	
	private int score_;
	private float time_;

	private Label score_text_;
	private Label time_text_;
	
	private final MenuButton pauseButton;
	
	private float alpha = 0;

	public Hub(MenuButton.Action action) {
		score_text_ = new Label("0", new LabelStyle(Assets.scoreFont, Color.WHITE));
		time_text_ = new Label("0:00", new LabelStyle(Assets.timeFont, Color.WHITE));
		
		pauseButton = new SimpleMenuButton(
					WorldRenderer.FRUSTUM_WIDTH / 2 - 83, WorldRenderer.FRUSTUM_HEIGHT / 2 - 86, 165f, 172f,
					Assets.pauseButton, Assets.pauseButtonClicked, action);
	}
	
	public void update(float deltaTime) {
		stateTime_ += deltaTime;
		pauseButton.checkClick();
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
		{
			Sprite s = Assets.scoreIcon;
			s.setAlpha(alpha);
			s.setPosition(COIN_X_POS, COIN_Y_POS);
			s.draw(batch);
		}
		
		
		score_text_.setPosition(COIN_X_POS + 50, COIN_Y_POS + 8);
		time_text_.setPosition(COIN_X_POS, COIN_Y_POS + 58);
		
		
        // TODO convert to real menu button.
		if (!isPaused) {
			pauseButton.setAlpha(alpha);
			pauseButton.render(batch);
		}
		
		updateScoreText();
		updateTimeText();
		
		score_text_.setColor(1, 1, 1, alpha);
		score_text_.draw(batch, 1);
		time_text_.setColor(1, 1, 1, alpha);
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
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getAlpha() {
		return this.alpha;
	}
}
