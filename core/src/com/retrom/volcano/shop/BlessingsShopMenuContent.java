package com.retrom.volcano.shop;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.shop.ShopMenu.Listener;

public class BlessingsShopMenuContent extends ItemsListShopMenuContent {

	BlessingsShopMenuContent(Listener listener) {
		super(listener);
	}

	@Override
	protected void initItems() {
		addMenuItem(Assets.shopItemAirCharmIcon, Assets.shopItemAirCharmTitle, ShopData.airCharm);
	}

	@Override
	public Sprite getBottomFade() {
		return Assets.bottomFadeBlessings; 
	}

}
