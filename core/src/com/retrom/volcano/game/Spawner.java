package com.retrom.volcano.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import sun.awt.AWTAccessor.EventQueueAccessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.retrom.volcano.game.EventQueue.Event;
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
		public void quake(boolean big);
	}
	
	private final SpawnerHandler handler_;
	private final ActiveFloors floors_;
	private static final Random rand = new Random();
	private final List<Wall> activeWalls;
	private final EventQueue queue = new EventQueue();
	
	// A list of time remaining until a column is free to drop blocks on.
	private final float[] sackSilenceTime_ = new float[NUMBER_OF_COLUMNS];
	
	private int level_ = 0;
	
	
	Spawner(ActiveFloors floors, List<Wall> activeWalls, SpawnerHandler handler) {
		this.activeWalls = activeWalls;
		handler_ = handler;
		this.floors_ = floors;
		initEvents();
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
	
	// Enable/disable for debug.
	public boolean enabled = true;
	
	// Events.
	private EventQueue.Event NOP;
	private EventQueue.Event QUAKE;
	private EventQueue.Event QUAKE_SMALL;
	
	public void update(float deltaTime) {
		if (!enabled) {
			return;
		}
		timeCount += deltaTime;
		
		
		updateHetkeys();
		updateSackSilence(deltaTime);
		
		
		queue.update(deltaTime);
		if (queue.isEmpty()) {
			updateLevel();
		}
		if (queue.isEmpty()) {
			dropWalls();
		}
//		dropCoins(deltaTime);

//		dropSackAtRate(deltaTime, AVG_SACK_TIME);
//		dropFireballAtRate(deltaTime, AVG_FIREBALL_TIME);
	}

	private void updateSackSilence(float deltaTime) {
		for (int col = 0; col < sackSilenceTime_.length; ++col) {
			if (sackSilenceTime_[col] > 0) {
				sackSilenceTime_[col] -= deltaTime;
			} else {
				sackSilenceTime_[col] = 0;
			}
		}
	}

	private void dropCoins(float deltaTime) {
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
	}

	private void updateLevel() {
		if (timeCount < 7) {
			setLevel(0);
		} else if (timeCount < 14) {
			setLevel(1);
		} else if (timeCount < 21){
			setLevel(2);
		} else if (timeCount < 35){
			setLevel(3);
		} else {
			setLevel(4);
		}
	}
	
	

	private void setLevel(int level) {
		if (level_ == level) {
			return;
		}
		level_ = level;
		switch (level_) {
		case 1: handler_.quake(true); break;
		case 3: dropBasicSequence(); handler_.quake(true); break;
		case 4:
			List<Integer> candidates = getWallCandidates();
			int col;
			if (!candidates.isEmpty()) {
				col = candidates.get(rand.nextInt(candidates.size()));
			} else {
				col = randomColumn();
			}
			handler_.dropBurningWall(col);
			queue.addEventFromNow(4f, NOP);
			handler_.quake(true);
			break;
		}
	}

	private void updateHetkeys() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
			sideToSideSequenceWithEdgeHole();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
			sideToSideSequenceWithMiddleHole();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
			seq3and3();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
			barsSequence();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
			seq3pairs();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
			seq5dice();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
			if (rand.nextBoolean()) {
				seqV();
			} else {
				seqHat();
			}
		}
	}
	

	private void dropFireballAtRate(float deltaTime, float fireball_rate) {
		if (timeCount > 10 && Math.random() < deltaTime / fireball_rate) {
			handler_.dropFireball(randomColumn());
		}
	}

	private void dropSackAtRate(float deltaTime, float sack_rate) {
		if (Math.random() < deltaTime / sack_rate) {
			int col = randomColumn();
			if (sackSilenceTime_[col] <= 0) {
				handler_.dropSack(col, randomSackCoins());
				sackSilenceTime_[col] = SACK_SACK_SILENCE_TIME;
			}
		}
	}

	private void dropWalls() {
		List<Integer> candidates = getWallCandidates();
		if (candidates.isEmpty()) {
			queue.addEventFromNow(TIME_BETWEEN_WALLS, NOP);
			return;
		}
		
		if (level_ == 0) {
			dropOneBasicWall(candidates);
			queue.addEventFromNow(TIME_BETWEEN_WALLS * 1.5f, NOP);
		} else if (level_ == 1) {
			dropOneBasicWall(candidates);
			queue.addEventFromNow(TIME_BETWEEN_WALLS, NOP);
		} else if (level_ == 2) {
			dropWallOrDual(candidates);
			queue.addEventFromNow(TIME_BETWEEN_WALLS, NOP);
		} else if (level_ == 3 || level_ == 4){
			if (rand.nextInt(6) == 0) {
				dropBasicSequence();
			} else {
				dropWallOrDual(candidates);
				queue.addEventFromNow(TIME_BETWEEN_WALLS, NOP);
			}
		}
	}

	private void dropBasicSequence() {
		handler_.quake(false);
		
		int seqType = rand.nextInt(7);
		switch (seqType) {
		case 0: sideToSideSequenceWithEdgeHole(); break;
		case 1: sideToSideSequenceWithMiddleHole(); break;
		case 2: seq3and3(); break;
		case 3: seq3pairs(); break;
		case 4: seq5dice(); break;
		case 5: seqV(); break;
		case 6: seqHat(); break;
		}
	}

	private void dropOneBasicWall(List<Integer> candidates) {
		Integer col = candidates.get(rand.nextInt(candidates.size()));
		handler_.dropWall(col);
	}

	private void dropWallOrDual(List<Integer> candidates) {
		boolean dualDropped = false;
		if (rand.nextInt(4) == 0) {
			List<Integer> dualCandidates = floors_.getNextPossibleDualCols();
			int col = dualCandidates.isEmpty() ? -1 : dualCandidates.get(rand.nextInt(dualCandidates.size()));
			
			boolean isClear = isClearForDualWall(col);
			
			if (col >= 0 &&  isClear && candidates.contains(col) && candidates.contains(col+1)) {
				dualDropped = true;
				handler_.dropDualWall(col);
			}
		}
		if (!dualDropped) {
			Integer col = candidates.isEmpty() ? -1 : candidates.get(rand.nextInt(candidates.size()));
			if (col >= 0) {
				if (level_ < 4) {
					handler_.dropWall(col);
				} else {
					dropSingleRandomTypeWall(col);
				}
			}
		}
	}

	private List<Integer> getWallCandidates() {
		List<Integer> candidates = new ArrayList<Integer>(floors_.getNextPossibleCols());
		for (Wall wall : activeWalls) {
			Integer col = wall.col();
			if (candidates.size() >= 2) {
				candidates.remove(col);
			}
		}
		for (int col = 0; col < sackSilenceTime_.length; ++col) {
			if (sackSilenceTime_[col] > SACK_SACK_SILENCE_TIME - SACK_WALL_SILENCE_TIME) {
				if (candidates.contains(col)) {
					candidates.remove(candidates.indexOf(col));
				}
			} 
		}
		return candidates;
	}

	private void dropSingleRandomTypeWall(Integer col) {
		if (level_ < 7) {
			if (rand.nextInt(4) == 0) {
				handler_.dropBurningWall(col);
			} else {
				handler_.dropWall(col);
			}
		} else {
			if (rand.nextInt(3) == 0) {
				if (rand.nextBoolean()) {
					handler_.dropBurningWall(col);
				} else {
					handler_.dropFlamethrower(col);
				}
			} else {
				handler_.dropWall(col);
			}
		}
	}
	
	private int randomSackCoins() {
		return rand.nextInt(10) + 4;
	}

	private int randomColumn() {
		return rand.nextInt(NUMBER_OF_COLUMNS);
	}
	
	private int randomNonEdgeColumn() {
		return rand.nextInt(NUMBER_OF_COLUMNS - 2) + 1;
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
	
	// ########## Events ##########
	
	private void initEvents() {
		NOP = new EventQueue.Event() {
			@Override
			public void invoke() {}
		};
		QUAKE = new EventQueue.Event() {
			@Override
			public void invoke() {
				handler_.quake(true);
			}
		};
		QUAKE_SMALL = new EventQueue.Event() {
			@Override
			public void invoke() {
				handler_.quake(false);
			}
		}; 
	}
	
	EventQueue.Event dropWallEvent(final int col) {
		return new EventQueue.Event() {
			@Override
			public void invoke() {
				handler_.dropWall(col);
			}
		};
	}
	
	// ########## Sequences ##########
	
	private void sideToSideSequence() {
		boolean leftToRight = rand.nextBoolean();
		
		for (int i = 0; i < 6; i++) {
			final int col = leftToRight ? i : 5 - i;
			EventQueue.Event event = new EventQueue.Event() {
				@Override
				public void invoke() {
					dropSingleRandomTypeWall(col);
				}
			};
			final float time = i * 0.5f;
			queue.addEventFromNow(time, event);
		}
	}
	
	private void sideToSideSequenceWithEdgeHole() {
		boolean leftToRight = rand.nextBoolean();
		
		for (int i = 0; i < 5; i++) {
			final int col = leftToRight ? i : 5 - i;
			EventQueue.Event event = new EventQueue.Event() {
				@Override
				public void invoke() {
					handler_.dropWall(col);
				}
			};
			final float time = i * 0.15f;
			queue.addEventFromNow(time, event);
		}
		queue.addEventFromNow(2, NOP);
	}
	
	private void sideToSideSequenceWithMiddleHole() {
		boolean leftToRight = rand.nextBoolean();
		int hole_col = randomNonEdgeColumn();
		for (int i = 0; i < 6; i++) {
			if (i == hole_col) {
				continue;
			}
			final int col = leftToRight ? i : 5 - i;
			EventQueue.Event event = new EventQueue.Event() {
				@Override
				public void invoke() {
					handler_.dropWall(col);
				}
			};
			final float time = i * 0.15f;
			queue.addEventFromNow(time, event);
		}
		queue.addEventFromNow(2, NOP);
	}
	
	private void seq3and3() {
		boolean leftToRight = rand.nextBoolean();
		float time = 0;
		for (int times=0; times < 2; times++) {
			for (int i = 0; i < 3; i++) {
				final int col = leftToRight ? i : 5 - i;
				EventQueue.Event event = new EventQueue.Event() {
					@Override
					public void invoke() {
						handler_.dropWall(col);
					}
				};
				queue.addEventFromNow(time, event);
				time += 0.15f;
			}
			leftToRight = !leftToRight;
		}
		queue.addEventFromNow(2, NOP);
	}
	
	private void barsSequence() {
		boolean leftToRight = rand.nextBoolean();
		int odd = rand.nextInt(2);
		for (int times = 0; times < 2; times++) {
			for (int i = 0; i < 6; i+=2) {
				int col_before = i + odd;
				final int col = leftToRight ? col_before : 5 - col_before;
				EventQueue.Event event = new EventQueue.Event() {
					@Override
					public void invoke() {
						handler_.dropWall(col);
					}
				};
				float time = times * 1.5f + 0.07f*i;
				queue.addEventFromNow(time, event);
			}
			odd = 1 - odd;
		}
		queue.addEventFromNow(TIME_BETWEEN_WALLS * 2, NOP);
	}
	
	private void seq3pairs() {
		float diff = 0.5f;
		
		final float leftToRight = rand.nextInt(2);
		queue.addEventFromNow(0, dropWallEvent(2));
		queue.addEventFromNow(0, dropWallEvent(3));
		
		queue.addEventFromNow((2 - leftToRight) * diff, dropWallEvent(0));
		queue.addEventFromNow((2 - leftToRight) * diff, dropWallEvent(1));
		
		queue.addEventFromNow((1 + leftToRight) * diff, dropWallEvent(4));
		queue.addEventFromNow((1 + leftToRight) * diff, dropWallEvent(5));
		queue.addEventFromNow(3, NOP);
	}
	
	private void seq5dice() {
		queue.addEventFromNow(0, dropWallEvent(0));
		queue.addEventFromNow(0, dropWallEvent(1));
		queue.addEventFromNow(0, dropWallEvent(4));
		queue.addEventFromNow(0, dropWallEvent(5));
		
		queue.addEventFromNow(0.7f, dropWallEvent(2));
		queue.addEventFromNow(0.7f, dropWallEvent(3));
		
		queue.addEventFromNow(1.4f, dropWallEvent(0));
		queue.addEventFromNow(1.4f, dropWallEvent(1));
		queue.addEventFromNow(1.4f, dropWallEvent(4));
		queue.addEventFromNow(1.4f, dropWallEvent(5));
		
		queue.addEventFromNow(2f, NOP);
	}
	
	private void seqV() {
		float diff = 0.5f;
		
		queue.addEventFromNow(0, dropWallEvent(2));
		queue.addEventFromNow(0, dropWallEvent(3));
		
		queue.addEventFromNow(1 * diff, dropWallEvent(1));
		queue.addEventFromNow(1 * diff, dropWallEvent(4));
		queue.addEventFromNow(2 * diff, dropWallEvent(0));
		queue.addEventFromNow(2 * diff, dropWallEvent(5));
		
		queue.addEventFromNow(3 * diff, NOP);
	}
	
	private void seqHat() {
		float diff = 0.5f;
		
		queue.addEventFromNow(2 * diff, dropWallEvent(2));
		queue.addEventFromNow(2 * diff, dropWallEvent(3));
		queue.addEventFromNow(1 * diff, dropWallEvent(1));
		queue.addEventFromNow(1 * diff, dropWallEvent(4));
		queue.addEventFromNow(0 * diff, dropWallEvent(0));
		queue.addEventFromNow(0 * diff, dropWallEvent(5));
		
		queue.addEventFromNow(4 * diff, NOP);
	}
}
