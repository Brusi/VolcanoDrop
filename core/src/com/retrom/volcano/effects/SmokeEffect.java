package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Utils;

public class SmokeEffect extends Particle {
	
	private static final float DURATION = 3;
	private static final Vector2 UPVEL = new Vector2(0, 20);
	
	private float scale;
	private float rotation;
	private float rotationVel;
	
	public SmokeEffect(Vector2 position) {
		this(position.x, position.y);
	}
	
	public SmokeEffect(float x, float y) {
		super(Utils.randomBool() ? Assets.smoke.get(0) : Assets.smoke.get(1),
				DURATION,
				new Vector2(x, y), 
				UPVEL);
		
		scale = (float) (Math.random() * 1 + 1);
		rotation = (float) (Math.random() * 360f);
		rotationVel = (float) (Math.random() * 60 - 30);
	}
	
	@Override
	public float getScale() {
		return scale;
	}
	
	@Override
	public float getTint() {
		if (stateTime() < 0.2f) {
			return stateTime() / 0.2f;
		}
		return Math.min(1, (DURATION - stateTime()) * 2 / DURATION);
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
