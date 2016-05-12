package com.retrom.volcano.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sun.nio.cs.HistoricallyNamedCharset;

public class TweenQueueTest {
	
	TweenQueue tq = new TweenQueue();
	private static float EPSILON = 0.00001f;

	@Before
	public void setUp() throws Exception {
		target[0] = 0;
	}

	@After
	public void tearDown() throws Exception {
	}
	
	private final float[] target = new float[1];
	private final Tween tween = new Tween() {
		@Override
		public void invoke(float t) {
			target[0] = t;
		}
	};

	@Test
	public void testSimpleTween() {
		tq.addTweenFromNow(1, 1, tween);
		assertEquals(0, target[0], EPSILON);
		tq.update(0.5f);
		assertEquals(0, target[0], EPSILON);
		tq.update(0.5f);
		assertEquals(0, target[0], EPSILON);
		tq.update(0.5f);
		assertEquals(0.5f, target[0], EPSILON);
		tq.update(0.3f);
		assertEquals(0.8f, target[0], EPSILON);
		tq.update(0.3f);
		assertEquals(1f, target[0], EPSILON);
	}
	
	@Test
	public void testEaseIn() {
		tq.addTweenFromNow(0, 1, new Tween.EaseOut(tween));
		assertEquals(0, target[0], EPSILON);
		tq.update(0.001f);
		// Slope at begin of EaseIn is 2.
		assertEquals(0.002f, target[0], EPSILON);
		tq.update(0.499f);
		// With EaseOut, aftet 0.5 time, 0.75 of the way is done.
		assertEquals(0.75f, target[0], EPSILON);
	}
	
	@Test
	public void testEaseOut() {
		tq.addTweenFromNow(0, 1, new Tween.EaseIn(tween));
		assertEquals(0, target[0], EPSILON);
		tq.update(0.5f);
		// With EaseIn, aftet 0.5 time, 0.25 of the way is done.
		assertEquals(0.25f, target[0], EPSILON);
		tq.update(0.499f);
		// Slope at end of EaseIn is 2.
		assertEquals(0.998f, target[0], EPSILON);
	}
	
	@Test
	public void testBounce() {
		tq.addTweenFromNow(0, 1, new Tween.Bounce(tween));
		System.out.println(""+target[0]);
		for (int i=0; i < 100; i++) {
			tq.update(1f / 100);
			System.out.println(""+target[0]);
		}
	}
	
	@Test
	public void testLongTime() {
		tq.addTweenFromNow(0, 2, tween);
		assertEquals(0, target[0], EPSILON);
		tq.update(0.5f);
		assertEquals(0.25f, target[0], EPSILON);
		tq.update(1f);
		assertEquals(0.75f, target[0], EPSILON);
		tq.update(1f);
		assertEquals(1f, target[0], EPSILON);
	}
}
