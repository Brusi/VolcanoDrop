package com.retrom.volcano.game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.retrom.volcano.game.EventQueue;

public class EventQueueTest {
	
	private EventQueue queue = new EventQueue();
	
	private List<String> invokations = new ArrayList<String>();

	@Test
	public void testNoEvents() {
		assertTrue(queue.isEmpty());
		assertTrue(invokations.isEmpty());
		queue.update(50f);
		assertTrue(queue.isEmpty());
		assertTrue(invokations.isEmpty());
	}
	
	@Test
	public void testOneEventAtTime() {
		queue.addEventFromNow(1f, new EventQueue.Event() {
			@Override
			public void invoke() {
				invokations.add("One");
			}
		});
		queue.update(0.75f);
		assertFalse(queue.isEmpty());
		assertTrue(invokations.isEmpty());
		queue.update(0.5f);
		assertTrue(queue.isEmpty());
		assertFalse(invokations.isEmpty());
		
		assertEquals(invokations.iterator().next(), "One");
	}
	
	@Test
	public void exactTimeEventIsInvoked() {
		queue.addEventFromNow(1f, new EventQueue.Event() {
			@Override
			public void invoke() {
				invokations.add("One");
			}
		});
		queue.update(1f);
		assertTrue(queue.isEmpty());
		assertFalse(invokations.isEmpty());
		assertEquals(invokations.iterator().next(), "One");
	}
	
	
	@Test
	public void twoEventsAtTheSameTime() {
		queue.addEventFromNow(1f, new EventQueue.Event() {
			@Override
			public void invoke() {
				invokations.add("One");
			}
		});
		queue.addEventFromNow(1f, new EventQueue.Event() {
			@Override
			public void invoke() {
				invokations.add("Two");
			}
		});
		queue.update(1f);
		assertTrue(queue.isEmpty());
		assertEquals(2, invokations.size());
		assertEquals(invokations.get(0), "One");
		assertEquals(invokations.get(1), "Two");
	}
	
	@Test
	public void oneEventsAtATime() {
		queue.addEventFromNow(1f, new EventQueue.Event() {
			@Override
			public void invoke() {
				invokations.add("One");
			}
		});
		queue.addEventFromNow(2f, new EventQueue.Event() {
			@Override
			public void invoke() {
				invokations.add("Two");
			}
		});
		queue.update(0.5f);
		queue.update(1f);
		assertEquals(1, invokations.size());
		queue.update(2f);
		assertEquals(2, invokations.size());
	}
	
	@Test
	public void eventFromNotTheBeginning() {
		queue.update(0.99f);
		queue.addEventFromNow(1f, new EventQueue.Event() {
			@Override
			public void invoke() {
				invokations.add("One");
			}
		});
		queue.update(0.99f);
		assertEquals(0, invokations.size());
		queue.update(0.09f);
		assertEquals(1, invokations.size());
	}
}
