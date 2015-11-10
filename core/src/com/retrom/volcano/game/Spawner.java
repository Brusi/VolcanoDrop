package com.retrom.volcano.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.retrom.volcano.game.objects.Collectable;
import com.retrom.volcano.game.objects.Wall;

public class Spawner {
	
	private static final int NUMBER_OF_COLUMNS = 6;

	public interface SpawnerHandler {
		public void dropWall(int col);
		public void dropCoin(float x, Collectable.Type type);
		public void dropDualWall(int col);
		public void dropBurningWall(int col);
		public void dropFlamethrower(int col);
		public void dropFireball(int col);
		public void dropSack(int col, int numCoins);
	}
	
	private final SpawnerHandler handler_;
	private final ActiveFloors floors_;
	private static final Random rand = new Random();
	private final List<Wall> activeWalls;
	private final EventQueue queue = new EventQueue();
	
	// A list of time remaining until a column is free to drop blocks on.
	private final float[] sackSilenceTime_ = new float[NUMBER_OF_COLUMNS];
	
	
	Spawner(ActiveFloors floors, List<Wall> activeWalls, SpawnerHandler handler) {
		this.activeWalls = activeWalls;
		handler_ = handler;
		this.floors_ = floors;
	}
	
	// Fixed time between walls in seconds.
	private static final float TIME_BETWEEN_WALLS = 1f;
	
	// Expected time between coins.
	private static final float AVG_COIN_TIME = 1f;
	private static final float AVG_FIREBALL_TIME = 7f;
	private static final float AVG_SACK_TIME = 5f;
	
	private static final Float SACK_WALL_SILENCE_TIME = 1f;
	private static final Float SACK_SACK_SILENCE_TIME = 1f;
	
	private float timeCount = 0;
	private float timeRemaining = TIME_BETWEEN_WALLS;
	
	public void update(float deltaTime) {
		timeCount += deltaTime;
		
		if (queue.isEmpty()) {
			timeRemaining -= deltaTime;
		}
		queue.update(deltaTime);
		
		for (int col = 0; col < sackSilenceTime_.length; ++col) {
			if (sackSilenceTime_[col] > 0) {
				sackSilenceTime_[col] -= deltaTime;
			} else {
				sackSilenceTime_[col] = 0;
			}
		}

		if (timeRemaining < 0) {
			timeRemaining += TIME_BETWEEN_WALLS;
			if (rand.nextInt(5) == 0) {
				sideToSideSequence();
			} else {
				List<Integer> candidates = new ArrayList<Integer>(floors_.getNextPossibleCols());
				for (int col = 0; col < sackSilenceTime_.length; ++col) {
					if (sackSilenceTime_[col] > SACK_SACK_SILENCE_TIME - SACK_WALL_SILENCE_TIME) {
						if (candidates.contains(col)) {
							candidates.remove(candidates.indexOf(col));
						}
					} 
				}
				
				boolean dualDropped = false;
				if (rand.nextInt(4) == 0) {
					List<Integer> dualCandidates = floors_.getNextPossibleDualCols();
					int col = dualCandidates.isEmpty() ? -1 : dualCandidates.get(rand.nextInt(dualCandidates.size()));
					
					boolean isClear = isClearForDualWall(col);
					
					if (col >= 0 &&  isClear && candidates.contains(col) && candidates.contains(col+1)) {
						System.out.println("Dropping at " + col);
						dualDropped = true;
						handler_.dropDualWall(col);
					}
				}
				if (!dualDropped) {
					Integer col = candidates.isEmpty() ? -1 : candidates.get(rand.nextInt(candidates.size()));
					if (col >= 0) {
						DropSingleRandomTypeWall(col);
					}
				}
			}
		}
		
		if (Math.random() < deltaTime / AVG_SACK_TIME) {
			int col = randomColumn();
			if (sackSilenceTime_[col] <= 0) {
				handler_.dropSack(col, randomSackCoins());
				sackSilenceTime_[col] = SACK_SACK_SILENCE_TIME;
			}
		}
		
		if (Math.random() < deltaTime / AVG_COIN_TIME) {
			float coinX = (float) (Math.random() * (Wall.SIZE * 3 - 60) - Wall.SIZE * 3 + 30);
			
			Collectable.Type type = getCoinType();
			
			
			if (Math.random() < 0.1) {
				type = Collectable.Type.POWERUP_SHIELD;
			} if (Math.random() < 0.1) {
				type = Collectable.Type.POWERUP_SLOMO;
			} else if (Math.random() < 0.1) {
				type = Collectable.Type.POWERUP_MAGNET;
			}
			
			handler_.dropCoin(coinX, type);
		}
		
		if (Math.random() < deltaTime / AVG_FIREBALL_TIME) {
			handler_.dropFireball(randomColumn());
		}
	}

	private void DropSingleRandomTypeWall(Integer col) {
		if (rand.nextInt(3) == 0) {
			if (rand.nextBoolean()) {
//				handler_.dropBurningWall(col);
				handler_.dropFlamethrower(col);
			} else {
				handler_.dropFlamethrower(col);
			}
		} else {
			handler_.dropFlamethrower(col);
//			handler_.dropWall(col);
		}
	}
	
	private void sideToSideSequence() {
		boolean leftToRight = rand.nextBoolean();
		
		for (int i = 0; i < 6; i++) {
			final int col = leftToRight ? i : 5 - i;
			EventQueue.Event event = new EventQueue.Event() {
				@Override
				public void invoke() {
					DropSingleRandomTypeWall(col);
				}
			};
			final float time = i * 0.5f;
			queue.addEventFromNow(time, event);
		}
	}

	private int randomSackCoins() {
		return rand.nextInt(10) + 4;
	}

	private int randomColumn() {
		return rand.nextInt(NUMBER_OF_COLUMNS);
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
