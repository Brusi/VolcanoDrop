package com.retrom.volcano.game.objects;

public abstract class Enemy extends DynamicGameObject {
	
	public static final int STATE_ACTIVE = 1;
	public static final int STATE_DONE = 2;
	
	protected int state_ = STATE_ACTIVE;
	private float stateTime_;

	public Enemy(float x, float y, float width, float height) {
		super(x, y, width, height);
	}
	
	public void update(float deltaTime) {
		stateTime_ += deltaTime;
		bounds.y += velocity.y * deltaTime;
		bounds.getCenter(position);
		childSpecificUpdating();
	}

	protected void childSpecificUpdating() {
		// May be overriden by children.
	}

	public int state() {
		return state_;
	}

	public float stateTime() {
		return stateTime_;
	}
	

	protected void setState(int state) {
		if (state_ == state) {
			return;
		}
		state_ = state;
		stateTime_ = 0;
	}
	
	public abstract <T> T accept(Visitor<T> v);
	
	public static interface Visitor<T> {
		T visit(Flame flame);
		T visit(TopFireball topFireball);
	}
}
