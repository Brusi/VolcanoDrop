package com.retrom.volcano.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.menus.Fade;
import com.retrom.volcano.menus.GraphicObject;
import com.retrom.volcano.menus.StaticGraphicObject;
import com.retrom.volcano.utils.Tween;
import com.retrom.volcano.utils.TweenQueue;

public class OpeningScene {
	
	TweenQueue queue = new TweenQueue();
	
	GraphicObject title = new StaticGraphicObject(Assets.title, -5,
			WorldRenderer.FRUSTUM_HEIGHT + 50);

	public Fade fade = new Fade();
	
	public OpeningScene() {
		queue.addTweenFromNow(0, 1, fade.in);
		queue.addTweenFromNow(1, 1, new Tween.Bounce(
new Tween.MovePoint(
				title.position_).from(-5, WorldRenderer.FRUSTUM_HEIGHT + 50)
				                .to(-5, WorldRenderer.FRUSTUM_HEIGHT - 250)
				));
	}
	
	public void update(float deltaTime) {
		queue.update(deltaTime);
	}
	
	public void render(SpriteBatch batch) {
		title.render(batch);
	}
}
