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
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.SoundAssets;
import com.retrom.volcano.effects.Effect;
import com.retrom.volcano.effects.EffectFactory;
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
import com.retrom.volcano.game.objects.WallDual;
import com.retrom.volcano.game.objects.GameObject;
import com.retrom.volcano.game.objects.Wall;
import com.retrom.volcano.game.objects.WallSingle;

public class World {

	public final Player player;
	
	public final List<Wall> walls_ = new ArrayList<Wall>();
	public final List<Wall> activeWalls_ = new ArrayList<Wall>();
	
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
	
	private float magnetTime = 0f;
	private float slomoTime = 0f;

	private List<Rectangle> obstacles_;

	public float camTarget;

	public Background background = new Background();

	final public List<Effect> effects = new ArrayList<Effect>();
	final public List<Effect> addEffects = new ArrayList<Effect>();
	final public List<Effect> screenEffects = new ArrayList<Effect>();

	private final WorldListener listener_;

	// Cheats:
	private boolean godMode_ = false;

	
	public interface WorldListener {
		public void restartGame();
	}
	
	public World (WorldListener listener) {
		this.listener_ = listener;
		this.player = new Player(0, 100);
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
		});
	}

	private void updateCheats() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
			magnetTime += 1f;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			slomoTime += 2f;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
			addCoin((float) (Math.random() * 200), Collectable.Type.COIN_3_2);
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			screenEffects.add(EffectFactory.coinCrushedEffect(new Vector2(100,100)));
			addEffects.add(EffectFactory.coinCrushedEffect(new Vector2(-100,100)));
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
			godMode_ = !godMode_;
		}
	}

	public void update (float deltaTime) {
		if (slomoTime > 0) {
			if (slomoTime > 1) {
				deltaTime /= 2;
			} else {
				deltaTime /= (slomoTime+1); 
			}
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
	
	private void updateEffects(float deltaTime) {
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
				System.out.println("Removing effect");
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
		}
	}

	private float topScreenY() {
		return floors_.getTotalBlocks() / 6f * Wall.SIZE + 12*Wall.SIZE;
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
		Wall wall = new BurningWall(col, wallY);
		walls_.add(wall);
		activeWalls_.add(wall);
	}
	
	public void addCoin(float x, Collectable.Type type) {
		float yval = topScreenY();
		Collectable coin = new Collectable(x, yval, type);
		collectables_ .add(coin);
	}
	
	private void updateSpawner(float deltaTime) {
		spawner_.update(deltaTime);
		
	}

	private void updateWalls(float deltaTime) {
		for (Wall wall : activeWalls_) {
//			if (/*wall instanceof BurningWall && */wall.bounds.overlaps(player.bounds)) {
//				System.out.println("Wall touches player!");
//				player.killByBurningWall();
//			}
			wall.update(deltaTime);
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
		default:
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
				addEffects.add(EffectFactory.playerExplodeEffect(player.position));
				if (player.deathType == Player.DEATH_BY_BURN) {
					SoundAssets.playSound(SoundAssets.playerDeathBurn);
				} else if (player.deathType == Player.DEATH_BY_CRUSH) {
					SoundAssets.playSound(SoundAssets.playerDeathCrush);
				} else {
					Gdx.app.error("ERROR", "Player death type not specified.");
				}
				worldEvents_.addEventFromNow(2f, new Event() {
					@Override
					public void invoke() {
						finishGame();
						listener_.restartGame();
					}
				});
			}
		}
		
		player.setObstacles(obstacles_);
		player.setActiveWalls(activeWalls_);
		player.update(deltaTime);
	}
	
	private void finishGame() {
		SoundAssets.stopAllSounds();
	}
}
