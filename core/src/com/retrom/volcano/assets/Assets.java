/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.retrom.volcano.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.game.Settings;

public class Assets {
	public static Texture background;
	public static TextureRegion backgroundRegion;
	
	public static Array<Sprite> walls1;
	public static Array<Sprite> walls2;
	public static Array<Sprite> playerIdle;
	public static Array<Sprite> playerRun;
	public static Array<Sprite> playerRunStart;
	public static Array<Sprite> playerJump;
	public static Array<Sprite> playerLand;
	
	public static Array<Sprite> pillars;
	public static Array<Sprite> pillars_big;
	public static Sprite pillars_start;
	public static Sprite pillars_end;

	public static Texture items;
	public static TextureRegion player;
	public static TextureRegion coin3_1;
	public static TextureRegion coin5_4;
	
	public static TextureRegion powerupMagnet;
	
	public static Music music;
	public static Sound jumpSound;
	public static Sound highJumpSound;
	public static Sound hitSound;
	public static Sound coinSound;
	public static Sound clickSound;

	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load () {
		background = loadTexture("background.jpg");
		backgroundRegion = new TextureRegion(background);

		player = new TextureRegion(loadTexture("player.png"));
		coin3_1 = new TextureRegion(loadTexture("coin 3_1.png"));
		coin5_4 = new TextureRegion(loadTexture("coin 5_4.png"));
		powerupMagnet = new TextureRegion(loadTexture("powerupmagnet.png"));
		
		TextureAtlas wallsSheet = new TextureAtlas("walls/walls.txt");
		 walls1 = wallsSheet.createSprites("x1");
		 walls2 = wallsSheet.createSprites("x2");
		 
		 TextureAtlas playerSheet = new TextureAtlas("player/playersheet.txt");
		 playerIdle = playerSheet.createSprites("player_idle");
		 playerRun = playerSheet.createSprites("player_run");
		 playerRunStart = playerSheet.createSprites("player_run_start");
		 playerJump = playerSheet.createSprites("player_jump");
		 playerLand = playerSheet.createSprites("player_land");
		 
		 TextureAtlas environmentSheet = new TextureAtlas("walls/enviroment.txt");
		 pillars = environmentSheet.createSprites("pillars");
		 pillars_big = environmentSheet.createSprites("pillars_big");
		 pillars_start = environmentSheet.createSprite("pillars_start");
		 pillars_end = environmentSheet.createSprite("pillars_end");

//		font = new BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"), false);
//
//		music = Gdx.audio.newMusic(Gdx.files.internal("data/music.mp3"));
//		music.setLooping(true);
//		music.setVolume(0.5f);
//		if (Settings.soundEnabled) music.play();
//		jumpSound = Gdx.audio.newSound(Gdx.files.internal("data/jump.wav"));
//		highJumpSound = Gdx.audio.newSound(Gdx.files.internal("data/highjump.wav"));
//		hitSound = Gdx.audio.newSound(Gdx.files.internal("data/hit.wav"));
//		coinSound = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
//		clickSound = Gdx.audio.newSound(Gdx.files.internal("data/click.wav"));
	}

	public static void playSound (Sound sound) {
		if (Settings.soundEnabled) sound.play(1);
	}
}
