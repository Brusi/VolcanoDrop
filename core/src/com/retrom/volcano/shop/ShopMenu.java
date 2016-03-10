package com.retrom.volcano.shop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.menus.BackMenuButton;
import com.retrom.volcano.menus.ExitMenuButton;
import com.retrom.volcano.menus.MenuButton;
import com.retrom.volcano.screens.GameScreen;

public class ShopMenu {

	private static final float menuFinalYPos = 180;

	GraphicObject menuBg = new StaticGraphicObject(Assets.shopMenuBg, 0, menuFinalYPos);
	GraphicObject menuFg = new StaticGraphicObject(Assets.shopMenuFg, 0, menuFinalYPos);
	
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
				break;
			case COSTUMES:
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
		}
	};
	
	private ShopMenuContent mainContent = new MainShopContent(listener);
	private ShopMenuContent powersContent = new PowersShopMenuContent(listener);
	
	private ShopMenuContent content;
			

	private boolean buy = false;
	
	private GoldCounter goldCounter = new GoldCounter();
	
	public ShopMenu() {
		content = mainContent;
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
		
		content.update(deltaTime);
		goldCounter.update();
		exitButton.checkClick();
		backButton.checkClick();
	}
	
	public void render(SpriteBatch batch) {
		menuBg.render(batch);
		content.render(batch);
		menuFg.render(batch);
		goldCounter.render(batch);
		exitButton.render(batch);
		backButton.render(batch);
	}
	
	public boolean getBuy() {
		boolean $ = buy;
		buy = false;
		return $;
	}
}
