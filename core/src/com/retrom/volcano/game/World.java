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

import com.badlogic.gdx.math.Vector2;

public class World {

	public final Bob player;

	public float heightSoFar;
	public int score;
	public int state;
	
	public static final Vector2 gravity = new Vector2(0, -1000);
	
	public World () {
		this.player = new Bob(0, 0);
		generateLevel();
	}

	private void generateLevel () {
	}

	public void update (float deltaTime) {
		updatePlayer(deltaTime);
		checkGameOver();
	}

	private void updatePlayer (float deltaTime) {
		player.update(deltaTime);
	}

	private void checkGameOver () {
	}
}
