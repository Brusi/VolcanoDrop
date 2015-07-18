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
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

public class Bob extends DynamicGameObject {
	public static final int BOB_WIDTH = 36;
	public static final int BOB_HEIGHT = 84;
	
	public static final int STATE_NOTHING = 0;
	
	public static final float MAX_ACCEL = 50;
	public static final float FRICTION_RATE = 0.9f;
	
	private List<Rectangle> obstacles_ = Arrays.asList();

	int state;
	float stateTime;

	public Bob (float x, float y) {
		super(x, y, BOB_WIDTH, BOB_HEIGHT);
		state = STATE_NOTHING;
		stateTime = 0;
	}
	
	public void setObstacles(List<Rectangle> obstacles) {
		obstacles_ = obstacles;
	}
	
	private void processInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			velocity.x -= MAX_ACCEL;
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			velocity.x += MAX_ACCEL;
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			velocity.y = 500;
		}
	}

	public void update (float deltaTime) {
		processInput();
		tryMove(deltaTime);

		stateTime += deltaTime;
	}

	private void tryMove(float deltaTime) {
		velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
		velocity.x *= FRICTION_RATE;
		
		bounds.x += velocity.x;
		for (Rectangle rect : obstacles_) {
			if (bounds.overlaps(rect)) {
				if (velocity.y < 0) {
					bounds.y = rect.y + rect.height + BOB_HEIGHT/2;
					// TODO: handle grounded.
					// TODO: set state.
				} else
					bounds.y = rect.y - bounds.height - BOB_HEIGHT/2;
				velocity.y = 0;
			}
		}
		
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		
		bounds.x = position.x - bounds.width / 2;
		bounds.y = position.y - bounds.height / 2;

		if (position.y < 0) position.y = 0;
	}
}