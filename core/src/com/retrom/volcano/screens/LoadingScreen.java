package com.retrom.volcano.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.assets.SoundAssets;

public class LoadingScreen implements Screen {

	private SpriteBatch batch;

	@Override
	public void show() {
        Assets.startLoad();
        SoundAssets.preload(Assets.assetManager);
		batch = new SpriteBatch();
    }

	@Override
	public void render(float delta) {
		batch.begin();

		batch.end();
        if (Assets.assetManager.update()) {
            Gdx.app.log("INFO", "Done loading");

            Assets.initAssets();
            SoundAssets.load();

            Game x = ((Game)(Gdx.app.getApplicationListener()));
            x.setScreen(new GameScreen());
            return;
        }
        Gdx.app.log("INFO", "loading " + Assets.assetManager.getProgress());
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
		batch.dispose();
	}
}
