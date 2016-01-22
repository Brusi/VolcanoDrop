package com.retrom.volcano.shop;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.data.ShopEntry;
import com.retrom.volcano.shop.ShopMenuItem.Action;

public class ShopMenu {

	private static final float menuFinalYPos = 180;

	GraphicObject menuBg = new StaticGraphicObject(Assets.shopMenuBg, 0, menuFinalYPos);
	GraphicObject menuFg = new StaticGraphicObject(Assets.shopMenuFg, 0, menuFinalYPos);
	
	List<ShopMenuItem> items = new ArrayList<ShopMenuItem>();
	
	private float scrollY = 0;
	
	private int runningIndex = 0;
	
	private boolean buy = false;
	
	private GoldCounter goldCounter = new GoldCounter();
	
	public ShopMenu() {
		addMenuItem(Assets.shopItemAirStepIcon, Assets.shopItemAirStepTitle, ShopData.airStep);
		addMenuItem(Assets.shopItemWallFootIcon, Assets.shopItemWallFootTitle, ShopData.wallFoot);
	}
	
	private void addMenuItem(Sprite icon, Sprite title, final ShopEntry entry) {
		Action action = new Action() {
			@Override
			public void act() {
				ShopData.reduceGold(entry.price);
				System.out.println("Buying " + entry.name);
				ShopData.buyFromShop(entry);
				
				buy = true;
				for (ShopMenuItem item : items) {
					item.updateState();
				}
			}
		};
		items.add(new ShopMenuItem(runningIndex++, icon, title, entry, action));
	}
	
	public void update(float deltaTime) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			ShopData.reset();
			for (ShopMenuItem item : items) {
				item.initState();
			}
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
			ShopData.addGold(1000);
			for (ShopMenuItem item : items) {
				item.initState();
			}
		}
		
		
		for (ShopMenuItem item : items) {
			item.update(deltaTime);
		}
		goldCounter.update();
	}
	
	public void render(SpriteBatch batch) {
		menuBg.render(batch);
		
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
		
		menuFg.render(batch);
		goldCounter.render(batch);
	}
	
	public boolean getBuy() {
		boolean $ = buy;
		buy = false;
		return $;
	}
}
