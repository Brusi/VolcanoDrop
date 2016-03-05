package com.retrom.volcano.shop;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.data.ShopEntry;
import com.retrom.volcano.menus.BackMenuButton;
import com.retrom.volcano.menus.MenuButton;
import com.retrom.volcano.menus.MenuButton.Action;

public abstract class ItemsListShopMenuContent implements ShopMenuContent {
	
	private ShopMenu.Listener listener_;

	public interface BuyListener {
		public void buy();
	}
	
	ItemsListShopMenuContent(ShopMenu.Listener listener) {
		this.listener_ = listener;
		initItems();
		addBackButton();
		refresh();
	}
	
	abstract protected void initItems();	
	
	List<ShopMenuItem> items = new ArrayList<ShopMenuItem>();
	private float scrollY = 0;
	private int runningIndex = 0;
	private BackMenuButton back;
	
	@Override
	public void update(float deltaTime) {
		for (ShopMenuItem item : items) {
			item.update(deltaTime);
		}
		back.checkClick();
	}

	@Override
	public void render(SpriteBatch batch) {
		for (ShopMenuItem item : items) {
			item.setScrollY(scrollY);
			item.render(batch);
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			scrollY += 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			scrollY -= 1;
		}
		back.render(batch);
	}

	@Override
	public void refresh() {
		for (ShopMenuItem item : items) {
			item.initState();
		}
	}
	
	protected void addMenuItem(Sprite icon, Sprite title, final ShopEntry entry) {
		Action action = new Action() {
			@Override
			public void act() {
				ShopData.reduceGold(entry.price);
				ShopData.buyFromShop(entry);
				System.out.println("buy");
				
				listener_.act(ShopMenu.Command.BUY);
				for (ShopMenuItem item : items) {
					item.updateState();
				}
			}
		};
		items.add(new ShopMenuItem(runningIndex++, icon, title, entry, action));
	}
	
	private void addBackButton() {
		back = new BackMenuButton(BackMenuButton.DEFAULT_X, BackMenuButton.DEFAULT_Y,
				new MenuButton.Action() {
					@Override
					public void act() {
						listener_.act(ShopMenu.Command.BACK);
					}
				});
	}

}
