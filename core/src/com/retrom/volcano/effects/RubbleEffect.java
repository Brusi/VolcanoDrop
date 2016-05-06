package com.retrom.volcano.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.World;

public class RubbleEffect extends OneFrameEffect {

	private float rotation_;
	private float rotationSpeed_;
	
	private static float MAX_ROTATION = 1600f;

	public RubbleEffect(float duration, Vector2 position) {
		super(Assets.wallParticles.random(), duration, position);
		rotation_ = (float) (Math.random() * 360);
		rotationSpeed_ = (float) (Math.random() * MAX_ROTATION - MAX_ROTATION / 2);
	}
	
	@Override
	protected void childSpecificUpdating(float deltaTime) {
		velocity_.y += World.gravity.y * deltaTime * 0.5;
		rotation_ += rotationSpeed_ * deltaTime;
	}

	@Override
	public float getRotation() {
		return rotation_;
	}
}
