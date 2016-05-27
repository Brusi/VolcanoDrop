package com.retrom.volcano.menus;

import com.retrom.volcano.assets.Assets;

public class BackMenuButton extends SimpleMenuButton {
	
	private static float WIDTH = 180;
	private static float HEIGHT = 90;
	
	public static float DEFAULT_X = -240;
	public static float DEFAULT_Y = 380;

	public BackMenuButton(float x, float y, Action action) {
		super(x, y, WIDTH, HEIGHT, Assets.shopBack, Assets.shopBackClick, action);
	}
}
