package com.retrom.volcano.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sun.nio.cs.HistoricallyNamedCharset;

public class FiniteRandomizerTest {
	
	private static float EPSILON = 0.005f;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	private float[] getRateOfChances(float[] chances, int totalRolls) {
		FiniteRandomizer.Builder<Integer> builder = new FiniteRandomizer.Builder<Integer>();
		for (int i=0; i < chances.length; i++) {
			builder.add(chances[i], i);
		}
		FiniteRandomizer<Integer> r = builder.build();
		
		int[] histogram = new int[chances.length];
		for (int i=0; i <  totalRolls; i++) {
			histogram[r.getNext()]++;
		}
		
		float[] rates = new float[chances.length];
		for (int i=0; i <  chances.length; i++) {
			rates[i] = histogram[i] / (float) totalRolls; 
		}
		return rates;
	}

	@Test
	public void testLegalChance() {
		float[] chances = {0.2f, 0.3f, 0.5f};
		float[] rates = getRateOfChances(chances, 100000);
		
		for (int i=0; i < rates.length; i++) {
			float rate = rates[i];
			System.out.println(""+rate);
			assertTrue(rate > chances[i] - EPSILON && rate < chances[i] + EPSILON);
		}
	}
	
	@Test
	public void testSeedChance() {
		FiniteRandomizer.Builder<Integer> builder = new FiniteRandomizer.Builder<Integer>()
				.add(0.1f, 0)
				.add(0.2f, 1)
				.add(0.3f, 2)
				.add(0.4f, 3)
				.setSeed(1234L);
		
		FiniteRandomizer<Integer> r1 = builder.build();
		FiniteRandomizer<Integer> r2 = builder.build();
		
		int[] histogram = new int[4];
		for (int i=0; i < 100000; i++) {
			histogram[r1.getNext()]++;
			histogram[r2.getNext()]--;
		}
		
		for (int i=0; i < 4; i++) {
			assertEquals(0, histogram[i]);
		}
	}

	@Test(expected = IllegalStateException.class)
	public void chancesSumTooLow() {
		new FiniteRandomizer.Builder<Integer>()
				.add(0.1f, 0)
				.add(0.2f, 1)
				.add(0.3f, 2)
				.add(0.3f, 3) // Total 0.9, smaller than 1.
				.build(); 
	}

	@Test(expected = IllegalStateException.class)
	public void chancesSumTooHigh() {
		new FiniteRandomizer.Builder<Integer>()
				.add(0.1f, 0)
				.add(0.2f, 1)
				.add(0.3f, 2)
				.add(0.4f, 3) 
				.add(0.1f, 4) // Total 1.1, larger than 1.
				.build(); 
	}
}
