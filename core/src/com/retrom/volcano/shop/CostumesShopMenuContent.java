package com.retrom.volcano.shop;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.shop.ShopMenu.Listener;

public class CostumesShopMenuContent extends ItemsListShopMenuContent {

	CostumesShopMenuContent(Listener listener) {
		super(listener);
	}

	@Override
	protected void initItems() {
		addMenuItem(Assets.shopItemDefaultCostumeIcon, Assets.shopItemDefaultCostumeTitle, ShopData.defaultCostume);
		// TODO: replace with real icons when ready. 
		addMenuItem(Assets.goboCostume.playerIdle.get(0), Assets.shopItemDefaultCostumeTitle, ShopData.goboCostume);
		addMenuItem(Assets.blikCostume.playerIdle.get(0), Assets.shopItemDefaultCostumeTitle, ShopData.blikCostume);
	}

	@Override
	public Sprite getBottomFade() {
		return Assets.bottomFadeCostumes;
	}

}
