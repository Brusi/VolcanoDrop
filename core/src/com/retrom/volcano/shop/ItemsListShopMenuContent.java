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
import com.retrom.volcano.menus.ExitMenuButton;
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
		refresh();
	}
	
	abstract protected void initItems();	
	
	List<ShopMenuItem> items = new ArrayList<ShopMenuItem>();
	private float scrollY = 0;
	private float tgtY = 0;
	private int runningIndex = 0;
	private float alpha_ = 1;
	
	@Override
	public void update(float deltaTime) {
		for (ShopMenuItem item : items) {
			item.update(deltaTime);
		}
		float vel = (tgtY - scrollY) * 10f;
		scrollY += vel * deltaTime;
	}

	@Override
	public void render(SpriteBatch batch) {
		for (ShopMenuItem item : items) {
			item.setScrollY(scrollY);
			item.setAlpha(alpha_);
			item.render(batch);
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
			tgtY += ShopMenuItem.ITEM_HEIGHT;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			tgtY -= ShopMenuItem.ITEM_HEIGHT;
		}
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
				ShopData.reduceGold(entry.getPrice());
				ShopData.buyFromShop(entry);
				
				listener_.act(ShopMenu.Command.BUY);
				for (ShopMenuItem item : items) {
					item.updateState();
				}
			}
		};
		items.add(new ShopMenuItem(runningIndex++, icon, title, entry, action));
	}
	
	@Override
	public void setAlpha(float alpha) {
		alpha_  = alpha;
	}
}
