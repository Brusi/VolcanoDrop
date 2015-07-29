package com.retrom.volcano.game;

import java.util.List;
import java.util.Random;

public class Spawner {
	
	public interface SpawnerHandler {
		public void dropWall(int col);
	}
	
	private final SpawnerHandler handler_;
	private final ActiveFloors floors_;
	private final Random rand = new Random();
	

	
	Spawner(ActiveFloors floors, SpawnerHandler handler) {
		handler_ = handler;
		this.floors_ = floors;
	}
	
	private static final float TIME_BETWEEN_WALLS = 0.5f;
	
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
	}
}
