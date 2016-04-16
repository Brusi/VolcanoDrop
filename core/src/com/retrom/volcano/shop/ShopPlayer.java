package com.retrom.volcano.shop;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.menus.GraphicObject;

public class ShopPlayer extends GraphicObject{

	public ShopPlayer(float x, float y) {
		super(x, y);
	}

	@Override
	protected Sprite getSprite() {
		return getFrameLoop(WorldRenderer.getCostumeAssets().playerIdle, stateTime_);
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
