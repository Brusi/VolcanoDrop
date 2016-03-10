package com.retrom.volcano.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.retrom.volcano.data.Levels;
import com.retrom.volcano.data.Levels.LevelDefinition;
import com.retrom.volcano.data.Levels.ProbabilityGroup;
import com.retrom.volcano.data.CoinChancesConfiguration;
import com.retrom.volcano.data.Sequence;
import com.retrom.volcano.data.SequenceLib;
import com.retrom.volcano.data.SpawnerAction;
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
	
	private int level_ = -1;
	private float time_between_walls_;
	
	SequenceLib slib = SequenceLib.loadFromDefault();
	Levels levels = Levels.loadFromDefault();
	private List<Collectable> collectables;
	
	Spawner(ActiveFloors floors, List<Wall> activeWalls, List<Collectable> collectables, SpawnerHandler handler) {
		this.activeWalls = activeWalls;
		this.collectables = collectables;
		handler_ = handler;
		this.floors_ = floors;
		initEvents();
		queue.addEventFromNow(1, NOP);
	}
	
	// Expected time between coins.
	private static final float AVG_COIN_TIME = 1f;
	private static final float AVG_FIREBALL_TIME = 12f;
	private static final float AVG_SACK_TIME = 12f;
	
	private static final Float SACK_WALL_SILENCE_TIME = 1f;
	private static final Float SACK_SACK_SILENCE_TIME = 1f;
	
	// Cooldown between *start* of sequence until the next can happen.
	private static final float SEQUENCE_COOLDOWN_TIME = 8f;
	private static final float SOUND_COOLDOWN_TIME = 0.5f;
	private float sequence_cooldown = 0;
	
	private float timeCount = 0;
	private float lastWarningSoundTime = 0;
	
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
		
		
		updateHotkeys();
		updateSackSilence(deltaTime);
		
		queue.update(deltaTime);
		if (queue.isEmpty()) {
			updateLevel();
		}
		// Updating the level can change isEmpty status, so checking again.
		if (queue.isEmpty()) {
			dropPowerupOrWalls();
		}
		dropCoins(deltaTime);

		dropSackAtRate(deltaTime, AVG_SACK_TIME);
//		dropFireballAtRate(deltaTime, AVG_FIREBALL_TIME);
	}

	// Drops powerup according to rules and chances.
	// Returns true if a powerup was dropped.
	private boolean tryDropPowerup() {
		Collectable.Type powerup = currentLevelDef().powerup_wr.getNext();
		if (powerup == null) {
			return false;
		}
		// Do not drop another powerup of a kind if exists.
		for (Collectable c : collectables) {
			if (c.type == powerup) {
				System.out.println("Powerup already exists: " + c.type);
				return false;
			}
		}
		
		// Good to go!
		handler_.dropCoin(randomCoinX(), powerup);
		queue.addEventFromNow(getTimeBetweenWalls(), NOP);
		return true;
	}
	
	private void dropPowerupOrWalls() {
		Collectable.Type powerup = currentLevelDef().powerup_wr.getNext();
		if (tryDropPowerup()) {
			return;
		}
		
		dropWalls();
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
		if (level_ < 0) return;
		if (Math.random() < deltaTime / AVG_COIN_TIME) {
//			float coinX = (float) (Math.random() * (Wall.SIZE * 3 - 60) - Wall.SIZE * 3 + 30);
			float coinX = randomCoinX();
			
			Collectable.Type type = CoinChancesConfiguration
					.CoinFromBase(currentLevelDef().coins_wr.getNext()); 
					
			if (type == null) return;
			
			
//			if (Math.random() < 0.05) {
//				type = Collectable.Type.POWERUP_SHIELD;
//			} if (Math.random() < 0.05) {
//				type = Collectable.Type.POWERUP_SLOMO;
//			} else if (Math.random() < 0.05) {
//				type = Collectable.Type.POWERUP_MAGNET;
//			}
			
			handler_.dropCoin(coinX, type);
		}
	}

	private float randomCoinX() {
		return Utils.random2Range(Wall.SIZE * 3 - 30);
	}
	
	public float getTimeBetweenWalls() {
		return time_between_walls_;
	}

	private void updateLevel() {
		for (int i=0; i < levels.levels.size(); i++) {
			LevelDefinition ld = levels.levels.get(i);
			float time = ld.start_time;
			if (timeCount < time) {
				setLevel(i-1);
				return;
			}
		}
		setLevel(levels.levels.size()-1);
	}
	
	private void setLevel(int level) {
		if (level_ == level) {
			return;
		}
		System.out.println("setting level " + level);
		level_ = level;
		
		LevelDefinition ld = currentLevelDef();
		{
			float tbw = ld.time_between_walls;
			if (tbw != 0) time_between_walls_ = tbw;
		}
		
		// TODO: change after opening scene is ready.
		if (level_ > 0) {
			handler_.quake(true);
		}
		
		if (ld.level_start_group != null) {
			dropGroup(ld.level_start_group);
		}
	}

	private LevelDefinition currentLevelDef() {
		return levels.levels.get(level_);
	}

	private void updateHotkeys() {
		for (String keyString : slib.hotkeys()) {
			if (keyString == null || keyString.isEmpty()) continue;
			int key = Keys.valueOf(keyString);
			if (key < 0) continue;
			if (Gdx.input.isKeyJustPressed(key)) {
				dropSequenceOrSingle(slib.getByHotkey(keyString));
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
		// Get the next sequence.
		ProbabilityGroup group;

		LevelDefinition levelDef = currentLevelDef();
		if (sequence_cooldown > 0 && levelDef.nswr != null) {
			group = levelDef.nswr.getNext();
		} else {
			group = levelDef.wr.getNext();
		}
		
		dropGroup(group);
	}

	private void dropGroup(ProbabilityGroup group) {
		String sequenceName = group.sequences.get(rand.nextInt(group.sequences.size()));
		Sequence sequence = slib.getSequence(sequenceName);
		dropSequenceOrSingle(sequence);
		
		// If single wall, the level-defined silence time.
		if (group.single) {
			queue.addEventFromNow(getTimeBetweenWalls(), NOP);
		} else {
			sequence_cooldown = SEQUENCE_COOLDOWN_TIME;
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
					|| (wall.isDual() && wall.col() + 1 == col))
				return false;
		}
		return true;
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
	EventQueue.Event dropFireballEvent(final int col) {
		return new EventQueue.Event() {
			@Override
			public void invoke() {
				handler_.dropFireball(col);
			}
		};
	}
	
	EventQueue.Event warningEvent(final int col, final boolean first) {
		return new EventQueue.Event() {
			@Override
			public void invoke() {
				System.out.println("invoking warning with sound="+first);
				handler_.warning(col, first);
			}
		};
	}
	
	// Sequence from class.
	private void dropSequenceOrSingle(Sequence seq) {
		boolean flipped = rand.nextBoolean();
		for (SpawnerAction action : seq.seq) {
			addAction(action, flipped);
		}
	}
	
	// Will replace the old one.
	private void newDropWallOrDual(List<Integer> candidates) {
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
				handler_.dropWall(col);
			}
		}
	}
		
	private void addAction(SpawnerAction action, boolean flipped) {
		if (action.type.random) {
			List<Integer> candidates = getWallCandidates();
			if (action.type == SpawnerAction.Type.WALL_OR_DUAL) {
				newDropWallOrDual(candidates);
				return;
			}
			if (candidates.isEmpty()) {
				queue.addEventFromNow(getTimeBetweenWalls(), NOP);
				return;
			}
			int col = candidates.get(rand.nextInt(candidates.size())); 
			
			switch (action.type) {
			case WALL_NOT_DUAL:
				queue.addEventFromNow(action.time, dropWallEvent(col));
				break;
			case SINGLE_BURN:
				queue.addEventFromNow(action.time, dropBurningWallEvent(col));
				break;
			case SINGLE_FIREBALL:
				queue.addEventFromNow(action.time, dropFireballEvent(col));
				break;
			case SINGLE_FLAME:
				queue.addEventFromNow(action.time, dropFlamethrowerEvent(col));
				break;
			default:
				break;
			}
			return;
		}
		
		int col = flipped ? 5 - action.col : action.col;
		if (!action.type.random && action.type != SpawnerAction.Type.NOP && action.type != SpawnerAction.Type.FIREBALL) {
			makeWarning(action.time, col);
		}
		
		switch(action.type) {
		case BURN:
			queue.addEventFromNow(action.time, dropBurningWallEvent(col));
			break;
		case FIREBALL:
			queue.addEventFromNow(action.time, dropFireballEvent(col));
			break;
		case FLAME:
			queue.addEventFromNow(action.time, dropFlamethrowerEvent(col));
			break;
		case NOP:
			queue.addEventFromNow(action.time, NOP);
			break;
		case WALL:
			queue.addEventFromNow(action.time, dropWallEvent(col));
			break;
		default:
			break;
		}
	}
	
	private boolean getWithSound(float time) {
		float diff = (timeCount + time) - lastWarningSoundTime;
		boolean $ = (timeCount + time) - lastWarningSoundTime > SOUND_COOLDOWN_TIME;
		System.out.println("diff="+diff);
		System.out.println("$="+$);
		if ($) {
			lastWarningSoundTime = timeCount + time;
		}
		return $;
	}
	
	private void makeWarning(float time, int col) {
		queue.addEventFromNow(time - 1, warningEvent(col, getWithSound(time)));
	}
}


