/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.retrom.volcano.game;

import javafx.print.Collation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.objects.Collectable;
import com.retrom.volcano.game.objects.Wall;

public class WorldRenderer {
	static final float FRUSTUM_WIDTH = 640;
	static final float FRUSTUM_HEIGHT = FRUSTUM_WIDTH / Gdx.graphics.getWidth() * Gdx.graphics.getHeight();
	World world;
	SpriteBatch batch;
	
	OrthographicCamera cam;
	private float cam_target;

	public WorldRenderer (SpriteBatch batch, World world) {
		this.world = world;
		this.cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		this.cam.position.y = FRUSTUM_HEIGHT / 3f;
		this.batch = batch;
	}
	
	public static final float CAM_SPEED = 40f;
	
	public void render(float deltaTime) {
		Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		cam_target = (world.floors_.getTotalBlocks()) * Wall.SIZE / 6f + FRUSTUM_HEIGHT / 3f;
		if (cam.position.y < cam_target) {
			cam.position.y += (cam_target - cam.position.y) * deltaTime / 2;
		}
		
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		renderBackground();
		renderObjects();
	}

	public void renderBackground () {
		batch.disableBlending();
		batch.begin();
		batch.draw(Assets.backgroundRegion, -Assets.backgroundRegion.getRegionWidth()/2, -Assets.backgroundRegion.getRegionHeight()/2);
		batch.end();
	}

	public void renderObjects () {
		batch.enableBlending();
		batch.begin();
		renderPlayer();
		renderWalls();
		renderCoins();
		batch.end();
	}

	private void renderWalls() {
		for (Wall wall : world.walls_) {
			TextureRegion keyFrame = Assets.wall;
			drawCenter(keyFrame, wall.position);
		}
		
	}
	
	private void renderCoins() {
		for (Collectable coin : world.collectables_) {
			TextureRegion keyFrame = null;
			switch (coin.type) {
			case COIN3_1:
				keyFrame = Assets.coin3_1;
				break;
			case COIN5_4:
				keyFrame = Assets.coin5_4;
				break;
			case POWERUP_MAGNET:
				keyFrame = Assets.powerupMagnet;
				break;
			}
			
			drawCenter(keyFrame, coin.position);
		}
		
	}

	private void renderPlayer () {
		TextureRegion keyFrame = null;
		switch (world.player.state) {
		case Player.STATE_NOTHING:
			keyFrame = Assets.player;
			break;
		}

		float side = world.player.velocity.x < 0 ? -1 : 1;
		drawCenter(keyFrame, world.player.position);
//		batch.draw(keyFrame, world.player.position.x, world.player.position.y, keyFrame.getRegionWidth() * side, keyFrame.getRegionHeight());
	}
	
	private void drawCenter(TextureRegion keyFrame, Vector2 position) {
		batch.draw(keyFrame, position.x - keyFrame.getRegionWidth()/2, position.y - keyFrame.getRegionHeight()/2);
	}
}
