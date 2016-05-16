package com.retrom.volcano.menus;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Utils;

public class SimpleMenuButton extends MenuButton {
	
	private Sprite sprite_;
	private Sprite sprite_pressed_;
	
	public SimpleMenuButton(float x, float y, float width, float height,
			Sprite sprite, Sprite sprite_pressed,
			Action action) {
		super(new Rectangle(x - width / 2, y - height / 2, width, height),
				action);
		this.sprite_ = sprite;
		this.sprite_pressed_ = sprite_pressed;
	}

	@Override
	public void render(Batch batch) {
		if (!isVisible()) {
			return;
		}
		Sprite s = isPressed() ? sprite_pressed_ : sprite_;
		s.setAlpha(alpha_);
		Utils.drawCenter(batch, s, getX(), getY());
	}
}
