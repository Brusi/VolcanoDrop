package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Utils;

public class MagnetTrailParticle extends FiniteAnimationEffect {
	private static final float SPEED = 20f;
	
	public MagnetTrailParticle(Vector2 position) {
		super(Assets.magnetTrailParticle, position.cpy(), false);
		velocity_ = Utils.randomDir();
		velocity_.x *= SPEED;
		velocity_.y *= SPEED;
	}
	
	@Override
	public float getScale() {
		return 0.60f;
	}
}
