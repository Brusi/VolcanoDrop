package com.retrom.volcano.menus;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Utils;

public class BackMenuButton extends MenuButton {
	
	private static float WIDTH = 180;
	private static float HEIGHT = 90;
	
	public static float DEFAULT_X = -220;
	public static float DEFAULT_Y = -435;

	public BackMenuButton(float x, float y, Action action) {
		super(MakeRect(x, y), action);
	}

	@Override
	public void render(Batch batch) {
		Sprite sprite = isPressed() ? Assets.shopBackClick : Assets.shopBack;
		Utils.drawCenter(batch, sprite,
				rect.x + rect.width / 2,
				rect.y + rect.height / 2);
	}

	private static Rectangle MakeRect(float x, float y) {
		return new Rectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
	}
}
