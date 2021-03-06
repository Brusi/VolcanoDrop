package com.retrom.volcano.assets;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.retrom.volcano.game.Settings;
import com.retrom.volcano.game.objects.Collectable.Type;
import com.retrom.volcano.utils.SoundEvictingQueue;

public class SoundAssets {
	
	public static Music gameMusic;
	public static long musicid;
	
	public static Music shopMusic;
	public static Music menuMusic;
	
	public static Sound[] playerJump;
	public static Sound[] playerJumpIntense;
	
	public static Sound[] wallHit;
	public static Sound[] wallDualHit;
	
	public static Sound[] quake;
	public static Sound[] quakeSmall;
	
	public static Sound[] coinsCollectBronze;
	public static Sound[] coinsCollectSilver;
	public static Sound[] coinsCollectGold;
	public static Sound[] coinsCollectDiamond;
	public static Sound[] coinsCollectRing;
	public static Sound coinsCollectGoldMask;
	public static Sound coinsCollectBigToken;
	
	public static Sound coinsRelic;
	
	public static Sound coinSackStart;
	public static Sound[] coinSackHit;
	public static Sound coinSackEnd;
	
	public static Sound coinCrushed;
	public static Sound powerupCrushed;
	
	public static Sound powerupMagnetStart;
	public static Sound powerupMagnetLoop;
	public static Sound powerupMagnetEnd;
	public static Sound powerupTimeStart;
	public static Sound powerupTimeEnd;
	public static Sound powerupShieldStart;
	public static Sound powerupShieldHit;
	public static Sound powerupShieldEnd;
	
	public static Sound playerDeathCrush;
	public static Sound playerDeathBurn;
	
	public static Sound burningWallStart;
	public static Sound burningWallEnd;
	public static Sound flamethrowerStart;
	public static Sound flamethrowerEnd;
	
	public static Sound fireballStart;
	public static Sound fireballEnd;
	public static Sound spitterSequence;
	
	public static Sound warning;
	
	public static Sound shopClick;
	public static Sound shopShamanOpenPop;
	
	public static Sound[] rockLava;
	public static Sound coinLava;
	public static Sound[] lavaBubble;
	
	// Boss
	public static Sound[] bossLaugh; 
	public static Sound[] bossFall;
	
	private static Random rand = new Random();
	
	private static final boolean ENABLE_OVERALL_SOUNDS = true;
	
	private final static SoundEvictingQueue currentlyPlaying = new SoundEvictingQueue(40);
	private static float pitch = 1f;
    // This should be a reference to the asset manager owned by Assets.
    private static AssetManager assetManager;

    public static void preload(AssetManager assetManager) {
        if (!ENABLE_OVERALL_SOUNDS) {
            return;
        }

        SoundAssets.assetManager = assetManager;
        {
            FileHandle dirHandle = Gdx.files.internal("sound/vfx");
            for (FileHandle fileHandle : dirHandle.list()) {
                assetManager.load(fileHandle.path(), Sound.class);
            }
        }
        {
            FileHandle dirHandle = Gdx.files.internal("sound/music");
            for (FileHandle fileHandle : dirHandle.list()) {
                Gdx.app.log("INFO", fileHandle.path());
                assetManager.load(fileHandle.path(), Music.class);
            }
        }
	}
	
	public static void load() {
		if (!ENABLE_OVERALL_SOUNDS) {
			return;
		}
		gameMusic = newMusic("gameplay.mp3");
		gameMusic.setLooping(true);

//		music.setVolume(0.5f);
//		if (Settings.soundEnabled) music.play();
		
		shopMusic = newMusic("shop.mp3");
		shopMusic.setLooping(true);
		
		menuMusic = newMusic("menu.mp3");
		menuMusic.setLooping(true);
		
		playerJump = new Sound[] {newSound("player_jump_0a.wav"), newSound("player_jump_0b.wav")};
		playerJumpIntense = new Sound[] {newSound("player_jump_1.wav"), newSound("player_jump_2.wav"), newSound("player_jump_3.wav"), newSound("player_jump_4.wav")};
		
		wallHit = new Sound[] {newSound("x1_rock_hit_1.wav"), newSound("x1_rock_hit_2.wav"), newSound("x1_rock_hit_3.wav"), newSound("x1_rock_hit_3.wav")};
		wallDualHit = new Sound[] {newSound("x2_rock_hit_1.wav"), newSound("x2_rock_hit_2.wav")};
		
		quake = new Sound[] {newSound("quake_1.wav"), newSound("quake_2.wav")};
		quakeSmall = new Sound[] {newSound("quake_small_1.wav"), newSound("quake_small_2.wav")};
		
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
		
		coinsRelic = newSound("coins_relic.wav");
		
		coinSackStart = newSound("coins_sack_start.wav");
		coinSackHit = new Sound[] { newSound("coins_sack_hit_1.wav"), newSound("coins_sack_hit_2.wav") };
		coinSackEnd = newSound("coins_sack_end.wav");
		
		coinCrushed = newSound("coins_crushed.wav");
		powerupCrushed = newSound("powerups_crushed.wav");
		
		// Powerups:
		powerupMagnetStart = newSound("powerups_magnet_start.wav");
		powerupMagnetLoop = newSound("powerups_magnet_loop.wav");
		powerupMagnetEnd = newSound("powerups_magnet_end.wav");
		
		powerupTimeStart = newSound("powerups_time_start.wav");
		powerupTimeEnd = newSound("powerups_time_end.wav");
		
		powerupShieldStart = newSound("powerups_shield_start.wav");
		powerupShieldHit = newSound("powerups_shield_hit.wav");
		powerupShieldEnd = newSound("powerups_shield_end.wav");
		
		// Player death:
		playerDeathCrush = newSound("player_death_crushed.wav");
		playerDeathBurn = newSound("player_death_burn.wav");
		
		//Enemies:
		burningWallStart = newSound("burner_rock_start.wav");
		burningWallEnd = newSound("burner_rock_end.wav");
		flamethrowerStart = newSound("flamethrower_rock_start.wav");
		flamethrowerEnd = newSound("flamethrower_rock_end.wav");
		
		fireballStart = newSound("fireball_start.wav");
		fireballEnd = newSound("fireball_end.wav");
		
		spitterSequence = newSound("wallspitter_sequence.wav");
		
		warning = newSound("warning.wav");
		
		shopClick = newSound("menus_shop_click.wav");
		shopShamanOpenPop = newSound("shop_shaman_openpop.wav");
		
		rockLava = new Sound[] { newSound("x1_rock_lava1.wav"), newSound("x1_rock_lava2.wav") };
		coinLava = newSound("coins_burned.wav");
		lavaBubble = new Sound[] { newSound("lava_bubble_1.wav"),
				                   newSound("lava_bubble_2.wav"),
				                   newSound("lava_bubble_3.wav"),
				                   newSound("lava_bubble_4.wav") };
		
		// Boss
		bossLaugh = new Sound[] { newSound("evilboss_laugh1.wav"),
								  newSound("evilboss_laugh1.wav"),
								  newSound("evilboss_laugh1.wav") };	
		
		bossFall = new Sound[] { newSound("evilboss_attacks_fall1.wav"),
				                  newSound("evilboss_attacks_fall1.wav") };
	}

    public static long playSound (Sound sound, float volume) {
		if (!ENABLE_OVERALL_SOUNDS) return 0;
		
		if (!Settings.soundEnabled.on())
			return 0;
		long id = sound.play(volume);
		currentlyPlaying.add(sound, id);
		sound.setPitch(id, pitch);
		return id;
	}
	
	public static long playSound(Sound sound) {
		return playSound(sound, 1);
	}
	
	public static void playRandomSound(Sound[] sounds) {
		playRandomSound(sounds, 1);
	}
	
	public static void playRandomSound(Sound[] sounds, float volume) {
		if (!ENABLE_OVERALL_SOUNDS) return;
		int index = rand.nextInt(sounds.length);
		playSound(sounds[index], volume);
	}
	
	private static Sound newSound(String filename) {
		return Gdx.audio.newSound(Gdx.files.internal("sound/vfx/" + filename));
//        return assetManager.get("sound/" + filename, Sound.class);
	}

    private static Music newMusic(String filename) {
        return Gdx.audio.newMusic(Gdx.files.internal("sound/music/" + filename));
//        return assetManager.get("sound/music/" + filename, Music.class);
    }

	public static long loopSound(Sound sound) {
		if (!ENABLE_OVERALL_SOUNDS) return 0;
		sound.stop();
		if (!Settings.soundEnabled.on()) {
			return 0;
		}
		long id = sound.loop(1);
		currentlyPlaying.add(sound, id);
		sound.setPitch(id, pitch);
		return id;
	}
	
	public static void stopSound(Sound sound) {
		if (!ENABLE_OVERALL_SOUNDS) return;
		sound.stop();
	}
	
	public static void stopAllSounds() {
		if (!ENABLE_OVERALL_SOUNDS) return;
		currentlyPlaying.forEach(new SoundEvictingQueue.Consumer() {
			@Override
			public void accept(Sound s, long id) {
				s.stop();
			}
		});
		currentlyPlaying.clear();
		
		stopAllMusic();
	}
	
	private static void stopAllMusic() {
		gameMusic.stop();
		menuMusic.stop();
		shopMusic.stop();
	}

	public static void setPitch(float newPitch) {
		if (newPitch == pitch) {
			return;
		}
		pitch = newPitch;
		currentlyPlaying.forEach(new SoundEvictingQueue.Consumer() {
			@Override
			public void accept(Sound s, long id) {
				s.setPitch(id, pitch);
			}
		});
	}
	
	public static void restart() {
		stopAllSounds();
		pitch = 1;
	}

	public static void pauseAllSounds() {
		currentlyPlaying.forEach(new SoundEvictingQueue.Consumer() {
			@Override
			public void accept(Sound s, long id) {
				s.pause();
			}
		});
	}

	public static void resumeAllSounds() {
		currentlyPlaying.forEach(new SoundEvictingQueue.Consumer() {
			@Override
			public void accept(Sound s, long id) {
				s.resume();
			}
		});
	}
	
	public static void startMusic() {
		if (!ENABLE_OVERALL_SOUNDS) return;
		stopAllMusic();
		if (!Settings.soundEnabled.on()) {
			return;
		}
		gameMusic.play();
	}

	public static void pauseMusic() {
		if (!ENABLE_OVERALL_SOUNDS) return;
		gameMusic.pause();
	}
	
	public static void resumeMusic() {
		if (!ENABLE_OVERALL_SOUNDS) return;
		if (!Settings.soundEnabled.on()) {
			return;
		}
		gameMusic.play();
	}
	
	public static void resumeMusicAt(float position) {
		if (!ENABLE_OVERALL_SOUNDS) return;
		
		if (!Settings.soundEnabled.on()) {
			return;
		}
		gameMusic.setPosition(position);
		gameMusic.play();
	}
	
	public static void startShopMusic() {
		if (!ENABLE_OVERALL_SOUNDS) return;
		if (!Settings.soundEnabled.on()) {
			return;
		}
		stopAllMusic();
		
		shopMusic.play();
	}
	
	public static void startMenuMusic() {
		if (!ENABLE_OVERALL_SOUNDS) return;
		stopAllMusic();
		if (!Settings.soundEnabled.on()) {
			return;
		}
		
		menuMusic.play();
	}
}
