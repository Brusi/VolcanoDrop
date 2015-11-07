package com.retrom.volcano.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.ui.GameUiRenderer;
import com.retrom.ui.Hub;
import com.retrom.volcano.game.World;
import com.retrom.volcano.game.World.WorldListener;
import com.retrom.volcano.game.WorldRenderer;

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
		uiRenderer_ = new GameUiRenderer(hub_);
	}

	private void togglePause() {
		isPaused_ = !isPaused_;
	}

	@Override
	public void render(float delta) {
		delta = Math.min(1/30f, delta);
		if (!isPaused_) {
			world_.update(delta);
			hub_.setScore(world_.score);
			hub_.update(delta);
		}
		
		worldRenderer_.render(delta, isPaused_);
		uiRenderer_.render(isPaused_);
		
		checkPause();
	}
	
	private boolean isPauseAreaTouched() {
		if (!Gdx.input.justTouched()) {
			return false;
		}
		float x = Gdx.input.getX();
		float y = Gdx.input.getY();
		return x > Gdx.graphics.getWidth() * 3f / 4f && y < Gdx.graphics.getWidth() / 4f;  
	}
	
	private void checkPause() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.P) || isPauseAreaTouched()) {
			togglePause();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		isPaused_ = true;
	}

	@Override
	public void resume() 
	{
		isPaused_ = false;
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
