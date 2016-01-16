package com.retrom.volcano.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;

public class TouchToPointTest {
	
	TouchToPoint ttp = new TouchToPoint(100, 200, 200f, 400f);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	private static final float DELTA = 0.001f;
	
	private void testOne(int x, int y, float exp_x, float exp_y) {
		Vector2 res = ttp.toPoint(x, y);
		assertEquals(exp_x, res.x, DELTA);
		assertEquals(exp_y, res.y, DELTA);
	}

	@Test
	public void testCenterAndCorners() {
		// Touch at center.
		testOne(50, 100, 0, 0);
		// Touch top left corner.
		testOne(0, 0, -100, 200);
		// Touch bottom left corner.
		testOne(0, 200, -100, -200);
		// Touch top right corner.
		testOne(100, 0, 100, 200);
		// Touch bottom right corner;
		testOne(100, 200, 100, -200);
	}

}
