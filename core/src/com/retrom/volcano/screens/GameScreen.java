package com.retrom.volcano.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.SoundAssets;
import com.retrom.volcano.game.Settings;
import com.retrom.volcano.game.World;
import com.retrom.volcano.game.World.WorldListener;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.ui.GameUiRenderer;
import com.retrom.volcano.ui.Hub;
import com.retrom.volcano.ui.PowerupUiRenderer;
import com.retrom.volcano.utils.TouchToPoint;

public class GameScreen extends ScreenAdapter implements Screen {
	
	SpriteBatch batch_ = new SpriteBatch();
	
	World world_;
	WorldRenderer worldRenderer_;
	
	Hub hub_;
	
	private GameUiRenderer uiRenderer_;

	boolean isPaused_ = false;

	@Override
	public void show() {
		world_ = new World(new WorldListener() {

			@Override
			public void restartGame() {
				((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen());
			}
		});
		
		hub_ = new Hub();
		
		worldRenderer_ = new WorldRenderer(batch_, world_);
		uiRenderer_ = new GameUiRenderer(hub_, world_);
	}

	private void togglePause() {
		if (!isPaused_) {
			pause();
		} else {
			resume();
		}
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyPressed(Input.Keys.TAB)) {
			delta /= 10f;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
			for (int i=0; i < 10; i++) {
				update(delta);
			}
		}
		update(delta);
	}

	private void update(float delta) {
		delta = Math.min(1/30f, delta);
		if (!isPaused_) {
			world_.update(delta);
			hub_.setScore(world_.score);
			hub_.update(delta);
		}
		
		worldRenderer_.render(delta, isPaused_);
		uiRenderer_.render(delta, isPaused_);
		
		checkPause();
	}
	
	private void checkPause() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.P) || hub_.isPauseAreaTouched()) {
			togglePause();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			Settings.toggleSound();
		}
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		isPaused_ = true;
		SoundAssets.pauseAllSounds();
		world_.pause();
	}

	@Override
	public void resume() 
	{
		isPaused_ = false;
		SoundAssets.resumeAllSounds();
		world_.unpause();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		pause();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
