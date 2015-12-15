package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;

public class PlayerOnionSkinEffect extends Effect {
	
	static private final float DURATION = 0.3f; 
	static private final float MAX_TINT = 0.7f;

	public final int playerState;
	public final float playerStateTime;
	public final boolean playerSide;

	public PlayerOnionSkinEffect(Vector2 position, int playerState,
			float playerStateTime, boolean side) {
		super(DURATION, position);
		this.playerState = playerState;
		this.playerStateTime = playerStateTime;
		this.playerSide = side;
	}

	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
	
	@Override
	public float getTint() {
		return (1 - stateTime() / duration()) * MAX_TINT;
	}

}
