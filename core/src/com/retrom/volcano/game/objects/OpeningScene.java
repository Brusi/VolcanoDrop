package com.retrom.volcano.game.objects;

import com.badlogic.gdx.Gdx;
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
	
	enum State {
		SPLASH,
	}
	State state = State.SPLASH;
	float stateTime = 0;
	
	TweenQueue queue = new TweenQueue();
	
	GraphicObject title = new StaticGraphicObject(Assets.title, -5,
												  WorldRenderer.FRUSTUM_HEIGHT + 50);
	GraphicObject tapToStart = new StaticGraphicObject(Assets.tapToStart, 0,
			  WorldRenderer.FRUSTUM_HEIGHT - 450);

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
		stateTime += deltaTime;
		if (state == State.SPLASH) {
			tapToStart.position_.x = ((stateTime % 1 > 0.5f && stateTime > 2) ? 0 : 10000);
		}
		checkTap();
	}
	
	private void checkTap() {
		if (Gdx.input.justTouched()) {
			queue.addTweenFromNow(0, 0.5f, new Tween() {
				@Override
				public void invoke(float t) {
					title.setAlpha(1-t);
					tapToStart.setAlpha(1-t);
				}
			});
		}
	}

	public void render(SpriteBatch batch) {
		switch (state) {
		case SPLASH:
			title.render(batch);
			tapToStart.render(batch);
			break;
		}
	}
}
