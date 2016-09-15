package com.retrom.volcano.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class CostumeAssets {

	final private String path;
	final private AssetManager assetManager;

	public Array<Sprite> playerIdle;
	public Array<Sprite> playerRun;
	public Array<Sprite> playerRunStart;
	public Array<Sprite> playerJump;
	public Array<Sprite> playerLand;
	public Array<Sprite> playerBurn;
	public Array<Sprite> playerBurnAdd;
	public Array<Sprite> playerSquash;
	public Array<Sprite> playerSquashAdd;
	
	public CostumeAssets(String path, AssetManager assetManager) {
		this.assetManager = assetManager;
		this.path = path;
		load();
	}

	private void load() {
		assetManager.load(path, TextureAtlas.class);
	}
	
	public void initAssets() {
		TextureAtlas sheet = assetManager.get(path, TextureAtlas.class);
		playerIdle = sheet.createSprites("player_idle");
		playerRun = sheet.createSprites("player_run");
		playerRunStart = sheet.createSprites("player_run_start");
		playerJump = sheet.createSprites("player_jump");
		playerLand = sheet.createSprites("player_land");
		playerBurn = sheet.createSprites("player_die_burn");
		playerBurnAdd = sheet.createSprites("player_die_burn_add");
		playerSquash = sheet.createSprites("player_die_squash");
		playerSquashAdd = sheet.createSprites("player_die_squash_add");
	}
}
