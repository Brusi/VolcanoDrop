package com.retrom.volcano.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.SoundAssets;
import com.retrom.volcano.control.ControlManager;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.game.Settings;
import com.retrom.volcano.game.World;
import com.retrom.volcano.game.World.WorldListener;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.menus.DeathMenu;
import com.retrom.volcano.menus.MenuButton;
import com.retrom.volcano.menus.PauseMenu;
import com.retrom.volcano.ui.GameUiRenderer;
import com.retrom.volcano.ui.Hub;
import com.retrom.volcano.ui.Splash;

public class GameScreen extends ScreenAdapter implements Screen {
	
	SpriteBatch batch_ = new SpriteBatch();
	
	World world_;
	WorldRenderer worldRenderer_;
	
	private PauseMenu pauseMenu_;
	private DeathMenu deathMenu_;
	
	boolean isPaused_ = false;
	boolean isGameFinished_ = false;
	
	public GameScreen() {
		this(true);
	}
	
	public GameScreen(boolean showOpening) {
		this.showOpening = showOpening;
		this.splash_ = showOpening ? new Splash(hub_) : null;
	}
	
	private final boolean showOpening;
	
	private final Hub hub_ = new Hub(new MenuButton.Action[] {
			new MenuButton.Action() {
				@Override
				public void act() {
					togglePause();
				}
			}, new MenuButton.Action() {
				@Override
				public void act() {
					goToShop();
				}
			} });
	
	private final Splash splash_;
	
	private GameUiRenderer uiRenderer_;

	@Override
	public void show() {
		ControlManager.getControl().reset();
		
		world_ = new World(new WorldListener() {
			@Override
			public void finish() {
				isGameFinished_ = true;
				deathMenu_.setScoreAndTime(world_.score, world_.gameTime);
//				restartGame();
			}
			
			@Override
			public void startOpeningScene() {
				hub_.hideBeforeSplash();
			}

			@Override
			public void startGame() {
				hub_.startGame();
			}
		}, showOpening);
		pauseMenu_ = new PauseMenu(new PauseMenu.Listener() {
			@Override
			public void act(PauseMenu.Command cmd) {
				System.out.println(cmd.name());
				switch (cmd) {
				case RESTART:
					restartGame();;
					break;
				case RESUME:
					// TODO: unpause?
					togglePause();
					break;
				case SHOP:
					goToShop();
					break;
				default:
					Gdx.app.error("Error", "Illegal pause menu command.");
					break;
				}
			}
		});
		deathMenu_ = new DeathMenu(new DeathMenu.Listener() {
			@Override
			public void act(DeathMenu.Command cmd) {
				System.out.println(cmd.name());
				switch (cmd) {
				case RESTART:
					restartGame();;
					break;
				case SHOP:
					goToShop();
					break;
				default:
					Gdx.app.error("Error", "Illegal pause menu command.");
					break;
				}
			}
		});
		
		worldRenderer_ = new WorldRenderer(batch_, world_);
		uiRenderer_ = new GameUiRenderer(hub_, world_, pauseMenu_, deathMenu_, splash_);
	}
	
	private void restartGame() {
		finalizeGame();
		((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(false));
	}

	private void togglePause() {
		if (!isPaused_) {
			pauseGame();
		} else {
			resumeFromPause();
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
		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			for (int i=0; i < 300; i++) {
				update(delta);
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
			world_.godMode_ = true;
			for (int i=0; i < 60* (156) && world_.gameTime < 60 + 60 + 30 ; i++) {
				update(0.1f);
			}
		}
		update(delta);
	}

	private void update(float delta) {
		delta = Math.min(1/30f, delta);
		
		if (isGameFinished_) {
			deathMenu_.update(delta);
		} else if (isPaused_) {
			pauseMenu_.update(delta);
		} else {
			world_.update(delta);
			hub_.setScore(world_.score);
			hub_.setTime(world_.gameTime);
			hub_.update(delta);
			if (splash_ != null)
				splash_.update(delta);
		}
		
		worldRenderer_.render(delta, isPaused_);
		uiRenderer_.render(delta, isPaused_, isGameFinished_);
		
		checkSplashTap();
		checkPause();
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.HOME)) {
			goToShop();
		}
	}
	
	private void checkSplashTap() {
		if (splash_ == null) return;
		if (splash_.state != Splash.State.SHOWING) {
			return;
		}
		if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
			splash_.disappear();
			world_.doneWithSplash();
		}
	}
	
	private void goToShop() {
		finalizeGame();
		((Game)(Gdx.app.getApplicationListener())).setScreen(new ShopScreen());
	}
	
	private void finalizeGame() {
		ShopData.addGold(world_.score);
		world_.score = 0;
	}

	private void checkPause() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			togglePause();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			Settings.soundEnabled.toggle();
		}
	}
	
	public void pauseGame() {
		isPaused_ = true;
		SoundAssets.pauseAllSounds();
		world_.pause();
	}

	public void resumeFromPause() 
	{
		isPaused_ = false;
		SoundAssets.resumeAllSounds();
		world_.unpause();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		pauseGame();
	}

	@Override
	public void resume() 
	{
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
//		pause();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
