package com.retrom.volcano.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;

public class Boss extends DynamicGameObject {
	
	static private final float WIDTH = Wall.SIZE * 2;
	static private final float HEIGHT = 214;
	
	enum State {
		HIDDEN,
		FOLLOWS_PLAYER,
		THOMPING
	}
	
	State state = State.HIDDEN;

	public Boss(float x, float y) {
		super(x, y, WIDTH, HEIGHT);
	}
	
	public void followPlayer() {
		this.state = State.FOLLOWS_PLAYER;
	}
	
	public void render(SpriteBatch batch) {
		
	}
}
