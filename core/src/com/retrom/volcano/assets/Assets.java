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
	public static Array<Sprite> wallParticles;
	
	
	public static Array<Sprite> playerIdle;
	public static Array<Sprite> playerRun;
	public static Array<Sprite> playerRunStart;
	public static Array<Sprite> playerJump;
	public static Array<Sprite> playerLand;
	public static Array<Sprite> playerBurn;
	public static Array<Sprite> playerSquash;
	public static Sprite empty;
	
	
	public static Array<Sprite> pillars;
	public static Array<Sprite> pillars_big;
	public static Sprite pillars_start;
	public static Sprite pillars_end;
	public static Sprite pillars_hole;
	public static Sprite pillars_hole_bg;
	
	public static Sprite background;
	public static Sprite floor;
	
	public static Array<Sprite> bg_world1;
	
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
	
	public static Sprite powerupMagnet;
	public static Sprite powerupSlomo;
	public static Sprite powerupShield;

	//// Effects
	// Score numbers:
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
	
	// Score collect
	public static Array<Sprite> bronzeCollectEffect1;
	public static Array<Sprite> bronzeCollectEffect2;
	public static Array<Sprite> silverCollectEffect1;
	public static Array<Sprite> silverCollectEffect2;
	public static Array<Sprite> goldCollectEffect1;
	public static Array<Sprite> goldCollectEffect2;
	public static Array<Sprite> greenDiamondCollectEffect1;
	public static Array<Sprite> greenDiamondCollectEffect2;
	public static Array<Sprite> cyanDiamondCollectEffect1;
	public static Array<Sprite> cyanDiamondCollectEffect2;
	public static Array<Sprite> purpleDiamondCollectEffect1;
	public static Array<Sprite> purpleDiamondCollectEffect2;
	
	// Score particles:
	public static Array<Sprite> bronzeCoinBreak;
	public static Array<Sprite> silverCoinBreak;
	public static Array<Sprite> goldCoinBreak;
	public static Array<Sprite> greenDiamondBreak;
	public static Array<Sprite> purpleDiamondBreak;
	public static Array<Sprite> cyanDiamondBreak;
	
	// vfx:
	public static Array<Sprite> playerExplode;
	public static Array<Sprite> coinCrushedEffect;
	public static Array<Sprite> fireballExplodeEffect;
	public static Array<Sprite> fireballStartEffect;
	
	// Glow:
	public static Sprite burningWallGlow;
	
	public static Sprite diamondCyanGlow;
	public static Sprite diamondGreenGlow;
	public static Sprite diamondPurpleGlow;
	public static Sprite tokenGlow;
	
	public static Sprite magnetBackGlow;
	public static Sprite slomoBackGlow;
	public static Sprite shieldBackGlow;
	
	// Enemies:
	public static Sprite burningWall;
	public static Array<Sprite> burningWallStart;
	public static Array<Sprite> burningWallBurn;
	public static Array<Sprite> burningWallEnd;
	
	public static Sprite flamethrower;
	public static Array<Sprite> flamethrowerAll;
	public static Array<Sprite> flamethrowerFlame;
	
	public static Array<Sprite> spitter;
	
	public static Array<Sprite> topFireballLoop;

	public static Array<Sprite> playerShieldEffect;

	

	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load() {
//		font = new BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"), false);
		
		TextureAtlas wallsSheet = new TextureAtlas("walls/walls.txt");
		walls1 = wallsSheet.createSprites("x1");
		walls2 = wallsSheet.createSprites("x2");
		wallParticles = wallsSheet.createSprites("wall_particle");

		TextureAtlas playerSheet = new TextureAtlas("player/playersheet.txt");
		playerIdle = playerSheet.createSprites("player_idle");
		playerRun = playerSheet.createSprites("player_run");
		playerRunStart = playerSheet.createSprites("player_run_start");
		playerJump = playerSheet.createSprites("player_jump");
		playerLand = playerSheet.createSprites("player_land");
		playerBurn = playerSheet.createSprites("player_die_burn");
		playerSquash = playerSheet.createSprites("player_die_squash");
		empty = playerSheet.createSprite("empty");

		TextureAtlas environmentSheet = new TextureAtlas("walls/enviroment.txt");
		pillars = environmentSheet.createSprites("pillars");
		pillars_big = environmentSheet.createSprites("pillars_big");
		pillars_start = environmentSheet.createSprite("pillars_start");
		pillars_end = environmentSheet.createSprite("pillars_end");
		pillars_hole = environmentSheet.createSprite("pillars_hole_front");
		pillars_hole_bg = environmentSheet.createSprite("pillars_hole_back");
		floor = environmentSheet.createSprite("floor");
		background = environmentSheet.createSprite("bg_base");

		bg_world1 = environmentSheet.createSprites("bg_world1");
		
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
		
		coin1_1_land = treasure.createSprites("coin_1_1_land/coin1_1_land");
		coin1_2_land = treasure.createSprites("coin_1_2_land/coin1_2_land");
		System.out.println(coin1_1_land.size);


		coin2_1_land = treasure.createSprites("coin_2_1_land/coin2_1_land");
		coin2_2_land = treasure.createSprites("coin_2_2_land/coin2_2_land");
		coin2_3_land = treasure.createSprites("coin_2_3_land/coin2_3_land");
		
		coin3_1_land = treasure.createSprites("coin_3_1_land/coin3_1_land");
		coin3_2_land = treasure.createSprites("coin_3_2_land/coin3_2_land");
		coin3_3_land = treasure.createSprites("coin_3_3_land/coin3_3_land");
		
		coin3_1_land = treasure.createSprites("coin_3_1_land/coin3_1_land");
		coin3_2_land = treasure.createSprites("coin_3_2_land/coin3_2_land");
		coin3_3_land = treasure.createSprites("coin_3_3_land/coin3_3_land");
		
		coin4_1_land = treasure.createSprites("coin_4_1_land/coin4_1_land");
		coin4_2_land = treasure.createSprites("coin_4_2_land/coin4_2_land");
		coin4_3_land = treasure.createSprites("coin_4_3_land/coin4_3_land");
		
		coin5_1_land = treasure.createSprites("coin_5_1_land/coin5_1_land");
		coin5_2_land = treasure.createSprites("coin_5_2_land/coin5_2_land");
		coin5_3_land = treasure.createSprites("coin_5_3_land/coin5_3_land");
		coin5_4_land = treasure.createSprites("coin_5_4_land/coin5_4_land");
		
		powerupMagnet = treasure.createSprite("powerup_magnet");
		powerupSlomo = treasure.createSprite("powerup_time");
		powerupShield = treasure.createSprite("powerup_armor");
		
		bronzeCoinBreak = treasure.createSprites("coins_bronze_break");
		silverCoinBreak = treasure.createSprites("coins_silver_break");
		goldCoinBreak = treasure.createSprites("coins_gold_break");
		greenDiamondBreak = treasure.createSprites("coins_diamonds_green_break");
		purpleDiamondBreak = treasure.createSprites("coins_diamonds_purple_break");
		cyanDiamondBreak = treasure.createSprites("coins_diamonds_cyan_break");
		
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
		
		
		TextureAtlas vfxSheet = new TextureAtlas("vfx/vfx.txt");
		playerExplode = vfxSheet.createSprites("player_die_explotion");
		bronzeCollectEffect1 = vfxSheet.createSprites("coin_collect_bronze1");
		bronzeCollectEffect2 = vfxSheet.createSprites("coin_collect_bronze2"); 
		silverCollectEffect1 = vfxSheet.createSprites("coin_collect_silver1"); 
		silverCollectEffect2 = vfxSheet.createSprites("coin_collect_silver2"); 
		goldCollectEffect1 = vfxSheet.createSprites("coin_collect_gold1"); 
		goldCollectEffect2 = vfxSheet.createSprites("coin_collect_gold2");
		
		diamondCyanGlow = vfxSheet.createSprite("diamond_cyan_glow");
		diamondGreenGlow = vfxSheet.createSprite("diamond_green_glow");
		diamondPurpleGlow = vfxSheet.createSprite("diamond_purple_glow");
		tokenGlow = vfxSheet.createSprite("token_glow");
		
		TextureAtlas powerupSheet = new TextureAtlas("vfx/powerup.txt");
		magnetBackGlow = powerupSheet.createSprite("powerup_magnet_backglow");
		slomoBackGlow = powerupSheet.createSprite("powerup_timebend_backglow");
		shieldBackGlow = powerupSheet.createSprite("powerup_shield_backglow");
		
		playerShieldEffect = powerupSheet.createSprites("powerup_shield_animated_glow_middle");
		
		greenDiamondCollectEffect1 = vfxSheet.createSprites("coin_collect_green1"); 
		greenDiamondCollectEffect2 = vfxSheet.createSprites("coin_collect_green2"); 
		cyanDiamondCollectEffect1 = vfxSheet.createSprites("coin_collect_cyan1"); 
		cyanDiamondCollectEffect2 = vfxSheet.createSprites("coin_collect_cyan2"); 
		purpleDiamondCollectEffect1 = vfxSheet.createSprites("coin_collect_purple1"); 
		purpleDiamondCollectEffect2 = vfxSheet.createSprites("coin_collect_purple2"); 
		coinCrushedEffect = vfxSheet.createSprites("loot_crushed_puff");
		
		burningWallGlow = vfxSheet.createSprite("fire_glow");
		
		TextureAtlas enemiesSheet = new TextureAtlas("enemies/enemies.txt");
		burningWall = enemiesSheet.createSprite("burning_wall");
		burningWallStart = enemiesSheet.createSprites("burning_wall_start");
		burningWallBurn = enemiesSheet.createSprites("burning_wall_burn");
		burningWallEnd = enemiesSheet.createSprites("burning_wall_end");
		
		flamethrower = enemiesSheet.createSprite("flamethrower");
		flamethrowerAll = enemiesSheet.createSprites("flamethrower_all");
		
		spitter = enemiesSheet.createSprites("wall_spitter_sequence");
		
		TextureAtlas enemiesAddSheet1 = new TextureAtlas("enemies/enemies_add_1.txt");
		flamethrowerFlame = enemiesAddSheet1.createSprites("flamethrower_flame");
		fireballExplodeEffect = enemiesAddSheet1.createSprites("fireball_end");
		topFireballLoop = enemiesAddSheet1.createSprites("fireball_loop");
		
		TextureAtlas enemiesAddSheet2 = new TextureAtlas("enemies/enemies_add_2.txt");
		fireballStartEffect = enemiesAddSheet2.createSprites("fireball_start");
	}
}
