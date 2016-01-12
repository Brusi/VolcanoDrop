package com.retrom.volcano.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedRandom<T> {
	static private final float EPSILON = 1e-4f;
	
	private final List<Entry<T>> chanceList;
	private final Random random = new Random();
	
	static private class Entry<T> {
		
		public final float chance;
		public final T value;
		
		public Entry(float chance, T value) {
			this.chance = chance;
			this.value = value;
		}
	}
	
	public static class Builder<T> {
		private List<Entry<T>> chanceList = new ArrayList<Entry<T>>();
		
		private boolean hasSeed = false;
		private long seed;
		
		public Builder() {};
		
		public Builder<T> add(float chance, T value) {
			chanceList.add(new Entry<T>(chance, value));
			return this;
		}
		
		public Builder<T> setSeed(long seed) {
			this.hasSeed = true;
			this.seed = seed;
			return this;
		}
		
		public WeightedRandom<T> build() {
			verifyList(chanceList);
			if (hasSeed) {
				return new WeightedRandom<T>(chanceList, seed);
			} else {
				return new WeightedRandom<T>(chanceList);
			}
		}

		private void verifyList(List<Entry<T>> chanceList) {
			float sum = 0;
			for (Entry<T> entry : chanceList) {
				sum += entry.chance;
			}
			if (sum > 1 + EPSILON || sum < 1 - EPSILON) {
				throw new IllegalStateException("Sum of chances is not 1 but " + sum);
			}
		}
	}
	
	private WeightedRandom(List<Entry<T>> chanceList, long seed) {
		this(chanceList);
		random.setSeed(seed);
	}
	
	private WeightedRandom(List<Entry<T>> chanceList) {
		this.chanceList = chanceList;
	}
	
	public T getNext() {
		float chance = random.nextFloat();
		float accumulateChance = 0;
		for (Entry<T> entry : chanceList) {
			accumulateChance += entry.chance;
			if (chance < accumulateChance) {
				return entry.value;
			}
		}
		
		// In the low-chance case the accumulated chance is numerically smaller
		// than 1, return an arbitrary legal value.
		return chanceList.get(0).value;
	}
}
