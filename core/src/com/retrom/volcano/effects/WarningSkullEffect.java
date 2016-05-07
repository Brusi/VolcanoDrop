package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;

public class WarningSkullEffect extends OneFrameEffect {
	
	private static final float DURATION = 1.25f;
	public static final float APPEAR_TIME = 0.2f;
	public static final float DISAPPEAR_TIME = 1 / 4f;
	public static final float DISTANCE_FROM_TOP = 87;
	
	public final float originalY; 

	public WarningSkullEffect(Vector2 position) {
		super(Assets.warningSkull, DURATION, position);
		originalY = position.y;
	}
	
	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
	
	@Override
	public float getScale() {
		float st = stateTime_;
		if (st < APPEAR_TIME * 4 / 5) {
			return st / (APPEAR_TIME * 4 / 5) * 1.2f;
		} else if (st < APPEAR_TIME) {
			float t = (st - APPEAR_TIME * 4 / 5) / APPEAR_TIME * 5;
			return 1f * t + 1.2f * (1-t);
		}
		return 1;
//		return Math.min(1, stateTime_ / APPEAR_TIME);
	}
	
//	@Override
//	public float getTint() {
//		float timeToEnd = DURATION - stateTime_;
//		return Math.max(0, Math.min(1, timeToEnd / DISAPPEAR_TIME));
//	}
	
	@Override
	public float getAlpha() {
		float timeToEnd = DURATION - stateTime_;
		return Math.max(0, Math.min(1, timeToEnd / DISAPPEAR_TIME));
	}
}
