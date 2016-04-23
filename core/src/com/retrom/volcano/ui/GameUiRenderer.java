package com.retrom.volcano.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.control.ControlManager;
import com.retrom.volcano.game.World;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.menus.PauseMenu;
import com.retrom.volcano.utils.BatchUtils;

public class GameUiRenderer {
	
	private final SpriteBatch batch_;
	private final ShapeRenderer shapes_;
	private final Camera cam_;
	private final PowerupUiRenderer powerupUiRenderer_;
	
	private final Hub scoreHub_;
	private final World world_;
	private final PauseMenu pauseMenu_;

	public GameUiRenderer(Hub scoreHub, World world, PauseMenu pauseMenu_) {
		world_ = world;
		this.pauseMenu_ = pauseMenu_;
		cam_ = new OrthographicCamera(
				WorldRenderer.FRUSTUM_WIDTH, WorldRenderer.FRUSTUM_HEIGHT);
		batch_ = new SpriteBatch();
		shapes_ = new ShapeRenderer();
		batch_.setProjectionMatrix(cam_.combined);
		shapes_.setProjectionMatrix(cam_.combined);
		
		scoreHub_ = scoreHub;
		
		powerupUiRenderer_ = new PowerupUiRenderer(cam_);
	}
	
	public void render(float deltaTime, boolean isPaused) {
		batch_.begin();
		renderScore(isPaused);
		renderControls();
		batch_.end();
		
		powerupUiRenderer_.update(deltaTime);
		powerupUiRenderer_.setMagnetRatio(world_.magnetRatio());
		powerupUiRenderer_.setSlomoRatio(world_.slomoRatio());
		powerupUiRenderer_.setShieldRatio(world_.shieldRatio());
		powerupUiRenderer_.render();
		
		if (isPaused) {
			renderPauseMenu();
		}
	}

	private void renderPauseMenu() {
		BatchUtils.setBlendFuncNormal(batch_);
		batch_.begin();
		pauseMenu_.render(batch_, shapes_);
		batch_.end();
		
	}

	private void renderControls() {
		ControlManager.getControl().render(batch_);
	}

	private void renderScore(boolean isPaused) {
		scoreHub_.render(batch_, cam_, isPaused);
	}
	
	public Camera getCamera() {
		return cam_;
	}
}
