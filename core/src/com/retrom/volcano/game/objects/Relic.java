package com.retrom.volcano.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.game.WorldRenderer;

public class Relic extends GameObject {

	public Relic() {
		super(0, 144, 50, 80);
	}
	
	float stateTime = 0;
	
	public void update(float deltaTime) {
		stateTime += deltaTime;
	}
	
	public void render(SpriteBatch batch) {
		Sprite s = WorldRenderer.getFrameLoop(Assets.relic_loop, stateTime);
		Utils.drawCenter(batch, s, this.position.x, this.position.y);
	}
}
