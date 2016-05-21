package com.retrom.volcano.menus;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class GraphicObject {
	
	public Vector2 position_;
	protected float stateTime_;
	
	float alpha_ = 1;
	float tint_ = 1;
	
	public GraphicObject(float x, float y) {
		position_ = new Vector2(x, y);
		stateTime_ = 0;
	}
	
	abstract protected Sprite getSprite();
	
	public void update(float deltaTime) {
		stateTime_ += deltaTime;
	}
	
	public void setAlpha(float alpha) {
		this.alpha_ = alpha;
	}
	
	public void setTint(float alpha) {
		this.tint_ = alpha;
	}
	
	public void render(SpriteBatch batch) {
		Sprite s = getSprite();
		s.setPosition(position_.x - s.getWidth() / 2, position_.y - s.getHeight() / 2);
		s.setFlip(false, false);
		s.setColor(tint_,tint_,tint_,1);
		s.setAlpha(alpha_);
		s.draw(batch);
	}
}
