package com.retrom.volcano.menus;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class StaticGraphicObject extends GraphicObject {
	
	private Sprite sprite_;

	public StaticGraphicObject(Sprite sprite, float x, float y) {
		super(x, y);
		sprite_ = sprite;
	}

	@Override
	protected Sprite getSprite() {
		return sprite_;
	}

}
