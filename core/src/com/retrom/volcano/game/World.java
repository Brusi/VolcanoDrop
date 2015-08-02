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

import javafx.print.Collation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.game.objects.Collectable;
import com.retrom.volcano.game.objects.GameObject;
import com.retrom.volcano.game.objects.Wall;

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
	
	private float magnetTime = 0f;
	private float slomoTime = 0f;

	private List<Rectangle> obstacles_;

	public float camTarget;

	public Background background = new Background();
	
	public World () {
		this.player = new Player(0, 100);
		this.spawner_ = new Spawner(floors_, new Spawner.SpawnerHandler() {
			
			@Override
			public void dropWall(int col) {
				addWall(col);
			}

			@Override
			public void dropCoin(float x, Collectable.Type type) {
				addCoin(x, type);
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
			addCoin((float) (Math.random() * 200), Collectable.Type.COIN_1_2);
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
		
		obstacles_ = new ArrayList<Rectangle>();
		obstacles_.add(leftWall_);
		obstacles_.add(rightWall_);
		for (Wall wall : activeWalls_) {
			obstacles_.add(wall.bounds);
		}
		obstacles_.addAll(floors_.getRects());
		
		updateWalls(deltaTime);
		updateCoins(deltaTime);
		updatePlayer(deltaTime);
		updateSpawner(deltaTime);
		updatePowerups(deltaTime);
		updateCamera(deltaTime);
		
		leftWall_.y = player.bounds.y - leftWall_.height/2;
		rightWall_.y = player.bounds.y - rightWall_.height/2;
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
			addCoin(50, Collectable.Type.COIN_3_1);
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
			}
		}
		if (slomoTime > 0) {
			slomoTime -= deltaTime;
		}
	}

	public void addWall(int col) {
		float wallY = floors_.getTotalBlocks() / 6f * Wall.SIZE + 10*Wall.SIZE;
		Wall wall = new Wall(col, wallY);
		walls_.add(wall);
		activeWalls_.add(wall);
	}
	
	public void addCoin(float x, Collectable.Type type) {
		float yval = floors_.getTotalBlocks() / 6f * Wall.SIZE + 10*Wall.SIZE;
		Collectable coin = new Collectable(x, yval, type);
		collectables_ .add(coin);
	}
	
	private void updateSpawner(float deltaTime) {
		spawner_.update(deltaTime);
		
	}

	private void updateWalls(float deltaTime) {
		for (Wall wall : activeWalls_) {
			wall.update(deltaTime);
			
			List<Rectangle> rects = new ArrayList<Rectangle>(floors_.getRects());
			for (Rectangle rect : rects) {
				if (wall.bounds.overlaps(rect)) {
					wall.bounds.y = rect.y+rect.height;
					wall.bounds.getCenter(wall.position);
					wall.status = Wall.STATUS_INACTIVE;
					floors_.addToColumn(wall.col());
				}
			}
		}
		
		// Remove inactive walls
		for (Iterator<Wall> it = activeWalls_.iterator(); it.hasNext();) {
			Wall wall = it.next();
			if (wall.status == Wall.STATUS_INACTIVE || wall.status == Wall.STATUS_GONE) {
				it.remove();
			}
		}
		
		for (Iterator<Wall> it = walls_.iterator(); it.hasNext();) {
			Wall wall = it.next();
			
			if (wall.position.y < camTarget - 8*Wall.SIZE) {
				wall.status = Wall.STATUS_GONE;
				System.out.println("wall gone!");
			}
			
			if (wall.status == Wall.STATUS_GONE) {
				it.remove();
			}
		}
		
	}
	
	private boolean isOutOfBounds(GameObject obj) {
		if (Math.abs(obj.position.x) > World.WIDTH/2) {
			return true;
		}
		if (obj.position.y < floors_.bottomLine()) {
			return true;
		}
		return false;
	}
	
	private void updateCoins(float deltaTime) {
		for (Collectable c : collectables_) {
			if (isOutOfBounds(c)) {
				c.setState(Collectable.STATUS_CRUSHED);
			}
			
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
			
			if (c.bounds.overlaps(player.bounds)) {
				c.setState(Collectable.STATUS_TAKEN);
				handleCollectable(c);
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
		case COIN_3_1:
			addScore(1);
			break;
		case COIN_5_4:
			addScore(3);
			break;
		case POWERUP_MAGNET:
			magnetTime = 5f;
			break;
		}
	}

	private void addScore(int scoreToAdd) {
		score += scoreToAdd;
//		System.out.println("Score: " + score);
		
	}

	private void updatePlayer (float deltaTime) {
		player.setObstacles(obstacles_);
		player.update(deltaTime);
	}
}
