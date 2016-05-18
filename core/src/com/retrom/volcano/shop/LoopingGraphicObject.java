package com.retrom.volcano.shop;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.menus.GraphicObject;

public class LoopingGraphicObject extends GraphicObject{

	private final Array<Sprite> animation;

	public LoopingGraphicObject(Array<Sprite> animation, float x, float y) {
		super(x, y);
		this.animation = animation;
	}

	@Override
	protected Sprite getSprite() {
		return getFrameLoop(animation, stateTime_);
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
