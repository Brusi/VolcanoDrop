package com.retrom.volcano.effects;

import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.objects.Enemy;

public class FireballGlow extends OneFrameEffect {
	
	private static float DURATION = 5f;
	private final Enemy fireball_;
	private int side_;

	public FireballGlow(Enemy fireball, int side) {
		super(Assets.burningWallGlow, DURATION, fireball.position.cpy());
		this.fireball_ = fireball;
		this.side_ = side;
	}
	
	@Override
	protected void childSpecificUpdating(float deltaTime) {
		float offsetX = 0;
		float offsetY = 0;
		switch (side_) {
		case FireballAnimationEffect.DOWN:
			offsetY = -30;
			break;
		case FireballAnimationEffect.LEFT:
			offsetX = 30;
			break;
		case FireballAnimationEffect.RIGHT:
			offsetX = -30;
			break;
		}

		this.position_.x = fireball_.position.x + offsetX;
		this.position_.y = fireball_.position.y + offsetY;
	}
	
	@Override
	public float getScale() {
		return 0.5f;
	}
	
	@Override
	public float getTint() {
		if (fireball_.state() == Effect.STATE_DONE) {
			return 0;
		}
		return 1;
	}
}
