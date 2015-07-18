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
	
	private List<Rectangle> obstacles_ = Arrays.asList(new Rectangle(-320,-20,640,20), new Rectangle(100,0,100,100));

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
		
		bounds.y += velocity.y * deltaTime;
		for (Rectangle rect : obstacles_) {
			Gdx.app.log("INFO", "checking rect y");
			if (bounds.overlaps(rect)) {
				Gdx.app.log("INFO", "overlap!!!");
				if (velocity.y < 0) {
					bounds.y = rect.y + rect.height;
					// TODO: handle grounded.
					// TODO: set state.
				} else
					bounds.y = rect.y - bounds.height;
				velocity.y = 0;
			}
		}
		
		bounds.x += velocity.x * deltaTime;
		for (Rectangle rect : obstacles_) {
			Gdx.app.log("INFO", "checking rect x");
			if (bounds.overlaps(rect)) {
				Gdx.app.log("INFO", "overlap!!!");
				if (velocity.x < 0) {
					bounds.x = rect.x + rect.height;
					// TODO: handle grounded.
					// TODO: set state.
				} else
					bounds.x = rect.x - bounds.width;
				velocity.x = 0;
			}
		}
		
		position.x = bounds.x + bounds.width / 2;
		position.y = bounds.y + bounds.height / 2;
	}
}