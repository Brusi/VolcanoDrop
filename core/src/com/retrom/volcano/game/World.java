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

import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.SoundAssets;
import com.retrom.volcano.effects.BurningWallGlow;
import com.retrom.volcano.effects.DiamondGlowEffect;
import com.retrom.volcano.effects.Effect;
import com.retrom.volcano.effects.EffectFactory;
import com.retrom.volcano.effects.FireballAnimationEffect;
import com.retrom.volcano.effects.FireballGlow;
import com.retrom.volcano.effects.FireballStartEffect;
import com.retrom.volcano.effects.FlameEffect;
import com.retrom.volcano.effects.FlameGlowEffect;
import com.retrom.volcano.effects.PlayerShieldEffect;
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
import com.retrom.volcano.game.EventQueue.Event;
import com.retrom.volcano.game.objects.BurningWall;
import com.retrom.volcano.game.objects.Collectable;
import com.retrom.volcano.game.objects.Enemy;
import com.retrom.volcano.game.objects.Flame;
import com.retrom.volcano.game.objects.FlamethrowerWall;
import com.retrom.volcano.game.objects.SideFireball;
import com.retrom.volcano.game.objects.Spitter;
import com.retrom.volcano.game.objects.TopFireball;
import com.retrom.volcano.game.objects.WallDual;
import com.retrom.volcano.game.objects.Wall;
import com.retrom.volcano.game.objects.WallSingle;

public class World {

	public final Player player;
	
	public final List<Wall> walls_ = new ArrayList<Wall>();
	public final List<Wall> activeWalls_ = new ArrayList<Wall>();
	
	public final List<Enemy> enemies_ = new ArrayList<Enemy>();
	
	public final List<Collectable> collectables_ = new ArrayList<Collectable>();
	
	public int score = 0;
	
	
	public static final Vector2 gravity = new Vector2(0, -2200);
	public static final float WIDTH = 640f;
	
	// Permanent obstacles.
	private Rectangle leftWall_ = new Rectangle(-World.WIDTH / 2, 0, Wall.SIZE, 5000);
	private Rectangle rightWall_ = new Rectangle(World.WIDTH / 2 - Wall.SIZE, 0, Wall.SIZE, 5000);
	
	public ActiveFloors floors_ = new ActiveFloors();
	
	private final Spawner spawner_;
	private final EventQueue worldEvents_ = new EventQueue();
	
	
	// Powerup time counters.
	private float magnetTime = 0f;
	private float slomoTime = 0f;
	private float shieldTime = 0f;
	
	// Special effect holders.
	private Effect playerShieldEffect;

	private List<Rectangle> obstacles_;

	public float camTarget;

	public Background background = new Background();
	

	final public List<Effect> addEffectsUnder = new ArrayList<Effect>();
	final public List<Effect> effects = new ArrayList<Effect>();
	final public List<Effect> addEffects = new ArrayList<Effect>();
	final public List<Effect> screenEffects = new ArrayList<Effect>();

	private final WorldListener listener_;
	
	private float leftHighestSpitter = 0;
	private float rightHighestSpitter = 0;

	// Cheats:
	private boolean godMode_ = false;

	
	public interface WorldListener {
		public void restartGame();
	}
	
	public World (WorldListener listener) {
		this.listener_ = listener;
		this.player = new Player(0, 100, new Player.HitRectHandler() {
			
			@Override
			public void handle(Rectangle rect) {
				if (shieldTime <= 0) {
					player.killByCrush();
					return;
				}
				for (Wall wall : activeWalls_) {
					if (wall.bounds == rect) {
						wall.setStatus(Wall.STATUS_EXPLODE);
					}
				}
			}
		});
		this.spawner_ = new Spawner(floors_, activeWalls_, new Spawner.SpawnerHandler() {
			
			@Override
			public void dropWall(int col) {
				addWall(col);
			}

			@Override
			public void dropCoin(float x, Collectable.Type type) {
				addCoin(x, type);
			}

			@Override
			public void dropDualWall(int col) {
				addDualWall(col);
			}

			@Override
			public void dropBurningWall(int col) {
				addBurningWall(col);
			}

			@Override
			public void dropFlamethrower(int col) {
				addFlamethrower(col);
			}

			@Override
			public void dropFireball(int col) {
				prepareFireball(col);
			}
		});
	}

	private void updateCheats() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
			addCoin(0, Collectable.Type.POWERUP_MAGNET);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			addCoin(0, Collectable.Type.POWERUP_SHIELD);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
			magnetTime += 1f;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			slomoTime += 2f;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
			shieldTime += 1f;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
			addCoin((float) (Math.random() * 200), Collectable.Type.COIN_5_1);
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			finishGame();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
			addSpitter(400, false);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.J)) {
			addSpitter(400, true);
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
			addSideFireball(0, 500, false);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
			prepareFireball((int) Math.floor(Math.random() * 6));
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			addEffects.add(new FireballStartEffect(new Vector2(100,100)));
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
			godMode_ = !godMode_;
		}
	}

	public void update(float deltaTime) {
		if (slomoTime > 0) {
			float slomoRatio;
			if (slomoTime > 1) {
				slomoRatio = 0.66f;
			} else {
				slomoRatio = 1f / (slomoTime+1);
			}
			deltaTime *= slomoRatio;
			
			SoundAssets.setPitch(slomoRatio);
		} else {
			SoundAssets.setPitch(1f);
		}
		
		updateCheats();
		worldEvents_.update(deltaTime);
		
		obstacles_ = new ArrayList<Rectangle>();
		obstacles_.add(leftWall_);
		obstacles_.add(rightWall_);
		for (Wall wall : activeWalls_) {
			obstacles_.add(wall.bounds);
		}
		obstacles_.addAll(floors_.getRects());
		
		updatePlayer(deltaTime);
		updateWalls(deltaTime);
		updateEnemies(deltaTime);
		
		checkSpitters();
		
		updateCoins(deltaTime);
		updateSpawner(deltaTime);
		updatePowerups(deltaTime);
		updateEffects(deltaTime);
		
		updateCamera(deltaTime);
		
		
		leftWall_.y = player.bounds.y - leftWall_.height/2;
		rightWall_.y = player.bounds.y - rightWall_.height/2;
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
			addCoin(50, Collectable.Type.COIN_3_1);
		}
	}
	
	private void checkSpitters() {
		for (float hole : background.leftHoleList) {
			if (camTarget > hole + 40 && hole > leftHighestSpitter) {
				leftHighestSpitter = hole;
				addSpitter(hole + 30, Spitter.LEFT);
			}
		}
		
		for (float hole : background.rightHoleList) {
			if (camTarget > hole + 40 && hole > rightHighestSpitter) {
				rightHighestSpitter = hole;
				addSpitter(hole + 30, Spitter.RIGHT);
			}
		}
		
	}

	private void updateEnemies(float deltaTime) {
		for (Enemy e : enemies_) {
			e.update(deltaTime);
			e.accept(new Enemy.Visitor<Void>() {
				@Override
				public Void visit(Flame flame) {
					if (flame.bounds.overlaps(player.bounds) && !player.isDead()) {
						if (shieldTime <= 0) {
							player.killByBurn();
						}
					}
					return null;
				}

				@Override
				public Void visit(TopFireball fireball) {
					if (fireball.state() == Enemy.STATE_ACTIVE && fireball.bounds.overlaps(player.bounds) && !player.isDead()) {
						if (shieldTime <= 0) {
							player.killByBurn();
						}
						fireball.explode();
					} else if (fireball.state() == Enemy.STATE_ACTIVE) {
						for (Rectangle obstacle : obstacles_) {
							if (fireball.bounds.overlaps(obstacle)) {
								fireball.explode();
							}
						}
					}
					if (fireball.state() == Enemy.STATE_DONE) {
						addEffects.add(EffectFactory.fireballExpodeEffect(new Vector2(fireball.position.x, fireball.position.y + 10f)));
						SoundAssets.playSound(SoundAssets.fireballEnd);
					}
					return null;
				}

				@Override
				public Void visit(Spitter spitter) {
					return null;
				}

				@Override
				public Void visit(SideFireball fireball) {
					// TODO: combine with topFireball
					if (fireball.state() == Enemy.STATE_ACTIVE && fireball.bounds.overlaps(player.bounds) && !player.isDead()) {
						if (shieldTime <= 0) {
							player.killByBurn();
						}
						fireball.explode();
					} else if (fireball.state() == Enemy.STATE_ACTIVE) {
						for (Rectangle obstacle : obstacles_) {
							if (fireball.bounds.overlaps(obstacle)) {
								fireball.explode();
							}
						}
					}
					if (fireball.state() == Enemy.STATE_DONE) {
						addEffects.add(EffectFactory.fireballExpodeEffect(new Vector2(fireball.position.x, fireball.position.y + 10f)));
						SoundAssets.playSound(SoundAssets.fireballEnd);
					}
					return null;
				}
			});
		}
		for (Iterator<Enemy> it = enemies_.iterator(); it.hasNext();) {
			Enemy e = it.next();
			if (e.state() == Enemy.STATE_DONE) {
				it.remove();
			}
		}
	}

	private void updateEffects(float deltaTime) {
		updateEffectsList(deltaTime, addEffectsUnder);
		updateEffectsList(deltaTime, effects);
		updateEffectsList(deltaTime, addEffects);
		updateEffectsList(deltaTime, screenEffects);
	}
	
	private void updateEffectsList(float deltaTime, List<Effect> l) {
		for (Effect e : l) {
			e.update(deltaTime);
		}
		for (Iterator<Effect> it = l.iterator(); it.hasNext();) {
			Effect e = it.next();
			if (e.state() == Effect.STATE_DONE) {
				it.remove();
			}
		}
	}

	private void updateCamera(float deltaTime) {
		camTarget = (floors_.getTotalBlocks()) * Wall.SIZE / 6f + WorldRenderer.FRUSTUM_HEIGHT / 3f;
		background.setY(camTarget);
		
	}

	private void updatePowerups(float deltaTime) {
		if (magnetTime > 0) {
			magnetTime -= deltaTime;
			if (magnetTime <= 0) {
				for (Collectable c : collectables_) {
					c.setState(Collectable.STATUS_FALLING);
					c.velocity.x = c.velocity.y = 0;
				}
				SoundAssets.stopSound(SoundAssets.powerupMagnetLoop);
				SoundAssets.playSound(SoundAssets.powerupMagnetEnd);
			}
		}
		if (slomoTime > 0) {
			slomoTime -= deltaTime;
			if (slomoTime <= 0) {
				SoundAssets.playSound(SoundAssets.powerupTimeEnd);
			}
		}
		if (shieldTime > 0) {
			shieldTime -= deltaTime;
			if (shieldTime <= 0) {
				playerShieldEffect.destroy();
				playerShieldEffect = null;
				SoundAssets.playSound(SoundAssets.powerupShieldEnd);
			}
		}
	}

	private float topScreenY() {
		return floors_.getTotalBlocks() / 6f * Wall.SIZE + 12*Wall.SIZE;
	}
	
	private void prepareFireball(final int col) {
		final float y = topScreenY() - TopFireball.DISTANCE_FROM_TOP - camTarget;
		addEffects.add(new FireballStartEffect(new Vector2(Utils
				.xOfCol(col), y)));
		SoundAssets.playSound(SoundAssets.fireballStart);
		worldEvents_.addEventFromNow(TopFireball.PREPARATION_DELAY, new Event() {
			@Override
			public void invoke() {
				addFireball(col, y + camTarget);
			}
		});
	}
	
	private void addSideFireball(float x, float y, boolean side) {
		SideFireball fireball = new SideFireball(x, y, side);
		enemies_.add(fireball);
		int side3 = side ? FireballAnimationEffect.RIGHT : FireballAnimationEffect.LEFT; 
		addEffects.add(new FireballAnimationEffect(fireball, side3));
		addEffects.add(new FireballGlow(fireball, side3));
	}
	
	private void addFireball(int col, float y) {
		TopFireball fireball = new TopFireball(col, y); 
		enemies_.add(fireball);
		addEffects.add(new FireballAnimationEffect(fireball, FireballAnimationEffect.DOWN));
		addEffects.add(new FireballGlow(fireball, 0));
	}
	
	public void addWall(int col) {
		float wallY = topScreenY();
		Wall wall = new WallSingle(col, wallY);
		walls_.add(wall);
		activeWalls_.add(wall);
	}

	protected void addDualWall(int col) {
		float wallY = topScreenY();
		Wall wall = new WallDual(col, wallY);
		walls_.add(wall);
		activeWalls_.add(wall);
	}
	
	public void addBurningWall(int col) {
		float wallY = topScreenY();
		BurningWall wall = new BurningWall(col, wallY);
		walls_.add(wall);
		activeWalls_.add(wall);
		addEffects.add(new BurningWallGlow(wall));
	}
	
	protected void addFlamethrower(int col) {
		float wallY = topScreenY();
		FlamethrowerWall wall = new FlamethrowerWall(col, wallY);
		walls_.add(wall);
		activeWalls_.add(wall);
	}
	
	public void addCoin(float x, Collectable.Type type) {
		float yval = topScreenY();
		Collectable coin = new Collectable(x, yval, type);
		collectables_.add(coin);
		if (coin.type == Collectable.Type.COIN_5_1 || coin.type == Collectable.Type.COIN_5_2 || coin.type == Collectable.Type.COIN_5_3 || coin.type == Collectable.Type.COIN_5_4) {
			addEffects.add(new DiamondGlowEffect(coin));
		}
		if (coin.isPowerup()) {
			addEffects.add(EffectFactory.powerupBackGlow(coin.type, coin));
		}
	}
	
	public void addSpitter(final float y, final boolean side) {
		Deque<Float> holeList = side == Spitter.LEFT ? background.leftHoleList : background.rightHoleList;
		if (holeList.peek() == null) {
			// Do not create spitter if there is no hole.
			return;
		}
		
		final float x = side == Spitter.LEFT ? -250 : 250;
		final float fireX = side == Spitter.LEFT ? -220 : 220;
		final Spitter spitter = new Spitter(x, y, side);
		enemies_.add(spitter);
		EventQueue.Event shootFireEvent = new Event() {
			@Override
			public void invoke() {
				addSideFireball(fireX, spitter.position.y, side);
			}
		};
		
		worldEvents_.addEventFromNow(3.2f, shootFireEvent);
		worldEvents_.addEventFromNow(3.9f, shootFireEvent);
		worldEvents_.addEventFromNow(4.6f, shootFireEvent);
		
		SoundAssets.playSound(SoundAssets.spitterSequence);
	}
	
	private void updateSpawner(float deltaTime) {
		spawner_.update(deltaTime);
		
	}

	private void updateWalls(float deltaTime) {
		for (Wall wall : activeWalls_) {
			wall.update(deltaTime);
			
			if (wall.status() == Wall.STATUS_EXPLODE) {
				wall.setStatus(Wall.STATUS_GONE);
				// TODO: add sound and particles.
			}
			
			List<Rectangle> rects = new ArrayList<Rectangle>(floors_.getRects());
			for (Rectangle rect : rects) {
				if (wall.bounds.overlaps(rect)) {
					wall.bounds.y = rect.y+rect.height;
					wall.bounds.getCenter(wall.position);
					wall.setStatus(Wall.STATUS_INACTIVE);
					if (wall.isDual()) {
						floors_.addToColumn(wall.col());
						floors_.addToColumn(wall.col() + 1);
						SoundAssets.playRandomSound(SoundAssets.wallDualHit);
					} else if (wall instanceof FlamethrowerWall){
						floors_.addToColumn(wall.col());
						SoundAssets.playSound(SoundAssets.flamethrowerStart);
					} else {
						floors_.addToColumn(wall.col());
						SoundAssets.playRandomSound(SoundAssets.wallHit);
					}
					
				}
			}
		}
		
		// Remove inactive walls
		for (Iterator<Wall> it = activeWalls_.iterator(); it.hasNext();) {
			Wall wall = it.next();
			if (wall.status() == Wall.STATUS_INACTIVE || wall.status() == Wall.STATUS_GONE) {
				it.remove();
			}
		}
		
		for (Iterator<Wall> it = walls_.iterator(); it.hasNext();) {
			Wall wall = it.next();
			wall.updateStateTime(deltaTime);
			
			if (wall instanceof FlamethrowerWall) {
				FlamethrowerWall f = (FlamethrowerWall) wall;
				if (wall.status() == Wall.STATUS_INACTIVE && !f.flameAdded && wall.stateTime() > 1.1) {
					f.flameAdded = true;
					addEffects.add(new FlameEffect(new Vector2(new Vector2(wall.position.x, wall.position.y + 96))));
					addEffects.add(new FlameGlowEffect(new Vector2(new Vector2(wall.position.x, wall.position.y + Wall.SIZE/2))));
					enemies_.add(new Flame(wall.position.x, wall.position.y + 96));
				}
			}
			
			if (wall.position.y < camTarget - 8*Wall.SIZE) {
				wall.setStatus(Wall.STATUS_GONE);
			}
			
			if (wall.status() == Wall.STATUS_GONE) {
				it.remove();
			}
		}
		
	}
	
	private void updateCoins(float deltaTime) {
		boolean coinCrushed = false;
		for (Collectable c : collectables_) {
			if (magnetTime > 0) {
				if (c.state() == Collectable.STATUS_FALLING
						|| c.state() == Collectable.STATUS_IDLE
						|| c.state() == Collectable.STATUS_MAGNETIZED) {
					c.magnetTo(player.position, deltaTime);
				}
			}
			
			c.setObstacles(obstacles_);
			c.update(deltaTime);
			for (Rectangle rect : floors_.getRects()) {
				if (c.bounds.overlaps(rect)) {
					if (c.state() == Collectable.STATUS_IDLE
							|| rect.contains(c.bounds) || c.state() == Collectable.STATUS_MAGNETIZED && rect.contains(c.position)) {
						c.setState(Collectable.STATUS_CRUSHED);
					} else if (c.state() == Collectable.STATUS_FALLING) {
						c.setState(Collectable.STATUS_IDLE);
						c.velocity.x = c.velocity.y = 0; 
						c.bounds.y = rect.y + rect.height;
						c.bounds.getCenter(c.position);
						c.velocity.y = 0;
					}
				}
			}
			if (player.isAlive() && c.bounds.overlaps(player.bounds)) {
				c.setState(Collectable.STATUS_TAKEN);
				handleCollectable(c);
			}
			if (c.state() == Collectable.STATUS_CRUSHED) {
				if (!coinCrushed) {
					SoundAssets.playSound(SoundAssets.coinCrushed);
					coinCrushed = true;
				}
				effects.add(EffectFactory.coinCrushParticle(c.type, c.position));
				effects.add(EffectFactory.coinCrushParticle(c.type, c.position));
				effects.add(EffectFactory.coinCrushParticle(c.type, c.position));
				screenEffects.add(EffectFactory.coinCrushedEffect(c.position));
			}
		}
		
		// Remove crushed coins.
		for (Iterator<Collectable> it = collectables_.iterator(); it.hasNext();) {
			Collectable coin = it.next();
			if (coin.state() == Collectable.STATUS_CRUSHED || coin.state() == Collectable.STATUS_TAKEN) {
				it.remove();
			}
		}
	}

	private void handleCollectable(Collectable collectable) {
		switch (collectable.type) {
		case POWERUP_MAGNET:
			magnetTime = 5f;
			SoundAssets.playSound(SoundAssets.powerupMagnetStart);
			SoundAssets.loopSound(SoundAssets.powerupMagnetLoop);
			break;
		case POWERUP_SLOMO:
			slomoTime = 5f;
			SoundAssets.playSound(SoundAssets.powerupTimeStart);
			break;
		case POWERUP_SHIELD:
			shieldTime = 5f;
			if (playerShieldEffect == null) {
				playerShieldEffect = new PlayerShieldEffect(player.position); 
				addEffects.add(playerShieldEffect);
				SoundAssets.playSound(SoundAssets.powerupShieldStart);
			}
			break;
		case COIN_1_1:
		case COIN_1_2:
			addScore(1);
			effects.add(new Score1Effect(collectable.position.cpy()));
			addEffects.add(EffectFactory.bronzeCollectEffect(collectable.position));
			SoundAssets.playRandomSound(SoundAssets.coinsCollectBronze);
			break;
		case COIN_2_1:
		case COIN_2_2:
			addScore(3);
			effects.add(new Score3Effect(collectable.position.cpy()));
			SoundAssets.playRandomSound(SoundAssets.coinsCollectSilver);
			addEffects.add(EffectFactory.silverCollectEffect(collectable.position));
			break;
		case COIN_2_3:
			addScore(4);
			effects.add(new Score4Effect(collectable.position.cpy()));
			SoundAssets.playRandomSound(SoundAssets.coinsCollectSilver);
			addEffects.add(EffectFactory.silverCollectEffect(collectable.position));
			break;
		case COIN_3_1:
		case COIN_3_2:
			addScore(5);
			effects.add(new Score5Effect(collectable.position.cpy()));
			SoundAssets.playRandomSound(SoundAssets.coinsCollectGold);
			addEffects.add(EffectFactory.goldCollectEffect(collectable.position));
			break;
		case COIN_3_3:
			addScore(10);
			effects.add(new Score10Effect(collectable.position.cpy()));
			SoundAssets.playSound(SoundAssets.coinsCollectGoldMask);
			addEffects.add(EffectFactory.goldCollectEffect(collectable.position));
			break;
		case COIN_4_1:
		case COIN_4_2:
		case COIN_4_3:
			addScore(6);
			effects.add(new Score6Effect(collectable.position.cpy()));
			SoundAssets.playRandomSound(SoundAssets.coinsCollectRing);
			addEffects.add(EffectFactory.goldCollectEffect(collectable.position));
			break;
		case COIN_5_1:
			addScore(25);
			effects.add(new Score25Effect(collectable.position.cpy()));
			SoundAssets.playSound(SoundAssets.coinsCollectBigToken);
			addEffects.add(EffectFactory.goldCollectEffect(collectable.position));
			break;
		case COIN_5_2:
			addScore(15);
			effects.add(new Score15TealEffect(collectable.position.cpy()));
			SoundAssets.playRandomSound(SoundAssets.coinsCollectDiamond);
			addEffects.add(EffectFactory.cyanDiamondCollectEffect(collectable.position));
			break;
		case COIN_5_3:
			addScore(15);
			effects.add(new Score15PurpleEffect(collectable.position.cpy()));
			SoundAssets.playRandomSound(SoundAssets.coinsCollectDiamond);
			addEffects.add(EffectFactory.purpleDiamondCollectEffect(collectable.position));
			break;
		case COIN_5_4:
			addScore(15);
			effects.add(new Score15GreenEffect(collectable.position.cpy()));
			SoundAssets.playRandomSound(SoundAssets.coinsCollectDiamond);
			addEffects.add(EffectFactory.greenDiamondCollectEffect(collectable.position));
			break;
		}
	}

	private void addScore(int scoreToAdd) {
		score += scoreToAdd;
		
	}

	private void updatePlayer (float deltaTime) {
		if (player.state() == Player.STATE_DIE) {
			if (godMode_) {
				player.revive();
				player.bounds.y = topScreenY();
				return;
			} else {
				player.deathAcknoladged();
				if (player.deathType == Player.DEATH_BY_BURN) {
					SoundAssets.playSound(SoundAssets.playerDeathBurn);
					addEffects.add(EffectFactory.playerExplodeEffect(player.position));
				} else if (player.deathType == Player.DEATH_BY_CRUSH) {
					addEffects.add(EffectFactory.playerExplodeEffect(new Vector2(player.position.x, player.position.y - player.bounds.height/2)));
					SoundAssets.playSound(SoundAssets.playerDeathCrush);
				} else {
					Gdx.app.error("ERROR", "Player death type not specified.");
				}
				worldEvents_.addEventFromNow(2f, new Event() {
					@Override
					public void invoke() {
						finishGame();
					}
				});
			}
		}
		
		player.setObstacles(obstacles_);
		player.setActiveWalls(activeWalls_);
		player.setShieldStatus(shieldTime > 0);
		player.update(deltaTime);
	}
	
	private void finishGame() {
		SoundAssets.stopAllSounds();
		listener_.restartGame();
	}
}
