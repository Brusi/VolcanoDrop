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

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.game.Utils;
import com.sun.corba.se.impl.javax.rmi.CORBA.Util;

public class Assets {
	public static CostumeAssets defaultCostume;
	public static CostumeAssets blikCostume;
	public static CostumeAssets goboCostume;
	
	public static Array<Sprite> walls1;
	public static Array<Sprite> walls2;
	public static Array<Sprite> wallParticles;
	
	public static Array<Sprite> pillars;
	public static Array<Sprite> pillars_big;
	public static Sprite pillars_start;
	public static Sprite pillars_end;
	public static Sprite pillars_hole;
	public static Sprite pillars_hole_bg;
	
	public static Sprite background;
	public static Sprite floor;
	
	public static Array<Sprite> bg_world1;
	public static Array<Sprite> bg_overlay_world1;
	
	public static Array<Sprite> bg_world2;
	public static Array<Sprite> bg_overlay_world2;
	
	public static Array<Sprite> bg_world3;
	
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
	
	public static Sprite goldSackIdle;
	public static Sprite goldSackFalling;
	public static Array<Sprite> goldSackLand;
	public static Array<Sprite> goldSackPump;
	public static Array<Sprite> goldSackEnd;
	
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
	public static Array<Sprite> wallExplode;
	public static Array<Sprite> doubleJumpEffect;
	
	public static Array<Sprite> coinCrushedEffect;
	public static Array<Sprite> fireballExplodeEffect;
	public static Array<Sprite> fireballStartEffect;
	public static Array<Sprite> dust;
	public static Array<Sprite> smoke;
	public static Array<Sprite> burnParticle;
	public static Array<Sprite> jumpPuff;
	
	public static Sprite hotBrick;
	
	// Glow:
	public static Sprite burningWallGlow;
	
	public static Sprite diamondCyanGlow;
	public static Sprite diamondGreenGlow;
	public static Sprite diamondPurpleGlow;
	public static Sprite tokenGlow;
	
	public static Sprite coinFlare;
	public static Sprite sackFlare;
	
	public static Array<Sprite> powerupMagnetCrushEffect;
	public static Array<Sprite> powerupSlomoCrushEffect;
	public static Array<Sprite> powerupShieldCrushEffect;
	
	public static Array<Sprite> powerupMagnetAppearSpark;
	public static Array<Sprite> powerupSlomoAppearSpark;
	public static Array<Sprite> powerupShieldAppearSpark;
	
	public static Array<Sprite> powerupMagnetAppearSparkReversed;
	public static Array<Sprite> powerupSlomoAppearSparkReversed;
	
	public static Sprite magnetBackGlow;
	public static Sprite slomoBackGlow;
	public static Sprite shieldBackGlow;
	
	public static Sprite magnetAura;
	public static Sprite slomoAura;
	public static Sprite shieldAura;
	
	public static Sprite magnetFlare;
	public static Sprite slomoFlare;
	public static Sprite shieldFlare;
	
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

	public static Array<Sprite> playerShieldEffectStart;
	public static Array<Sprite> playerShieldEffect;
	public static Array<Sprite> playerShieldEffectHit;
	public static Array<Sprite> playerShieldEffectEnd;
	
	public static Array<Sprite> playerSlomoEffect;
	
	public static Array<Sprite> playerMagnetEffect;
	public static Sprite playerMagnetGlow;
	public static Array<Sprite> playerMagnetGlowDie;
	
	// Gui
	public static Sprite scoreIcon;
	public static Sprite pauseButton;
	public static Sprite pauseButtonClicked;
	
	// On screen warnings
	public static Sprite warningSkull;
	public static Sprite warningExcl;
	
	// Powerup ui
	public static Sprite magnetGui;
	public static Sprite slomoGui;
	public static Sprite shieldGui;
	public static Sprite magnetGuiOn;
	public static Sprite slomoGuiOn;
	public static Sprite shieldGuiOn;
	
	// On screen control
	public static Sprite leftRightControl;
	public static Sprite jumpControl;
	
	public static Sprite leftControlOn;
	public static Sprite rightControlOn;
	public static Sprite jumpControlOn;
	
	// Fonts
	public static BitmapFont scoreFont;
	public static BitmapFont timeFont;
	
	// Shop
	public static Sprite shopBg;
	public static Sprite shopMenuBg;
	public static Sprite shopMenuFg;
	
	public static Sprite bottomFadePowers;
	public static Sprite bottomFadeBlessings;
	public static Sprite bottomFadeCostumes;
	
	public static Array<Sprite> shamanIdle;
	public static Array<Sprite> shamanBuy;
	public static Sprite shopFg;
	public static Array<Sprite> shopFire;
	
	public static Sprite shopItemButtonBg;
	public static Array<Sprite> shopItemButtonBuy;
	public static Sprite shopItemButtonGoldSack;
	public static Sprite shopItemButtonOwn;
	public static Sprite shopItemButtonEquip;
	public static Sprite shopItemButtonEquipped;
	
	public static Sprite shopPrice1500;
	public static Sprite shopPrice4500;
	
	// Main shop menu items
	public static Sprite mainShopPowers;
	public static Sprite mainShopBlessings;
	public static Sprite mainShopCostumes;
	
	public static Sprite mainShopBg;
	
	public static Sprite shopBack;
	public static Sprite shopBackClick;
	
	public static Sprite shopExit;
	public static Sprite shopExitClick;
	
	// Shop items
	public static Map<Integer, Sprite> prices;
	
	public static Sprite shopItemAirStepIcon;
	public static Sprite shopItemAirStepTitle;
	
	public static Sprite shopItemWallFootIcon;
	public static Sprite shopItemWallFootTitle;
	
	public static Sprite shopItemChargeIcon;
	public static Sprite shopItemChargeTitle;
	
	public static Sprite shopItemCheetahrIcon;
	public static Sprite shopItemCheetahrTitle;
	
	public static Sprite shopItemSlowWalkerIcon;
	public static Sprite shopItemSlowWalkerTitle;
	
	public static Sprite shopItemFroggerIcon;
	public static Sprite shopItemFroggerTitle;
	
	public static Sprite shopItemDefaultCostumeIcon;
	public static Sprite shopItemDefaultCostumeTitle;
	
	// Pause menu
	public static Sprite pauseMenuBG;
	public static Sprite pauseMenuTitle;
	public static Sprite pauseOptionsButton;
	public static Sprite pauseOptionsButtonClicked;
	public static Sprite pauseShopButton;
	public static Sprite pauseShopButtonClicked;
	public static Sprite pauseResumeButton;
	public static Sprite pauseResumeButtonClicked;
	public static Sprite pauseRetryButton;
	public static Sprite pauseRetryButtonClicked;
	public static Sprite pauseQuestbox;
	public static Sprite pauseQuestboxCompleted;
	
	public static Sprite optionsMenuBG;
	public static Sprite optionsMenuTitle;
	public static Sprite optionsMenuBack;
	public static Sprite optionsMenuBackClicked;
	public static Sprite optionsMenuOn;
	public static Sprite optionsMenuOff;
	
	public static Sprite yesNoMenuBg;
	public static Sprite yesNoMenuTitle;
	public static Sprite yesButton;
	public static Sprite yesButtonClicked;
	public static Sprite noButton;
	public static Sprite noButtonClicked;
	

	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}
	
	public static void setFilterNearest(Array<Sprite> sprites) {
		for (Sprite sprite : sprites) {
			setFilterNearest(sprite);
		}
	}
	
	public static void setFilterNearest(Sprite sprite) {
		sprite.getTexture().setFilter(Texture.TextureFilter.Nearest,
				Texture.TextureFilter.Nearest);
	}

	public static void load() {
		defaultCostume = new CostumeAssets("player/playersheet.txt");
		
		
		TextureAtlas wallsSheet = new TextureAtlas("walls/walls.txt");
		walls1 = wallsSheet.createSprites("x1");
		walls2 = wallsSheet.createSprites("x2");
		wallParticles = wallsSheet.createSprites("wall_particle");

		TextureAtlas environmentSheet = new TextureAtlas("walls/enviroment.txt");
		pillars = environmentSheet.createSprites("pillars");
		setFilterNearest(pillars);
		pillars_big = environmentSheet.createSprites("pillars_big");
		setFilterNearest(pillars_big);
		pillars_start = environmentSheet.createSprite("pillars_start");
		setFilterNearest(pillars_start);
		pillars_end = environmentSheet.createSprite("pillars_end");
		setFilterNearest(pillars_end);
		pillars_hole = environmentSheet.createSprite("pillars_hole_front");
		setFilterNearest(pillars_hole);
		pillars_hole_bg = environmentSheet.createSprite("pillars_hole_back");
		setFilterNearest(pillars_hole_bg);
		floor = environmentSheet.createSprite("floor");
		setFilterNearest(floor);
		background = environmentSheet.createSprite("bg_base");
		setFilterNearest(background);
		
		bg_world1 = environmentSheet.createSprites("bg_world1");
		setFilterNearest(bg_world1);
		bg_overlay_world1 = environmentSheet.createSprites("bg_world1_base_1");
		setFilterNearest(bg_overlay_world1);
		
		TextureAtlas environmentSheet2 = new TextureAtlas("walls/enviroment2.txt");
		bg_world2 = environmentSheet2.createSprites("bg_world2");
		setFilterNearest(bg_world2);
		bg_overlay_world2 = environmentSheet2.createSprites("bg_world2_base_1");
		setFilterNearest(bg_overlay_world2);
		bg_world3 = environmentSheet2.createSprites("bg_world3");
		setFilterNearest(bg_world3);
		
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
		
		Array<Sprite> gold_sack_sprites = treasure.createSprites("gold_sack");
		goldSackIdle = gold_sack_sprites.get(0);
		goldSackFalling = gold_sack_sprites.get(1);
		goldSackLand = treasure.createSprites("gold_sack_landing/gold_sack_landing");
		goldSackPump = treasure.createSprites("gold_sack_pump/gold_sack_pump");
		goldSackEnd = treasure.createSprites("gold_sack_end/gold_sack_end");
		
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
		
		TextureAtlas playerVfxSheet = new TextureAtlas("vfx/playervfx.txt");
		playerExplode = playerVfxSheet.createSprites("player_die_explotion");
		wallExplode = playerVfxSheet.createSprites("StoneBreak_puff");
		
		TextureAtlas playerVfxSheet2 = new TextureAtlas("vfx/playervfx2.txt");
		doubleJumpEffect = playerVfxSheet2.createSprites("double_jump_puff");
		
		TextureAtlas vfxSheet = new TextureAtlas("vfx/vfx.txt");
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
		
		Array<Sprite> flares = vfxSheet.createSprites("coin_flare");
		coinFlare = flares.get(0);
		sackFlare = flares.get(1);
		
		powerupMagnetCrushEffect = vfxSheet.createSprites("magnet_crushed");
		powerupSlomoCrushEffect = vfxSheet.createSprites("timebend_crushed");
		powerupShieldCrushEffect = vfxSheet.createSprites("shield_crushed");
		
		powerupMagnetAppearSpark = vfxSheet.createSprites("powerup_magnet_appearance_spark");
		powerupSlomoAppearSpark = vfxSheet.createSprites("powerup_timebend_appearance_spark");
		powerupShieldAppearSpark = vfxSheet.createSprites("powerup_shield_appearance_spark");
		
		powerupSlomoAppearSparkReversed = new Array<Sprite>(powerupSlomoAppearSpark);
		powerupSlomoAppearSparkReversed.reverse();
		
		powerupMagnetAppearSparkReversed = new Array<Sprite>(powerupMagnetAppearSpark);
		powerupMagnetAppearSparkReversed.reverse();
		
		TextureAtlas powerupSheet = new TextureAtlas("vfx/powerup.txt");
		magnetBackGlow = powerupSheet.createSprite("powerup_magnet_backglow");
		slomoBackGlow = powerupSheet.createSprite("powerup_timebend_backglow");
		shieldBackGlow = powerupSheet.createSprite("powerup_shield_backglow");
		
		magnetAura = powerupSheet.createSprite("powerup_magnet_aura");
		slomoAura = powerupSheet.createSprite("powerup_timebend_aura");
		shieldAura = powerupSheet.createSprite("powerup_shield_aura");
		
		magnetFlare = powerupSheet.createSprite("powerup_magnet_ flare");
		slomoFlare = powerupSheet.createSprite("powerup_timebend_ flare");
		shieldFlare = powerupSheet.createSprite("powerup_shield_ flare");
		
		playerShieldEffectStart = powerupSheet.createSprites("powerup_shield_animated_glow_start");
		playerShieldEffect = powerupSheet.createSprites("powerup_shield_animated_glow_middle");
		playerShieldEffectHit = powerupSheet.createSprites("powerup_shield_animated_glow_hit");
		playerShieldEffectEnd = powerupSheet.createSprites("powerup_shield_animated_glow_die");
		
		playerSlomoEffect = powerupSheet.createSprites("powerup_timebend_animated_glow_die");
		
		TextureAtlas powerupMagnetSheet = new TextureAtlas("vfx/magnet.txt");
		playerMagnetEffect = powerupMagnetSheet.createSprites("powerup_magnet_animated_glow");
		playerMagnetGlow = powerupSheet.createSprite("powerup_magnet_glow");
		playerMagnetGlowDie = powerupMagnetSheet.createSprites("powerup_magnet_animated_glow_die");
		
		greenDiamondCollectEffect1 = vfxSheet.createSprites("coin_collect_green1"); 
		greenDiamondCollectEffect2 = vfxSheet.createSprites("coin_collect_green2"); 
		cyanDiamondCollectEffect1 = vfxSheet.createSprites("coin_collect_cyan1"); 
		cyanDiamondCollectEffect2 = vfxSheet.createSprites("coin_collect_cyan2"); 
		purpleDiamondCollectEffect1 = vfxSheet.createSprites("coin_collect_purple1"); 
		purpleDiamondCollectEffect2 = vfxSheet.createSprites("coin_collect_purple2"); 
		coinCrushedEffect = vfxSheet.createSprites("loot_crushed_puff");
		
		dust = vfxSheet.createSprites("dust");
		smoke = vfxSheet.createSprites("smoke");
		burnParticle = vfxSheet.createSprites("particles_burnpiece");
		jumpPuff = vfxSheet.createSprites("jump_puff");
		
		hotBrick = vfxSheet.createSprite("fire_glow_extrahotbricks_x1");
		
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
		
		// Gui
		TextureAtlas guiSheet = new TextureAtlas("ui/gui.txt");
		scoreIcon =  guiSheet.createSprite("gui_score_gold_icon");
		pauseButton= guiSheet.createSprite("GUI_buttons_pause");
		pauseButtonClicked = guiSheet.createSprite("GUI_buttons_pause_click");
		
		// Warnings
		warningSkull = guiSheet.createSprite("GUI_warnings_skull");
		warningExcl = guiSheet.createSprite("GUI_warnings_excmark");
		
		magnetGui = guiSheet.createSprite("gui_powerups_magnet");
		magnetGuiOn = guiSheet.createSprite("gui_powerups_magnet_on");
		slomoGui = guiSheet.createSprite("gui_powerups_timebend");
		slomoGuiOn = guiSheet.createSprite("gui_powerups_timebend_on");
		shieldGui = guiSheet.createSprite("gui_powerups_shield");
		shieldGuiOn = guiSheet.createSprite("gui_powerups_shield_on");
		
		
		
		leftRightControl = guiSheet.createSprite("GUI_MovementControls_LeftRight_body");
		jumpControl = guiSheet.createSprite("GUI_MovementControls_Jump_body");
		
		leftControlOn = guiSheet.createSprite("GUI_MovementControls_LeftRight_left_click");
		rightControlOn = guiSheet.createSprite("GUI_MovementControls_LeftRight_right_click");
		jumpControlOn = guiSheet.createSprite("GUI_MovementControls_Jump_up_click");
		
		scoreFont = new BitmapFont(Gdx.files.internal("ui/font/volcano_score.fnt"), false);
		timeFont = new BitmapFont(Gdx.files.internal("ui/font/volcano_time.fnt"), false);
		
		TextureAtlas shopBgSheet = new TextureAtlas("shop/shopbg.txt");
		shopBg = shopBgSheet.createSprite("shopmenu_bg");
		shopMenuBg = shopBgSheet.createSprite("shopmenu_menubox_bg");
		shopMenuFg = shopBgSheet.createSprite("shopmenu_menubox");
		
		bottomFadePowers = shopBgSheet.createSprite("shopmenu_menubox_buttomfade_powers");
		bottomFadeBlessings = shopBgSheet.createSprite("shopmenu_menubox_buttomfade_blessings");
		bottomFadeCostumes = shopBgSheet.createSprite("shopmenu_menubox_buttomfade_costume");
		
		TextureAtlas shopSheet = new TextureAtlas("shop/shop.txt");
		shamanIdle = shopSheet.createSprites("shopmenu_shaman_idle");
		shamanBuy = shopSheet.createSprites("shopmenu_shaman_buy");
		shopFg = shopSheet.createSprite("shopmenu_bg_foreground");
		shopFire = shopSheet.createSprites("shopmenu_fireplace");
		
		shopItemButtonBg = shopSheet.createSprite("shopmenu_item_button");
		shopItemButtonBuy = shopSheet.createSprites("shopmenu_item_button_buy");
		shopItemButtonGoldSack = shopSheet.createSprite("shopmenu_item_button_goldsackicon");
		shopItemButtonOwn = shopSheet.createSprite("shopmenu_item_button_ownicon");
		shopItemButtonEquip = shopSheet.createSprite("shopmenu_item_button_equipbutton");
		shopItemButtonEquipped = shopSheet.createSprite("shopmenu_item_button_equippedicon");
		
		shopPrice1500 = shopSheet.createSprite("1500_price");
		shopPrice4500 = shopSheet.createSprite("4500_price");
		
		mainShopPowers = shopSheet.createSprite("shopmenu_maintitles_powers");
		mainShopBlessings = shopSheet.createSprite("shopmenu_maintitles_blessings");
		mainShopCostumes = shopSheet.createSprite("shopmenu_maintitles_costume");
		mainShopBg = shopSheet.createSprite("shopmenu_maintitles_buttonbg");
		
		shopBack = shopSheet.createSprite("shopmenu_buttons_back");
		shopBackClick = shopSheet.createSprite("shopmenu_buttons_back_click");
		
		shopExit = shopSheet.createSprite("shopmenu_buttons_exit");
		shopExitClick = shopSheet.createSprite("shopmenu_buttons_exit_click");
		
		prices = Utils.createSpritesIndexMap(shopSheet, "shopmenu_items_cost");
		
		TextureAtlas shopItemsSheet = new TextureAtlas("shop/shopitems.txt");
		shopItemAirStepIcon = shopItemsSheet.createSprite("shopmenu_items_powers_airstep");
		shopItemAirStepTitle = shopItemsSheet.createSprite("shopmenu_items_powers_airstep_text");
		
		shopItemWallFootIcon = shopItemsSheet.createSprite("shopmenu_items_powers_wallfoot");
		shopItemWallFootTitle = shopItemsSheet.createSprite("shopmenu_items_powers_wallfoot_text");
		
		shopItemChargeIcon = shopItemsSheet.createSprite("shopmenu_items_powers_charge");
		shopItemChargeTitle = shopItemsSheet.createSprite("shopmenu_items_powers_charge_text");
		
		shopItemCheetahrIcon = shopItemsSheet.createSprite("shopmenu_items_powers_cheetahr");
		shopItemCheetahrTitle = shopItemsSheet.createSprite("shopmenu_items_powers_cheetahr_text");
		
		shopItemFroggerIcon = shopItemsSheet.createSprite("shopmenu_items_powers_frogger");
		shopItemFroggerTitle = shopItemsSheet.createSprite("shopmenu_items_powers_frogger_text");
		
		shopItemSlowWalkerIcon = shopItemsSheet.createSprite("shopmenu_items_powers_slowalker");
		shopItemSlowWalkerTitle = shopItemsSheet.createSprite("shopmenu_items_powers_slowalker_text");
		
		shopItemDefaultCostumeIcon = shopItemsSheet.createSprite("shopmenu_items_costume_default");
		shopItemDefaultCostumeTitle = shopItemsSheet.createSprite("shopmenu_items_costume_default_text");
		
		TextureAtlas pauseMenuSheet = new TextureAtlas("menu/pausemenu.txt");
		pauseMenuBG = pauseMenuSheet.createSprite("pausemenu_body");
		pauseMenuTitle = pauseMenuSheet.createSprite("pausemenu_title");
		pauseOptionsButton = pauseMenuSheet.createSprite("pausemenu_buttons_options");
		pauseOptionsButtonClicked = pauseMenuSheet.createSprite("pausemenu_buttons_options_click");
		pauseShopButton = pauseMenuSheet.createSprite("pausemenu_buttons_shop");
		pauseShopButtonClicked = pauseMenuSheet.createSprite("pausemenu_buttons_shop_click");
		pauseResumeButton = pauseMenuSheet.createSprite("pausemenu_buttons_resume");
		pauseResumeButtonClicked = pauseMenuSheet.createSprite("pausemenu_buttons_resume_click");
		pauseRetryButton = pauseMenuSheet.createSprite("pausemenu_buttons_retry");
		pauseRetryButtonClicked = pauseMenuSheet.createSprite("pausemenu_buttons_retry_click");
		pauseQuestbox = pauseMenuSheet.createSprite("pausemenu_questbox");
		pauseQuestboxCompleted = pauseMenuSheet.createSprite("pausemenu_questbox_completed");
		
		optionsMenuBG = pauseMenuSheet.createSprite("pausemenu_options_body");
		optionsMenuTitle = pauseMenuSheet.createSprite("pausemenu_options_title");
		optionsMenuBack = pauseMenuSheet.createSprite("pausemenu_options_buttons_back");
		optionsMenuBackClicked = pauseMenuSheet.createSprite("pausemenu_options_buttons_back_click");
		optionsMenuOn = pauseMenuSheet.createSprite("pausemenu_options_buttons_on");
		optionsMenuOff = pauseMenuSheet.createSprite("pausemenu_options_buttons_off");
		
		yesNoMenuBg = pauseMenuSheet.createSprite("pausemenu_rusuremenu_body");
		yesNoMenuTitle = pauseMenuSheet.createSprite("pausemenu_rusuremenu_text");
		yesButton = pauseMenuSheet.createSprite("pausemenu_rusuremenu_answers_yes");
		yesButtonClicked = pauseMenuSheet.createSprite("pausemenu_rusuremenu_answers_yes_click");
		noButton = pauseMenuSheet.createSprite("pausemenu_rusuremenu_answers_no");
		noButtonClicked = pauseMenuSheet.createSprite("pausemenu_rusuremenu_answers_no_click");
	}
}
