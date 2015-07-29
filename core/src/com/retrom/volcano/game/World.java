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
import com.retrom.volcano.game.objects.Coin;
import com.retrom.volcano.game.objects.Wall;

public class World {

	public final Player player;
	
	public final List<Wall> walls_ = new ArrayList<Wall>();
	public final List<Wall> activeWalls_ = new ArrayList<Wall>();
	
	public final List<Coin> coins_ = new ArrayList<Coin>();
	
	public float heightSoFar;
	public int score;
	public int state;
	
	public static final Vector2 gravity = new Vector2(0, -2200);
	public static final float WIDTH = 640f;
	
	// Permanent obstacles.
	Rectangle leftWall_ = new Rectangle(-World.WIDTH / 2, 0, Wall.SIZE, 500);
	Rectangle rightWall_ = new Rectangle(World.WIDTH / 2 - Wall.SIZE, 0, Wall.SIZE, 500);;
	
	ActiveFloors floors_ = new ActiveFloors();

	private final Spawner spawner_;

	
	public World () {
		this.player = new Player(0, 100);
		this.spawner_ = new Spawner(floors_, new Spawner.SpawnerHandler() {
			
			@Override
			public void dropWall(int col) {
				addWall(col);
			}

			@Override
			public void dropCoin(float x) {
				addCoin(x);
			}
		});
	}

	public void update (float deltaTime) {
		updateWalls(deltaTime);
		updateCoins(deltaTime);
		updatePlayer(deltaTime);
		updateSpawner(deltaTime);
		
		leftWall_.y = player.bounds.y;
		rightWall_.y = player.bounds.y;
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
			addCoin(50);
		}
	}
	
	public void addWall(int col) {
		float wallY = floors_.getTotalBlocks() / 6f * Wall.SIZE + 10*Wall.SIZE;
		Wall wall = new Wall(col, wallY);
		walls_.add(wall);
		activeWalls_.add(wall);
	}
	
	public void addCoin(float x) {
		float yval = floors_.getTotalBlocks() / 6f * Wall.SIZE + 10*Wall.SIZE;
		Coin coin = new Coin(x, yval);
		coins_ .add(coin);
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
			if (wall.status == Wall.STATUS_INACTIVE) {
				it.remove();
			}
		}
		
	}
	
	private void updateCoins(float deltaTime) {
		for (Coin coin : coins_) {
			coin.update(deltaTime);
			for (Rectangle rect : floors_.getRects()) {
				if (coin.bounds.overlaps(rect)) {
					if (coin.status == Coin.STATUS_FALLING) {
						coin.status = Coin.STATUS_IDLE;
						coin.bounds.y = rect.y + rect.height;
						coin.bounds.getCenter(coin.position);
						coin.velocity.y = 0;
					} else if (coin.status == Coin.STATUS_IDLE) {
						coin.status = Coin.STATUS_CRUSHED;
					}
				}
			}
			
			if (coin.bounds.overlaps(player.bounds)) {
				coin.status = Coin.STATUS_TAKEN;
			}
		}
		// Remove crushed coins.
		for (Iterator<Coin> it = coins_.iterator(); it.hasNext();) {
			Coin coin = it.next();
			if (coin.status == Coin.STATUS_CRUSHED || coin.status == Coin.STATUS_TAKEN) {
				it.remove();
			}
		}
	}

	private void updatePlayer (float deltaTime) {
		List<Rectangle> obstacles = new ArrayList<Rectangle>();
		obstacles.add(leftWall_);
		obstacles.add(rightWall_);
		for (Wall wall : activeWalls_) {
			obstacles.add(wall.bounds);
		}
		obstacles.addAll(floors_.getRects());
		player.setObstacles(obstacles);
		player.update(deltaTime);
	}
}
