package com.retrom.volcano.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.WorldRenderer;

public class Shaman extends GraphicObject{
	
	enum State {
		IDLE, BUY;
	}
	
	private State state_ = State.IDLE;

	public Shaman(float x, float y) {
		super(x, y);
	}

	@Override
	protected Sprite getSprite() {
		switch (state_) {
		case BUY:
			return getFrameLoop(Assets.shamanBuy, stateTime_);
		case IDLE:
			return getFrameLoop(Assets.shamanIdle, stateTime_);
		}
		Gdx.app.log("ERROR", "Illegal shaman state.");
		return null;
	}

	private Sprite getFrameLoop(Array<Sprite> anim, float stateTime) {
		return anim.get((int) (stateTime * WorldRenderer.FPS) % anim.size);
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (stateTime_ >= 1) {
			stateTime_ = 0;
			state_ = State.IDLE;
		}
	}

	public void buy() {
		stateTime_ = 0;
		state_ = State.BUY;
	}
}
