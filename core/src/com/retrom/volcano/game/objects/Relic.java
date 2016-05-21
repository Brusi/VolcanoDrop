package com.retrom.volcano.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.utils.BatchUtils;

public class Relic extends GameObject {

	public Relic() {
		super(0, 140, 50, 80);
	}
	
	float stateTime = 0;
	float relicStateTime = 0;
	
	public void update(float deltaTime) {
		stateTime += deltaTime;
		relicStateTime += deltaTime;
	}
	
	public void render(SpriteBatch batch) {
		BatchUtils.setBlendFuncAdd(batch);
		drawFlames(batch);
		drawGlow(batch);
		BatchUtils.setBlendFuncNormal(batch);
		drawRelic(batch);
		
	}

	private void drawGlow(SpriteBatch batch) {
		{
			float tint = ((float) Math.sin(stateTime * 3) + 1) / 2 * 0.3f + 0.7f;
			Assets.relic_glow.setColor(tint, tint, tint, 1);
			Utils.drawCenter(batch, Assets.relic_glow, position.x,
					position.y + 16);
		}
		{
			float tint = Utils.randomRange(0.8f, 1f);
			Assets.burningWallGlow.setColor(tint, tint, tint, 1);
			Utils.drawCenter(batch, Assets.burningWallGlow, position.x,
					position.y + 16);
		}
	}

	private void drawFlames(SpriteBatch batch) {
		Utils.drawCenter(batch, WorldRenderer.getFrameLoop(Assets.relic_flames, stateTime), position.x + 5, position.y + 16);
	}

	private void drawRelic(SpriteBatch batch) {
		Sprite s = Assets.relic_loop.get(0);
		if (relicStateTime > 1) {
			s = WorldRenderer.getFrameLoop(Assets.relic_loop, stateTime);
		}
		
		if (relicStateTime > 2) {
			relicStateTime -= 2;
		}
		Utils.drawCenter(batch, s, this.position.x, this.position.y);
	}
}
