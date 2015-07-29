package com.retrom.volcano.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.World;
import com.retrom.volcano.game.WorldRenderer;

public class GameScreen extends ScreenAdapter implements Screen {
	
	World world_;
	WorldRenderer renderer_;
	
	@Override
	public void show() {
		world_ = new World();
		// TODO: switch to global batch
		renderer_ = new WorldRenderer(new SpriteBatch(), world_);
		Assets.load();
	}

	@Override
	public void render(float delta) {
		world_.update(delta);
		renderer_.render(delta);
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
		// TODO Auto-generated method stub

	}

}
