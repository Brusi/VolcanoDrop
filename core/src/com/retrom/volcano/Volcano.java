package com.retrom.volcano;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.assets.SoundAssets;
import com.retrom.volcano.control.ControlManager;
import com.retrom.volcano.data.CoinChancesConfiguration;
import com.retrom.volcano.data.Levels;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.screens.ShopScreen;
import com.retrom.volcano.screens.GameScreen;

public class Volcano extends Game {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		// TODO: preload levels and sequences.
		SoundAssets.load();
		ControlManager.init();
		CoinChancesConfiguration.init();
		ShopData.init();
		Assets.load();
		setScreen(new GameScreen());
//		setScreen(new ShopScreen());
	}
}
