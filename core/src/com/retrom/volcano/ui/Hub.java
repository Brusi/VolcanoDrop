package com.retrom.volcano.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.assets.SoundAssets;
import com.retrom.volcano.control.ControlManager;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.menus.MenuButton;
import com.retrom.volcano.menus.SimpleMenuButton;
import com.retrom.volcano.shop.GoldCounter;
import com.retrom.volcano.utils.EventQueue;
import com.retrom.volcano.utils.Tween;
import com.retrom.volcano.utils.TweenQueue;

public class Hub {
	
	private final TweenQueue queue = new TweenQueue();
	
	private static final float PADDING = 20;
	
	private static final float POP_TIME = 0.2f;
	private static final float POP_ADDED_SCALE = 0.3f;

	float stateTime_ = 0;
	
	private int score_;
	private float time_;

	private Label score_text_;
	private Label time_text_;
	
	private float best_time_scale_ = 0;
	
	private final MenuButton pauseButton;
	private final MenuButton shopButton;
	
	private boolean gameStarted = false;
	
	private final GoldCounter total_gold_counter;
	
	
	
	public Hub(MenuButton.Action[] action) {
		score_text_ = new Label("0", new LabelStyle(Assets.scoreFont, Color.WHITE));
		time_text_ = new Label("0:00", new LabelStyle(Assets.timeFont, Color.WHITE));
		
		pauseButton = new SimpleMenuButton(
					WorldRenderer.FRUSTUM_WIDTH / 2 - 83, WorldRenderer.FRUSTUM_HEIGHT / 2 - 86, 165f, 172f,
					Assets.pauseButton, Assets.pauseButtonClicked, action[0]);
		shopButton = new SimpleMenuButton(
				WorldRenderer.FRUSTUM_WIDTH / 2 - 88, WorldRenderer.FRUSTUM_HEIGHT / 2 - 88, 165f, 172f,
				Assets.pauseShopButton, Assets.pauseShopButtonClicked, action[1]);
		shopButton.setScale(0);
		
		total_gold_counter = new GoldCounter(WorldRenderer.FRUSTUM_HEIGHT / 2 - 40);
		total_gold_counter.update();
	}
	
	public void update(float deltaTime) {
		stateTime_ += deltaTime;
		
		queue.update(deltaTime);
		total_gold_counter.update();
		
		pauseButton.checkClick();
		shopButton.checkClick();
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
		if (!gameStarted) {
			if (best_time_scale_ > 0) {
				Sprite s = Assets.guiTimeBestIcon;
				s.setScale(best_time_scale_);
				Utils.drawCenter(batch, s, -WorldRenderer.FRUSTUM_WIDTH / 2 + 66,
						                   WorldRenderer.FRUSTUM_HEIGHT / 2 - 34);
			
				time_text_.setPosition(-WorldRenderer.FRUSTUM_WIDTH / 2 + 26 + time_text_.getWidth() * (1-best_time_scale_) / 2,
						               WorldRenderer.FRUSTUM_HEIGHT / 2 - 91);
				time_text_.setFontScale(best_time_scale_);
				time_text_.draw(batch, 1);
				updateTimeText(ShopData.getBestTime());
				
				total_gold_counter.render(batch);
			}
			
			shopButton.render(batch);
			
			return;
		}
		
		float COIN_X_POS = - cam.viewportWidth / 2 + PADDING;
		float COIN_Y_POS = cam.viewportHeight / 2 - Assets.scoreIcon.getHeight() - PADDING - 50;
		{
			Sprite s = Assets.scoreIcon;
			s.setPosition(COIN_X_POS, COIN_Y_POS);
			s.draw(batch);
		}
		
		
		score_text_.setPosition(COIN_X_POS + 50, COIN_Y_POS + 8);
		time_text_.setPosition(COIN_X_POS, COIN_Y_POS + 58);
		
		
        // TODO convert to real menu button.
		if (!isPaused) {
			pauseButton.render(batch);
		}
		
		updateScoreText();
		updateTimeText(time_);
		
		score_text_.draw(batch, 1);
		time_text_.draw(batch, 1);
		
	}
	
	private void updateTimeText(float time) {
		int t = (int)Math.floor(time);
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
	
	public void startGame() {
		gameStarted = true;
	}

	public void showBeforeSplash() {
		appearControls();
		appearUI();
	}
	
	static private EventQueue.Event playPopSoundEvent = new EventQueue.Event() {
		@Override
		public void invoke() {
			SoundAssets.playSound(SoundAssets.shopShamanOpenPop);
		}
	};

	private void appearUI() {
		queue.addTweenFromNow(0.8f, 0.333f, Tween.bubble(new Tween() {
			@Override
			public void invoke(float t) {
				shopButton.setScale(t);
			}
		}));
		queue.addEventFromNow(0.633f, playPopSoundEvent); 
		
		queue.addTweenFromNow(0.7f, 0.333f, Tween.bubble(new Tween() {
			@Override
			public void invoke(float t) {
				total_gold_counter.setScale(t);
			}
		}));
		queue.addEventFromNow(0.867f, playPopSoundEvent);
		
		queue.addTweenFromNow(0.933f, 0.333f, Tween.bubble(new Tween() {
			@Override
			public void invoke(float t) {
				best_time_scale_ = t;
			}
		}));
		queue.addEventFromNow(0.933f, playPopSoundEvent);
	}

	private void appearControls() {
		ControlManager.getControl().show();
		ControlManager.getControl().setSidesButtonsScale(0);
		ControlManager.getControl().setJumpButtonScale(0);
		queue.addEventFromNow(0.5f, playPopSoundEvent);
		queue.addTweenFromNow(0.5f, 0.333f, Tween.bubble(new Tween() {
			@Override
			public void invoke(float t) {
				ControlManager.getControl().setSidesButtonsScale(t);
			}
		}));
		queue.addEventFromNow(0.567f, playPopSoundEvent);
		queue.addTweenFromNow(0.567f, 0.333f, Tween.bubble(new Tween() {
			@Override
			public void invoke(float t) {
				ControlManager.getControl().setJumpButtonScale(t);
			}
		}));
		
		queue.addEventFromNow(0.9f, new EventQueue.Event() {
			@Override
			public void invoke() {
				ControlManager.getControl().enable();
			}
		});
	}
}
