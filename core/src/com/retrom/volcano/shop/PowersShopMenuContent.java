package com.retrom.volcano.shop;

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
	}

}
