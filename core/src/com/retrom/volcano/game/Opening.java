package com.retrom.volcano.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface Opening {
	void update(float deltaTime);
	
	void render(ShapeRenderer shapes, SpriteBatch batch);
	void renderTop(SpriteBatch batch);
	void renderForeground(SpriteBatch batch);
	
	void startScene();
}
