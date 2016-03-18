package com.retrom.volcano.shop;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.menus.ExitMenuButton;
import com.retrom.volcano.menus.MenuButton;
import com.retrom.volcano.shop.ShopMenu.Listener;

public class MainShopContent implements ShopMenuContent {
	
	List<MenuButton> buttons = new ArrayList<MenuButton>();
	final Listener listener_;
	
	MainShopContent(Listener listener) {
		this.listener_ = listener;
		
		buttons.add(new MainShopMenuButton(270, new MenuButton.Action() {
			@Override
			public void act() { listener_.act(ShopMenu.Command.POWERS); }
		}, Assets.mainShopPowers));
		
		buttons.add(new MainShopMenuButton(155, new MenuButton.Action() {
			@Override
			public void act() { listener_.act(ShopMenu.Command.BLESSINGS); }
		}, Assets.mainShopBlessings));
		
		buttons.add(new MainShopMenuButton(40, new MenuButton.Action() {
			@Override
			public void act() { listener_.act(ShopMenu.Command.COSTUMES); }
		}, Assets.mainShopCostumes));
	}

	@Override
	public void update(float deltaTime) {
		for (MenuButton button : buttons) {
			button.checkClick();
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		for (MenuButton button : buttons) {
			button.render(batch);
		}
	}

	@Override
	public void refresh() {
		// Nothing to refresh...
	}

	@Override
	public Sprite getBottomFade() {
		return null;
	}
}
