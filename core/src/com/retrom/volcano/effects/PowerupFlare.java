package com.retrom.volcano.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.retrom.volcano.game.objects.Collectable;

public class PowerupFlare extends OneFrameEffect {

	private final static float DURATION = 20f;
	
	private boolean isOnGround = false;
	
	public final Collectable c;
	
	public PowerupFlare(Sprite sprite, Collectable c) {
		super(sprite, DURATION, c.position.cpy());
		this.c = c;
	}
	
	@Override
	protected void childSpecificUpdating(float deltaTime) {
		if (c.state() == Collectable.STATUS_CRUSHED
				|| c.state() == Collectable.STATUS_TAKEN) {
			this.state_ = Effect.STATE_DONE;
		}
		
		if (!isOnGround && c.state() == Collectable.STATUS_IDLE) {
			isOnGround = true;
			stateTime_ = 0;
		}
		
		position_.x = c.position.x; 
		position_.y = c.position.y + 20;
	}
	
	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
	
	@Override
	public float getTint() {
		if (c.state() != Collectable.STATUS_IDLE) {
			return 0;
		}
		if (stateTime() <= 0.25) {
			return stateTime() * 4;
		} else {
			float x = stateTime() - 0.25f;
			return Math.max(0, 1 - 0.4f*x);
		}
	}
	
	
}