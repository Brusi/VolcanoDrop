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

import sun.net.www.content.text.plain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.retrom.volcano.assets.SoundAssets;
import com.retrom.volcano.control.AbstractControl;
import com.retrom.volcano.control.ControlManager;
import com.retrom.volcano.game.objects.BurningWall;
import com.retrom.volcano.game.objects.DynamicGameObject;
import com.retrom.volcano.game.objects.Wall;

public class Player extends DynamicGameObject {
	
	interface HitRectHandler {
		void handle(Rectangle rect);
	}
	
	public static final int WIDTH = 36;
	public static final int HEIGHT = 84;
	
	public static final int STATE_IDLE = 1;
	public static final int STATE_RUNNING = 2;
	public static final int STATE_JUMPING = 3;
	public static final int STATE_LANDING = 4;
	public static final int STATE_DIE = 5;
	public static final int STATE_DEAD = 6;
	
	public static final int DEATH_BY_CRUSH = 1;
	public static final int DEATH_BY_BURN = 2;
	
	protected static final float X_ANALOG_ACCEL = 3400f;
	public static final float FRICTION_RATE = 0.03f;
	public static final float STOP_FRICTION_RATE = 0.06f;
	private static final float JUMP_VEL = 900;
	private static final float JUMP_PRESS_ACCELL = /*650*/ 0;
	
	private List<Rectangle> obstacles_;
	private List<Wall> activeWalls_;

	private int state_ = STATE_IDLE;
	float stateTime;
	private float timeSinceLanding;
	
	public int deathType;
	
	boolean grounded_ = true;
	private boolean jumping_ = false;
	
	static final boolean LEFT = true; 
	static final boolean RIGHT = false;
	
	boolean side;
	private boolean is_shield_active_;
	
	private final HitRectHandler hitRectHandler_;
	
	// X position after push; only if not pushed by two stones at once.
	private Float newBoundsX = null;
	
	public Player (float x, float y, HitRectHandler handler) {
		super(x, y, WIDTH, HEIGHT);
		hitRectHandler_ = handler;
		setState(STATE_IDLE);
		stateTime = 0;
	}
	
	public void setObstacles(List<Rectangle> obstacles) {
		obstacles_ = obstacles;
	}
	
	private void processInput(float deltaTime) {
		AbstractControl control = ControlManager.getControl();
		
		// PC controls
		if (control.isAnalog()) {
			velocity.x = control.getAnalogXVel();
		} else {
			velocity.x += control.getDigitalXDir() * X_ANALOG_ACCEL * deltaTime;
		}
		
//		// Phone controls
//		float accel = Gdx.input.getAccelerometerX();
//		if (Math.abs(accel) > 0.5) {
//			velocity.x = -200 * accel;
//		}
		
		if (/*grounded_ && */control.isJumpPressed()) {
			if (!grounded_)
				jumping_ = true;
			grounded_ = false;
			velocity.y = JUMP_VEL;
			if (timeSinceLanding > 0.1f) {
				SoundAssets.playRandomSound(SoundAssets.playerJump);
			} else {
				SoundAssets.playRandomSound(SoundAssets.playerJumpIntense);
			}
		}
	}
	
	public void update (float deltaTime) {
		stateTime += deltaTime;
		timeSinceLanding += deltaTime;
		if (state_ == STATE_DIE || state_ == STATE_DEAD) {
			return;
		}
		processInput(deltaTime);
		tryMove(deltaTime);
		
		updateState(deltaTime);
	}

	private void updateState(float deltaTime) {
		if (state_ == STATE_DIE || state_ == STATE_DEAD) {
			return;
		}

		if (grounded_) {
			if (state_ != STATE_LANDING && Math.abs(velocity.x) > 50) {
				setState(STATE_RUNNING);
			} else if (state_ != STATE_LANDING || stateTime > 1/30f * 9) {
				setState(STATE_IDLE);
			}
		} else {
			setState(STATE_JUMPING);
		}
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
		AbstractControl control = ControlManager.getControl();
		velocity.add(0, World.gravity.y * deltaTime);
		if (!grounded_ && control.isJumpPressedContinuously()) {
			velocity.add(0, JUMP_PRESS_ACCELL * deltaTime);
		}
		if (!control.isAnalog()) {
			float friction_rate = FRICTION_RATE;
			if (velocity.x > 0 && control.getDigitalXDir() <= 0
		     || velocity.x < 0 && control.getDigitalXDir() >= 0) {
				friction_rate /= 100;
			}
			velocity.x *= Math.pow(friction_rate, deltaTime);
		}
		
		if (velocity.x > 0) {
			side = RIGHT;
		} else if (velocity.x < 0) {
			side = LEFT;
		}
		
		// The rectangle the player hit from the top, in case it is needed to be crushed.
		
		boolean wasGrounded = grounded_;
		Rectangle topRect = null;
		grounded_ = false;
		
		bounds.y += velocity.y * deltaTime;
		checkBurningWalls();
		for (Rectangle rect : obstacles_) {
			if (bounds.overlaps(rect)) {
				if (bounds.y + bounds.height/ 2 > rect.y + rect.height / 2) {
					bounds.y = rect.y + rect.height;
					grounded_ = true;
					if (topRect != null) {
						if (checkCrushDeath(obstacles_, topRect)) {
							return;
						}
					}
				} else {
					if (wasGrounded) {
						topRect = rect;
						if (checkCrushDeath(obstacles_, topRect)) {
							return;
						}
					} else { 
						topRect = rect;
						bounds.y = rect.y - bounds.height;
					}
				}
				velocity.y = 0;
			}
		}
		
		if (!wasGrounded && grounded_) {
			setState(STATE_LANDING);
			timeSinceLanding = 0;
		}
		
		bounds.x += velocity.x * deltaTime;
		checkBurningWalls();
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
		if (newBoundsX != null && state_ != STATE_DEAD) {
			bounds.x = newBoundsX;
			newBoundsX = null;
		}
		position.x = bounds.x + bounds.width / 2;
		position.y = bounds.y + bounds.height / 2;
	}

	private boolean checkCrushDeath(List<Rectangle> obstacles, Rectangle topRect) {
		// 'Save' the player if is close to the edge of the crushing block.
		final float ALLOWED_SPAN = bounds.width / 2;
		if (position.x < topRect.x + ALLOWED_SPAN) {
			for (Rectangle rect : obstacles) {
				if (rect.y == topRect.y && rect.x == topRect.x - Wall.SIZE) {
					hitRectHandler_.handle(topRect);
					System.out.println("2 WALLS");
					return true;
				}
			}
			newBoundsX = topRect.x - bounds.width;
			return false;
		}
		if (position.x > topRect.x + topRect.width - ALLOWED_SPAN) {
			for (Rectangle rect : obstacles) {
				if (rect.y == topRect.y && rect.x == topRect.x + Wall.SIZE) {
					hitRectHandler_.handle(topRect);
					System.out.println("2 WALLS");
					return true;
				}
			}
			newBoundsX = topRect.x + topRect.width;
			return false;
		}
		
		hitRectHandler_.handle(topRect);
		return true;
	}

	private void checkBurningWalls() {
		for (Wall wall : activeWalls_) {
			if (wall.bounds.overlaps(this.bounds)) {
				if (wall instanceof BurningWall && !is_shield_active_) {
					killByBurn();
					return;
				}
			} 
		}
	}

	public void killByBurn() {
		setState(STATE_DIE);
		deathType = DEATH_BY_BURN;
	}

	public void killByCrush() {
		setState(STATE_DIE);
		deathType = DEATH_BY_CRUSH;
	}

	public void deathAcknoladged() {
		setState(STATE_DEAD);
	}

	public boolean isAlive() {
		return state_ != STATE_DEAD && state_ != STATE_DIE;  
	}

	public void revive() {
		setState(STATE_IDLE);
		this.bounds.y += 1000f;
	}

	public void setActiveWalls(List<Wall> activeWalls_) {
		this.activeWalls_ = activeWalls_;
		
	}

	public boolean isDead() {
		return state() == STATE_DEAD || state() == STATE_DIE; 
	}

	public void setShieldStatus(boolean is_shield_active) {
		is_shield_active_ = is_shield_active;
	}

	public void landAnimation() {
		setState(STATE_LANDING);
	}

	public boolean getJumping() {
		boolean $ = jumping_;
		jumping_ = false;
		return $;
	}
}