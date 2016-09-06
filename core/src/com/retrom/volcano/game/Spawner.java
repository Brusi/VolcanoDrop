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
import com.retrom.volcano.data.SpawnerAction.Type;
import com.retrom.volcano.game.Lava.State;
import com.retrom.volcano.game.objects.Collectable;
import com.retrom.volcano.game.objects.Wall;
import com.retrom.volcano.utils.EventQueue;

public class Spawner {
	
	private static final int NUMBER_OF_COLUMNS = 6;

	public interface SpawnerHandler {
		void dropWall(int col);
		void dropCoin(float x, Collectable.Type type);
		void dropDualWall(int col);
		void dropStackWall(int col, int size);
		void dropBurningWall(int col);
		void dropFlamethrower(int col);
		void dropFireball(int col);
		void dropSack(int col, int numCoins);
		void warning(int col, boolean sound);
		void quake(boolean big);
		void setLavaState(State state);

        void bossLastThomp();
        void bossFollowPlayerThomp();
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
	private EventQueue.Event NOP_NO_SEQUENCE_COOLDOWN;
	private EventQueue.Event QUAKE;
	private EventQueue.Event QUAKE_SMALL;
	
	public void update(float deltaTime) {
		timeCount += deltaTime;
		if (sequence_cooldown > 0) {
			sequence_cooldown -= deltaTime;
            if (sequence_cooldown < 0) {
                Gdx.app.log("","Sequence cooldown done.");
            }
        }
		
		
		updateHotkeys();
		updateSackSilence(deltaTime);
		
		queue.update(deltaTime);
		if (queue.isEmpty()) {
			updateLevel();
		}
		
		if (level_ < 0) return;
		
		if (!enabled) {
			return;
		}
		
		// Updating the level can change isEmpty status, so checking again.
		if (queue.isEmpty()) {
			dropPowerupOrWalls();
		}
		dropCoins(deltaTime);

		
		dropSackAtRate(deltaTime, getCurrentLevelDef().avg_sack_time);
//		dropFireballAtRate(deltaTime, AVG_FIREBALL_TIME);
	}

	// Drops powerup according to rules and chances.
	// Returns true if a powerup was dropped.
	private boolean tryDropPowerup() {
		Collectable.Type powerup = getCurrentLevelDef().powerup_wr.getNext();
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
		queue.addEventFromNow(getTimeBetweenWalls(), NOP_NO_SEQUENCE_COOLDOWN);
		return true;
	}
	
	private void dropPowerupOrWalls() {
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
					.CoinFromBase(getCurrentLevelDef().coins_wr.getNext()); 
					
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
		
		LevelDefinition ld = getCurrentLevelDef();
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

	private LevelDefinition getCurrentLevelDef() {
		if (level_ >= 0) {
			return levels.levels.get(level_);
		}
		throw new IllegalStateException("Cannot get level " + level_);
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
		if (sack_rate <= 0) {
			// Rate 0 means disabled.
			return;
		}
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

		LevelDefinition levelDef = getCurrentLevelDef();
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
			queue.addEventFromNow(getTimeBetweenWalls(), NOP_NO_SEQUENCE_COOLDOWN);
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
			public void invoke() {
                sequence_cooldown = 3; /// TODO: use constant.
                Gdx.app.log("INFO", "Starting sequence_cooldown="+sequence_cooldown);
                   }
		};
		NOP_NO_SEQUENCE_COOLDOWN = new EventQueue.Event() {
			@Override public void invoke() {}
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
	
	EventQueue.Event dropStackWallEvent(final int col, final int size) {
		return new EventQueue.Event() {
			@Override
			public void invoke() {
				handler_.dropStackWall(col, size);
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
				handler_.warning(col, first);
			}
		};
	}
	
	EventQueue.Event lavaEvent(final Lava.State state) {
		return new EventQueue.Event() {
			@Override
			public void invoke() {
				handler_.setLavaState(state);
			}
		};
	}

	private EventQueue.Event dropCoinEvent(final int col, final Collectable.BaseType coin_type) {
		return new EventQueue.Event() {
			@Override
			public void invoke() {
				handler_.dropCoin(Utils.xOfCol(col),
						          CoinChancesConfiguration.CoinFromBase(coin_type));
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
	
	private void dropWallForBalance() {
		// First check for double walls.
		int maxRow = floors_.getMaxRow();
		int minRow = floors_.getMinRow();
		if (maxRow == minRow) return;  // Floor already balanced!
		
		
		// Prefer to drop a dual wall
		List<Integer> candidates = new ArrayList<Integer>();
//		for (int col = 0; col < NUMBER_OF_COLUMNS - 1; col++) {
//			if (floors_.getColumnFloor(col) == minRow
//					&& floors_.getColumnFloor(col + 1) == minRow) {
//				candidates.add(col);
//			}
//		}
//		for (Wall wall : activeWalls) {
//			candidates.remove(new Integer(wall.col()));
//			if (wall.isDual()) {
//				candidates.remove(new Integer(wall.col() + 1));
//			}
//		}
//		if (!candidates.isEmpty()) {
//			int colToDrop = candidates.get(rand.nextInt(candidates.size()));
//			handler_.dropDualWall(colToDrop);
//			return;
//		}

		// If cant drop a dual, drop single.
		for (int col = 0; col < NUMBER_OF_COLUMNS; col++) {
			if (floors_.getColumnFloor(col) < maxRow) {
				candidates.add(col);
			}
		}
		for (Wall wall : activeWalls) {
			candidates.remove(new Integer(wall.col()));
			if (wall.isDual()) {
				candidates.remove(new Integer(wall.col() + 1));
			}
		}
		if (!candidates.isEmpty()) {
			int colToDrop = candidates.get(rand.nextInt(candidates.size()));
			handler_.dropWall(colToDrop);
			return;
		}
	}

	private void addAction(SpawnerAction action, boolean flipped) {
        if (addBossAction(action)) {
            return;
        }

		if (action.type == Type.BALANCE_FLOOR) {
			dropWallForBalance();
		}

		if (action.type.random) {
			List<Integer> candidates = getWallCandidates();
			if (action.type == SpawnerAction.Type.WALL_OR_DUAL) {
				newDropWallOrDual(candidates);
				return;
			}
			if (candidates.isEmpty()) {
				queue.addEventFromNow(getTimeBetweenWalls(), NOP_NO_SEQUENCE_COOLDOWN);
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
                queue.addEventFromNow(action.time + 2.3f, NOP_NO_SEQUENCE_COOLDOWN);
				break;
			case SINGLE_FLAME:
				queue.addEventFromNow(action.time, dropFlamethrowerEvent(col));
                queue.addEventFromNow(action.time + 2, NOP_NO_SEQUENCE_COOLDOWN);
				break;
			default:
				break;
			}
			return;
		}
		
		int col = flipped ? 5 - action.col : action.col;
		if (!SpawnerAction.isLavaType(action.type) && !action.type.random
				&& action.type != SpawnerAction.Type.NOP
				&& action.type != SpawnerAction.Type.FIREBALL
				&& action.type != SpawnerAction.Type.BALANCE_FLOOR
                && action.type != SpawnerAction.Type.COIN) {
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
		case STACK:
			assert(action.size > 0);
			queue.addEventFromNow(action.time, dropStackWallEvent(col, action.size));
			break;
		case WALL:
			queue.addEventFromNow(action.time, dropWallEvent(col));
			break;
		case LAVA_NONE:
		case LAVA_HARMLESS:
		case LAVA_LOW:
		case LAVA_MEDIUM:
		case LAVA_HIGH:
			queue.addEventFromNow(action.time, lavaEvent(lavaState(action.type)));
			break;
		case COIN:
			queue.addEventFromNow(action.time, dropCoinEvent(col, action.coin_type));
		default:
			break;
		}
	}

    private boolean addBossAction(SpawnerAction action) {
        if (action.type == Type.BOSS_LAST_THOMP) {
            queue.addEventFromNow(action.time, new EventQueue.Event() {
                @Override
                public void invoke() {
                    handler_.bossLastThomp();
                }
            });
            return true;
        }
        if (action.type == Type.BOSS_FOLLOW_PLAYER_THOMP) {
            queue.addEventFromNow(action.time, new EventQueue.Event() {
                @Override
                public void invoke() {
                    handler_.bossFollowPlayerThomp();
                }
            });
            return true;
        }
        return false;
    }

    private State lavaState(Type type) {
		switch(type) {
		case LAVA_NONE:
			return Lava.State.NONE;
		case LAVA_HARMLESS:
			return Lava.State.HARMLESS;
		case LAVA_LOW:
			return Lava.State.LOW;
		case LAVA_MEDIUM:
			return Lava.State.MEDIUM;
		case LAVA_HIGH:
			return Lava.State.HIGH;
		default:
			Gdx.app.error("ERROR", "Invalid lava state.");
			return null;
		}
	}

	private boolean getWithSound(float time) {
		float diff = (timeCount + time) - lastWarningSoundTime;
		boolean $ = (timeCount + time) - lastWarningSoundTime > SOUND_COOLDOWN_TIME;
		if ($) {
			lastWarningSoundTime = timeCount + time;
		}
		return $;
	}
	
	private void makeWarning(float time, int col) {
		queue.addEventFromNow(time - 1, warningEvent(col, getWithSound(time)));
	}

	public void setTimeCount(float time) {
		timeCount = time;
	}
}


