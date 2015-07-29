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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.objects.Wall;

public class WorldRenderer {
	static final float FRUSTUM_WIDTH = 640;
	static final float FRUSTUM_HEIGHT = FRUSTUM_WIDTH / Gdx.graphics.getWidth() * Gdx.graphics.getHeight();
	World world;
	OrthographicCamera cam;
	SpriteBatch batch;

	public WorldRenderer (SpriteBatch batch, World world) {
		this.world = world;
		this.cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		this.batch = batch;
	}
	
	public void render() {
		Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		if (world.player.position.y > cam.position.y)
			cam.position.y = world.player.position.y;
		
		cam.position.y = (world.floors_.getTotalBlocks()) * 80f / 6f + FRUSTUM_HEIGHT/4;
		
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
		batch.end();
	}

	private void renderWalls() {
		for (Wall wall : world.walls_) {
			TextureRegion keyFrame = Assets.wall;
			drawCenter(keyFrame, wall.position);
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
