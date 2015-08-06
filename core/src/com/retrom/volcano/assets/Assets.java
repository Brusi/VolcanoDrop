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
	
	public static Sprite background;
	public static Sprite floor;
	
	public static Sprite coin1_1;
	public static Array<Sprite> coin1_1_land;
	public static Sprite coin1_2;
	public static Array<Sprite> coin1_2_land;
	public static Sprite coin2_1;
	public static Array<Sprite> coin2_1_land;
	public static Sprite coin2_2;
	public static Array<Sprite> coin2_2_land;
	public static Sprite coin2_3;
	public static Array<Sprite> coin2_3_land;
	public static Sprite coin3_1;
	public static Array<Sprite> coin3_1_land;
	public static Sprite coin3_2;
	public static Array<Sprite> coin3_2_land;
	public static Sprite coin3_3;
	public static Array<Sprite> coin3_3_land;
	public static Sprite coin4_1;
	public static Array<Sprite> coin4_1_land;
	public static Sprite coin4_2;
	public static Array<Sprite> coin4_2_land;
	public static Sprite coin4_3;
	public static Array<Sprite> coin4_3_land;
	public static Sprite coin5_1;
	public static Array<Sprite> coin5_1_land;
	public static Sprite coin5_2;
	public static Array<Sprite> coin5_2_land;
	public static Sprite coin5_3;
	public static Array<Sprite> coin5_3_land;
	public static Sprite coin5_4;
	public static Array<Sprite> coin5_4_land;

	public static Sprite scoreNum1;
	public static Sprite scoreNum3;
	public static Sprite scoreNum4;
	public static Sprite scoreNum5;
	public static Sprite scoreNum6;
	public static Sprite scoreNum10;
	public static Sprite scoreNum15green;
	public static Sprite scoreNum15purple;
	public static Sprite scoreNum15teal;
	public static Sprite scoreNum25;
	
	
	public static TextureRegion player;
	
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

	public static void load() {
		player = new TextureRegion(loadTexture("player.png"));
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
		background = environmentSheet.createSprite("bg");
		floor = environmentSheet.createSprite("floor");

		TextureAtlas treasure = new TextureAtlas("treasure/treasure.txt");
		
		Array<Sprite> coin1Arr = treasure.createSprites("coin_1");
		coin1_1 = coin1Arr.get(0);
		coin1_2 = coin1Arr.get(1);
		
		Array<Sprite> coin2Arr = treasure.createSprites("coin_2");
		coin2_1 = coin2Arr.get(0);
		coin2_2 = coin2Arr.get(1);
		coin2_3 = coin2Arr.get(2);
		
		Array<Sprite> coin3Arr = treasure.createSprites("coin_3");
		coin3_1 = coin3Arr.get(0);
		coin3_2 = coin3Arr.get(1);
		coin3_3 = coin3Arr.get(2);
		
		Array<Sprite> coin4Arr = treasure.createSprites("coin_4");
		coin4_1 = coin4Arr.get(0);
		coin4_2 = coin4Arr.get(1);
		coin4_3 = coin4Arr.get(2);
		
		Array<Sprite> coin5Arr = treasure.createSprites("coin_5");
		coin5_1 = coin5Arr.get(0);
		coin5_2 = coin5Arr.get(1);
		coin5_3 = coin5Arr.get(2);
		coin5_4 = coin5Arr.get(3);
		
		coin1_1_land = treasure.createSprites("coin_1_1_land/coin_1_1_land");
		coin1_2_land = treasure.createSprites("coin_1_2_land/coin_1_2_land");
		
		coin2_1_land = treasure.createSprites("coin_2_1_land/coin_2_1_land");
		coin2_2_land = treasure.createSprites("coin_2_2_land/coin_2_2_land");
		coin2_3_land = treasure.createSprites("coin_2_3_land/coin_2_3_land");
		
		coin3_1_land = treasure.createSprites("coin_3_1_land/coin_3_1_land");
		coin3_2_land = treasure.createSprites("coin_3_2_land/coin_3_2_land");
		coin3_3_land = treasure.createSprites("coin_3_3_land/coin_3_3_land");
		
		coin3_1_land = treasure.createSprites("coin_3_1_land/coin_3_1_land");
		coin3_2_land = treasure.createSprites("coin_3_2_land/coin_3_2_land");
		coin3_3_land = treasure.createSprites("coin_3_3_land/coin_3_3_land");
		
		coin4_1_land = treasure.createSprites("coin_4_1_land/coin_4_1_land");
		coin4_2_land = treasure.createSprites("coin_4_2_land/coin_4_2_land");
		coin4_3_land = treasure.createSprites("coin_4_3_land/coin_4_3_land");
		
		coin5_1_land = treasure.createSprites("coin_5_1_land/coin_5_1_land");
		coin5_2_land = treasure.createSprites("coin_5_2_land/coin_5_2_land");
		coin5_3_land = treasure.createSprites("coin_5_3_land/coin_5_3_land");
		coin5_4_land = treasure.createSprites("coin_5_4_land/coin_5_4_land");
		
		TextureAtlas scoreNumsSheet = new TextureAtlas("treasure/score_num.txt");
		scoreNum1 = scoreNumsSheet.createSprite("gui_score_+1");
		scoreNum3 = scoreNumsSheet.createSprite("gui_score_+3");
		scoreNum4 = scoreNumsSheet.createSprite("gui_score_+4");
		scoreNum5 = scoreNumsSheet.createSprite("gui_score_+5");
		scoreNum6 = scoreNumsSheet.createSprite("gui_score_+6");
		scoreNum10 = scoreNumsSheet.createSprite("gui_score_+10");
		scoreNum15green = scoreNumsSheet.createSprite("gui_score_+15green");
		scoreNum15purple = scoreNumsSheet.createSprite("gui_score_+15purple");
		scoreNum15teal = scoreNumsSheet.createSprite("gui_score_+15teal");
		scoreNum25 = scoreNumsSheet.createSprite("gui_score_+25");
		
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
