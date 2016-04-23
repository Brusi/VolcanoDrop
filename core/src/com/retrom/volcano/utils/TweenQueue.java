package com.retrom.volcano.utils;

import java.util.ArrayList;
import java.util.List;

public class TweenQueue extends EventQueue {
	
	// Defines a tween added to queue.
	private static class TweenEventDef {
		private float startTime;
		public final float duration;
		public final Tween tween;
		public TweenEventDef(float startTime, float duration, Tween tween) {
			this.startTime = startTime;
			this.duration = duration;
			this.tween = tween;
			
		}
	}
	
	private List<TweenEventDef> activeTweens = new ArrayList<TweenEventDef>();
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		for (TweenEventDef tween : activeTweens) {
			float t = (getTime() - tween.startTime) / tween.duration;
			tween.tween.invoke(t);
		}
	}
	
	/**
	 * Adds a tween to start x time after the current eventQueue inner clock, and last duration.
	 * @param timeFromNow The time to start the event since the current time.
     * @param duration The time to start the event since the current time.
	 * @param event The tween to invoke.
	 */
	public void addTweenFromNow(float timeFromNow, float duration, Tween tween) {
		final TweenEventDef eventDef =
				new TweenEventDef(getTime() + timeFromNow, duration, tween);
		addEventFromNow(timeFromNow, new Event() {
			@Override
			public void invoke() {
				activeTweens.add(eventDef);
			}
		});
		addEventFromNow(timeFromNow + duration, new Event() {
			@Override
			public void invoke() {
				// Lastly, always invoke last tween phase.
				eventDef.tween.invoke(1.0f);
				activeTweens.remove(eventDef);
			}
		});
	}
	
	
}
