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

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.retrom.volcano.game.objects.DynamicGameObject;

public class Player extends DynamicGameObject {
	public static final int BOB_WIDTH = 36;
	public static final int BOB_HEIGHT = 84;
	
	public static final int STATE_NOTHING = 0;
	
	public static final float MAX_ACCEL = 50;
	public static final float FRICTION_RATE = 0.001797f;
	private static final float JUMP_VEL = 900;
	
	private List<Rectangle> obstacles_;

	int state;
	float stateTime;
	
	boolean grounded_ = true;
	
	public Player (float x, float y) {
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
		
		if (grounded_ && Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			grounded_ = false;
			velocity.y = JUMP_VEL;
		}
	}
	
	public void update (float deltaTime) {
		processInput();
		tryMove(deltaTime);

		stateTime += deltaTime;
	}

	private void tryMove(float deltaTime) {
		velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
		velocity.x *= Math.pow(FRICTION_RATE, deltaTime);
		
		bounds.y += velocity.y * deltaTime;
		boolean wasGrounded = grounded_;
		grounded_ = false;
		for (Rectangle rect : obstacles_) {
			if (bounds.overlaps(rect)) {
				if (bounds.y + bounds.height/ 2 > rect.y + rect.height / 2) {
					bounds.y = rect.y + rect.height;
					grounded_ = true;
				} else {
					if (wasGrounded) {
						bounds.y+=1000;
					} else { 
						bounds.y = rect.y - bounds.height;
					}
				}
				velocity.y = 0;
			}
		}
		
		bounds.x += velocity.x * deltaTime;
		for (Rectangle rect : obstacles_) {
			if (bounds.overlaps(rect)) {
				if (bounds.x + bounds.width/ 2 > rect.x + rect.width / 2) {
					bounds.x = rect.x + rect.width;
				} else
					bounds.x = rect.x - bounds.width;
				velocity.x = 0;
			}
		}
		
		position.x = bounds.x + bounds.width / 2;
		position.y = bounds.y + bounds.height / 2;
	}
}