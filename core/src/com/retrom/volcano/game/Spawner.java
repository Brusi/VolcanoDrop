package com.retrom.volcano.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import sun.awt.AWTAccessor.EventQueueAccessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.retrom.volcano.game.EventQueue.Event;
import com.retrom.volcano.game.conf.CoinChancesConfiguration;
import com.retrom.volcano.game.objects.Collectable;
import com.retrom.volcano.game.objects.Wall;
import com.retrom.volcano.utils.WeightedRandom;

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
		public void warning(int col, boolean sound);
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
		queue.addEventFromNow(1, NOP);
	}
	
	// Fixed time between walls in seconds.
	private static final float TIME_BETWEEN_WALLS = 1f;
	
	// Expected time between coins.
	private static final float AVG_COIN_TIME = 1f;
	private static final float AVG_FIREBALL_TIME = 12f;
	private static final float AVG_SACK_TIME = 12f;
	
	private static final Float SACK_WALL_SILENCE_TIME = 1f;
	private static final Float SACK_SACK_SILENCE_TIME = 1f;
	
	// Cooldown between *start* of sequence until the next can happen.
	private static final float SEQUENCE_COOLDOWN_TIME = 8f;
	private float sequence_cooldown = 0;
	
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
		if (sequence_cooldown > 0) {
			sequence_cooldown -= deltaTime;
		}
		
		
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
//			float coinX = (float) (Math.random() * (Wall.SIZE * 3 - 60) - Wall.SIZE * 3 + 30);
			float coinX = Utils.random2Range(Wall.SIZE * 3 - 30);
			
			Collectable.Type type = getCoinType();
			if (type == null) return;
			
			
			if (Math.random() < 0.05) {
				type = Collectable.Type.POWERUP_SHIELD;
			} if (Math.random() < 0.05) {
				type = Collectable.Type.POWERUP_SLOMO;
			} else if (Math.random() < 0.05) {
				type = Collectable.Type.POWERUP_MAGNET;
			}
			
			handler_.dropCoin(coinX, type);
		}
	}
	
	public float getTimeBetweenWalls() {
		if (level_ < 6) {
			return 1.2f * TIME_BETWEEN_WALLS;
		} else if (level_ < 9) {
			return 1f * TIME_BETWEEN_WALLS;
		} else if (level_ < 11) {
			return 0.9f * TIME_BETWEEN_WALLS;
		} else if (level_ < 13) {
			return 0.8f * TIME_BETWEEN_WALLS;
		} else {
			return 0.7f * TIME_BETWEEN_WALLS;
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
			handler_.quake(false);
			sideToSideSequenceWithEdgeHole();
//			seqBurningPair();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
			handler_.quake(false);
			sideToSideSequenceWithMiddleHole();
//			seqBurningClosingHole();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
			handler_.quake(false);
//			seq3and3();
			seqFlamethrower2middle();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
			handler_.quake(false);
			if (rand.nextBoolean()) {
				seqFlamethrowerHat();
			} else {
				seqFlamethrowerV(); 
			}
//			barsSequence();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
			handler_.quake(false);
			seq3pairs();
//			sideToSideThrower();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
			handler_.quake(false);
			seq5dice();
//			sideToSideThrowerWithMiddleHole();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
			handler_.quake(false);
//			seq3and3Thrower();
			if (rand.nextBoolean()) {
				seqV();
			} else {
				seqHat();
			}
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
			if (shouldDropSequence()) {
				dropBasicSequence();
			} else {
				dropWallOrDual(candidates);
				queue.addEventFromNow(getTimeBetweenWalls(), NOP);
			}
		} else if (level_ == 5) {
			if (shouldDropSequence()) {
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
			if (shouldDropSequence()) {
				dropBurningSequence();
			} else {
				dropWallOrDual(candidates);
				queue.addEventFromNow(getTimeBetweenWalls(), NOP);
			}
		} else if (level_ >= 8) {
			if (shouldDropSequence()) {
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

	private boolean shouldDropSequence() {
		if (sequence_cooldown > 0) {
			System.out.println("cooldown still on.");
		}
		if (sequence_cooldown > 0  || rand.nextInt(5) != 0) {
			return false;
		}
		return true;
	}

	private void dropBasicSequence() {
		handler_.quake(false);
		sequence_cooldown = SEQUENCE_COOLDOWN_TIME;
		warningWithSound = true;
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
		sequence_cooldown = SEQUENCE_COOLDOWN_TIME;
		warningWithSound = true;
		int seqType = rand.nextInt(5);
		switch (seqType) {
			case 0: seqBurning1(); break;
			case 1: seqBurning102(); break;
			case 2: sideToSideBurning(); break;
			case 3: seqBurningHole(); break;
//			case 4: seqBurningPair(); break;
			case 4: seqBurningClosingHole(); break;
		}
	}
	
	private void dropFlamethrowerSequence() {
		handler_.quake(false);
		sequence_cooldown = SEQUENCE_COOLDOWN_TIME;
		warningWithSound = true;
		int seqType = rand.nextInt(5);
		switch (seqType) {
			case 0: seqFlamethrower2middle(); break;
			case 1: if (rand.nextBoolean()) seqFlamethrowerHat(); else seqFlamethrowerV(); break;
			case 2: sideToSideThrower(); break;
			case 3: sideToSideThrowerWithMiddleHole(); break;
			case 4: seq3and3Thrower(); break;
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
		return CoinChancesConfiguration.getNextCoin(level_);
//		int index = rand.nextInt(15);
//		return Collectable.Type.values()[index];
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
	
	EventQueue.Event warningEvent(final int col, final boolean first) {
		return new EventQueue.Event() {
			@Override
			public void invoke() {
				handler_.warning(col, first);
			}
		};
	}
	
	// ########## Sequences ##########
	private boolean warningWithSound = false;
	
	private boolean getWithSound() {
		boolean $ = warningWithSound;
		warningWithSound = false;
		return $;
	}
	
	private void makeWarning(float time, int col) {
		queue.addEventFromNow(time - 1, warningEvent(col, getWithSound()));
	}
	
	// Drops wall with warning.
	private void dropWallAt(float time, int col) {
		makeWarning(time, col);
		queue.addEventFromNow(time, dropWallEvent(col));
	}
	
	// Drops burning wall with warning.
	private void dropBurningWallAt(float time, int col) {
		makeWarning(time, col);
		queue.addEventFromNow(time, dropBurningWallEvent(col));
	}
	
	// Drops burning wall with warning.
	private void dropFlamethrowerAt(float time, int col) {
		makeWarning(time, col);
		queue.addEventFromNow(time, dropFlamethrowerEvent(col));
	}
	
//	private void eventWithWarning(float time, EventQueue.Event event, boolean sound) {
//		queue.addEventFromNow(time, event);
//		queue.addEventFromNow(time, warningEvent(col, first));
//	}
	
//	private void eventWithWarning(float time, EventQueue.Event event) {
//		eventWithWarning(time, event, false);
//	}
	
	private void sideToSideSequenceWithEdgeHole() {
		warningWithSound = true;
		
		boolean leftToRight = rand.nextBoolean();
		
		for (int i = 0; i < 5; i++) {
			final int col = leftToRight ? i : 5 - i;
			final float time = i * 0.15f + 1f;
			dropWallAt(time, col);
		}
		queue.addEventFromNow(3f, NOP);
	}
	
	private void sideToSideSequenceWithMiddleHole() {
		warningWithSound = true;
		boolean leftToRight = rand.nextBoolean();
		int hole_col = randomNonEdgeColumn();
		for (int i = 0; i < 6; i++) {
			if (i == hole_col) {
				continue;
			}
			final int col = leftToRight ? i : 5 - i;
			final float time = i * 0.15f + 1f;
			dropWallAt(time, col);
		}
		queue.addEventFromNow(3f, NOP);
	}
	
	private void seq3and3() {
		boolean leftToRight = rand.nextBoolean();
		float time = 1;
		for (int times=0; times < 2; times++) {
			warningWithSound = true;
			for (int i = 0; i < 3; i++) {
				final int col = leftToRight ? i : 5 - i;
				dropWallAt(time, col);
				time += 0.15f;
			}
			time += 0.5f;
			leftToRight = !leftToRight;
		}
		queue.addEventFromNow(3f, NOP);
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
		queue.addEventFromNow(2.5f, NOP);
	}
	
	private void seq3pairs() {
		float diff = 1f;
		
		final float leftToRight = rand.nextInt(2);
		warningWithSound = true;
		dropWallAt(1 * diff, 2);
		dropWallAt(1 * diff, 3);
		
		warningWithSound = true;
		dropWallAt((3 - leftToRight) * diff, 0);
		dropWallAt((3 - leftToRight) * diff, 1);
		
		warningWithSound = true;
		dropWallAt((2 + leftToRight) * diff, 4);
		dropWallAt((2 + leftToRight) * diff, 5);
		queue.addEventFromNow(4, NOP);
	}
	
	private void seq5dice() {
		warningWithSound = true;
		dropWallAt(1, 0);
		dropWallAt(1, 1);
		dropWallAt(1, 4);
		dropWallAt(1, 5);

		warningWithSound = true;
		dropWallAt(2, 2);
		dropWallAt(2, 3);
		
		queue.addEventFromNow(3f, NOP);
	}
	
	private void seqV() {
		float diff = 0.5f;
		
		warningWithSound = true;
		dropWallAt(0.5f + 1 * diff, 2);
		dropWallAt(0.5f + 1 * diff, 3);
		
		warningWithSound = true;
		dropWallAt(0.5f + 2 * diff, 1);
		dropWallAt(0.5f + 2 * diff, 4);
		
		warningWithSound = true;
		dropWallAt(0.5f + 3 * diff, 0);
		dropWallAt(0.5f + 3 * diff, 5);
		
		queue.addEventFromNow(0.5f + 5 * diff, NOP);
	}
	
	private void seqHat() {
		float diff = 0.5f;
		
		warningWithSound = true;
		dropWallAt(0.5f + 3 * diff, 2);
		dropWallAt(0.5f + 3 * diff, 3);

		warningWithSound = true;
		dropWallAt(0.5f + 2 * diff, 1);
		dropWallAt(0.5f + 2 * diff, 4);
		
		warningWithSound = true;
		dropWallAt(0.5f + 1 * diff, 0);
		dropWallAt(0.5f + 1 * diff, 5);
		
		queue.addEventFromNow(0.5f + 5 * diff, NOP);
	}
	
	private void seqBurning1() {
		final float diff = 0.5f;
		if (rand.nextBoolean()) {
			warningWithSound = true;
			dropBurningWallAt(0 * diff + 1, 1);
			warningWithSound = true;
			dropBurningWallAt(1 * diff + 1, 5);
			warningWithSound = true;
			dropBurningWallAt(2 * diff + 1, 3);
			warningWithSound = true;
			dropBurningWallAt(3 * diff + 1, 0);
			warningWithSound = true;
			dropBurningWallAt(4 * diff + 1, 2);
			warningWithSound = true;
			dropBurningWallAt(5 * diff + 1, 4);
		} else {
			warningWithSound = true;
			dropBurningWallAt(0 * diff + 1, 4);
			warningWithSound = true;
			dropBurningWallAt(1 * diff + 1, 0);
			warningWithSound = true;
			dropBurningWallAt(2 * diff + 1, 2);
			warningWithSound = true;
			dropBurningWallAt(3 * diff + 1, 5);
			warningWithSound = true;
			dropBurningWallAt(4 * diff + 1, 3);
			warningWithSound = true;
			dropBurningWallAt(5 * diff + 1, 1);
		}
		
		queue.addEventFromNow(7 * diff + 1, NOP);
	}
	
	private void seqBurning102() {
		final float diff = 0.6f;
		if (rand.nextBoolean()) {
			warningWithSound = true;
			dropBurningWallAt(0 * diff + 1, 1);
			dropBurningWallAt(0 * diff + 1, 4);
			warningWithSound = true;
			dropBurningWallAt(1 * diff + 1, 0);
			dropBurningWallAt(1 * diff + 1, 5);
			warningWithSound = true;
			dropBurningWallAt(2 * diff + 1, 2);
			dropBurningWallAt(2 * diff + 1, 3);
		} else {
			warningWithSound = true;
			dropBurningWallAt(2 * diff + 1, 1);
			dropBurningWallAt(2 * diff + 1, 4);
			warningWithSound = true;
			dropBurningWallAt(1 * diff + 1, 0);
			dropBurningWallAt(1 * diff + 1, 5);
			warningWithSound = true;
			dropBurningWallAt(0 * diff + 1, 2);
			dropBurningWallAt(0 * diff + 1, 3);
		}
		queue.addEventFromNow(4 * diff + 1, NOP);
	}
	
	private void sideToSideBurning() {
		boolean leftToRight = rand.nextBoolean();
		warningWithSound = true;
		for (int i = 0; i < 5; i++) {
			final int col = leftToRight ? i : 5 - i;
			final float time = i * 0.15f + 1;
			if (i % 2 == 0) {
				dropBurningWallAt(time, col);
			} else {
				dropWallAt(time, col);
			}
		}
		queue.addEventFromNow(3, NOP);
	}
	
	private void seqBurningHole() {
		boolean sound1 = true;
		boolean sound2 = true;
		
		int hole = rand.nextInt(3) + 1;
		
		for (int i = 0; i < 6; i++) {
			if (i == hole || i == hole +1) {
				if (sound2) {
					warningWithSound = true;
					sound2 = false;
				}
				dropBurningWallAt(2f, i);
				continue;
			}
			if (sound1) {
				warningWithSound = true;
				sound1 = false;
			}
			if (i == hole -1 || i == hole + 2) {
				dropBurningWallAt(1f, i);
			} else {
				dropWallAt(1f, i);
			}
		}
		queue.addEventFromNow(3, NOP);
	}
	
	// Depracated?
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
		warningWithSound = true;
		dropBurningWallAt(0 * diff + 1, 0);
		dropBurningWallAt(0 * diff + 1, 5);
		
		warningWithSound = true;
		dropBurningWallAt(1 * diff + 1, 1);
		dropBurningWallAt(1 * diff + 1, 4);
		
		warningWithSound = true;
		dropBurningWallAt(3 * diff + 1, 2 + rand.nextInt(2));
		
		queue.addEventFromNow(8 * diff + 1, NOP);
	}
	
	private void seqFlamethrower2middle() {
		warningWithSound = true;
		dropFlamethrowerAt(1, 2);
		dropFlamethrowerAt(1.1f, 3);
		warningWithSound = true;
		dropWallAt(1.5f, 0);
		dropWallAt(1.5f, 5);
		dropWallAt(1.7f, 1);
		dropWallAt(1.7f, 4);
		queue.addEventFromNow(3f, NOP);
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
			final float time = i * 0.15f + 0.5f;
			queue.addEventFromNow(time, event);
			
			if (i == hole_col + 2 || i == hole_col - 2) {
				queue.addEventFromNow(time + 0.5f, dropWallEvent(col));
			}
		}
		queue.addEventFromNow(2, NOP);
	}
	
	private void seq3and3Thrower() {
		boolean leftToRight = rand.nextBoolean();
		float time = 0.5f;
		for (int times=0; times < 2; times++) {
			for (int i = 0; i < 3; i++) {
				final int col = leftToRight ? i : 5 - i;
				EventQueue.Event event = times == 0 ? dropFlamethrowerEvent(col) : dropWallEvent(col);
				queue.addEventFromNow(time, event);
				time += 0.15f;
			}
			time += 0.5f;
			leftToRight = !leftToRight;
		}
		queue.addEventFromNow(2, NOP);
	}
}


