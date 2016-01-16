package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Utils;

public class BurnParticle extends Particle {
	
	private static final float DURATION = 2;
	private static final Vector2 UPVEL = new Vector2(0, 20);
	
	private final float base_scale; 
	
	private float rotation;
	private float rotationVel;
	
	public BurnParticle(Vector2 position) {
		this(position.x, position.y);
	}
	
	public BurnParticle(float x, float y) {
		super(Assets.burnParticle.random(), DURATION + Utils.random2Range(0.5f), new Vector2(x, y), UPVEL);
		rotation = Utils.randomRange(0, 360f);
		rotationVel = Utils.randomRange(60, 100) * (Utils.randomBool() ? 1 : -1);
		base_scale = Utils.randomRange(0.6f, 1);
	}
	
	
	@Override
	public float getScale() {
		if (stateTime() < 0.2f) {
			return stateTime() / 0.2f;
		}
		return base_scale * Math.min(1, (duration() - stateTime()) * 3 / duration());
	}
	
	@Override
	public float getRotation() {
		return rotation;
	}
	
	@Override
	protected void childSpecificUpdating(float deltaTime) {
		rotation += rotationVel * deltaTime;
	}
}