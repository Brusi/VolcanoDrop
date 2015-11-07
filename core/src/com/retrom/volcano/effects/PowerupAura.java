package com.retrom.volcano.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.retrom.volcano.game.objects.Collectable;

public class PowerupAura extends OneFrameEffect {

	private final static float DURATION = 20f;
	
	public final Collectable c;
	
	public PowerupAura(Sprite sprite, Collectable c) {
		super(sprite, DURATION, c.position);
		this.c = c;
	}
	
	@Override
	protected void childSpecificUpdating(float deltaTime) {
		if (c.state() == Collectable.STATUS_CRUSHED
				|| c.state() == Collectable.STATUS_TAKEN) {
			this.state_ = Effect.STATE_DONE;
		}
	}
	
	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
	
	@Override
	public float getScale() {
		float tint = (float) (0.8 + (Math.sin(stateTime() * 3) + 1) / 10);
		return tint;
	};
}