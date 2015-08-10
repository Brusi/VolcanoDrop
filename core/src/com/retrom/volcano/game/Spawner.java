package com.retrom.volcano.game;

import java.util.List;
import java.util.Random;

import com.retrom.volcano.game.objects.Collectable;
import com.retrom.volcano.game.objects.Wall;

public class Spawner {
	
	public interface SpawnerHandler {
		public void dropWall(int col);
		public void dropCoin(float x, Collectable.Type type);
		public void dropDualWall(int col);
		public void dropBurningWall(int col);
	}
	
	private final SpawnerHandler handler_;
	private final ActiveFloors floors_;
	private static final Random rand = new Random();
	private final List<Wall> activeWalls;
	

	
	Spawner(ActiveFloors floors, List<Wall> activeWalls, SpawnerHandler handler) {
		this.activeWalls = activeWalls;
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
			
			boolean dualDropped = false;
			if (rand.nextInt(4) == 0) {
				List<Integer> dualCandidates = floors_.getNextPossibleDualCols();
				int col = dualCandidates.isEmpty() ? -1 : dualCandidates.get(rand.nextInt(dualCandidates.size()));
				
				boolean isClear = isClearForDualWall(col);
				System.out.println("---------"+col);
				System.out.println("dualCandidates="+dualCandidates);
				System.out.println("floors_="+floors_.toString());
				System.out.println("col="+col);
				System.out.println("isClearForDualWall(col)="+isClearForDualWall(col));
				System.out.println("candidates.contains(col)="+candidates.contains(col));
				System.out.println("candidates.contains(col+1)="+candidates.contains(col+1));
				
				
				if (col >= 0 &&  isClear && candidates.contains(col) && candidates.contains(col+1)) {
					System.out.println("Dropping at " + col);
					dualDropped = true;
					handler_.dropDualWall(col);
				}
			}
			if (!dualDropped) {
				if (rand.nextInt(3) == 0) {
					handler_.dropBurningWall(candidates.get(rand.nextInt(candidates.size())));
				} else {
					handler_.dropWall(candidates.get(rand.nextInt(candidates.size())));
				}
			}
		}
		
		if (Math.random() < deltaTime / AVG_COIN_TIME) {
			float coinX = (float) (Math.random() * (Wall.SIZE * 3 - 60) - Wall.SIZE * 3 + 30);
			
			Collectable.Type type = getCoinType();
			
			
			if (Math.random() < 0.1) {
				type = Collectable.Type.POWERUP_MAGNET;
			}
			
			handler_.dropCoin(coinX, type);
		}
	}

	private boolean isClearForDualWall(int col) {
		for (Wall wall : activeWalls) {
			if (wall.col() == col || wall.col() == col + 1
					|| (wall.isDual() || wall.col() + 1 == col))
				return false;
		}
		return true;
	}

	private Collectable.Type getCoinType() {
		int index = rand.nextInt(15);
		return Collectable.Type.values()[index];
	}
}
