package com.retrom.volcano.shop;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface ShopMenuContent {
	public void update(float deltaTime);
	public void render(SpriteBatch batch);
	
	// Refreshes data shown by the menu.
	public void refresh();
}
