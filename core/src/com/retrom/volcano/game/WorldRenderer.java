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

import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.effects.Effect;
import com.retrom.volcano.effects.EffectVisitor;
import com.retrom.volcano.effects.FiniteAnimationEffect;
import com.retrom.volcano.effects.Score10Effect;
import com.retrom.volcano.effects.Score15GreenEffect;
import com.retrom.volcano.effects.Score15PurpleEffect;
import com.retrom.volcano.effects.Score15TealEffect;
import com.retrom.volcano.effects.Score1Effect;
import com.retrom.volcano.effects.Score25Effect;
import com.retrom.volcano.effects.Score3Effect;
import com.retrom.volcano.effects.Score4Effect;
import com.retrom.volcano.effects.Score5Effect;
import com.retrom.volcano.effects.Score6Effect;
import com.retrom.volcano.game.objects.BurningWall;
import com.retrom.volcano.game.objects.Collectable;
import com.retrom.volcano.game.objects.Wall;

public class WorldRenderer {
	static final float FRUSTUM_WIDTH = 640;
	static final float FRUSTUM_HEIGHT = FRUSTUM_WIDTH / Gdx.graphics.getWidth() * Gdx.graphics.getHeight();
	
	public static final float FPS = 30f;
	public static final float FRAME_TIME = 1 / FPS;
	
	private static final List<Integer> wallBounceArray = Arrays.asList(1,6,8,6,3,1,2,3,2,0,1,0);
	
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
		
		cam.position.y = snapToPixels(cam_position);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		
		renderBackground();
		renderObjects();
	}

	private static float snapToPixels(float cam_position) {
		return (float) (Math.floor(cam_position / FRUSTUM_HEIGHT * Gdx.graphics.getHeight()) * FRUSTUM_HEIGHT  / Gdx.graphics.getHeight());
	}

	public void renderBackground () {
		batch.disableBlending();
		batch.begin();
		drawPillar(world.background.bgPillar, 0, world.background.bgBaseY());
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
	
	private void setBlendFuncNormal() {
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void setBlendFuncAdd() {
		batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
	}

	private void setBlendFuncScreen() {
		batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_COLOR);
	}
	
	public void renderObjects () {
		batch.enableBlending();
		setBlendFuncNormal();
		batch.begin();
		renderPlayer();
		renderWalls();
		renderCoins();
		renderFloor();
		drawPillar(world.background.leftPillar, FRUSTUM_WIDTH / 2 - 40, world.background.leftBaseY());
		drawPillar(world.background.rightPillar, -(FRUSTUM_WIDTH / 2 - 40), world.background.rightBaseY());
		
		renderEffects(world.effects);
		setBlendFuncAdd();
		renderEffects(world.addEffects);
		setBlendFuncScreen();
		renderEffects(world.screenEffects);
		
		// TODO: draw "add" and "screen" effects.
		
		batch.end();
	}
	private void renderEffects(List<Effect> effects) {
		for (Effect e : effects) {
			Sprite s = e.accept(new EffectVisitor<Sprite>() {

				@Override
				public Sprite visit(Score1Effect effect) {
					Sprite s = Assets.scoreNum1;
					s.setAlpha(effect.getAlpha());
					s.setScale(effect.getScale());
					return s;
				}

				@Override
				public Sprite visit(Score3Effect effect) {
					Sprite s = Assets.scoreNum3;
					s.setAlpha(effect.getAlpha());
					s.setScale(effect.getScale());
					return s;
				}

				@Override
				public Sprite visit(Score4Effect effect) {
					Sprite s = Assets.scoreNum4;
					s.setAlpha(effect.getAlpha());
					s.setScale(effect.getScale());
					return s;
				}

				@Override
				public Sprite visit(Score5Effect effect) {
					Sprite s = Assets.scoreNum5;
					s.setAlpha(effect.getAlpha());
					s.setScale(effect.getScale());
					return s;
				}

				@Override
				public Sprite visit(Score6Effect effect) {
					Sprite s = Assets.scoreNum6;
					s.setAlpha(effect.getAlpha());
					s.setScale(effect.getScale());
					return s;
				}

				@Override
				public Sprite visit(Score10Effect effect) {
					Sprite s = Assets.scoreNum10;
					s.setAlpha(effect.getAlpha());
					s.setScale(effect.getScale());
					return s;
				}

				@Override
				public Sprite visit(Score15GreenEffect effect) {
					Sprite s = Assets.scoreNum15green;
					s.setAlpha(effect.getAlpha());
					s.setScale(effect.getScale());
					return s;
				}

				@Override
				public Sprite visit(Score15PurpleEffect effect) {
					Sprite s = Assets.scoreNum15purple;
					s.setAlpha(effect.getAlpha());
					s.setScale(effect.getScale());
					return s;
				}

				@Override
				public Sprite visit(Score15TealEffect effect) {
					Sprite s = Assets.scoreNum15teal;
					s.setAlpha(effect.getAlpha());
					s.setScale(effect.getScale());
					return s;
				}

				@Override
				public Sprite visit(Score25Effect effect) {
					Sprite s = Assets.scoreNum25;
					s.setAlpha(effect.getAlpha());
					s.setScale(effect.getScale());
					return s;
				}

				@Override
				public Sprite visit(FiniteAnimationEffect effect) {
					return getFrameStopAtLastFrame(effect.getAnimation(), effect.stateTime());
				}
			});
			s.setPosition(e.position_.x - s.getWidth()/2, e.position_.y - s.getHeight()/2);
			s.setRotation(e.getRotation());
			s.draw(batch);
		}
	}

	private void renderFloor() {
		drawCenterBottom(Assets.floor, 0, -90);
		drawCenter(Assets.pillars_big.get(0), 0, -149);
		drawCenter(Assets.pillars_end, 0, -260);
	}

	private void renderWalls() {
		for (Wall wall : world.walls_) {
			float y = wall.position.y;
			TextureRegion keyFrame = null;
			if (wall instanceof BurningWall) {
				if (wall.status() == Wall.STATUS_ACTIVE) {
					if (wall.stateTime() < BurningWall.TIME_WITHOUT_BURN) {
						keyFrame = Assets.burningWall;
					} else if (wall.stateTime() < BurningWall.TIME_START){
						keyFrame = getFrameStopAtLastFrame(Assets.burningWallStart, wall.stateTime() - BurningWall.TIME_WITHOUT_BURN);
					} else {
						keyFrame = getFrameLoop(Assets.burningWallBurn, wall.stateTime() - BurningWall.TIME_START);
					}
				} else if (wall.status() == Wall.STATUS_INACTIVE) {
					keyFrame = getFrameStopAtLastFrame(Assets.burningWallEnd, wall.stateTime());
				}
				y += 12;
			} else {
				keyFrame = wall.isDual() ? keyFrame = Assets.walls2
						.get(wall.graphic_) : Assets.walls1.get(wall.graphic_);  
			}
			
			
			if (wall.status() == Wall.STATUS_INACTIVE && !(wall instanceof BurningWall)) {
				int index = (int) (wall.stateTime() * FPS);
				if (index < wallBounceArray.size())
				y +=  wallBounceArray.get(index);
			}
			drawCenter(keyFrame, wall.position.x, y);
		}
		
	}
	
	private Array<Sprite> getCoinAnimation(Collectable.Type type) {
		switch(type) {
		case COIN_1_1: return Assets.coin1_1_land;
		case COIN_1_2: return Assets.coin1_2_land;
		case COIN_2_1: return Assets.coin2_1_land;
		case COIN_2_2: return Assets.coin2_2_land;
		case COIN_2_3: return Assets.coin2_3_land;
		case COIN_3_1: return Assets.coin3_1_land;
		case COIN_3_2: return Assets.coin3_2_land;
		case COIN_3_3: return Assets.coin3_3_land;
		case COIN_4_1: return Assets.coin4_1_land;
		case COIN_4_2: return Assets.coin4_2_land;
		case COIN_4_3: return Assets.coin4_3_land;
		case COIN_5_1: return Assets.coin5_1_land;
		case COIN_5_2: return Assets.coin5_2_land;
		case COIN_5_3: return Assets.coin5_3_land;
		case COIN_5_4: return Assets.coin5_4_land;
		case POWERUP_MAGNET:
		}
		return null;
	}
	
	private TextureRegion getCoinKeyFrame(Collectable.Type type) {
		switch(type) {
		case COIN_1_1: return Assets.coin1_1;
		case COIN_1_2: return Assets.coin1_2;
		case COIN_2_1: return Assets.coin2_1;
		case COIN_2_2: return Assets.coin2_2;
		case COIN_2_3: return Assets.coin2_3;
		case COIN_3_1: return Assets.coin3_1;
		case COIN_3_2: return Assets.coin3_2;
		case COIN_3_3: return Assets.coin3_3;
		case COIN_4_1: return Assets.coin4_1;
		case COIN_4_2: return Assets.coin4_2;
		case COIN_4_3: return Assets.coin4_3;
		case COIN_5_1: return Assets.coin5_1;
		case COIN_5_2: return Assets.coin5_2;
		case COIN_5_3: return Assets.coin5_3;
		case COIN_5_4: return Assets.coin5_4;
		case POWERUP_MAGNET: return Assets.powerupMagnet;
		}
		return null;
	}
	
	private void renderCoins() {
		for (Collectable coin : world.collectables_) {
			TextureRegion keyFrame = null;
			if (coin.isPowerup() || coin.state() != Collectable.STATUS_IDLE) {
				keyFrame = getCoinKeyFrame(coin.type);
			} else {
				
				keyFrame = getFrameStopAtLastFrame(getCoinAnimation(coin.type), coin.stateTime());
			}
			
			drawWithTilt(keyFrame, coin.position, -15f, -15f);
		}
		
	}

	private void drawWithTilt(TextureRegion keyFrame, Vector2 position,
			float xTilt, float yTilt) {
		batch.draw(keyFrame, position.x + xTilt, position.y + yTilt);
	}

	private void renderPlayer () {
		Sprite keyFrame = null;
		switch (world.player.state()) {
		case Player.STATE_IDLE:
			keyFrame = getFrameLoop(Assets.playerIdle, world.player.stateTime);
			keyFrame.setFlip(world.player.side, false);
			break;
		case Player.STATE_RUNNING:
			float startTime = FRAME_TIME * Assets.playerRunStart.size / 3;
			if (world.player.stateTime < startTime * Assets.playerRunStart.size) {
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
		case Player.STATE_DIE:
		case Player.STATE_DEAD:
			return;
		default:
			Gdx.app.log("ERROR", "Player is in invalid state: " + world.player.state());
		}
		drawCenter(keyFrame, world.player.position);
	}
	
	private Sprite getFrameLoop(Array<Sprite> anim, float stateTime) {
		return anim.get((int) (stateTime * FPS) % anim.size);
	}
	
	private Sprite getFrameStopAtLastFrame(Array<Sprite> anim, float stateTime) {
		int frameIndex = (int) (stateTime * FPS);
		frameIndex = Math.min(frameIndex, anim.size-1);
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
