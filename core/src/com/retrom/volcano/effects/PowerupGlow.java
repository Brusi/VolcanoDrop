package com.retrom.volcano.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.retrom.volcano.game.objects.Collectable;

public class PowerupGlow extends OneFrameEffect {

	private final static float DURATION = 20f;
	
	public final Collectable c;
	
	private float rotation;

	public PowerupGlow(Sprite sprite, Collectable c) {
		super(sprite, DURATION, c.position.cpy());
		this.c = c;
		rotation = (float) Math.random() * 360;
	}
	
	@Override
	protected void childSpecificUpdating(float deltaTime) {
		rotation += deltaTime * 80;
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
	public float getRotation() {
		return rotation;
	}
}