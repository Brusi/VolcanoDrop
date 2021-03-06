package com.retrom.volcano.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.game.WorldRenderer;

public class FiniteAnimationEffect extends Effect {
	
	private Array<Sprite> animation_;
	private float rotation_ = 0;
	private float scale_ = 1f;

	public FiniteAnimationEffect(Array<Sprite> animation_, Vector2 position) {
		this(animation_, position, true);
	}
	
	public FiniteAnimationEffect(Array<Sprite> animation_, Vector2 position, boolean randomRotate) {
		super(getDuration(animation_), position);
		this.animation_ = animation_;
		if (randomRotate) {
			setRandomRotation();
		}
	}
	
	private static float getDuration(Array<Sprite> animation) {
		return animation.size * WorldRenderer.FRAME_TIME;
	}
	
	public void setRandomRotation() {
		rotation_ = (float) Math.random() * 360;
	}
	
	public void setNoRotation() {
		rotation_ = 0f;
	}
	
	public void setConstantNoRotation(float rotation) {
		rotation_ = rotation;
	}
	
	public void setRandomScale(float minScale, float maxScale) {
		scale_ = Utils.randomRange(minScale, maxScale); 
	}
	
	public void setScale(float scale) {
		scale_ = scale; 
	}
	
	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
	
	@Override
	public float getRotation() {
		return rotation_;
	}
	
	@Override
	public float getScale() {
		return scale_;
	}
	
	public Array<Sprite> getAnimation() {
		return animation_;
	}
}
