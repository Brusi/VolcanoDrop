package com.retrom.volcano.shop;

import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.data.ShopData;

public class PowersShopMenuContent extends ItemsListShopMenuContent {

	PowersShopMenuContent(BuyListener bl) {
		super(bl);
	}

	@Override
	protected void initItems() {
		addMenuItem(Assets.shopItemAirStepIcon, Assets.shopItemAirStepTitle, ShopData.airStep);
		addMenuItem(Assets.shopItemWallFootIcon, Assets.shopItemWallFootTitle, ShopData.wallFoot);
	}

}
