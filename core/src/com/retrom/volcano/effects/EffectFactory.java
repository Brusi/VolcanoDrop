package com.retrom.volcano.effects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.objects.Collectable;

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

	public static Effect fireballExpodeEffect(Vector2 position) {
		FiniteAnimationEffect $ = new FiniteAnimationEffect(Assets.fireballExplodeEffect, position);
		$.setNoRotation();
		return $;
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
			// TODO: only temporary.
			return new CoinBreakParticle(Assets.greenDiamondBreak, position);
		default:
			Gdx.app.error("ERROR", "Unsupported collectable particle.");
		}
		return null;
	}
}
