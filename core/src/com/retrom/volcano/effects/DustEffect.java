package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;

public class DustEffect extends Particle {
	
	private static final float DURATION = 2;
	private static final Vector2 DOWNVEL = new Vector2(0, -20);
	
	private float scale;
	private float rotation;
	private float rotationVel;
	
	public DustEffect(Vector2 position) {
		this(position.x, position.y);
	}
	
	public DustEffect(float x, float y) {
		super(Math.random() > 0.5 ? Assets.dust.get(0) : Assets.dust.get(1),
				DURATION,
				new Vector2(x, y), 
				DOWNVEL);
		
		setScale((float) (Math.random() * 1 + 1));
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

	public void setScale(float scale) {
		this.scale = scale;
	}
}
