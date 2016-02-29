package com.retrom.volcano.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.data.ShopData;

public class ShopMenu {

	private static final float menuFinalYPos = 180;

	GraphicObject menuBg = new StaticGraphicObject(Assets.shopMenuBg, 0, menuFinalYPos);
	GraphicObject menuFg = new StaticGraphicObject(Assets.shopMenuFg, 0, menuFinalYPos);
	
	
	ItemsListShopMenuContent.BuyListener bl = new ItemsListShopMenuContent.BuyListener() {
		@Override
		public void buy() {
			buy = true;
		}
	}; 
	
	private ShopMenuContent powersContent = new PowersShopMenuContent(bl);
	
	private ShopMenuContent content;
			

	private boolean buy = false;
	
	private GoldCounter goldCounter = new GoldCounter();
	
	public ShopMenu() {
		content = powersContent;
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
	}
	
	public void render(SpriteBatch batch) {
		menuBg.render(batch);
		content.render(batch);
		menuFg.render(batch);
		goldCounter.render(batch);
	}
	
	public boolean getBuy() {
		boolean $ = buy;
		buy = false;
		return $;
	}
}
