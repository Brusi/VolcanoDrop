package com.retrom.volcano.effects;

import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.objects.BurningWall;
import com.retrom.volcano.game.objects.Wall;

public class BurningWallGlow extends OneFrameEffect {
	
	private static float DURATION = 5f;
	private final BurningWall wall_;

	public BurningWallGlow(BurningWall wall) {
		super(Assets.burningWallGlow, DURATION, wall.position);
		this.wall_ = wall;
	}
	
	@Override
	public float getScale() {
		return 1f;
	}
	

	@Override
	protected void childSpecificUpdating(float deltaTime) {
		if (wall_.status() == Wall.STATUS_GONE) {
			this.destroy();
		}
	}
	
	@Override
	public float getTint() {
		if (wall_.status() == Wall.STATUS_ACTIVE) {
			if (wall_.stateTime() < BurningWall.TIME_WITHOUT_BURN) {
				return 0.2f;
			}
			return Math.min(1,
					0.2f + (wall_.stateTime() - BurningWall.TIME_WITHOUT_BURN) * 2);
		}
		if (wall_.status() == Wall.STATUS_INACTIVE) {
			return Math.max(0, 1 - wall_.stateTime() * 2);
		}
		return 1;
	}
}
