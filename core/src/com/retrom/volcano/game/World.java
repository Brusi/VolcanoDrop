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
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.SoundAssets;
import com.retrom.volcano.effects.BurnParticle;
import com.retrom.volcano.effects.BurningWallGlow;
import com.retrom.volcano.effects.CoinMagnetGlowEffect;
import com.retrom.volcano.effects.CoinMagnetStartEffect;
import com.retrom.volcano.effects.DiamondGlowEffect;
import com.retrom.volcano.effects.DustEffect;
import com.retrom.volcano.effects.Effect;
import com.retrom.volcano.effects.EffectFactory;
import com.retrom.volcano.effects.FireballAnimationEffect;
import com.retrom.volcano.effects.FireballGlow;
import com.retrom.volcano.effects.FireballStartEffect;
import com.retrom.volcano.effects.FlameEffect;
import com.retrom.volcano.effects.FlameGlowEffect;
import com.retrom.volcano.effects.PlayerMagnetEffect;
import com.retrom.volcano.effects.PlayerMagnetGlow;
import com.retrom.volcano.effects.PlayerOnionSkinEffect;
import com.retrom.volcano.effects.PlayerShieldEffect;
import com.retrom.volcano.effects.SackFlare;
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
import com.retrom.volcano.effects.ShieldFlare;
import com.retrom.volcano.effects.SmokeEffect;
import com.retrom.volcano.game.EventQueue.Event;
import com.retrom.volcano.game.objects.BurningWall;
import com.retrom.volcano.game.objects.Collectable;
import com.retrom.volcano.game.objects.Collectable.Type;
import com.retrom.volcano.game.objects.Enemy;
import com.retrom.volcano.game.objects.Flame;
import com.retrom.volcano.game.objects.FlamethrowerWall;
import com.retrom.volcano.game.objects.GoldSack;
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
	public final List<GoldSack> goldSacks_ = new ArrayList<GoldSack>();
	
	public int score = 0;
	
	public static final Vector2 gravity = new Vector2(0, -2200);
	public static final float WIDTH = 640f;
	
	// Permanent obstacles.
	private Rectangle leftWall_ = new Rectangle(-World.WIDTH / 2, 0, Wall.SIZE, 5000);
	private Rectangle rightWall_ = new Rectangle(World.WIDTH / 2 - Wall.SIZE, 0, Wall.SIZE, 5000);
	
	public ActiveFloors floors_ = new ActiveFloors();
	
	private final Spawner spawner_;
	private final EventQueue worldEvents_ = new EventQueue();
	
	// Pause effect objects.
	public static final float PAUSE_EFFECT_DURATION = 0.3f;
	final EventQueue pauseEffectEvents = new EventQueue();
	final public List<Effect> pauseEffects = new ArrayList<Effect>();
	float pauseEffectStateTime_;
	
	
	// Powerup time counters.
	float magnetTime = 0f;
	float slomoTime = 0f;
	float shieldTime = 0f;
	boolean consecutiveSlomo = false;
	
	static final float TOTAL_SHIELD_TIME = 5f;
	static final float TOTAL_SLOMO_TIME = 5f;
	static final float TOTAL_MAGNET_TIME = 5f;
	static final public float POWERUP_APPEAR_TIME = 1f;
	
	// Special effect holders.
	private PlayerShieldEffect playerShieldEffect;
	private PlayerMagnetEffect playerMagnetEffect;
	private PlayerMagnetGlow playerMagnetGlow;
	
	private List<CoinMagnetGlowEffect> coinMagnetGlows = new ArrayList<CoinMagnetGlowEffect>();
	
	private final List<Effect> magnetEffects = new ArrayList<Effect>();

	private List<Rectangle> obstacles_;

	public float camTarget;
	boolean quakeOn;
	float quakeShakeFactor;
	float quakeX;

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

	private static float QUAKE_DURATION = 2.6f;
	
	private float gameTime = 0;

	public interface WorldListener {
		public void restartGame();
	}
	
	public World (WorldListener listener) {
		SoundAssets.startMusic();
		
		this.listener_ = listener;
		this.player = new Player(0, 200, new Player.HitRectHandler() {
			@Override
			public void handle(Rectangle rect) {
				if (shieldTime <= 0) {
					player.killByCrush();
					return;
				}
				for (Wall wall : activeWalls_) {
					if (wall.bounds == rect) {
						wall.setStatus(Wall.STATUS_EXPLODE);
						playerShieldEffect.hit();
						SoundAssets.playSound(SoundAssets.powerupShieldHit);
						player.landAnimation();
						addDust(wall.position.x, wall.position.y);
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
				dropCoinFromCeiling(x, type);
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

			@Override
			public void dropSack(int col, int numCoins) {
				addSack(col, numCoins);
			}

			@Override
			public void quake(boolean big) {
				if (big) {
					startQuake();
				} else {
					startSmallQuake();
				}
			}
		});
		
//		worldEvents_.addEventFromNow(2f, new EventQueue.Event() {
//			@Override
//			public void invoke() {
//				startQuake();
//			}
//		});
	}

	private void updateCheats() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
			goldSacks_.add(new GoldSack(100, 100, 5));
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
			dropCoinFromCeiling(0, Collectable.Type.POWERUP_MAGNET);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			dropCoinFromCeiling(0, Collectable.Type.POWERUP_SHIELD);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
			magnetTime += 1f;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
			dropCoinFromCeiling(0, Collectable.Type.POWERUP_SLOMO);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
			dropCoinFromCeiling((float) (Math.random() * 200), Collectable.Type.COIN_5_1);
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
		if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
			prepareFireball((int) Math.floor(Math.random() * 6));
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			addEffects.add(new FireballStartEffect(new Vector2(100,100)));
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
			godMode_ = !godMode_;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			startSmallQuake();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
			screenEffects.add(new DustEffect(new Vector2(100,100)));
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
			spawner_.enabled = !spawner_.enabled;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
			addFlamethrower(3);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
			addBurnParticles(100, 100);
		}
	}

	private void addBurnParticle(float base_x, float base_y) {
		Vector2 dir = Utils.randomDir();
		float x = base_x + dir.x * Utils.randomRange(10, 35);
		float y = base_y + dir.y * Utils.randomRange(10, 35);
		addEffectsUnder.add(new BurnParticle(x, y));
	}
	
	private void addBurnParticles(float base_x, float base_y) {
		for (int i=0; i < 10; i++) {
			addBurnParticle(base_x, base_y);
		}
	}

	private void AddOneOnionSkin() {
		addEffectsUnder.add(new PlayerOnionSkinEffect(player.position.cpy(), player.state(), player.stateTime, player.side));
	}

	private void startQuakeWithParams(float duration, float shakeFactor) {
		quakeOn = true;
		quakeShakeFactor = shakeFactor;
//		Gdx.input.vibrate((int)QUAKE_DURATION * 1000);
		worldEvents_.addEventFromNow(duration, new EventQueue.Event() {
			@Override
			public void invoke() {
				quakeOn = false;
			}
		});
		int NUM_DUST = Math.round(12 * duration / QUAKE_DURATION);
		System.out.println("NUM_DUST =" + NUM_DUST );
		for (int i=0; i < NUM_DUST; ++i) {
			worldEvents_.addEventFromNow((float)(duration * Math.random()), new EventQueue.Event() {
				@Override
				public void invoke() {
					float ypos = Utils.randomRange(camTarget - WorldRenderer.FRUSTUM_HEIGHT / 2, WorldRenderer.FRUSTUM_HEIGHT);
					float xpos = WorldRenderer.FRUSTUM_WIDTH / 2 - Wall.SIZE + Utils.random2Range(10);
					if (Utils.randomBool()) {
						xpos *= -1;
					}
					screenEffects.add(new DustEffect(new Vector2(xpos, ypos)));
				}
			});
		}
	}
	
	private void startQuake() {
		SoundAssets.playRandomSound(SoundAssets.quake);
		startQuakeWithParams(QUAKE_DURATION, 1);
	}
	
	private void startSmallQuake() {
		SoundAssets.playRandomSound(SoundAssets.quakeSmall);
		startQuakeWithParams(QUAKE_DURATION / 3, 0.5f);
	}

	public void update(float deltaTime) {
		if (!pauseEffectEvents.isEmpty()) {
			pauseEffectEvents.update(deltaTime);
			updateEffectsList(deltaTime, pauseEffects);
			pauseEffectStateTime_ += deltaTime;
			return;
		}
		step(deltaTime);
	}
	
	public void step(float deltaTime) {
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
		
		gameTime += deltaTime;
		if (gameTime > 1 && gameTime < 60) {
			background.level = 2;
		} else if (gameTime >= 60){
			background.level = 3;
		}
		
		updateQuake(deltaTime);
		
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
		updateGoldSacks(deltaTime);
		updateSpawner(deltaTime);
		updatePowerups(deltaTime);
		updateEffects(deltaTime);
		
		updateCamera(deltaTime);
		
		
		leftWall_.y = player.bounds.y - leftWall_.height/2;
		rightWall_.y = player.bounds.y - rightWall_.height/2;
	}

	private void updateQuake(float deltaTime) {
		if (quakeOn) {
			quakeX += (Math.random() * 500 - 250) * deltaTime * quakeShakeFactor;
		}
		quakeX *= (float) Math.pow(0.1, deltaTime);
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

	private void updateEnemies(final float deltaTime) {
		for (Enemy e : enemies_) {
			e.update(deltaTime);
			e.accept(new Enemy.Visitor<Void>() {
				@Override
				public Void visit(Flame flame) {
					if (flame.bounds.overlaps(player.bounds) && !player.isDead()) {
						if (shieldTime <= 0) {
							if (flame.stateTime() > 0.3) {
								player.killByBurn();
							}
						} else {
							player.velocity.y += (56 - (player.position.y - flame.position.y)) * 2 * 60 * deltaTime;
							if (Math.random() < deltaTime * 8) {
								addSingleSmoke(player.position.x, player.position.y);
							}
						}
					}
					return null;
				}

				@Override
				public Void visit(TopFireball fireball) {
					if (fireball.state() == Enemy.STATE_ACTIVE && fireball.bounds.overlaps(player.bounds) && !player.isDead()) {
						if (shieldTime <= 0) {
							player.killByBurn();
						} else {
							playerShieldEffect.hit();
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
						} else {
							playerShieldEffect.hit();
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
				if (l != pauseEffects) {
					pauseEffects.remove(e);
				}
				if (l != addEffects) {
					addEffects.remove(e);
				}
				magnetEffects.remove(e);
			}
		}
	}

	private void updateCamera(float deltaTime) {
		camTarget = (floors_.getTotalBlocks()) * Wall.SIZE / 6f + WorldRenderer.FRUSTUM_HEIGHT / 3f;
		background.setY(camTarget);
		
	}
	
	private void addOnionSkinEvent() {
		final float PLAYER_ONION_SKIN_RATE = 0.05f; // Time between onion skin effects, in seconds.
		final EventQueue.Event event = new EventQueue.Event() {
			@Override
			public void invoke() {
				AddOneOnionSkin();
				if (slomoTime > PLAYER_ONION_SKIN_RATE) {
					addOnionSkinEvent();
				}
			}
		};
		worldEvents_.addEventFromNow(PLAYER_ONION_SKIN_RATE, event);
	}

	private void updatePowerups(float deltaTime) {
		if (magnetTime > 0) {
			magnetTime -= deltaTime;
			if (magnetTime <= 0) {
				for (Collectable c : collectables_) {
					if (c.isPowerup()) {
						continue;
					}
					c.setState(Collectable.STATUS_FALLING);
					c.velocity.x = c.velocity.y = 0;
					addEffects.add(CoinMagnetStartEffect.createReversed(c.position));
				}
				for (CoinMagnetGlowEffect e : coinMagnetGlows) {
					e.endAnim();
				}
				coinMagnetGlows.clear();
				
				SoundAssets.stopSound(SoundAssets.powerupMagnetLoop);
				SoundAssets.playSound(SoundAssets.powerupMagnetEnd);
				for (Effect e : magnetEffects) {
					e.destroy();
				}
				addEffects.add(EffectFactory.playerMagnetGlowDie(player.position));
			}
		}
		if (slomoTime > 0) {
			slomoTime -= deltaTime;
			if (slomoTime <= 0) {
				SoundAssets.playSound(SoundAssets.powerupTimeEnd);
				SoundAssets.resumeMusicAt(gameTime);
				Effect e = EffectFactory.powerupSlomoDisappearEffect(player.position);
				pauseEffects.add(e);
				addEffects.add(e);
				pauseEffectStateTime_ = 0;
			}
		}
		if (shieldTime > 0) {
			shieldTime -= deltaTime;
			if (shieldTime <= 0.4) {
				if (playerShieldEffect != null && playerShieldEffect.shieldState() != PlayerShieldEffect.ShieldState.DIE) {
					playerShieldEffect.die();
					SoundAssets.playSound(SoundAssets.powerupShieldEnd);
				}
			}
			if (shieldTime <= 0) {
				playerShieldEffect.destroy();
				playerShieldEffect = null;
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

	public void addDualWall(int col) {
		float wallY = topScreenY();
		Wall wall = new WallDual(col, wallY);
		walls_.add(wall);
		activeWalls_.add(wall);
	}

	public void addBurningWall(int col) {
		float wallY = topScreenY();
		final BurningWall wall = new BurningWall(col, wallY);
		walls_.add(wall);
		activeWalls_.add(wall);
		addEffects.add(new BurningWallGlow(wall));
	}

	public void addFlamethrower(int col) {
		float wallY = topScreenY();
		FlamethrowerWall wall = new FlamethrowerWall(col, wallY);
		walls_.add(wall);
		activeWalls_.add(wall);
	}
	
	public void dropCoinFromCeiling(float x, Collectable.Type type) {
		float yval = topScreenY();
		createCoin(x, yval, type);
	}
	
	public Collectable createCoin(float x, float y, Collectable.Type type) {
		Collectable coin = new Collectable(x, y, type);
		collectables_.add(coin);
		if (coin.type == Collectable.Type.COIN_5_1 || coin.type == Collectable.Type.COIN_5_2 || coin.type == Collectable.Type.COIN_5_3 || coin.type == Collectable.Type.COIN_5_4) {
			addEffects.add(new DiamondGlowEffect(coin));
		}
		if (coin.isPowerup()) {
			addEffects.add(EffectFactory.powerupBackGlow(coin.type, coin));
			addEffectsUnder.add(EffectFactory.powerupAura(coin.type, coin));
			addEffects.add(EffectFactory.powerupFlare(coin.type, coin));
		} else {
			if (magnetTime > 0) {
				addCoinMagnetGlowEffect(coin);
			}
		}
		return coin;
	}
	
	public void addSack(int col, int numCoins) {
		float yval = topScreenY();
		goldSacks_.add(new GoldSack(Utils.xOfCol(col), yval, numCoins));
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
		
		final float dustX = x * 1.1f;
		EventQueue.Event dustEvent = new Event() {
			@Override
			public void invoke() {
				addDust(dustX, y);
			}
		};
		worldEvents_.addEventFromNow(0.4f, dustEvent);
		worldEvents_.addEventFromNow(0.6f, dustEvent);
		worldEvents_.addEventFromNow(6f, dustEvent);
		worldEvents_.addEventFromNow(6.2f, dustEvent);
	}
	
	private void updateSpawner(float deltaTime) {
		spawner_.update(deltaTime);
	}
	
	private void updateGoldSacks(float deltaTime) {
		sackloop:
		for (GoldSack sack : goldSacks_) {
			sack.update(deltaTime);
			List<Rectangle> rects = new ArrayList<Rectangle>(floors_.getRects());
			for (Rectangle rect : rects) {
				if (sack.bounds.overlaps(rect)) {
					if (sack.state() != GoldSack.STATE_FALLING) {
						sack.setState(GoldSack.STATE_DONE);
						continue sackloop;
					}
					sack.setState(GoldSack.STATE_GROUND);
					sack.bounds.y = rect.y+rect.height + sack.bounds.height / 2;
					sack.bounds.getCenter(sack.position);
					SoundAssets.playSound(SoundAssets.coinSackStart);
				}
			}
			if (sack.bounds.overlaps(player.bounds)) {
				boolean shouldPumpNow = false;
				if (sack.state() == GoldSack.STATE_GROUND) {
					shouldPumpNow = true;
				}
				if (sack.hasCoinsLeft() && sack.state() == GoldSack.STATE_PUMP && deltaTime * Math.abs(player.velocity.len()) > Math.random() * 40 + 2) {
					shouldPumpNow = true;
				}
				if (shouldPumpNow) {
					sack.pump();
					float COIN_X_SPEED = 250;
					float COIN_Y_SPEED = 250;
					Type type = GoldSack.randomSackCoin();
					Collectable coin = createCoin(sack.position.x, sack.position.y + 15, type) ;
					coin.velocity.x = Utils.randomDir().x * COIN_X_SPEED;
					coin.velocity.y = COIN_Y_SPEED;
					SoundAssets.playRandomSound(SoundAssets.coinSackHit);
					if (magnetTime > 0) {
						addCoinMagnetGlowEffect(coin);
					}
				}
			}
			if (sack.state() == GoldSack.STATE_EMPTY && sack.stateTime() > GoldSack.EMPTY_ANIMATION_TIME) {
				sack.setState(GoldSack.STATE_DONE);
			}
			if (sack.shouldFlare()) {
				addEffects.add(new SackFlare(sack.position));
			}
		}
		// Remove done sacks
		for (Iterator<GoldSack> it = goldSacks_.iterator(); it.hasNext();) {
			GoldSack sack = it.next();
			if (sack.state() == GoldSack.STATE_DONE) {
				it.remove();
			}
		}
	}

	private void updateWalls(float deltaTime) {
		for (Wall wall : activeWalls_) {
			wall.update(deltaTime);
			
			if (wall.status() == Wall.STATUS_EXPLODE) {
				wall.setStatus(Wall.STATUS_GONE);
				if (wall.isDual()) {
					for (int i = 0; i < 16; i++) {
						effects.add(EffectFactory
								.wallBreakParticle(wall.position.cpy()));
					}
				} else {
					for (int i = 0; i < 8; i++) {
						effects.add(EffectFactory.wallBreakParticle(wall.position
								.cpy()));
					}
				}
//				addDust(wall.position.x, wall.position.y - Wall.SIZE / 3);
				effects.add(EffectFactory.wallExplodeEffect(wall.position.cpy()));
			}
			
			List<Rectangle> rects = new ArrayList<Rectangle>(floors_.getRects());
			for (Rectangle rect : rects) {
				if (wall.bounds.overlaps(rect)) {
					turnOffFlameThrower(wall);
					
					addDust(wall.position.x, wall.position.y - Wall.SIZE / 3);
					
					wall.bounds.y = rect.y+rect.height;
					wall.bounds.getCenter(wall.position);
					wall.setStatus(Wall.STATUS_INACTIVE);
					if (wall.isDual()) {
						floors_.addToColumn(wall.col());
						floors_.addToColumn(wall.col() + 1);
						SoundAssets.playRandomSound(SoundAssets.wallDualHit);
					} else if (wall instanceof FlamethrowerWall){
						floors_.addToColumn(wall.col());
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
				if (f.shouldAddFlame()) {
					Effect flameEffect = new FlameEffect(new Vector2(new Vector2(wall.position.x, wall.position.y + 96)));
					Effect flameGlowEffect = new FlameGlowEffect(new Vector2(new Vector2(wall.position.x, wall.position.y + Wall.SIZE/2)));
					addEffects.add(flameEffect);
					addEffects.add(flameGlowEffect);
					Flame flame = new Flame(wall.position.x, wall.position.y + 96, Arrays.asList(flameEffect, flameGlowEffect));
					enemies_.add(flame);
					f.addFlame(flame);
					for (int i=0; i < 5; i++) {
						effects.add(EffectFactory.flameBreakParticle(wall.position));
					}
				}
			}
			
			if (wall.position.y < camTarget - 9*Wall.SIZE) {
				wall.setStatus(Wall.STATUS_GONE);
			}
			
			if (wall.status() == Wall.STATUS_GONE) {
				it.remove();
			}
		}
		
	}
	
	private void addDust(float x, float y) {
		screenEffects.add(new DustEffect(x, y));
	}
	
	private void addSingleSmoke(final float x, final float y) {
		screenEffects.add(new SmokeEffect(x, y));
	}
	
	private void addSmoke(final float x, final float y) {
		for (int i=0; i < 4; i++) {
			final float time = i * 0.4f;
			worldEvents_.addEventFromNow(time, new Event() {
				@Override
				public void invoke() {
					addSingleSmoke(x, y);
				}
			});
		}
	}

	private void turnOffFlameThrower(Wall coveringWall) {
		for (Wall wall : walls_) {
			if (!(wall instanceof FlamethrowerWall)) {
				continue;
			}
			// Do not turn off self.
			if (wall == coveringWall) {
				continue;
			}
			
			final FlamethrowerWall f = (FlamethrowerWall)wall;
			
			if (f.position.y >= coveringWall.position.y - Wall.SIZE * 1.1f 
					&& f.position.y <= coveringWall.position.y
					&& f.position.x >= coveringWall.position.x - Wall.SIZE * 0.9f
					&& f.position.x <= coveringWall.position.x + Wall.SIZE * 0.9f) {

				if (f.isFlameOn()) {
					addSmoke(f.position.x, f.position.y + Wall.SIZE / 2);
				}
				f.turnOff();
			}
		}
	}

	private void updateCoins(float deltaTime) {
		boolean coinCrushed = false;
		boolean powerupCrushed = false;
		for (Collectable c : collectables_) {
			if (magnetTime > 0 && !c.isPowerup()) {
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
							|| rect.contains(c.bounds)
							|| c.state() == Collectable.STATUS_MAGNETIZED
							&& rect.contains(c.position)) {
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
				if ((c.state() != Collectable.STATUS_FALLING && c.state() != Collectable.STATUS_MAGNETIZED) || c.stateTime() > 0.2) {
					c.setState(Collectable.STATUS_TAKEN);
					handleCollectable(c);
				}
			}
			if (c.state() == Collectable.STATUS_CRUSHED) {
				if (c.isPowerup()) {
					if (!powerupCrushed) {
						SoundAssets.playSound(SoundAssets.powerupCrushed);
						powerupCrushed = true;
					}
					addEffects.add(EffectFactory.powerupCrushedEffect(c.type, c.position));
				} else {
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
		final Collectable.Type type = collectable.type; 
		switch (type) {
		case POWERUP_MAGNET:
			SoundAssets.pauseAllSounds();
			SoundAssets.setPitch(1f);
			if (magnetTime <= 0) {
				playerMagnetEffect = new PlayerMagnetEffect(player.position);
				addEffectsUnder.add(playerMagnetEffect);
				magnetEffects.add(playerMagnetEffect);

				playerMagnetGlow = new PlayerMagnetGlow(player.position);
				addEffectsUnder.add(playerMagnetGlow);
				magnetEffects.add(playerMagnetGlow);
				for (Collectable coin : collectables_) {
					if (coin.isPowerup()) {
						continue;
					}
					{
						Effect e = CoinMagnetStartEffect.create(coin.position);
						addEffects.add(e);
						pauseEffects.add(e);
					}
					addCoinMagnetGlowEffect(coin);
				}
			}
			magnetTime = TOTAL_MAGNET_TIME;
			
			pauseEffectEvents.addEventFromNow(0.0f, new EventQueue.Event() {
				@Override
				public void invoke() {
					SoundAssets.playSound(SoundAssets.powerupMagnetStart);
					Effect e = EffectFactory.powerupAppearEffect(type, player.position);
					pauseEffects.add(e);
					addEffects.add(e);
				}
			});
			pauseEffectEvents.addEventFromNow(PAUSE_EFFECT_DURATION, new EventQueue.Event() {
				@Override
				public void invoke() {
					SoundAssets.resumeAllSounds();
					SoundAssets.stopSound(SoundAssets.powerupMagnetLoop);
					SoundAssets.loopSound(SoundAssets.powerupMagnetLoop);
				}
			});
			break;
		case POWERUP_SLOMO:
			consecutiveSlomo = slomoTime > 0;
			if (!consecutiveSlomo) {
				addOnionSkinEvent();
			}
			slomoTime = TOTAL_SLOMO_TIME;
			SoundAssets.pauseAllSounds();
			SoundAssets.pauseMusic();
			pauseEffectEvents.addEventFromNow(0.0f, new EventQueue.Event() {
				@Override
				public void invoke() {
					SoundAssets.playSound(SoundAssets.powerupTimeStart);
					Effect e1 = EffectFactory.powerupAppearEffect(type, player.position);
					pauseEffects.add(e1);
					addEffects.add(e1);
				}
			});
			pauseEffectEvents.addEventFromNow(PAUSE_EFFECT_DURATION, new EventQueue.Event() {
				@Override
				public void invoke() {
					SoundAssets.resumeAllSounds();
					Effect e2 = EffectFactory.playerSlomoEffect(player.position);
					pauseEffects.add(e2);
					addEffects.add(e2);
				}
			});
			break;
		case POWERUP_SHIELD:
			SoundAssets.pauseAllSounds();
			shieldTime = TOTAL_SHIELD_TIME;
			pauseEffectEvents.addEventFromNow(0, new EventQueue.Event() {
				@Override
				public void invoke() {
					SoundAssets.playSound(SoundAssets.powerupShieldStart);
					Effect e = EffectFactory.powerupAppearEffect(type, player.position);
					pauseEffects.add(e);
					addEffects.add(e);
				}
			});
			pauseEffectEvents.addEventFromNow(PAUSE_EFFECT_DURATION, new EventQueue.Event() {
				@Override
				public void invoke() {
					SoundAssets.resumeAllSounds();
					if (playerShieldEffect == null) {
						playerShieldEffect = new PlayerShieldEffect(player.position); 
						pauseEffects.add(playerShieldEffect);
						addEffects.add(playerShieldEffect);
						
						addEffects.add(new ShieldFlare(playerShieldEffect));
					} else {
						playerShieldEffect.renew();
					}
				}
			});
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

	private void addCoinMagnetGlowEffect(Collectable coin) {
		{
			CoinMagnetGlowEffect e = new CoinMagnetGlowEffect(coin);
			addEffectsUnder.add(e);
			pauseEffects.add(e);
			coinMagnetGlows.add(e);
		}
	}

	private void addScore(int scoreToAdd) {
		score += scoreToAdd;
		
	}

	private void updatePlayer (float deltaTime) {
		if (player.state() == Player.STATE_DIE) {
			endEffects();
			if (godMode_) {
				player.revive();
				player.bounds.y = topScreenY();
				return;
			} else {
				Gdx.input.vibrate(500);
				player.deathAcknoladged();
				if (player.deathType == Player.DEATH_BY_BURN) {
					SoundAssets.playSound(SoundAssets.playerDeathBurn);
					addEffects.add(EffectFactory.playerExplodeEffect(player.position));
					addBurnParticles(player.position.x, player.position.y);
					addSingleSmoke(player.position.x, player.position.y);
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
	
	private void endEffects() {
		float epsilon = 1e-10f;
		magnetTime = Math.min(magnetTime, epsilon);
		slomoTime = Math.min(slomoTime, epsilon);
		shieldTime = Math.min(shieldTime, epsilon);
		
	}

	private void finishGame() {
		SoundAssets.stopAllSounds();
		listener_.restartGame();
	}
	
	public void pause() {
		SoundAssets.pauseMusic();
	}
	
	public void unpause() {
		SoundAssets.resumeMusicAt(gameTime);
	}

	public float magnetRatio() {
		return magnetTime / TOTAL_MAGNET_TIME;
	}
	
	public float slomoRatio() {
		return slomoTime / TOTAL_SLOMO_TIME;
	}
	
	public float shieldRatio() {
		return shieldTime / TOTAL_SHIELD_TIME;
	}
}
