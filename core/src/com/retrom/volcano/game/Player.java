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
	public static final int WIDTH = 36;
	public static final int HEIGHT = 84;
	
	public static final int STATE_IDLE = 1;
	public static final int STATE_RUNNING = 2;
	public static final int STATE_JUMPING = 3;
	public static final int STATE_LANDING = 4;
	
	public static final float MAX_ACCEL = 50;
	public static final float FRICTION_RATE = 0.001797f;
	private static final float JUMP_VEL = 900;
	
	private List<Rectangle> obstacles_;

	private int state_ = STATE_IDLE;
	float stateTime;
	
	boolean grounded_ = true;
	
	static final boolean LEFT = true; 
	static final boolean RIGHT = false;
	
	boolean side;
	
	public Player (float x, float y) {
		super(x, y, WIDTH, HEIGHT);
		setState(STATE_IDLE);
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
		
		updateState(deltaTime);
	}

	private void updateState(float deltaTime) {
		if (grounded_) {
			if (Math.abs(velocity.x) > 50) {
				setState(STATE_RUNNING);
			} else if (state_ != STATE_LANDING || stateTime > 1/30f * 9) {
				setState(STATE_IDLE);
			}
		} else {
			setState(STATE_JUMPING);
		}
		stateTime += deltaTime;
	}

	private void setState(int state) {
		if (state_ == state) {
			return;
		}
		state_ = state;
		stateTime = 0;
	}
	

	public int state() {
		return state_;
	}

	private void tryMove(float deltaTime) {
		velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
		velocity.x *= Math.pow(FRICTION_RATE, deltaTime);
		
		if (velocity.x > 0) {
			side = RIGHT;
		} else if (velocity.x < 0) {
			side = LEFT;
		}
		
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
		
		if (!wasGrounded && grounded_) {
			setState(STATE_LANDING);
		}
		
		bounds.x += velocity.x * deltaTime;
		for (Rectangle rect : obstacles_) {
			if (bounds.overlaps(rect)) {
				if (bounds.x + bounds.width/ 2 > rect.x + rect.width / 2 && velocity.x < 0) {
					bounds.x = rect.x + rect.width;
				} else if (bounds.x + bounds.width/ 2 < rect.x + rect.width / 2 && velocity.x > 0) {
					bounds.x = rect.x - bounds.width;
				}
				velocity.x = 0;
			}
		}
		
		position.x = bounds.x + bounds.width / 2;
		position.y = bounds.y + bounds.height / 2;
	}
}