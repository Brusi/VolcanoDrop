package com.retrom.volcano.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.control.ControlManager;
import com.retrom.volcano.game.World;
import com.retrom.volcano.game.WorldRenderer;

public class GameUiRenderer {
	
	private final SpriteBatch batch_;
	private final Camera cam_;
	private final PowerupUiRenderer powerupUiRenderer_;
	
	private final Hub scoreHub_;
	private World world_;

	public GameUiRenderer(Hub scoreHub, World world) {
		world_ = world;
		cam_ = new OrthographicCamera(
				WorldRenderer.FRUSTUM_WIDTH, WorldRenderer.FRUSTUM_HEIGHT);
		batch_ = new SpriteBatch();
		batch_.setProjectionMatrix(cam_.combined);
		
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
