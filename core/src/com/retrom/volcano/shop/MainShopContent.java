package com.retrom.volcano.shop;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.menus.MenuButton;

public class MainShopContent implements ShopMenuContent {
	
	List<MenuButton> buttons = new ArrayList<MenuButton>();
	
	MainShopContent() {
		buttons.add(new MainShopMenuButton(40, new MenuButton.Action() {
			@Override
			public void act() {
				// TODO Auto-generated method stub
			}
		}, Assets.mainShopPowers));
	}

	@Override
	public void update(float deltaTime) {
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

}
