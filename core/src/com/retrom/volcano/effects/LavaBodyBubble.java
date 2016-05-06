package com.retrom.volcano.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Lava;
import com.retrom.volcano.game.Utils;

public class LavaBodyBubble extends OneFrameEffect {
	
	private static final float DURATION = 100;
	
	private static final float INIT_ADVANCE_POS = -200;
	
	private float advanceY = INIT_ADVANCE_POS;

	private Lava lava_;
	
	private float scale_ = Utils.randomRange(0.5f, 1f);

	private float up_vel = Utils.randomRange(40, 80);

	private float phase_ = Utils.randomRange(0.8f, 1f);

	private static Vector2 initialPosition(Lava lava) {
		return new Vector2(Utils.random2Range(Lava.WIDTH / 2 - 20),
				lava.finalY() + INIT_ADVANCE_POS);
	}
	
	public LavaBodyBubble(Lava lava) {
		super(Assets.bodyBubble.random(), DURATION, initialPosition(lava));
		lava_ = lava;
		this.stateTime_ = (float) (Math.random() * 100f);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		advanceY += up_vel * deltaTime;
		this.position_.y = lava_.finalY() + advanceY;
	}
	
	@Override
	public Sprite sprite() {
		Sprite s = super.sprite();
		
		float scaleX = (float) Math.sin(stateTime_);
		float scaleY = (float) Math.cos(stateTime_);
		
		s.setScale(scaleX, scaleY);
		s.setAlpha(0.1f);
		
		return s;
	}
	
	@Override
	public float getAlpha() {
		return Math.max(0, Math.min(1, (-advanceY - 10) / 20));
	}
	
	@Override
	public float getXScale() {
		return (float) (scale_ * (1 + 0.1f * Math.sin(stateTime_ * 4 * phase_)));
	}
	
	@Override
	public float getYScale() {
		return (float) (scale_ * (1 - 0.1f * Math.sin(stateTime_ * 4 * phase_)));
	}
}
