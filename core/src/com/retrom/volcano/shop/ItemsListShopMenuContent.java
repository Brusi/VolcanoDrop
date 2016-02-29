package com.retrom.volcano.shop;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.data.ShopEntry;
import com.retrom.volcano.menus.MenuButton.Action;

public abstract class ItemsListShopMenuContent implements ShopMenuContent {
	
	private BuyListener bl;

	public interface BuyListener {
		public void buy();
	}
	
	ItemsListShopMenuContent(BuyListener bl) {
		this.bl = bl;
		initItems();
		refresh();
	}
	
	abstract protected void initItems();	
	
	List<ShopMenuItem> items = new ArrayList<ShopMenuItem>();
	private float scrollY = 0;
	private int runningIndex = 0;
	
	@Override
	public void update(float deltaTime) {
		for (ShopMenuItem item : items) {
			item.update(deltaTime);
		}
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
				
				bl.buy();
				for (ShopMenuItem item : items) {
					item.updateState();
				}
			}
		};
		items.add(new ShopMenuItem(runningIndex++, icon, title, entry, action));
	}

}
