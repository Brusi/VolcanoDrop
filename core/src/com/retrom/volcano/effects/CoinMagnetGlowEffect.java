package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.objects.Collectable;

public class CoinMagnetGlowEffect extends OneFrameEffect {
	
	private static final float INF_DURATION = 60000f;
	private static final float FADE_DURATION = 0.25f;
	
	private final Collectable coin_;
	private boolean end_ = false; 

	public CoinMagnetGlowEffect(Collectable coin) {
		super(Assets.playerMagnetGlow, INF_DURATION, coin.position);
		coin_ = coin;
	}
	
	@Override
	public float getTint() {
		if (!end_) {
			return Math.min(1, stateTime() / FADE_DURATION);
		} else {
			float val = 1 - stateTime() / FADE_DURATION;
			return Math.max(0, Math.min(1, val));
		}
	}
	
	public void endAnim() {
		if (end_) {
			return;
		}
		end_ = true;
		stateTime_ = 0;
	}
	
	@Override
	protected void childSpecificUpdating(float deltaTime) {
		if (end_ && stateTime_ > FADE_DURATION) {
			endAnim();
			return;
		}
		if (coin_.state() == Collectable.STATUS_TAKEN || coin_.state() == Collectable.STATUS_CRUSHED) {
			endAnim();
		}
	}
	
	@Override
	public float getScale() {
		return 0.5f;
	}
}
