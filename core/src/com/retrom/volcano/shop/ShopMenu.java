package com.retrom.volcano.shop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.menus.BackMenuButton;
import com.retrom.volcano.menus.ExitMenuButton;
import com.retrom.volcano.menus.GraphicObject;
import com.retrom.volcano.menus.MenuButton;
import com.retrom.volcano.menus.StaticGraphicObject;
import com.retrom.volcano.screens.GameScreen;
import com.retrom.volcano.utils.Tween;
import com.retrom.volcano.utils.TweenQueue;
import com.sun.corba.se.impl.javax.rmi.CORBA.Util;

public class ShopMenu {

	private static final float menuStartYPos = 1000;
	private static final float menuFinalYPos = 176;
	
	TweenQueue tweens = new TweenQueue();

	GraphicObject menuBg = new StaticGraphicObject(Assets.shopMenuBg, 0, menuStartYPos - 10);
	GraphicObject menuFg = new StaticGraphicObject(Assets.shopMenuFg, 0, menuStartYPos);
	
	MenuButton exitButton;
	MenuButton backButton;
	
	enum Command {
		EXIT, BACK, POWERS, BLESSINGS, COSTUMES, BUY;
	}
	
	public interface Listener {
		public void act(Command cmd);
	}
	
	private Listener listener = new Listener() {
		@Override
		public void act(Command cmd) {
			switch (cmd) {
			case BACK:
				content = mainContent;
				backButton.hide();
				break;
			case BLESSINGS:
				content = blessingsContent;
				backButton.show();
				break;
			case COSTUMES:
				content = costumesContent;
				backButton.show();
				break;
			case POWERS:
				content = powersContent;
				backButton.show();
				break;
			case BUY:
				buy = true;
				break;
			case EXIT:
				Game x = ((Game)(Gdx.app.getApplicationListener()));
				x.setScreen(new GameScreen());
				break;
			default:
				break;
			}
			content.refresh();
		}
	};
	
	private ShopMenuContent mainContent = new MainShopContent(listener);
	private ShopMenuContent powersContent = new PowersShopMenuContent(listener);
	private ShopMenuContent blessingsContent = new BlessingsShopMenuContent(listener);
	private ShopMenuContent costumesContent = new CostumesShopMenuContent(listener);
	
	private ShopMenuContent content;
			

	private boolean buy = false;
	
	private GoldCounter goldCounter = new GoldCounter(374);
	
	public ShopMenu() {
		content = mainContent;
		content.refresh();
		exitButton = new ExitMenuButton(ExitMenuButton.DEFAULT_X,
				ExitMenuButton.DEFAULT_Y, new MenuButton.Action() {
					@Override
					public void act() {
						listener.act(ShopMenu.Command.EXIT);
					}
				});
		backButton = new BackMenuButton(BackMenuButton.DEFAULT_X,
				BackMenuButton.DEFAULT_Y, new MenuButton.Action() {
					@Override
					public void act() {
						listener.act(ShopMenu.Command.BACK);
					}
				});
		backButton.hide();
		
		tweens.addTweenFromNow(1, 1f, Tween.bounce(Tween.movePoint(menuBg.position_).from(1, menuStartYPos).to(1, menuFinalYPos)));
		tweens.addTweenFromNow(1, 1f, Tween.bounce(Tween.movePoint(menuFg.position_).from(1, menuStartYPos-10).to(1, menuFinalYPos)));
		tweens.addTweenFromNow(1.5f, 0.6f, Tween.bubble(new Tween() {
			@Override
			public void invoke(float t) {
				goldCounter.setScale(t);
			}}));
	}

	public void update(float deltaTime) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			ShopData.reset();
			content.refresh();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
			ShopData.addGold(1000);
			content.refresh();
		}
		
		tweens.update(deltaTime);
		content.update(deltaTime);
		goldCounter.update();
		exitButton.checkClick();
		backButton.checkClick();
	}
	
	public void render(SpriteBatch batch) {
		menuBg.render(batch);
		content.render(batch);
		renderBottomFade(batch);
		menuFg.render(batch);
		goldCounter.render(batch);
		exitButton.render(batch);
		backButton.render(batch);
	}
	
	private void renderBottomFade(SpriteBatch batch) {
		Sprite s = content.getBottomFade();
		if (s != null) Utils.drawCenter(batch, s, 0, 174);
	}

	public boolean getBuy() {
		boolean $ = buy;
		buy = false;
		return $;
	}
}
