package com.retrom.volcano.effects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Player;
import com.retrom.volcano.game.objects.Collectable;
import com.retrom.volcano.game.objects.Collectable.Type;
import com.retrom.volcano.game.objects.Wall;

public class EffectFactory {
	
	private static Random rand = new Random();
	
	public static Effect playerExplodeEffect(Vector2 position) {
		return new FiniteAnimationEffect(Assets.playerExplode, position);
	}

	public static Effect bronzeCollectEffect(Vector2 position) {
		Array<Sprite> animation = rand.nextBoolean() ? Assets.bronzeCollectEffect1 : Assets.bronzeCollectEffect2;    
		return new FiniteAnimationEffect(animation, position);
	}
	
	public static Effect silverCollectEffect(Vector2 position) {
		Array<Sprite> animation = rand.nextBoolean() ? Assets.silverCollectEffect1 : Assets.silverCollectEffect2;    
		return new FiniteAnimationEffect(animation, position);
	}
	
	public static Effect goldCollectEffect(Vector2 position) {
		Array<Sprite> animation = rand.nextBoolean() ? Assets.goldCollectEffect1 : Assets.goldCollectEffect2;    
		return new FiniteAnimationEffect(animation, position);
	}
	
	public static Effect greenDiamondCollectEffect(Vector2 position) {
		Array<Sprite> animation = rand.nextBoolean() ? Assets.greenDiamondCollectEffect1 : Assets.greenDiamondCollectEffect2;    
		return new FiniteAnimationEffect(animation, position);
	}
	
	public static Effect purpleDiamondCollectEffect(Vector2 position) {
		Array<Sprite> animation = rand.nextBoolean() ? Assets.purpleDiamondCollectEffect1 : Assets.purpleDiamondCollectEffect2;    
		return new FiniteAnimationEffect(animation, position);
	}
	
	public static Effect cyanDiamondCollectEffect(Vector2 position) {
		Array<Sprite> animation = rand.nextBoolean() ? Assets.cyanDiamondCollectEffect1 : Assets.cyanDiamondCollectEffect2;    
		return new FiniteAnimationEffect(animation, position);
	}

	public static Effect coinCrushedEffect(Vector2 position) {
		FiniteAnimationEffect $ = new FiniteAnimationEffect(Assets.coinCrushedEffect, position);
		$.setRandomScale(0.75f, 1f);
		return $;
	}
	
	public static Effect playerSlomoEffect(Vector2 position) {
		return new FiniteAnimationEffect(Assets.playerSlomoEffect, position);
	}
	
	public static Effect powerupCrushedEffect(Collectable.Type type, Vector2 position) {
		switch (type) {
		case POWERUP_MAGNET:
			return new FiniteAnimationEffect(Assets.powerupMagnetCrushEffect, position);
		case POWERUP_SLOMO:
			return new FiniteAnimationEffect(Assets.powerupSlomoCrushEffect, position);
		case POWERUP_SHIELD:
			return new FiniteAnimationEffect(Assets.powerupShieldCrushEffect, position);
		default:
			Gdx.app.log("ERROR", "Type is not a powerup.");
			return null;
		}
	}

	public static Effect fireballExpodeEffect(Vector2 position) {
		FiniteAnimationEffect $ = new FiniteAnimationEffect(Assets.fireballExplodeEffect, position);
		return $;
	}
	
	public static Effect wallBreakParticle(Vector2 position) {
		return new WallBreakParticle(Assets.wallParticles, position);
	}
	public static Effect flameBreakParticle(Vector2 position) {
		Vector2 effect_position = position.cpy();
		effect_position.y += Wall.SIZE / 2;
		return new FlameBreakParticle(Assets.wallParticles, effect_position);
	}
	public static Effect wallExplodeEffect(Vector2 position) {
		return new FiniteAnimationEffect(Assets.wallExplode, position);
	}
	

	public static Effect coinCrushParticle(Collectable.Type type, Vector2 position) {
		switch (type) {
		case COIN_1_1:
		case COIN_1_2:
			return new CoinBreakParticle(Assets.bronzeCoinBreak, position);
		case COIN_2_1:
		case COIN_2_2:
		case COIN_2_3:
			return new CoinBreakParticle(Assets.silverCoinBreak, position);
		case COIN_3_1:
		case COIN_3_2:
		case COIN_3_3:
		case COIN_4_1:
		case COIN_4_2:
		case COIN_4_3:
		case COIN_5_1:
			return new CoinBreakParticle(Assets.goldCoinBreak, position);
		case COIN_5_2:
			return new CoinBreakParticle(Assets.cyanDiamondBreak, position);
		case COIN_5_3:
			return new CoinBreakParticle(Assets.purpleDiamondBreak, position);
		case COIN_5_4:
			return new CoinBreakParticle(Assets.greenDiamondBreak, position);
		case POWERUP_MAGNET:
			// TODO: change to actual powerup particle.
			return new CoinBreakParticle(Assets.greenDiamondBreak, position);
		case POWERUP_SLOMO:
			// TODO: change to actual powerup particle.
			return new CoinBreakParticle(Assets.purpleDiamondBreak, position);
		case POWERUP_SHIELD:
			// TODO: change to actual powerup particle.
			return new CoinBreakParticle(Assets.silverCoinBreak, position);
		default:
			Gdx.app.error("ERROR", "Unsupported collectable particle.");
		}
		return null;
	}

	public static Effect powerupBackGlow(Type type, Collectable c) {
		switch (type) {
		case POWERUP_MAGNET:
			return new PowerupGlow(Assets.magnetBackGlow, c);
		case POWERUP_SLOMO:
			return new PowerupGlow(Assets.slomoBackGlow, c);
		case POWERUP_SHIELD:
			return new PowerupGlow(Assets.shieldBackGlow, c);
		default:
			Gdx.app.error("ERROR", "Type is not a powerup, no powerup back glow.");
			break;
		}
		return null;
	}

	public static Effect powerupAura(Type type, Collectable c) {
		switch (type) {
		case POWERUP_MAGNET:
			return new PowerupAura(Assets.magnetAura, c);
		case POWERUP_SLOMO:
			return new PowerupAura(Assets.slomoAura, c);
		case POWERUP_SHIELD:
			return new PowerupAura(Assets.shieldAura, c);
		default:
			Gdx.app.error("ERROR", "Type is not a powerup, no powerup back glow.");
			break;
		}
		return null;
	}
	
	public static Effect powerupFlare(Type type, Collectable c) {
		switch (type) {
		case POWERUP_MAGNET:
			return new PowerupFlare(Assets.magnetFlare, c);
		case POWERUP_SLOMO:
			return new PowerupFlare(Assets.slomoFlare, c);
		case POWERUP_SHIELD:
			return new PowerupFlare(Assets.shieldFlare, c);
		default:
			Gdx.app.error("ERROR", "Type is not a powerup, no powerup back glow.");
			break;
		}
		return null;
	}

	public static Effect powerupAppearEffect(Type type, Vector2 position) {
		switch (type) {
		case POWERUP_MAGNET:
			return new FiniteAnimationEffect(Assets.powerupMagnetAppearSpark, position);
		case POWERUP_SLOMO:
			return new FiniteAnimationEffect(Assets.powerupSlomoAppearSpark, position);
		case POWERUP_SHIELD:
			return new FiniteAnimationEffect(Assets.powerupShieldAppearSpark, position);
		default:
			Gdx.app.error("ERROR", "Type is not a powerup, no appear effect.");
			break;
		}
		return null;
	}
	
	public static Effect powerupSlomoDisappearEffect(Vector2 position) {
		return new FiniteAnimationEffect(Assets.powerupSlomoAppearSparkReversed, position);
	}
	
	public static Effect playerMagnetGlowDie(Vector2 position) {
		Vector2 pos = position.cpy();
		pos.y += 20;
		return new FiniteAnimationEffect(Assets.playerMagnetGlowDie, pos, false);
	}
	
	public static Effect playerJumpPuff(Vector2 player_position) {
		Vector2 pos = player_position.cpy();
		return new FiniteAnimationEffect(Assets.doubleJumpEffect, pos, false);
	}
	
	public static Effect playerJumpPuffLeftWall(Vector2 player_position) {
		Vector2 pos = player_position.cpy();
		pos.y -= 50f;
		pos.x += 10f;
		FiniteAnimationEffect e = new FiniteAnimationEffect(Assets.jumpPuff, pos, false);
		e.setConstantNoRotation(90);
		return e;
	}
	
	public static Effect playerJumpPuffRightWall(Vector2 player_position) {
		Vector2 pos = player_position.cpy();
		pos.y -= 50f;
		pos.x -= 10f;
		FiniteAnimationEffect e = new FiniteAnimationEffect(Assets.jumpPuff, pos, false);
		e.setConstantNoRotation(-90);
		return e;
	}
}
