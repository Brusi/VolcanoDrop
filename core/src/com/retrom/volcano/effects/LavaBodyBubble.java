package com.retrom.volcano.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;

public class LavaBodyBubble extends OneFrameEffect {
	
	private static final float DURATION = 10;

	public LavaBodyBubble(Vector2 position) {
		super(Assets.bodyBubble.random(), DURATION, position);
		velocity_.y = 20f;
	}
	
	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		super.update(deltaTime);
	}
	
	@Override
	public Sprite sprite() {
		Sprite s = super.sprite();
		
		float scaleX = (float) Math.sin(stateTime_);
		float scaleY = (float) Math.cos(stateTime_);
		
		s.setScale(scaleX, scaleY);
		
		return s;
	}
	
}
