package com.retrom.volcano.game.objects;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.assets.SoundAssets;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.game.World;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.menus.StaticGraphicObject;
import com.retrom.volcano.utils.EventQueue;

public class Boss extends DynamicGameObject {

    public interface Listener {
		void thompHit();
        void finalThompHit();
	}
	
	static private final float WIDTH = Wall.SIZE * 2 - 1;
	static private final float HEIGHT = 214;
	
	static private final float POSITION_OVER_BASE_LINE = 300;

	static private final float MAX_HORIZONTAL_SPEED = 500;

	private boolean followingPlayer = false;
	
	enum State {
		HIDDEN,
		APPROACH,
		FLOAT,
		PREPARE_THOMP,
		THOMP,
		STOP,
		RISE,
		BACKGROUND,
	}
	
	private final StaticGraphicObject body = new StaticGraphicObject(Assets.bossRegular, 0, 0); 
	private final StaticGraphicObject body_dark = new StaticGraphicObject(Assets.bossDark, 0, 0);
	
	private final Vector2 target_pos = new Vector2();
	
	private final EventQueue queue = new EventQueue();
	
	State state_ = State.HIDDEN;
	float stateTime_ = 0;
	
	private float baseLine_;
	private List<Rectangle> obstacles_;
	private Vector2 playerPosition_;
	private final Listener listener;

	private boolean last_thomp_ = false;
	private float scale_ = 1;

	public Boss(Listener listener) {
		super(0, 0, WIDTH, HEIGHT);
		this.listener = listener;
	}
	
	public void approach() {
		setState(State.APPROACH);
		System.out.println("approaching....");
	}
	
	public void updateFloorBaseLine(float baseLine) {
		baseLine_ = baseLine;
	}
	
	public void setObstacles(List<Rectangle> obstacles) {
		obstacles_ = obstacles;
	}
	
	public void update(float deltaTime) {
		if (state_ == State.HIDDEN) {
			return;
		}
		queue.update(deltaTime);
		stateTime_ += deltaTime;
		updateTargetPos(deltaTime);
		updatePos(deltaTime);
	}

	private void updatePos(float deltaTime) {
		if (state_ == State.FLOAT ||
			state_ == State.BACKGROUND ||
		    state_ == State.APPROACH ||
		    state_ == State.RISE ||
		    state_ == State.PREPARE_THOMP) {

            position.y += (target_pos.y - position.y) * deltaTime * 1.5f;
            updatePosX(deltaTime);
			updateBounds();
			return;
		}
		if (state_ == State.THOMP) {
			velocity.y += World.gravity.y * deltaTime; 
			position.y += velocity.y * deltaTime;
            updatePosX(deltaTime);
			updateBounds();
			
			for (Rectangle rect : obstacles_) {
				if (bounds.overlaps(rect)) {
					if (bounds.y + bounds.height/ 2 > rect.y + rect.height / 2) {
						position.y = rect.y + rect.height + bounds.height / 2;
						updateBounds();

                        stopToFollowPlayer();
						listener.thompHit();
						setState(State.STOP);
                        if (!last_thomp_) {
                            queue.addEventFromNow(1, setStateEvent(State.RISE));
                        } else {
                            listener.finalThompHit();
                        }
						return;
					}
				}
			}
		}
	}

    private void updatePosX(float deltaTime) {
        float dx = (target_pos.x - position.x) * deltaTime * 5f;
        position.x += Utils.clamp(dx,
                                  -MAX_HORIZONTAL_SPEED * deltaTime,
                                  +MAX_HORIZONTAL_SPEED * deltaTime);
    }

    private void startToFollowPlayer() {
		followingPlayer = true;
	}
	private void stopToFollowPlayer() {
		followingPlayer = false;
	}
	
	private void updateBounds() {
		bounds.x = position.x - bounds.width / 2;
		bounds.y = position.y - bounds.height / 2;
	}

	private void updateTargetPos(float deltaTime) {
		if (followingPlayer) {
			target_pos.x = playerPosition_.x;
			target_pos.x = clampToGameArea(target_pos.x);
		}

		float float_y_pos = calc_float_y_pos();
		if (state_ == State.FLOAT || state_ == State.BACKGROUND) {
			target_pos.y = float_y_pos;
			return;
		}
		if (state_ == State.APPROACH) {
			float approach_speed = (approachStartPos() - float_y_pos) / 8;
			
			target_pos.y -= approach_speed * deltaTime;
			if (target_pos.y <= float_y_pos) {
				setState(State.FLOAT);
				return;
			}
		}
		
		if (state_ == State.RISE) {
			target_pos.y += 500 * deltaTime;
			if (target_pos.y >= float_y_pos) {
				setState(State.FLOAT);
				return;
			}
		}
	}

	private float clampToGameArea(float x) {
		float side = Wall.SIZE * 3  - bounds.width / 2; 
		return Utils.clamp(x, -side, side);
	}

	private float calc_float_y_pos() {
		return baseLine_ + POSITION_OVER_BASE_LINE;
	}
	
	public void renderBg(SpriteBatch batch) {
		if (this.state_ == State.BACKGROUND) {
			body_dark.position_.set(position);
			body_dark.setAlpha(1);
			body_dark.render(batch);
		}
	}

	public void render(SpriteBatch batch) {
		if (this.state_ == State.HIDDEN) {
			return;
		}
		body.position_.set(position);
		if (this.state_ == State.BACKGROUND) {
			body.setAlpha(Math.max(0, 1-stateTime_));
			body.render(batch);
		} else {
			body.setAlpha(1);
			body.render(batch);
		} 
	}
	
	private void setState(State state) {
		if (state == state_) {
			return;
		}
		
		stateTime_ = 0;
		state_ = state;
		
		if (state_ == State.APPROACH) {
			position.y = target_pos.y = approachStartPos();
			updateBounds();
			return;
		}
		if (state == State.THOMP) {
			velocity.set(0, 0);
            target_pos.x = position.x;
			return;
		}
		if (state == State.FLOAT) {
			target_pos.y = calc_float_y_pos();
			return;
		}
		if (state == State.RISE) {
			target_pos.set(position);
			velocity.set(0, 0);
			return;
		}
	}

	private float approachStartPos() {
		return baseLine_ +
//					World.GAME_CAM_OFFSET;// +
				WorldRenderer.FRUSTUM_HEIGHT / 2 +
				bounds.height / 2;
	}

	public boolean isActive() {
		return this.state_ != State.HIDDEN;
	}
	public boolean isAtFront() {
		return isActive() && this.state_ != State.BACKGROUND;
	}

	public void updatePlayerPosition(Vector2 playerPosition) {
		playerPosition_ = playerPosition;
	}

	public void approachSequence() {
		SoundAssets.playRandomSound(SoundAssets.bossLaugh);
		approach();

		thompSequence(10);

		queue.addEventFromNow(16, new EventQueue.Event() {
			@Override
			public void invoke() {
				startToFollowPlayer();
			}
		});


		queue.addEventFromNow(16, new EventQueue.Event() {
			@Override
			public void invoke() {
				startToFollowPlayer();
			}
		});

		thompSequence(18);
		queue.addEventFromNow(18, new EventQueue.Event() {
			@Override
			public void invoke() {
				stopToFollowPlayer();
			}
		});

		queue.addEventFromNow(23f, new EventQueue.Event() {
			@Override
			public void invoke() {
				setState(State.FLOAT);
				target_pos.x = 0;
			}
		});
		queue.addEventFromNow(24f, new EventQueue.Event() {
			@Override
			public void invoke() {
				setState(State.BACKGROUND);
				target_pos.x = 0;
			}
		});
	}

    public void finishSequence() {
        SoundAssets.playRandomSound(SoundAssets.bossLaugh);

        setState(State.FLOAT);
        target_pos.x = 0;
        stopToFollowPlayer();

        thompSequence(2);
        queue.addEventFromNow(2, new EventQueue.Event() {
            @Override
            public void invoke() {
                position.x = 0;
            }
        });

        last_thomp_ = true;
    }

    public void followPlayerSequence() {
        SoundAssets.playRandomSound(SoundAssets.bossLaugh);

        setState(State.FLOAT);
        queue.addEventFromNow(1, new EventQueue.Event() {
            @Override
            public void invoke() {
                startToFollowPlayer();
            }
        });

        thompSequence(2);

        queue.addEventFromNow(6, setStateEvent(State.BACKGROUND));
    }

	private void thompSequence(float t) {
		queue.addEventFromNow(t, new EventQueue.Event() {
			@Override
			public void invoke() {
				setState(State.PREPARE_THOMP);

				SoundAssets.playRandomSound(SoundAssets.bossFall);
				target_pos.y = calc_float_y_pos() + -50;
				target_pos.x = position.x;
			}
		});
		queue.addEventFromNow(t + 0.5f, new EventQueue.Event() {
			@Override
			public void invoke() {
				target_pos.y = calc_float_y_pos() + 50;
				target_pos.x = position.x;
			}
		});
		queue.addEventFromNow(t + 1f, new EventQueue.Event() {
			@Override
			public void invoke() {
				setState(State.THOMP);
			}
		});
	}
	
	private EventQueue.Event setStateEvent(final State state) {
		return new EventQueue.Event() {
			@Override
			public void invoke() {
				setState(state);
			}
		};
	}
}
