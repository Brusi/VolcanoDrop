package com.retrom.volcano.game.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.menus.GraphicObject;
import com.retrom.volcano.utils.BatchUtils;
import com.sun.scenario.effect.impl.state.RenderState;

public class BossGraphic extends GraphicObject {
	
	public enum State {
		REGULAR,
		DARK,
		ANGRY,
		HIDDEN;
	}
	
	State state_ = State.REGULAR;

	public BossGraphic(float x, float y) {
		super(x, y);
		tint_ = 0;
	}

	@Override
	protected Sprite getSprite() {
		switch (state_) {
		case ANGRY:
			return Assets.bossAngry;
		case DARK:
			return Assets.bossDark;
		case REGULAR:
			return Assets.bossRegular;
		default:
			break;
		}
		return null;
	}
	
	public void setState_(State state_) {
		this.state_ = state_;
		stateTime_ = 0;
	}
	
	@Override
	public void render(Batch batch) {
		if (state_ == State.HIDDEN) {
			return;
		}
		
		BatchUtils.setBlendFuncAdd(batch);
		Sprite s = Assets.bossGlow;
		s.setColor(tint_, tint_, tint_, 1);
		Utils.drawCenter(batch, s, position_.x, position_.y);;
		BatchUtils.setBlendFuncNormal(batch);
		
		super.render(batch);
		
		if (state_ == State.DARK && stateTime_ < 0.5f) {
			float tmpAlpha = alpha_;
			alpha_ = Utils.clamp01(1 - stateTime_ * 2);
			renderSprite(batch, Assets.bossRegular);
			alpha_ = tmpAlpha;
		}
	}

}
