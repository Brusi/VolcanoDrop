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
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.assets.CostumeAssets;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.effects.DiamondGlowEffect;
import com.retrom.volcano.effects.Effect;
import com.retrom.volcano.effects.EffectVisitor;
import com.retrom.volcano.effects.FiniteAnimationEffect;
import com.retrom.volcano.effects.FireballAnimationEffect;
import com.retrom.volcano.effects.FireballStartEffect;
import com.retrom.volcano.effects.FlameEffect;
import com.retrom.volcano.effects.OneFrameEffect;
import com.retrom.volcano.effects.PlayerMagnetEffect;
import com.retrom.volcano.effects.PlayerOnionSkinEffect;
import com.retrom.volcano.effects.PlayerShieldEffect;
import com.retrom.volcano.effects.PowerupGlow;
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
import com.retrom.volcano.effects.WarningExclEffect;
import com.retrom.volcano.effects.WarningSkullEffect;
import com.retrom.volcano.game.objects.BurningWall;
import com.retrom.volcano.game.objects.Collectable;
import com.retrom.volcano.game.objects.Enemy;
import com.retrom.volcano.game.objects.Flame;
import com.retrom.volcano.game.objects.FlamethrowerWall;
import com.retrom.volcano.game.objects.GoldSack;
import com.retrom.volcano.game.objects.SideFireball;
import com.retrom.volcano.game.objects.Spitter;
import com.retrom.volcano.game.objects.TopFireball;
import com.retrom.volcano.game.objects.Wall;
import com.retrom.volcano.utils.BatchUtils;
import com.sun.org.apache.regexp.internal.recompile;

public class WorldRenderer {
	static final public float FRUSTUM_WIDTH = 640;
	static final public float FRUSTUM_HEIGHT = FRUSTUM_WIDTH / Gdx.graphics.getWidth() * Gdx.graphics.getHeight();
	
	public static final float FPS = 30f;
	public static final float FRAME_TIME = 1 / FPS;
	
	private static final List<Integer> wallBounceArray = Arrays.asList(1,6,8,6,3,1,2,3,2,0,1,0);
	
	private static final float PILLAR_POS = FRUSTUM_WIDTH / 2 - 40;
	
	
	World world;
	SpriteBatch batch;
	
	OrthographicCamera cam;
	private float cam_target;
	private float cam_position;
	private ShapeRenderer shapeRenderer = new ShapeRenderer();

	public WorldRenderer (SpriteBatch batch, World world) {
		this.world = world;
		this.cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		cam_position = this.cam.position.y = 3000;
		this.batch = batch;
		System.out.println("FRUSTUM_HEIGHT=" + FRUSTUM_HEIGHT);
	}
	
	public static final float CAM_SPEED = 40f;
	public static final float PLAYER_SPRITE_Y_OFFSET = 42f;
	
	
	public void render(float deltaTime, boolean isPaused) {
		Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		if (!isPaused) {
			cam_target = world.camTarget;
			if (cam_position < cam_target) {
				cam_position += (cam_target - cam_position) * deltaTime / 2;
			} else if (cam_position > cam_target + 50) {
				// Set minimum height
				cam_position = this.cam.position.y = cam_target + 50;
			}

			cam.position.y = snapToY(cam_position);
			cam.position.x = world.quakeX;
			cam.update();
			batch.setProjectionMatrix(cam.combined);
		}
		
		renderBackground();
		renderObjects();
		renderOverlay();
		if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS)) {
			offset += 1;
		}
	}

	public float offset = 0;

	private void renderOverlay() {
		/**/System.out.println("cam_target="+cam_target);
		
		if (world.slomoTime <= 0 && world.lava_ == null) {
			return;
		}
		float alpha = 
			Math.min( Math.min(1, world.slomoTime),
			(world.consecutiveSlomo ? 1 : (World.TOTAL_SLOMO_TIME - world.slomoTime))/0.25f);
		alpha *= 0.30f;

		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Filled);
		if (alpha > 0) {
			shapeRenderer.setColor(1, 0, 1, alpha);
			shapeRenderer.rect(- FRUSTUM_WIDTH / 2, - FRUSTUM_HEIGHT + world.camTarget, FRUSTUM_WIDTH, FRUSTUM_HEIGHT * 2);
		}
		shapeRenderer.end();
		return;
	}
	
	private void renderLava() {
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Filled);
		if (world.lava_ != null) {
			world.lava_.cam_y = cam.position.y;
			world.lava_.render(batch, shapeRenderer);
		}
		shapeRenderer.end();
	}
	
	private void renderLavaTop() {
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Filled);
		if (world.lava_ != null) {
			world.lava_.cam_y = cam.position.y;
			world.lava_.renderTop(batch, shapeRenderer);
		}
		shapeRenderer.end();
	}

	private static float snapToPixels(float cam_position) {
		return (float) (Math.floor(cam_position / FRUSTUM_HEIGHT * Gdx.graphics.getHeight()) * FRUSTUM_HEIGHT  / Gdx.graphics.getHeight());
	}

	public void renderBackground () {
		BatchUtils.setBlendFuncNormal(batch);
		batch.enableBlending();
		batch.begin();
		drawPillar(world.background.bgPillar, 0, world.background.bgBaseY(), false);
		batch.end();
	}
	
	static class DrawTask {
		public final Sprite keyFrame;
		public final float x;
		public final float y;
		public DrawTask(Sprite keyFrame, float x, float y) {
			this.keyFrame = keyFrame;
			this.x = x;
			this.y = y;
		}
	}
	
	public void drawPillar(Deque<Background.Element> pillar, float x, float y, boolean flip) {
		LinkedList<DrawTask> drawTasks = new LinkedList<DrawTask>();
		
		for (Background.Element e : pillar) {
			Sprite keyFrame = null;
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
			case PILLAR_HOLE:
				keyFrame = Assets.pillars_hole;
				break;
			case PILLAR_HOLE_BG:
				keyFrame = Assets.pillars_hole_bg;
				break;
			case BACKGROUND_BASE:
				keyFrame = Assets.background;
				keyFrame.rotate(30f);
				break;
			case BACKGROUND_WORLD1_1:
			case BACKGROUND_WORLD1_2:
			case BACKGROUND_WORLD1_3:
			case BACKGROUND_WORLD1_4:
			case BACKGROUND_WORLD1_5:
			case BACKGROUND_WORLD1_6:
				keyFrame = Assets.bg_world1.get(e.index());
				break;
			case BACKGROUND_OVERLAY1_1:
			case BACKGROUND_OVERLAY1_2:
			case BACKGROUND_OVERLAY1_3:
			case BACKGROUND_OVERLAY1_4:
			case BACKGROUND_OVERLAY1_5:
			case BACKGROUND_OVERLAY1_6:
			case BACKGROUND_OVERLAY1_7:
			case BACKGROUND_OVERLAY1_8:
				keyFrame = Assets.bg_overlay_world1.get(e.index());
				break;
			case BACKGROUND_WORLD2_1:
			case BACKGROUND_WORLD2_2:
			case BACKGROUND_WORLD2_3:
			case BACKGROUND_WORLD2_4:
			case BACKGROUND_WORLD2_5:
			case BACKGROUND_WORLD2_6:
				keyFrame = Assets.bg_world2.get(e.index());
				break;
			case BACKGROUND_OVERLAY2_1:
			case BACKGROUND_OVERLAY2_2:
			case BACKGROUND_OVERLAY2_3:
			case BACKGROUND_OVERLAY2_4:
			case BACKGROUND_OVERLAY2_5:
			case BACKGROUND_OVERLAY2_6:
			case BACKGROUND_OVERLAY2_7:
			case BACKGROUND_OVERLAY2_8:
				keyFrame = Assets.bg_overlay_world2.get(e.index());
				break;
			case BACKGROUND_WORLD3_1:
			case BACKGROUND_WORLD3_2:
				keyFrame = Assets.bg_world3.get(e.index());
			default:
//				Gdx.app.log("ERROR", "Unhandled pillar type: " + e);
				break;
			}
			keyFrame.setFlip(flip, false);
			drawTasks.addFirst(new DrawTask(keyFrame, x, y));
			y += e.height();
		}
		for (DrawTask task : drawTasks) {
			drawCenterBottom(task.keyFrame, task.x, task.y);
		}
	}
	
	public void renderObjects () {
		batch.enableBlending();
		BatchUtils.setBlendFuncNormal(batch);
		batch.begin();
		renderPillarBg();
		renderWalls();
		BatchUtils.setBlendFuncAdd(batch);
		renderEffects(world.addEffectsUnder);
		BatchUtils.setBlendFuncNormal(batch);
		
		renderPlayer();
		renderEnemies();
		renderGoldSacks();
		renderCoins();
		renderRelic();
		renderFloor();
		
		drawPillar(world.background.leftPillar, -PILLAR_POS, world.background.leftBaseY(), false);
		drawPillar(world.background.rightPillar, PILLAR_POS, world.background.rightBaseY(), true);

		batch.end();
		renderLava();
		batch.enableBlending();
		batch.begin();
		
		renderEffects(world.effects);
		BatchUtils.setBlendFuncScreen(batch);
		renderEffects(world.screenEffects);
		BatchUtils.setBlendFuncAdd(batch);
		renderEffects(world.addEffects);
		batch.end();
		
		renderLavaTop();
	}

	private void renderRelic() {
		if (world.relic_ != null) {
			world.relic_.render(batch);
		}
	}

	private void renderPillarBg() {
		for (float f : world.background.leftHoleList) {
			Assets.pillars_hole_bg.setFlip(false, false);
			drawCenterBottom(Assets.pillars_hole_bg, -PILLAR_POS, f);
		}
		for (float f : world.background.rightHoleList) {
			Assets.pillars_hole_bg.setFlip(true, false);
			drawCenterBottom(Assets.pillars_hole_bg, +PILLAR_POS, f);
		}
	}

	private void renderEnemies() {
		for (Enemy e : world.enemies_) {
			Sprite s = e.accept(new Enemy.Visitor<Sprite>() {
				@Override
				public Sprite visit(Flame flame) {
					return null;
				}

				@Override
				public Sprite visit(TopFireball topFireball) {
					return null;
				}

				@Override
				public Sprite visit(Spitter spitter) {
					Sprite $ = getFrameStopAtLastFrame(Assets.spitter, spitter.stateTime());
					$.setFlip(spitter.side(), false);
					return $;
				}

				@Override
				public Sprite visit(SideFireball sideFireball) {
					return null;
				}
			});
			if (s == null) {
				continue;
			}
			drawCenter(s, e.position);
		}
	}

	private void renderEffects(List<Effect> effects) {
		final float[] tiltY = {0};
		
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

				@Override
				public Sprite visit(OneFrameEffect effect) {
					Sprite s = effect.sprite();
					float tint = effect.getTint();
					s.setColor(tint, tint, tint, tint);
					s.setFlip(effect.getFlip(), false);
					return s;
				}

				@Override
				public Sprite visit(FlameEffect effect) {
					return getFrameStopAtLastFrame(Assets.flamethrowerFlame, effect.stateTime());
				}

				@Override
				public Sprite visit(FireballAnimationEffect effect) {
					return getFrameLoop(Assets.topFireballLoop, effect.stateTime());
				}

				@Override
				public Sprite visit(DiamondGlowEffect effect) {
					Sprite s = null;
					switch (effect.diamond.type) {
					case TOKEN:
						s = Assets.tokenGlow;
						break;
					case DIANOMD_BLUE:
						s = Assets.diamondCyanGlow;
						break;
					case DIAMOND_PURPLE:
						s = Assets.diamondPurpleGlow;
						break;
					case DIAMOND_GREEN:
						s = Assets.diamondGreenGlow;
						break;
					default:
						Gdx.app.error("Error", "Diamond glow on a non-diamond collectable.");
						break;
					}
					float tint = (float) (0.5 + (Math.sin(effect.stateTime() * 6) + 1) / 5);
					s.setColor(tint, tint, tint, tint);
					effect.position_.y = effect.diamond.position.y + getBounceY(effect.diamond.stateTime());
					effect.position_.x = effect.diamond.position.x;
					return s;
				}
				
				@Override
				public Sprite visit(PowerupGlow effect) {
					Sprite s = effect.sprite();
					if (effect.c.state() == Collectable.STATUS_IDLE) {
						effect.position_.y = effect.c.position.y + getBounceY(effect.c.stateTime());
					} else {
						effect.position_.y = effect.c.position.y;
					}
					effect.position_.x = effect.c.position.x;
					return s;
				}

				@Override
				public Sprite visit(FireballStartEffect effect) {
					effect.position_.y = effect.originalY + cam.position.y; 
					Sprite $ = getFrameStopAtLastFrame(effect.getAnimation(), effect.stateTime());
//					$.setY($.getY() + world.camTarget);
					return $;
				}
				
				@Override
				public Sprite visit(WarningSkullEffect effect) {
					effect.position_.y = effect.originalY + cam.position.y; 
					Sprite s = effect.sprite();
					float tint = effect.getTint();
					s.setColor(tint, tint, tint, tint);
					s.setY(s.getY() + world.camTarget);
					return s;
				}
				
				@Override
				public Sprite visit(WarningExclEffect effect) {
					// TODO: merge with WarningSkullEffect.
					effect.position_.y = effect.originalY + cam.position.y; 
					Sprite s = effect.sprite();
					float tint = effect.getTint();
					s.setColor(tint, tint, tint, tint);
					s.setY(s.getY() + world.camTarget);
					return s;
				}
				
				

				@Override
				public Sprite visit(PlayerShieldEffect effect) {
					Sprite s = null;
					switch (effect.shieldState()) {
					case START:
						s = getFrameLoopOnSecondAnim(Assets.playerShieldEffectStart, Assets.playerShieldEffect, effect. stateTime());
						break;
					case MIDDLE:
						s = getFrameLoop(Assets.playerShieldEffect, effect.stateTime());
						break;
					case DIE:
						s = getFrameStopAtLastFrame(Assets.playerShieldEffectEnd, effect.stateTime());
						break;
					case HIT:
						s = getFrameLoopOnSecondAnim(Assets.playerShieldEffectHit, Assets.playerShieldEffect, effect. stateTime());
						break;
					}
					return s;
				}

				@Override
				public Sprite visit(PlayerMagnetEffect effect) {
					Sprite s = getFrameLoop(Assets.playerMagnetEffect, effect.stateTime());
					float tint = effect.getTint();
					s.setColor(tint, tint, tint, tint);
					return s; 
				}

				@Override
				public Sprite visit(PlayerOnionSkinEffect effect) {
					Sprite s = getPlayerFrame(effect.playerState,
							effect.playerStateTime, effect.playerSide, getCostumeAssets());
					float tint = effect.getTint();
					s.setColor(tint, tint, tint, tint);
					tiltY[0] = PLAYER_SPRITE_Y_OFFSET;
					return s;
				}
			});
			if (s != null) {
				s.setPosition(e.position_.x - s.getWidth()/2, e.position_.y - s.getHeight()/2);
				s.setRotation(e.getRotation());
				s.setScale(e.getXScale(), e.getYScale());
				s.setY(snapToY(s.getY()) + tiltY[0]);
				s.setAlpha(e.getAlpha());
				s.draw(batch);
			}
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
			} else if (wall instanceof FlamethrowerWall) {
				if (wall.status() == Wall.STATUS_ACTIVE) {
					keyFrame = Assets.flamethrower;
				} else if (wall.status() == Wall.STATUS_INACTIVE) {
					keyFrame = getFrameStopAtLastFrame(Assets.flamethrowerAll, wall.stateTime());
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
		case BRONZE_1: return Assets.coin1_1_land;
		case BRONZE_2: return Assets.coin1_2_land;
		case SILVER_1: return Assets.coin2_1_land;
		case SILVER_2: return Assets.coin2_2_land;
		case SILVER_MASK: return Assets.coin2_3_land;
		case GOLD_1: return Assets.coin3_1_land;
		case GOLD_2: return Assets.coin3_2_land;
		case GOLD_MASK: return Assets.coin3_3_land;
		case RING_GREEN: return Assets.coin4_1_land;
		case RING_PURPLE: return Assets.coin4_2_land;
		case RING_BLUE: return Assets.coin4_3_land;
		case TOKEN: return Assets.coin5_1_land;
		case DIANOMD_BLUE: return Assets.coin5_2_land;
		case DIAMOND_PURPLE: return Assets.coin5_3_land;
		case DIAMOND_GREEN: return Assets.coin5_4_land;
		case POWERUP_MAGNET:
		case POWERUP_SLOMO:
		case POWERUP_SHIELD:
		}
		return null;
	}
	
	private TextureRegion getCoinKeyFrame(Collectable.Type type) {
		switch(type) {
		case BRONZE_1: return Assets.coin1_1;
		case BRONZE_2: return Assets.coin1_2;
		case SILVER_1: return Assets.coin2_1;
		case SILVER_2: return Assets.coin2_2;
		case SILVER_MASK: return Assets.coin2_3;
		case GOLD_1: return Assets.coin3_1;
		case GOLD_2: return Assets.coin3_2;
		case GOLD_MASK: return Assets.coin3_3;
		case RING_GREEN: return Assets.coin4_1;
		case RING_PURPLE: return Assets.coin4_2;
		case RING_BLUE: return Assets.coin4_3;
		case TOKEN: return Assets.coin5_1;
		case DIANOMD_BLUE: return Assets.coin5_2;
		case DIAMOND_PURPLE: return Assets.coin5_3;
		case DIAMOND_GREEN: return Assets.coin5_4;
		case POWERUP_MAGNET: return Assets.powerupMagnet;
		case POWERUP_SLOMO: return Assets.powerupSlomo;
		case POWERUP_SHIELD: return Assets.powerupShield;
		}
		return null;
	}
	
	static public CostumeAssets getCostumeAssets() {
		if (ShopData.defaultCostume.isEquipped()) {
			return Assets.defaultCostume;
		}
		if (ShopData.goboCostume.isEquipped()) {
			return Assets.goboCostume;
		}
		if (ShopData.blikCostume.isEquipped()) {
			return Assets.blikCostume;
		}
		Gdx.app.error("ERROR", "No costume defined for this shop entry.");
		return Assets.defaultCostume;
	}
	
	private void renderCoins() {
		for (Collectable coin : world.collectables_) {
			TextureRegion keyFrame = null;
			if (coin.isPowerup() || coin.state() != Collectable.STATUS_IDLE) {
				keyFrame = getCoinKeyFrame(coin.type);
			} else {
				keyFrame = getFrameStopAtLastFrame(getCoinAnimation(coin.type), coin.stateTime());
			}
			
			float y = coin.position.y;
			if (coin.state() == Collectable.STATUS_IDLE) {
				y += getBounceY(coin.stateTime());
			}
			drawCenter(keyFrame, coin.position.x, y);
		}
	}
	
	private void renderGoldSacks() {
		for (GoldSack gs : world.goldSacks_) {
			Sprite s = null;
			switch (gs.state()) {
			case GoldSack.STATE_FALLING:
				s = Assets.goldSackFalling;
				break;
			case GoldSack.STATE_GROUND:
				s = getFrameStopAtLastFrame(Assets.goldSackLand, gs.stateTime());
				break;
			case GoldSack.STATE_PUMP:
				s = getFrameStopAtLastFrame(Assets.goldSackPump, gs.stateTime());
				break;
			case GoldSack.STATE_EMPTY:
				s = getFrameStopAtLastFrame(Assets.goldSackEnd, gs.stateTime());
				break;
			}
			drawCenter(s, gs.position.x, gs.position.y);
		}
	}

	private float getBounceY(float stateTime) {
		int index = (int) (stateTime * FPS);
		if (index < wallBounceArray.size())
			return wallBounceArray.get(index);
		return 0;
	}

	private void drawCenterWithTilt(TextureRegion keyFrame, Vector2 position, float xTilt, float yTilt) {
		batch.draw(keyFrame, position.x - keyFrame.getRegionWidth()/2+ xTilt, position.y - keyFrame.getRegionHeight()/2 + yTilt);
	}

	private void drawWithTilt(TextureRegion keyFrame, Vector2 position,
			float xTilt, float yTilt) {
		batch.draw(keyFrame, position.x + xTilt, position.y + yTilt);
	}

	private void renderPlayer () {
		Player player = world.player;
		Sprite keyFrame = getPlayerFrame(player.state(), player.stateTime, player.side, getCostumeAssets());
		if (keyFrame == null) return;
		keyFrame.setColor(1,1,1,1);
		drawCenterBottom(keyFrame, player.position.x, player.position.y - PLAYER_SPRITE_Y_OFFSET);
		
		Sprite addKeyFrame = getPlayerAddFrame(player.state(), player.stateTime, player.side, getCostumeAssets());
		if (addKeyFrame != null) {
			BatchUtils.setBlendFuncAdd(batch);
			drawCenterBottom(addKeyFrame , player.position.x, player.position.y - PLAYER_SPRITE_Y_OFFSET);
			BatchUtils.setBlendFuncNormal(batch);
		}
	}
	
	private Sprite getPlayerFrame(int state, float stateTime, boolean side, CostumeAssets ca) {
		Sprite keyFrame = null;
		switch (state) {
		case Player.STATE_IDLE:
			keyFrame = getFrameLoop(ca.playerIdle, stateTime);
			keyFrame.setFlip(side, false);
			break;
		case Player.STATE_RUNNING:
			float startTime = FRAME_TIME * ca.playerRunStart.size;
			if (stateTime < startTime) {
				keyFrame = getFrameLoop(ca.playerRunStart, stateTime, FPS); 
			} else {
				keyFrame = getFrameLoop(ca.playerRun, stateTime - startTime, FPS);
			}
			keyFrame.setFlip(side, false);
			break;
		case Player.STATE_JUMPING:
			keyFrame = getFrameLoop(ca.playerJump, stateTime);
			keyFrame.setFlip(side, false);
			break;
		case Player.STATE_LANDING:
			keyFrame = getFrameStopAtLastFrame(ca.playerLand, stateTime);
			keyFrame.setFlip(side, false);
			break;
		case Player.STATE_DIE:
		case Player.STATE_DEAD:
			if (world.player.deathType == Player.DEATH_BY_BURN) {
				keyFrame = getFrameDisappearAfterLastFrame(ca.playerBurn, stateTime);
			} else if (world.player.deathType == Player.DEATH_BY_CRUSH) {
				keyFrame = getFrameDisappearAfterLastFrame(ca.playerSquash, stateTime);
			} else {
				Gdx.app.log("ERROR", "Player is in death type: " + world.player.deathType);
			}
			if (keyFrame != null) {
				keyFrame.setFlip(side, false);
			}
			break;
		default:
			Gdx.app.log("ERROR", "Player is in invalid state: " + state);
		}
		return keyFrame;
	}
	
	private Sprite getPlayerAddFrame(int state, float stateTime, boolean side, CostumeAssets ca) {
		Sprite keyFrame = null;
		switch (state) {
		case Player.STATE_IDLE:
		case Player.STATE_RUNNING:
		case Player.STATE_JUMPING:
		case Player.STATE_LANDING:
			return null;
		case Player.STATE_DIE:
		case Player.STATE_DEAD:
			if (world.player.deathType == Player.DEATH_BY_BURN) {
				keyFrame = getFrameDisappearAfterLastFrame(ca.playerBurnAdd, stateTime);
			} else if (world.player.deathType == Player.DEATH_BY_CRUSH) {
				keyFrame = getFrameDisappearAfterLastFrame(ca.playerSquashAdd, stateTime);
			} else {
				Gdx.app.log("ERROR", "Player is in death type: " + world.player.deathType);
			}
			if (keyFrame != null) {
				keyFrame.setFlip(side, false);
			}
			break;
		default:
			Gdx.app.log("ERROR", "Player is in invalid state: " + state);
		}
		return keyFrame;
	}
	
	private Sprite getFrameLoopOnSecondAnim(Array<Sprite> startAnim,
			Array<Sprite> loopAnim, float stateTime) {
		float startAnimTime = FRAME_TIME * startAnim.size;
		if (stateTime < startAnimTime) {
			return getFrameStopAtLastFrame(startAnim, stateTime); 
		} else {
			return getFrameLoop(loopAnim, stateTime - startAnimTime);
		}
	}
	
	public static Sprite getFrameLoop(Array<Sprite> anim, float stateTime) {
		return getFrameLoop(anim, stateTime, FPS);
	}
	
	public static Sprite getFrameLoop(Array<Sprite> anim, float stateTime, float fps) {
		return anim.get((int) (stateTime * fps) % anim.size);
	}
	
	public static Sprite getFrameStopAtLastFrame(Array<Sprite> anim, float stateTime) {
		int frameIndex = (int) (stateTime * FPS);
		frameIndex = Math.min(frameIndex, anim.size-1);
		return anim.get(frameIndex);
	}
	
	private Sprite getFrameDisappearAfterLastFrame(Array<Sprite> anim, float stateTime) {
		int frameIndex = (int) (stateTime * FPS);
		if (frameIndex > anim.size-1) {
			return null;
		}
		return anim.get(frameIndex);
	}
	
	private void drawCenterBottom(TextureRegion keyFrame, float x, float y) {
		drawCenter(keyFrame, x, y + keyFrame.getRegionHeight()/2);
	}
	
	private void drawCenter(TextureRegion keyFrame, Vector2 position) {
		drawCenter(keyFrame, position.x, position.y);
	}
	
	private  void drawCenter(TextureRegion keyFrame, float x, float y) {
		y = snapToY(y);
		batch.draw(keyFrame, x - keyFrame.getRegionWidth()/2, y - keyFrame.getRegionHeight()/2);
	}
	
	public static float snapToY(float y) {
		double fraction = FRUSTUM_WIDTH / Gdx.graphics.getWidth();
		float newy = (float) (Math.floor(y / fraction) * fraction);
		return newy;
		
	}
}
