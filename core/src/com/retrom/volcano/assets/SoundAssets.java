package com.retrom.volcano.assets;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.retrom.volcano.game.Settings;

public class SoundAssets {
	
	public static Music music;
	
	public static Sound[] wallHit;
	public static Sound[] wallDualHit;
	
	public static Sound[] coinsCollectBronze;
	public static Sound[] coinsCollectSilver;
	public static Sound[] coinsCollectGold;
	public static Sound[] coinsCollectDiamond;
	public static Sound[] coinsCollectRing;
	public static Sound coinsCollectGoldMask;
	public static Sound coinsCollectBigToken;
	
	public static Sound powerupMagnetStart;
	public static Sound powerupMagnetLoop;
	public static Sound powerupMagnetEnd;
	
	public static Sound playerDeathCrush;
	public static Sound playerDeathBurn;
	
	private static Random rand = new Random();
	
	public static void load() {
//
		music = Gdx.audio.newMusic(Gdx.files.internal("music/gameplay.mp3"));
		music.setLooping(true);
		music.setVolume(0.5f);
//		if (Settings.soundEnabled) music.play();
		
		wallHit = new Sound[] {newSound("x1_rock_hit_1.wav"), newSound("x1_rock_hit_2.wav"), newSound("x1_rock_hit_3.wav"), newSound("x1_rock_hit_3.wav")};
		wallDualHit = new Sound[] {newSound("x2_rock_hit_1.wav"), newSound("x2_rock_hit_2.wav")};
		
		// Coins:
		coinsCollectBronze = new Sound[] {
				newSound("coins_collect_bronze_1.wav"),
				newSound("coins_collect_bronze_2.wav") };
		
		coinsCollectSilver = new Sound[] {
				newSound("coins_collect_silver_1.wav"),
				newSound("coins_collect_silver_2.wav") };
		
		coinsCollectGold = new Sound[] {
				newSound("coins_collect_gold_1.wav"),
				newSound("coins_collect_gold_2.wav") };
		
		coinsCollectDiamond = new Sound[] {
				newSound("coins_collect_diamond_1.wav"),
				newSound("coins_collect_diamond_2.wav") };
		
		coinsCollectRing = new Sound[] {
				newSound("coins_collect_ring_1.wav"),
				newSound("coins_collect_ring_2.wav") };
		
		coinsCollectBigToken = newSound("coins_collect_bigtoken.wav");
		coinsCollectGoldMask = newSound("coins_collect_goldmask.wav");
		
		// Powerups:
		powerupMagnetStart = newSound("powerups_magnet_start.wav");
		powerupMagnetLoop = newSound("powerups_magnet_loop.wav");
		powerupMagnetEnd = newSound("powerups_magnet_end.wav");
		
		// Player death:
		playerDeathCrush = newSound("player_death_crushed.wav");
		playerDeathBurn = newSound("player_death_burn.wav");
	}
	
	public static void playSound (Sound sound) {
		if (Settings.soundEnabled) sound.play(1);
	}
	
	public static void playRandomSound(Sound[] sounds) {
		int index = rand.nextInt(sounds.length);
		playSound(sounds[index]);
	}
	
	private static Sound newSound(String filename) {
		return Gdx.audio.newSound(Gdx.files.internal("sound/" + filename));
	}

	public static void loopSound(Sound sound) {
		sound.stop();
		if (Settings.soundEnabled) sound.loop(1);
	}
	
	public static void stopSound(Sound sound) {
		sound.stop();
	}
	
	public static void stopAllSounds() {
		stopSound(powerupMagnetLoop);
	}
}
