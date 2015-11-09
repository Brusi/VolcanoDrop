package com.retrom.volcano.game.objects;

import java.util.List;

import com.retrom.volcano.effects.Effect;

public class Flame extends Enemy {
	
	public static final float DURATION = 3f;
	public static final float OFF_ANIM_TIME = 0.3f;
	private List<Effect> associatedEffects_;

	public Flame(float x, float y, List<Effect> associatedEffects) {
		super(x, y, 30, 30);
		associatedEffects_ = associatedEffects;
	}
	
	@Override
	protected void childSpecificUpdating() {
		if (stateTime() >= DURATION) {
			state_ = Enemy.STATE_DONE;
		}
	}

	@Override
	public <T> T accept(Visitor<T> v) {
		return v.visit(this);
	}

	public void turnOff() {
		stateTime_ = DURATION - OFF_ANIM_TIME;
		for (Effect effect : associatedEffects_) {
			effect.advanceStateTime(stateTime_);
		}
	}
}
