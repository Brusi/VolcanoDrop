package com.retrom.volcano.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.audio.Sound;

public class SoundEvictingQueueTest {
	
	SoundEvictingQueue q = new SoundEvictingQueue(40); 

	@Test
	public void forEachDoesNothingWhenEmpty() {
		q.forEach(new SoundEvictingQueue.Consumer() {
			@Override
			public void accept(Sound s, long id) {
				fail("Nothing should happen!");
			}
		});
	}
	
	@Test
	public void forEachHappensOnce() {
		final int[] times = new int[1];
		times[0] = 0;
		q.add(null, 7);
		q.forEach(new SoundEvictingQueue.Consumer() {
			@Override
			public void accept(Sound s, long id) {
				times[0]++;
			}
		});
		assertEquals(1, times[0]);
	}
}
