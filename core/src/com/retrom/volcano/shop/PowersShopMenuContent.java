package com.retrom.volcano.shop;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.shop.ShopMenu.Listener;

public class PowersShopMenuContent extends ItemsListShopMenuContent {

	PowersShopMenuContent(Listener listener) {
		super(listener);
	}

	@Override
	protected void initItems() {
		addMenuItem(Assets.shopItemAirStepIcon, Assets.shopItemAirStepTitle, ShopData.airStep);
		addMenuItem(Assets.shopItemWallFootIcon, Assets.shopItemWallFootTitle, ShopData.wallFoot);
		addMenuItem(Assets.shopItemChargeIcon, Assets.shopItemChargeTitle, ShopData.charge);
		addMenuItem(Assets.shopItemCheetahrIcon, Assets.shopItemCheetahrTitle, ShopData.cheetahr);
		addMenuItem(Assets.shopItemFroggerIcon, Assets.shopItemFroggerTitle, ShopData.frogger);
		addMenuItem(Assets.shopItemSlowWalkerIcon, Assets.shopItemSlowWalkerTitle, ShopData.slowWalker);
		
	}

	@Override
	public Sprite getBottomFade() {
		return Assets.bottomFadePowers;
	}

}
