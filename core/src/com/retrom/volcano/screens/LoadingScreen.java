package com.retrom.volcano.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LoadingScreen implements Screen {

	private Sprite splash;
	private SpriteBatch batch;
	private Texture texture;

	@Override
	public void show() {
		showLoading();
	}

	@Override
	public void render(float delta) {
		texture = new Texture("menu/shopmenu_bg.png");
		splash = new Sprite(texture);
		batch = new SpriteBatch();
		batch.begin();
		splash.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		texture.dispose();
		batch.dispose();
	}
	
	private void showLoading() {

	}

}
