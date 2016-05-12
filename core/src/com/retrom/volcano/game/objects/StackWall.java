package com.retrom.volcano.game.objects;

import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Utils;

public class StackWall extends Wall {
	
	int height_;
	
	public int graphics_[];
	
	public StackWall(int col, float y, int height) {
		super(Utils.xOfCol(col), y, Wall.SIZE, Wall.SIZE * height, col, -1);
		this.height_ = height;
		this.graphics_ = new int[height];
		for (int i=0; i < graphics_.length; i++) {
			graphics_[i] = rand.nextInt(Assets.walls1.size);
		}
	}
	
	@Override
	public int getHeight() {
		return height_;
	}
}
