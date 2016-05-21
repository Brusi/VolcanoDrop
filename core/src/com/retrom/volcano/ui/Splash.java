package com.retrom.volcano.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.assets.SoundAssets;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.menus.Fade;
import com.retrom.volcano.menus.GraphicObject;
import com.retrom.volcano.menus.StaticGraphicObject;
import com.retrom.volcano.utils.EventQueue;
import com.retrom.volcano.utils.Tween;
import com.retrom.volcano.utils.TweenQueue;

public class Splash {
	
	public enum State {
		APPEARING,
		SHOWING,
		DISAPPEARING,
		DONE;
	}
	public State state = State.APPEARING;
	float stateTime = 0;
	
	private static final float TITLE_INITIAL_Y = WorldRenderer.FRUSTUM_HEIGHT / 2 + 122 ;
	private static final float TITLE_FINAL_Y = WorldRenderer.FRUSTUM_HEIGHT / 2 - 155 ;
	
	private static final float TAP_TO_START_Y = WorldRenderer.FRUSTUM_HEIGHT / 2 - 350 ;
	
	TweenQueue queue = new TweenQueue();
	
	GraphicObject title = new StaticGraphicObject(Assets.title, -5, TITLE_INITIAL_Y);
	GraphicObject tapToStart = new StaticGraphicObject(Assets.tapToStart, 10000,
			TAP_TO_START_Y);

	public Fade fade = new Fade();
	private final Hub hub;
	
	public Splash(Hub hub) {
		this.hub = hub;
		fade.setAlpha(1);
		queue.addTweenFromNow(0, 2/3f, new Tween.Bounce(new Tween.MovePoint(
				title.position_).from(-5, TITLE_INITIAL_Y)
				.to(-5, TITLE_FINAL_Y)));
		queue.addTweenFromNow(2/3f, 2/3f, fade.in);
		queue.addEventFromNow(4/3f, new EventQueue.Event() {
			@Override
			public void invoke() {
				state = State.SHOWING;
			}
		});
		
		queue.addEventFromNow(1/3f, new EventQueue.Event() {
			@Override
			public void invoke() {
				SoundAssets.startMenuMusic();
				SoundAssets.playRandomSound(SoundAssets.wallDualHit);
			}
		});
	}
	
	public void update(float deltaTime) {
		queue.update(deltaTime);
		stateTime += deltaTime;
		if (stateTime > 4f/3) stateTime -= 4f/3;
		tapToStart.position_.x = stateTime > 2/3f ? 0 : 100000;
	}	

	public void render(SpriteBatch batch) {
		if (state != State.DONE) {
			title.render(batch);
			tapToStart.render(batch);
		}
	}

	public void disappear() {
		state = State.DISAPPEARING;
		queue.addTweenFromNow(0, 0.5f, new Tween() {
			@Override
			public void invoke(float t) {
				title.setAlpha(1 - t);
				tapToStart.setAlpha(1 - t);
			}
		});
		queue.addEventFromNow(1.5f, new EventQueue.Event() {
			@Override
			public void invoke() {
				state = State.DONE;
			}
		});
		
		hub.showBeforeSplash();
	}
}
