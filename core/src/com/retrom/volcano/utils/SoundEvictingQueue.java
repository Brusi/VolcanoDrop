package com.retrom.volcano.utils;

import com.badlogic.gdx.audio.Sound;

public class SoundEvictingQueue {
	
	public interface Consumer {
		public void accept(Sound s, long id);
	}
	
	private long[] ids;
	private Sound[] sounds;
	int index = 0;
	int maxSize = 0;
	

	public SoundEvictingQueue(int size) {
		ids = new long[size];
		sounds  = new Sound[size];
	}
	
	public void add(Sound sound, long id) {
		ids[index] = id;
		sounds[index] = sound;
		index = (index + 1) % sounds.length; 
		maxSize = maxSize == ids.length ? ids.length : maxSize+1;
	}
	
	public void clear() {
		index = maxSize = 0;
	}
	
	public void forEach(Consumer c) {
		for (int i=0; i < maxSize; i++) {
			c.accept(sounds[i], ids[i]);
		}
	}

}
