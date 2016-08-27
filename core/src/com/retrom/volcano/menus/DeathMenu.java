package com.retrom.volcano.menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.shop.GoldCounter;

public class DeathMenu extends Menu {
	
	private GoldCounter goldCounter = new GoldCounter(266);
	
	public enum Command {
		RESTART, SHOP,
	}
	
	public interface Listener {
		public void act(Command cmd);
	}
	
	private final Fade fade = new Fade(new Color(0, 0.05f, 0.075f, 1));
//	
//	private final Fade yesNoFade = new Fade(new Color(0, 0, 0.0f, 1));
	
	private final Listener listener_;
	
	private Label score_text_ = new Label("0", new LabelStyle(Assets.scoreFont, Color.WHITE));
	private Label time_text_ = new Label("0:00", new LabelStyle(Assets.timeFont, Color.WHITE));

	MenuButton.Action commandAction(final Command cmd) {
		return new MenuButton.Action() {
			@Override
			public void act() {
				listener_.act(cmd);
			}
		};
	}
	
	public DeathMenu(final Listener listener) {
		this.listener_ = listener;
		graphics.add(new StaticGraphicObject(Assets.pauseMenuBG, 4, 97));
		
		buttons.add(new SimpleMenuButton(223, 330, 140, 140,
				Assets.pauseShopButton, Assets.pauseShopButtonClicked,
				commandAction(Command.SHOP)));
		
		buttons.add(new SimpleMenuButton(0, -9, 230, 100,
				Assets.pauseRetryButton, Assets.pauseRetryButtonClicked,
				commandAction(Command.RESTART)));
	}
	
	@Override
	public void render(SpriteBatch batch, ShapeRenderer shapes) {
		fade.setAlpha(0.75f);
		fade.render(shapes);
		batch.begin();
		super.render(batch, shapes);
//		goldCounter.setAlpha(1);
		goldCounter.setScale(1);
		goldCounter.render(batch);
		
		drawLabels(batch);
		
		batch.end();
	}
	
	private void drawLabels(SpriteBatch batch) {
		// TODO Auto-generated method stub
		score_text_.setPosition(0, 300);
		score_text_.draw(batch, 1);
		time_text_.setPosition(0, 350);
		time_text_.draw(batch, 1);
		
	}

	@Override
	public void update(float deltaTime) {
		goldCounter.update();
		super.update(deltaTime);
	}
	
	public void setScoreAndTime(int score, float time) {
		updateTimeText(time);
		updateScoreText(score);
	}
	
	private void updateTimeText(float time) {
		int t = (int)Math.floor(time);
		String text = "" + t/60 + ":" + ((t%60 < 10) ? "0" : "") + t%60;
		time_text_.setText(text);
		
	}

	private void updateScoreText(int score) {
		score_text_.setText("" + score);
		LabelStyle style = new LabelStyle(Assets.scoreFont, Color.WHITE);
		score_text_.setStyle(style);
	}
}
