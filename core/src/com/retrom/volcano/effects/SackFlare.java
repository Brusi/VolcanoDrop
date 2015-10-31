package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;

public class SackFlare extends Particle {
	
	private static final Vector2 vel = new Vector2(0, 15f);

	public SackFlare(Vector2 position) {
		super(Assets.sackFlare, 2, position.cpy(), vel);
		position_.x += Math.random() * 24 - 12;
		position_.y += 12;
	}
	
	@Override
	public float getScale() {
		if (stateTime() < 0.5) {
			return stateTime() * 2;
		} else if (stateTime() > 1.5) {
			return (2 - stateTime()) * 2;
		}
		return 1;
	}

}
