package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;

public class WarningExclEffect extends OneFrameEffect {
	
	public static final float APPEAR_TIME = 0.24f;
	private static final float DURATION = APPEAR_TIME * 3 + WarningSkullEffect.APPEAR_TIME;
	public static final float DISTANCE_FROM_TOP = 35;
	
	public final boolean floating;
	
	public WarningExclEffect(Vector2 position, boolean floating) {
		super(Assets.warningExcl, DURATION, position);
		this.floating = floating;
	}
	
	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
	
	@Override
	public float getAlpha() {
		if (stateTime_ < WarningSkullEffect.APPEAR_TIME)
			return 0;
		else
			return 1;
	}
	
	@Override
	public float getScale() {
		float st = stateTime_ - WarningSkullEffect.APPEAR_TIME;
		while (st - APPEAR_TIME > 0) st -= APPEAR_TIME;
		
		if (st < APPEAR_TIME * 4 / 6) {
			return st / (APPEAR_TIME * 4 / 6) * 1.2f;
		} else if (st < APPEAR_TIME * 5 / 6) {
			float t = (st - APPEAR_TIME * 4 / 6) / APPEAR_TIME * 6;
			return 1f * t + 1.2f * (1-t);
		} else {
			return 1;
		}
		
//		return Math.min(1, st / APPEAR_TIME);
	}
}
