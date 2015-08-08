package com.retrom.volcano.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * A queue of timed events. Users can add events to the queue with a timing and
 * a callback which is invoked when the event time arrives.
 * The event queue must be updated so it will know that time has passed.
 * @author Ori
 *
 */
public class EventQueue {
	
	/**
	 * An event to be added to the queue.
	 * @author Ori
	 */
	public interface Event {
		public void invoke();
	}
	
	private TreeMap<Float, List<Event>> events_ = new TreeMap<Float, List<Event>>();
	private float time_;
	
	/**
	 * Updates inner time and invokes events if necessary.
	 * @param deltaTime the time that passed.
	 */
	public void update(float deltaTime) {
		time_ += deltaTime;
		while (!events_.isEmpty() && events_.firstKey() <= time_) {
			Entry<Float, List<Event>> entry = events_.pollFirstEntry();
			for (Event event : entry.getValue()) {
				event.invoke();
			}
		}
	}
	
	/**
	 * Returns whether there are no more events in the queue.
	 * @return true if empty.
	 */
	public boolean isEmpty() {
		return events_.isEmpty();
	}
	
	/**
	 * Add an event to start x time after the current eventQueue inner clock.
	 * @param timeFromNow The time to start the event since the current time.
	 * @param event The event to invoke.
	 */
	public void addEventFromNow(float timeFromNow, Event event) {
		float timing = time_ + timeFromNow;
		addEventAt(event, timing);
	}

	private void addEventAt(Event event, float timing) {
		if (!events_.containsKey(timing)) {
			events_.put(timing, new ArrayList<Event>(1));
		}
		List<Event> l = events_.get(timing);
		l.add(event);
	}
}
