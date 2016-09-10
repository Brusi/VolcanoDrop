package com.retrom.volcano.shop;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface ShopMenuContent {
	void update(float deltaTime);
	 void render(SpriteBatch batch);
	
	// Refreshes data shown by the menu.
	 void refresh();
	
	// Set master opacity.
	 void setAlpha(float alpha);
	
	 Sprite getBottomFade();
}
