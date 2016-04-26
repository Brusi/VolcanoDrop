package com.retrom.volcano.assets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class CostumeAssets {
	
	final private String path;
	
	public Array<Sprite> playerIdle;
	public Array<Sprite> playerRun;
	public Array<Sprite> playerRunStart;
	public Array<Sprite> playerJump;
	public Array<Sprite> playerLand;
	public Array<Sprite> playerBurn;
	public Array<Sprite> playerSquash;
	
	public CostumeAssets(String path) {
		this.path = path;
		init();
	}
	
	private void init() {
		TextureAtlas sheet = new TextureAtlas(path);
		playerIdle = sheet.createSprites("player_idle");
		playerRun = sheet.createSprites("player_run");
		playerRunStart = sheet.createSprites("player_run_start");
		playerJump = sheet.createSprites("player_jump");
		playerLand = sheet.createSprites("player_land");
		playerBurn = sheet.createSprites("player_die_burn");
		playerSquash = sheet.createSprites("player_die_squash");
	}
}
