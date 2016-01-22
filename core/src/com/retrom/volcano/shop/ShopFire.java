package com.retrom.volcano.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.WorldRenderer;

public class ShopFire extends GraphicObject{

	public ShopFire(float x, float y) {
		super(x, y);
	}

	@Override
	protected Sprite getSprite() {
		return getFrameLoop(Assets.shopFire, stateTime_);
	}

	private Sprite getFrameLoop(Array<Sprite> anim, float stateTime) {
		Sprite s = anim.get((int) (stateTime * WorldRenderer.FPS) % anim.size);
		return s;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}
}
