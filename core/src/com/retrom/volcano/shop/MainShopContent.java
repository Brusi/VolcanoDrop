package com.retrom.volcano.shop;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.menus.ExitMenuButton;
import com.retrom.volcano.menus.MenuButton;
import com.retrom.volcano.shop.ShopMenu.Listener;
import com.retrom.volcano.utils.Tween;
import com.retrom.volcano.utils.TweenQueue;

public class MainShopContent implements ShopMenuContent {
	
	List<MenuButton> buttons = new ArrayList<MenuButton>();
	final Listener listener_;
	
	TweenQueue tweens = new TweenQueue();
	
	MainShopContent(Listener listener) {
		this.listener_ = listener;
		
		buttons.add(new MainShopMenuButton(280, new MenuButton.Action() {
			@Override
			public void act() { listener_.act(ShopMenu.Command.POWERS); }
		}, Assets.mainShopPowers));
		
		buttons.add(new MainShopMenuButton(158, new MenuButton.Action() {
			@Override
			public void act() { listener_.act(ShopMenu.Command.BLESSINGS); }
		}, Assets.mainShopBlessings));
		
		buttons.add(new MainShopMenuButton(37, new MenuButton.Action() {
			@Override
			public void act() { listener_.act(ShopMenu.Command.COSTUMES); }
		}, Assets.mainShopCostumes));
	}

	@Override
	public void update(float deltaTime) {
		tweens.update(deltaTime);
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
		tweens = new TweenQueue();
		tweens.addTweenFromNow(0, 0.5f, new Tween() {
			@Override
			public void invoke(float t) {
				buttons.get(0).setScale(t);
			}
		});
		tweens.addTweenFromNow(0.1f, 0.5f, new Tween() {
			@Override
			public void invoke(float t) {
				buttons.get(1).setScale(t);
			}
		});
		tweens.addTweenFromNow(0.2f, 0.5f, new Tween() {
			@Override
			public void invoke(float t) {
				buttons.get(2).setScale(t);
			}
		});
	}

	@Override
	public Sprite getBottomFade() {
		return null;
	}

	@Override
	public void setAlpha(float alpha) {
		// Nothing now?
	}
}
