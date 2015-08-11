package com.retrom.volcano.game.objects;

import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Utils;

public class WallSingle extends Wall {

	public WallSingle(int col, float y) {
		super(Utils.xOfCol(col), y, Wall.SIZE, Wall.SIZE, col, rand.nextInt(Assets.walls1.size));
	}
}
