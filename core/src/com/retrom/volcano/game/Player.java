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
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.game.Player.EventHandler.SIDE;
import com.retrom.volcano.game.objects.BurningWall;
import com.retrom.volcano.game.objects.DynamicGameObject;
import com.retrom.volcano.game.objects.Wall;

public class Player extends DynamicGameObject {
	
	interface HitRectHandler {
		void handle(Rectangle rect);
	}
	interface EventHandler {
		enum SIDE {
			LEFT, RIGHT, UP, DOUBLE;
		}
		// -1 for jumping from wall, facing left.
		// 0 for jumping up.
		// 1 for jumping from wall, facing right.
		void handleJump(SIDE side);
		
		void handleDash();
		
		void handleBurnWall(Wall wall);
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
	public static final float WALLGLIDE_FRICTION_RATE = 0.001f;
	public static final float STOP_FRICTION_RATE = 0.06f;
	private static final float JUMP_VEL = 900;
	private static final float WALL_JUMP_Y_VEL = 800;
	private static final float WALL_JUMP_X_VEL = 800f;
	private static final float JUMP_PRESS_ACCELL = 650; ///
	
	private static final float WALLJUMP_ALLOWED_DELAY = 0.2f;
	private static final float DASH_CLICK_MAX_DELAY = 0.2f;
	private static final float DASH_VEL = 3000f;
	private static final float MAX_NON_DASH_VEL = 1000f;
	protected static final float DASH_DURATION = 0.05f;
	
	private List<Rectangle> obstacles_;
	private List<Wall> activeWalls_;
	
	private int state_ = STATE_IDLE;
	float stateTime;
	private float timeSinceLanding;
	private float timeSinceWallTouch;
	
	// Dash
	private float timeSinceLastPressLeft = 0;
	private float timeSinceLastPressRight = 0;
	
	public int deathType;
	
	boolean grounded_ = true;
	boolean airJump_ = false;
	public int wallGlide = 0;
	
	static final boolean LEFT = true; 
	static final boolean RIGHT = false;
	
	boolean side;
	private boolean is_shield_active_;
	
	private final HitRectHandler hitRectHandler_;
	private final EventHandler handler_;
	
	// X position after push; only if not pushed by two stones at once.
	private Float newBoundsX = null;
	private boolean active_ = false;
	
	public Player (float x, float y, HitRectHandler rectHandler, EventHandler jumpHandler) {
		super(x, y, WIDTH, HEIGHT);
		hitRectHandler_ = rectHandler;
		handler_ = jumpHandler;
		setState(STATE_IDLE);
		stateTime = 0;
	}
	
	public void setObstacles(List<Rectangle> obstacles) {
		obstacles_ = obstacles;
	}
	
	private void processInput(float deltaTime) {
		if (!active_) {
			return;
		}
		AbstractControl control = ControlManager.getControl();
		
		// PC controls
		if (control.isAnalog()) {
			velocity.x = control.getAnalogXVel();
		} else {
			velocity.x += control.getDigitalXDir() * X_ANALOG_ACCEL * deltaTime;
		}
		
		// Dash logic.
		checkDash(control);
		
//		// Phone controls
//		float accel = Gdx.input.getAccelerometerX();
//		if (Math.abs(accel) > 0.5) {
//			velocity.x = -200 * accel;
//		}
		
		if ((grounded_ || canAirJump()) && control.isJumpPressed()) {
			if (!grounded_) {
				airJump_ = true;
			}
			handler_.handleJump(grounded_ ? SIDE.UP : SIDE.DOUBLE);
			grounded_ = false;
			resetState(STATE_JUMPING);
			velocity.y = JUMP_VEL;
			if (timeSinceLanding > 0.1f && !airJump_) {
				SoundAssets.playRandomSound(SoundAssets.playerJump);
			} else {
				SoundAssets.playRandomSound(SoundAssets.playerJumpIntense);
			}
		} else if (canWallJump() && control.isJumpPressed()) {
			velocity.y = WALL_JUMP_Y_VEL;
			handler_.handleJump(wallGlide == 1 ? SIDE.LEFT : SIDE.RIGHT);
			resetState(STATE_JUMPING);
			velocity.x = -WALL_JUMP_X_VEL * wallGlide;
			wallGlide = 0;
			SoundAssets.playRandomSound(SoundAssets.playerJumpIntense);
		}
	}

	private void checkDash(AbstractControl control) {
		if (!ShopData.charge.isOwn()) return;
		if (control.isLeftJustPressed()) {
			if (timeSinceLastPressLeft < DASH_CLICK_MAX_DELAY) {
				handler_.handleDash();
				this.velocity.x = -DASH_VEL;
			}
			timeSinceLastPressLeft = 0;
		}
		if (control.isRightJustPressed()) {
			if (timeSinceLastPressRight < DASH_CLICK_MAX_DELAY) {
				handler_.handleDash();
				this.velocity.x = DASH_VEL;
			}
			timeSinceLastPressRight = 0;
		}
	}

	private boolean canWallJump() {
		return !grounded_ && timeSinceWallTouch < WALLJUMP_ALLOWED_DELAY
				&& wallGlide != 0;
	}

	private boolean canAirJump() {
		return ShopData.airStep.isOwn() && !airJump_ && !canWallJump();
	}
	
	public void update(float deltaTime) {
		stateTime += deltaTime;
		timeSinceLanding += deltaTime;
		timeSinceWallTouch += deltaTime;
		timeSinceLastPressLeft += deltaTime;
		timeSinceLastPressRight += deltaTime;
		
		if (state_ == STATE_DIE || state_ == STATE_DEAD) {
			return;
		}
		if (state_ == STATE_RUNNING) {
			stateTime -= deltaTime / 2;
			float addition = deltaTime * Math.abs(velocity.x) * 4 / MAX_NON_DASH_VEL;
			stateTime += addition;
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
		resetState(state);
	}
	
	private void resetState(int state) {
		state_ = state;
		stateTime = 0;
	}
	

	public int state() {
		return state_;
	}

	private void tryMove(float deltaTime) {
		AbstractControl control = ControlManager.getControl();
		velocity.add(0, World.gravity.y * deltaTime);
		// TODO: Activate only if shop item activated.
		if (ShopData.frogger.isOwn() && !grounded_ && control.isJumpPressedContinuously()) {
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
					airJump_ = false;
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
				if (!grounded_ && ShopData.wallFoot.isOwn()) {
					// Glides on wall
					wallGlide = Math.round(Math.signum(velocity.x));
//					airJump_ = false;
					timeSinceWallTouch = 0;
					if (velocity.y < 0) {
						velocity.y *= Math.pow(WALLGLIDE_FRICTION_RATE, deltaTime);
					}
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
			if (!is_shield_active_ && wall instanceof BurningWall) {
				if (wall.bounds.overlaps(this.bounds)) {
					killByBurn();
					handler_.handleBurnWall(wall);
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

	public int getGliding() {
		if (timeSinceWallTouch != 0 || velocity.y >= 0) return 0;
		return wallGlide;
	}

	public void endDash() {
		velocity.limit(MAX_NON_DASH_VEL);
	}

	public void activate() {
		active_ = true;
	}
}