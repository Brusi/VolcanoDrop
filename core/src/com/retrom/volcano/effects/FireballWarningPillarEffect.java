package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.game.objects.TopFireball;

public class FireballWarningPillarEffect extends OneFrameEffect {
	
	private static final float DURATION = 2.9f; 
	private static final float TIME_100_PERCENT = TopFireball.PREPARATION_DELAY;
	public static final float Y_OFFSET = 507 - 82;
	private final boolean overlay;

	public FireballWarningPillarEffect(Vector2 position, boolean overlay) {
		super(Assets.fireballWarningPillarEffect, DURATION, position);
		this.overlay = overlay;
	}
	
	@Override
	public float getTint() {
		if (stateTime_ < TIME_100_PERCENT) {
			if (overlay) return 0;
			return Utils.limit01(stateTime_ / TIME_100_PERCENT);
		}
		float t = (stateTime_ - TIME_100_PERCENT) / (DURATION - TIME_100_PERCENT);
		t *= 1.2f;
		return Utils.limit01(1-t); 
	}
	
	@Override
	public float getXScale() {
		// XScale should be the same as tint in this effect animation. 
		return getTint();
	}
}
