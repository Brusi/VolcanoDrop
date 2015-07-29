package com.retrom.volcano.game;

import java.util.List;
import java.util.Random;

import com.retrom.volcano.game.objects.Wall;

public class Spawner {
	
	public interface SpawnerHandler {
		public void dropWall(int col);
		public void dropCoin(float x);
	}
	
	private final SpawnerHandler handler_;
	private final ActiveFloors floors_;
	private final Random rand = new Random();
	

	
	Spawner(ActiveFloors floors, SpawnerHandler handler) {
		handler_ = handler;
		this.floors_ = floors;
	}
	
	// Fixed time between walls in seconds.
	private static final float TIME_BETWEEN_WALLS = 1f;
	
	// Expected time between coins.
	private static final float AVG_COIN_TIME = 1f;
	
	private float timeCount = 0;
	private float timeRemaining = TIME_BETWEEN_WALLS;
	
	public void update(float deltaTime) {
		timeCount += deltaTime;
		timeRemaining -= deltaTime;
		
		if (timeRemaining < 0) {
			timeRemaining += TIME_BETWEEN_WALLS;
			List<Integer> candidates = floors_.getNextPossibleCols();
			handler_.dropWall(candidates.get(rand.nextInt(candidates.size())));
		}
		
		if (Math.random() < deltaTime / AVG_COIN_TIME) {
			float coinX = (float) (Math.random() * Wall.SIZE * 6 - Wall.SIZE * 3);
			handler_.dropCoin(coinX);
		}
	}
}
