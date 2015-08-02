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

import java.util.Deque;

import javafx.animation.KeyFrame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.objects.Collectable;
import com.retrom.volcano.game.objects.Wall;

public class WorldRenderer {
	static final float FRUSTUM_WIDTH = 640;
	static final float FRUSTUM_HEIGHT = FRUSTUM_WIDTH / Gdx.graphics.getWidth() * Gdx.graphics.getHeight();
	
	static final float FPS = 30f;
	static final float FRAME_TIME = 1 / FPS;
	
	World world;
	SpriteBatch batch;
	
	OrthographicCamera cam;
	private float cam_target;
	private float cam_position;

	public WorldRenderer (SpriteBatch batch, World world) {
		this.world = world;
		this.cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		cam_position = this.cam.position.y = FRUSTUM_HEIGHT / 3f;
		this.batch = batch;
	}
	
	public static final float CAM_SPEED = 40f;
	
	public void render(float deltaTime) {
		Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		cam_target = world.camTarget;
		if (cam_position < cam_target) {
			cam_position += (cam_target - cam_position) * deltaTime / 2;
		}
		
		cam.position.y = Math.round(cam_position);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		
		renderBackground();
		renderObjects();
	}

	public void renderBackground () {
		batch.disableBlending();
		batch.begin();
		drawPillar(world.background.bgPillar, 0, world.background.bgBaseY());
		drawPillar(world.background.leftPillar, FRUSTUM_WIDTH / 2 - 35, world.background.leftBaseY());
		drawPillar(world.background.rightPillar, -(FRUSTUM_WIDTH / 2 - 35), world.background.rightBaseY());
		
		batch.end();
	}
	
	public void drawPillar(Deque<Background.Element> pillar, float x, float y) {
		for (Background.Element e : pillar) {
			TextureRegion keyFrame = null;
			switch (e) {
			case PILLAR_1:
			case PILLAR_2:
			case PILLAR_3:
				int index = e.index();
				keyFrame = Assets.pillars.get(index);
				break;
			case PILLAR_BIG_1:
			case PILLAR_BIG_2:
				keyFrame = Assets.pillars_big.get(e.index() % Assets.pillars_big.size);
				break;
			case PILLAR_END:
				keyFrame = Assets.pillars_end;
				break;
			case PILLAR_START:
				keyFrame = Assets.pillars_start;
				break;
			case BACKGROUND:
				keyFrame = Assets.background;
				break;
			default:
				Gdx.app.log("ERROR", "Unhandled pillar type: " + e);
				break;
			}
			
			drawCenterBottom(keyFrame, x, y);
			y += e.height();
		}
	}

	public void renderObjects () {
		batch.enableBlending();
		batch.begin();
		renderPlayer();
		renderWalls();
		renderCoins();
		renderFloor();
		batch.end();
	}

	private void renderFloor() {
		drawCenterBottom(Assets.floor, 0, -90);
		drawCenter(Assets.pillars_big.get(0), 0, -149);
		drawCenter(Assets.pillars_end, 0, -260);
	}

	private void renderWalls() {
		for (Wall wall : world.walls_) {
			TextureRegion keyFrame = Assets.walls1.get(wall.graphic_);
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
		Sprite keyFrame = null;
		switch (world.player.state()) {
		case Player.STATE_IDLE:
			keyFrame = getFrameLoop(Assets.playerIdle, world.player.stateTime);
			keyFrame.setFlip(world.player.side, false);
			break;
		case Player.STATE_RUNNING:
			float startTime = FPS * Assets.playerRunStart.size;
			if (world.player.stateTime < FRAME_TIME * Assets.playerRunStart.size) {
				keyFrame = getFrameLoop(Assets.playerRunStart, world.player.stateTime); 
			} else {
				keyFrame = getFrameLoop(Assets.playerRun, world.player.stateTime - startTime);
			}
			keyFrame.setFlip(world.player.side, false);
			break;
		case Player.STATE_JUMPING:
			keyFrame = getFrameLoop(Assets.playerJump, world.player.stateTime);
			keyFrame.setFlip(world.player.side, false);
			break;
		case Player.STATE_LANDING:
			keyFrame = getFrameStopAtLastFrame(Assets.playerLand, world.player.stateTime);
			keyFrame.setFlip(world.player.side, false);
			break;
		default:
			Gdx.app.log("ERROR", "Player is in invalid state: " + world.player.state());
		}
		drawCenter(keyFrame, world.player.position);
	}
	
	private Sprite getFrameLoop(Array<Sprite> anim, float stateTime) {
		return anim.get((int) (world.player.stateTime * FPS) % anim.size);
	}
	
	private Sprite getFrameStopAtLastFrame(Array<Sprite> anim, float stateTime) {
		int frameIndex = (int) (world.player.stateTime * FPS);
		if (frameIndex >= anim.size) {
			frameIndex = anim.size-1;
		}
		return anim.get(frameIndex);
	}
	
	private void drawCenterBottom(TextureRegion keyFrame, float x, float y) {
		drawCenter(keyFrame, x, y + keyFrame.getRegionHeight()/2);
	}
	
	private void drawCenter(TextureRegion keyFrame, Vector2 position) {
		drawCenter(keyFrame, position.x, position.y);
	}
	
	private void drawCenter(TextureRegion keyFrame, float x, float y) {
		batch.draw(keyFrame, x - keyFrame.getRegionWidth()/2, y - keyFrame.getRegionHeight()/2);
	}
}
