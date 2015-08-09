package com.retrom.volcano.effects;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.assets.Assets;

public class EffectFactory {
	
	private static Random rand = new Random();
	
	public static Effect getPlayerExplodeEffect(Vector2 position) {
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
	
}
