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
	private static final float AVG_FIREBALL_TIME = 12f;
	private static final float AVG_SACK_TIME = 12f;
	
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
		dropCoins(deltaTime);

		dropSackAtRate(deltaTime, AVG_SACK_TIME);
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
	
	public float getTimeBetweenWalls() {
		if (level_ < 6) {
			return 1.0f * TIME_BETWEEN_WALLS;
		} else if (level_ < 9) {
			return 0.8f * TIME_BETWEEN_WALLS;
		} else if (level_ < 11) {
			return 0.7f * TIME_BETWEEN_WALLS;
		} else if (level_ < 13) {
			return 0.6f * TIME_BETWEEN_WALLS;
		} else {
			return 0.5f * TIME_BETWEEN_WALLS;
		}
	}

	private void updateLevel() {
		float[] timesArray = {
				7, 14, 21,
				35, 48, 60 + 16,
				60 + 29, 60 + 43, 60 + 57,
				120 + 10, 120 + 24,
				120 + 37, 120 + 51,
				180 + 33, 180 + 47 };
		
		for (int i=0; i < timesArray.length; i++) {
			if (timeCount < timesArray[i]) {
				setLevel(i);
				return;
			}
		}
	}
	
	

	private void setLevel(int level) {
		if (level_ == level) {
			return;
		}
		level_ = level;
		System.out.println("Level " + level_);
		switch (level_) {
		case 1: handler_.quake(true); break;
		case 3: dropBasicSequence(); handler_.quake(true); break;
		case 4: {
			List<Integer> candidates = getWallCandidates();
			int col;
			if (!candidates.isEmpty()) {
				col = candidates.get(rand.nextInt(candidates.size()));
			} else {
				col = randomColumn();
			}
			handler_.dropBurningWall(col);
			queue.addEventFromNow(1f, NOP);
			handler_.quake(true);
			break;
		}
		case 5:
			dropBurningSequence(); handler_.quake(true); break;
		case 6:
			handler_.quake(true);
			 break;
		case 7: {
			handler_.quake(true);
			// TODO: extract 'get the next available column' to a methed.
			List<Integer> candidates = getWallCandidates();
			int col;
			if (!candidates.isEmpty()) {
				col = candidates.get(rand.nextInt(candidates.size()));
			} else {
				col = randomColumn();
			}
			handler_.dropFlamethrower(col);
			queue.addEventFromNow(1.5f, NOP);
			handler_.quake(true);
			break;
		}
		case 8:
			dropFlamethrowerSequence(); handler_.quake(true); break;
		}
		
	}

	private void updateHetkeys() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
//			sideToSideSequenceWithEdgeHole();
			seqBurningPair();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
			seqBurningClosingHole();
//			sideToSideSequenceWithMiddleHole();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
			seq3and3();
//			seqFlamethrower2middle();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
			if (rand.nextBoolean()) {
				seqFlamethrowerHat();
			} else {
				seqFlamethrowerV(); 
			}
//			barsSequence();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
//			seq3pairs();
			sideToSideThrower();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
//			seq5dice();
			sideToSideThrowerWithMiddleHole();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
			seq3and3Thrower();
//			if (rand.nextBoolean()) {
//				seqV();
//			} else {
//				seqHat();
//			}
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8) || Gdx.input.getAccelerometerX() > 7 && queue.size() < 3) {
//			seqBurning1();
			seqBurningClosingHole();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9) || Gdx.input.getAccelerometerX() < -7 && queue.size() < 3) {
			seqBurning102();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
//			sideToSideBurning();
			seqBurningHole();
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
			queue.addEventFromNow(getTimeBetweenWalls(), NOP);
			return;
		}
		
		if (level_ == 0) {
			dropOneBasicWall(candidates);
			queue.addEventFromNow(getTimeBetweenWalls() * 1.5f, NOP);
		} else if (level_ == 1) {
			dropOneBasicWall(candidates);
			queue.addEventFromNow(getTimeBetweenWalls(), NOP);
		} else if (level_ == 2) {
			dropWallOrDual(candidates);
			queue.addEventFromNow(getTimeBetweenWalls(), NOP);
		} else if (level_ == 3 || level_ == 4){
			if (rand.nextInt(6) == 0) {
				dropBasicSequence();
			} else {
				dropWallOrDual(candidates);
				queue.addEventFromNow(getTimeBetweenWalls(), NOP);
			}
		} else if (level_ == 5) {
			if (rand.nextInt(6) == 0) {
				if (rand.nextBoolean()) {
					dropBurningSequence();
				} else {
					dropBasicSequence();
				}
			} else {
				dropWallOrDual(candidates);
				queue.addEventFromNow(getTimeBetweenWalls(), NOP);
			}			
		} else if (level_ == 6 || level_ == 7) {
			if (rand.nextInt(6) == 0) {
				dropBurningSequence();
			} else {
				dropWallOrDual(candidates);
				queue.addEventFromNow(getTimeBetweenWalls(), NOP);
			}
		} else if (level_ >= 8) {
			if (rand.nextInt(6) == 0) {
				if (rand.nextBoolean()) {
					dropBurningSequence();
				} else {
					dropFlamethrowerSequence();
				}
			} else {
				dropWallOrDual(candidates);
				queue.addEventFromNow(getTimeBetweenWalls(), NOP);
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
	
	private void dropBurningSequence() {
		handler_.quake(false);
		
		int seqType = rand.nextInt(6);
		switch (seqType) {
			case 0: seqBurning1(); break;
			case 1: seqBurning102(); break;
			case 2: sideToSideBurning(); break;
			case 3: seqBurningHole(); break;
			case 4: seqBurningPair(); break;
			case 5: seqBurningClosingHole(); break;
		}
	}
	
	private void dropFlamethrowerSequence() {
		handler_.quake(false);
		int seqType = rand.nextInt(5);
		switch (seqType) {
			case 0: seqFlamethrower2middle(); break;
			case 1: if (rand.nextBoolean()) seqFlamethrowerHat(); else seqFlamethrowerV(); break;
			case 2: sideToSideThrower();
			case 3: sideToSideThrowerWithMiddleHole();
			case 4: seq3and3Thrower();
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
			if (rand.nextInt(3) == 0) {
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
		return rand.nextInt(NUMBER_OF_COLUMNS - 3) + 2;
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
	
	EventQueue.Event dropBurningWallEvent(final int col) {
		return new EventQueue.Event() {
			@Override
			public void invoke() {
				handler_.dropBurningWall(col);
			}
		};
	}
	
	EventQueue.Event dropFlamethrowerEvent(final int col) {
		return new EventQueue.Event() {
			@Override
			public void invoke() {
				handler_.dropFlamethrower(col);
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
			time += 0.3f;
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
		queue.addEventFromNow(getTimeBetweenWalls() * 2, NOP);
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
		
		queue.addEventFromNow(0.8f, dropWallEvent(2));
		queue.addEventFromNow(0.8f, dropWallEvent(3));
		
//		queue.addEventFromNow(1.6f, dropWallEvent(0));
//		queue.addEventFromNow(1.6f, dropWallEvent(1));
//		queue.addEventFromNow(1.6f, dropWallEvent(4));
//		queue.addEventFromNow(1.6f, dropWallEvent(5));
//		
		queue.addEventFromNow(2.6f, NOP);
	}
	
	private void seqV() {
		float diff = 0.5f;
		
		queue.addEventFromNow(0, dropWallEvent(2));
		queue.addEventFromNow(0, dropWallEvent(3));
		
		queue.addEventFromNow(1 * diff, dropWallEvent(1));
		queue.addEventFromNow(1 * diff, dropWallEvent(4));
		queue.addEventFromNow(2 * diff, dropWallEvent(0));
		queue.addEventFromNow(2 * diff, dropWallEvent(5));
		
		queue.addEventFromNow(4 * diff, NOP);
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
	
	private void seqBurning1() {
		final float diff = 0.5f;
		if (rand.nextBoolean()) {
			queue.addEventFromNow(0 * diff, dropBurningWallEvent(1));
			queue.addEventFromNow(1 * diff, dropBurningWallEvent(5));
			queue.addEventFromNow(2 * diff, dropBurningWallEvent(3));
			queue.addEventFromNow(3 * diff, dropBurningWallEvent(0));
			queue.addEventFromNow(4 * diff, dropBurningWallEvent(2));
			queue.addEventFromNow(5 * diff, dropBurningWallEvent(4));
		} else {
			queue.addEventFromNow(0 * diff, dropBurningWallEvent(4));
			queue.addEventFromNow(1 * diff, dropBurningWallEvent(0));
			queue.addEventFromNow(2 * diff, dropBurningWallEvent(2));
			queue.addEventFromNow(3 * diff, dropBurningWallEvent(5));
			queue.addEventFromNow(4 * diff, dropBurningWallEvent(3));
			queue.addEventFromNow(5 * diff, dropBurningWallEvent(1));
		}
		
		queue.addEventFromNow(7 * diff, NOP);
	}
	
	private void seqBurning102() {
		final float diff = 0.6f;
		if (rand.nextBoolean()) {
			queue.addEventFromNow(1 * diff, dropBurningWallEvent(1));
			queue.addEventFromNow(1 * diff, dropBurningWallEvent(4));
			queue.addEventFromNow(2 * diff, dropBurningWallEvent(0));
			queue.addEventFromNow(2 * diff, dropBurningWallEvent(5));
			queue.addEventFromNow(3 * diff, dropBurningWallEvent(2));
			queue.addEventFromNow(3 * diff, dropBurningWallEvent(3));
		} else {
			queue.addEventFromNow(3 * diff, dropBurningWallEvent(1));
			queue.addEventFromNow(3 * diff, dropBurningWallEvent(4));
			queue.addEventFromNow(2 * diff, dropBurningWallEvent(0));
			queue.addEventFromNow(2 * diff, dropBurningWallEvent(5));
			queue.addEventFromNow(1 * diff, dropBurningWallEvent(2));
			queue.addEventFromNow(1 * diff, dropBurningWallEvent(3));
		}
		queue.addEventFromNow(5 * diff, NOP);
	}
	
	private void sideToSideBurning() {
		boolean leftToRight = rand.nextBoolean();
		for (int i = 0; i < 5; i++) {
			final int col = leftToRight ? i : 5 - i;
			EventQueue.Event event = (i % 2 == 0) ? dropBurningWallEvent(col)
					: dropWallEvent(col);
			final float time = i * 0.15f;
			queue.addEventFromNow(time, event);
		}
		queue.addEventFromNow(2, NOP);
	}
	
	private void seqBurningHole() {
		int hole = rand.nextInt(3) + 1;
		
		for (int i = 0; i < 6; i++) {
			if (i == hole || i == hole +1) {
				EventQueue.Event event = dropBurningWallEvent(i);
				queue.addEventFromNow(2f, event);
				continue;
			}
			EventQueue.Event event;
			if (i == hole -1 || i == hole + 2) {
				event = dropBurningWallEvent(i);
			} else {
				event = dropWallEvent(i);
			}
			queue.addEventFromNow(1f, event);
		}
		queue.addEventFromNow(3, NOP);
	}
	
	private void seqBurningPair() {
		final float diff = 0.9f;
		final float delay = 0.2f;
		if (rand.nextBoolean()) {
			queue.addEventFromNow(0 * diff + delay, dropBurningWallEvent(0));
			queue.addEventFromNow(0 * diff, dropBurningWallEvent(2));
			queue.addEventFromNow(1 * diff + delay, dropBurningWallEvent(1));
			queue.addEventFromNow(1 * diff, dropBurningWallEvent(3));
			queue.addEventFromNow(2 * diff + delay, dropBurningWallEvent(2));
			queue.addEventFromNow(2 * diff, dropBurningWallEvent(4));
			queue.addEventFromNow(3 * diff + delay, dropBurningWallEvent(3));
			queue.addEventFromNow(3 * diff, dropBurningWallEvent(5));
			queue.addEventFromNow(4 * diff, NOP);
		} else {
			queue.addEventFromNow(0 * diff + delay, dropBurningWallEvent(5 - 0));
			queue.addEventFromNow(0 * diff, dropBurningWallEvent(5 - 2));
			queue.addEventFromNow(1 * diff + delay, dropBurningWallEvent(5 - 1));
			queue.addEventFromNow(1 * diff, dropBurningWallEvent(5 - 3));
			queue.addEventFromNow(2 * diff + delay, dropBurningWallEvent(5 - 2));
			queue.addEventFromNow(2 * diff, dropBurningWallEvent(5 - 4));
			queue.addEventFromNow(3 * diff + delay, dropBurningWallEvent(5 - 3));
			queue.addEventFromNow(3 * diff, dropBurningWallEvent(5 - 5));
			queue.addEventFromNow(4 * diff, NOP);
		}
	}
	
	private void seqBurningClosingHole() {
		final float diff = 0.2f;
		queue.addEventFromNow(0 * diff, dropBurningWallEvent(0));
		queue.addEventFromNow(0 * diff, dropBurningWallEvent(5));
		
		queue.addEventFromNow(1 * diff, dropBurningWallEvent(1));
		queue.addEventFromNow(1 * diff, dropBurningWallEvent(4));
		
		queue.addEventFromNow(3 * diff, dropBurningWallEvent(2 + rand.nextInt(2)));
		queue.addEventFromNow(8 * diff, NOP);
	}
	
	private void seqFlamethrower2middle() {
		queue.addEventFromNow(0, dropFlamethrowerEvent(2));
		queue.addEventFromNow(0.1f, dropFlamethrowerEvent(3));
		queue.addEventFromNow(0.5f, dropWallEvent(0));
		queue.addEventFromNow(0.5f, dropWallEvent(5));
		queue.addEventFromNow(0.7f, dropWallEvent(1));
		queue.addEventFromNow(0.7f, dropWallEvent(4));
		queue.addEventFromNow(2f, NOP);
	}
	
	private void seqFlamethrowerHat() {
		float diff = 0.5f;
		
		queue.addEventFromNow(2 * diff, dropWallEvent(2));
		queue.addEventFromNow(2 * diff, dropWallEvent(3));
		queue.addEventFromNow(1 * diff, dropFlamethrowerEvent(1));
		queue.addEventFromNow(1 * diff, dropFlamethrowerEvent(4));
		queue.addEventFromNow(0 * diff, dropWallEvent(0));
		queue.addEventFromNow(0 * diff, dropWallEvent(5));
		
		queue.addEventFromNow(4 * diff, NOP);
	}
	
	private void seqFlamethrowerV() {
		float diff = 0.5f;
		
		queue.addEventFromNow(0, dropWallEvent(2));
		queue.addEventFromNow(0, dropWallEvent(3));
		
		queue.addEventFromNow(1 * diff, dropFlamethrowerEvent(1));
		queue.addEventFromNow(1 * diff, dropFlamethrowerEvent(4));
		queue.addEventFromNow(2 * diff, dropWallEvent(0));
		queue.addEventFromNow(2 * diff, dropWallEvent(5));
		
		queue.addEventFromNow(4 * diff, NOP);
	}
	
	private void sideToSideThrower() {
		boolean leftToRight = rand.nextBoolean();
		if (leftToRight) {
			queue.addEventFromNow(0, dropFlamethrowerEvent(5));
		} else {
			queue.addEventFromNow(0, dropFlamethrowerEvent(0));
		}
		
		for (int i = 0; i < 5; i++) {
			final int col = leftToRight ? i : 5 - i;
			EventQueue.Event event = (i == 0) ? dropFlamethrowerEvent(col) : dropWallEvent(col);
			final float time = i * 0.15f + 0.2f;
			queue.addEventFromNow(time, event);
		}
		queue.addEventFromNow(2, NOP);
	}
	
	private void sideToSideThrowerWithMiddleHole() {
		boolean leftToRight = rand.nextBoolean();
		int hole_col = randomNonEdgeColumn();
		for (int i = 0; i < 6; i++) {
			if (i == hole_col) {
				continue;
			}
			EventQueue.Event event;
			final int col = leftToRight ? i : 5 - i;
			if (i == hole_col + 1 || i == hole_col - 1) {
				event = dropFlamethrowerEvent(col);
			} else {
				event = dropWallEvent(col);
			}
			final float time = i * 0.15f;
			queue.addEventFromNow(time, event);
			
			if (i == hole_col + 2 || i == hole_col - 2) {
				queue.addEventFromNow(time + 0.5f, dropWallEvent(col));
			}
		}
		queue.addEventFromNow(2, NOP);
	}
	
	private void seq3and3Thrower() {
		boolean leftToRight = rand.nextBoolean();
		float time = 0;
		for (int times=0; times < 2; times++) {
			for (int i = 0; i < 3; i++) {
				final int col = leftToRight ? i : 5 - i;
				EventQueue.Event event = times == 0 ? dropFlamethrowerEvent(col) : dropWallEvent(col);
				queue.addEventFromNow(time, event);
				time += 0.15f;
			}
			time += 0.3f;
			leftToRight = !leftToRight;
		}
		queue.addEventFromNow(2, NOP);
	}
}


