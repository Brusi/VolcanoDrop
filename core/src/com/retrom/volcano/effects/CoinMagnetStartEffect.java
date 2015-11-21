package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;

public class CoinMagnetStartEffect extends FiniteAnimationEffect {

	public static CoinMagnetStartEffect create(Vector2 position) {
		return new CoinMagnetStartEffect(position, false);
	}
	
	public static CoinMagnetStartEffect createReversed(Vector2 position) {
		return new CoinMagnetStartEffect(position, true);
	}
	
	private CoinMagnetStartEffect(Vector2 position, boolean reverse) {
		super(reverse ? Assets.powerupMagnetAppearSpark
				: Assets.powerupMagnetAppearSparkReversed, position);
	}
	
	@Override
	public float getScale() {
		return 0.7f;
	}

}
