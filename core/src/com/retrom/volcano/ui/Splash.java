package com.retrom.volcano.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.control.ControlManager;
import com.retrom.volcano.control.OnScreenPhoneControl;
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
		queue.addTweenFromNow(0, 1, fade.in);
		queue.addTweenFromNow(1, 1, new Tween.Bounce(new Tween.MovePoint(
				title.position_).from(-5, TITLE_INITIAL_Y)
				.to(-5, TITLE_FINAL_Y)));
		queue.addEventFromNow(2, new EventQueue.Event() {
			@Override
			public void invoke() {
				state = State.SHOWING;
			}
		});
	}
	
	public void update(float deltaTime) {
		queue.update(deltaTime);
		stateTime += deltaTime;
		if (state == State.SHOWING) {
			tapToStart.position_.x = ((stateTime % 1 > 0.5f && stateTime > 2) ? 10000 : 0);
		}
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
		
		showControls();
	}
	
	private void showControls() {
		queue.addTweenFromNow(1.5f, 1f, new Tween() {
			@Override
			public void invoke(float t) {
				hub.setAlpha(t);
			}
		});
	}
}
