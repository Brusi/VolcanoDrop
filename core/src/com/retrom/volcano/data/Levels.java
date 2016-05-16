package com.retrom.volcano.data;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.retrom.volcano.game.objects.Collectable;
import com.retrom.volcano.utils.WeightedRandom;

public class Levels {
	private static final String PATH = "levels/levels.json";
	private static final Json json = new Json();
	
	// A group of sequences and their probability in a level.
	static public class ProbabilityGroup {
		public float chance;
		public List<String> sequences;
		// Whether this is a single wall and should:
		// 1. Not be silenced in sequence silence (after a sequence).
		// 2. Have the level-specific cooldown time.
		public boolean single;
		
		// Play this group on the start of the level.
		public boolean on_level_start;
	}
	
	static public class PowerupChance {
		float chance;
		Collectable.Type powerup; 
	}
	
	static public class CoinChance {
		float chance;
		Collectable.BaseType coin; 
	}
	
	static public class LevelDefinition {
		// Starting time of the level;
		public int number;
		public float start_time;
		public float time_between_walls;
		public float avg_sack_time;
		// List of level sequence groups and their probabilities.
		public List<ProbabilityGroup> groups;
		public ProbabilityGroup level_start_group;
		
		public List<PowerupChance> powerups;
		public List<CoinChance> coins;
		
		// Auto generated.
		public WeightedRandom<ProbabilityGroup> wr;
		public WeightedRandom<ProbabilityGroup> nswr;
		
		public WeightedRandom<Collectable.Type> powerup_wr;
		public WeightedRandom<Collectable.BaseType> coins_wr;
		
		public void index() {
			indexPowerups();
			indexCoins();
			
			for (ProbabilityGroup pg : groups) {
				if (pg.on_level_start) {
					level_start_group = pg;
				}
			}
			
			{
				WeightedRandom.Builder<ProbabilityGroup> builder = new WeightedRandom.Builder<ProbabilityGroup>();
				for (ProbabilityGroup pg : groups) {
					builder.add(pg.chance, pg);
				}
				wr = builder.build();
			}
			
			float non_sequence_total_prob = 0;
			for (ProbabilityGroup pg : groups) {
				if (pg.single) {
					non_sequence_total_prob += pg.chance;
				}
			}
			
			if (non_sequence_total_prob != 0) {
				WeightedRandom.Builder<ProbabilityGroup> builder = new WeightedRandom.Builder<ProbabilityGroup>();
				for (ProbabilityGroup pg : groups) {
					if (pg.single) {
						builder.add(pg.chance / non_sequence_total_prob, pg);
					}
				}				
				nswr = builder.build();
			}
		}

		private void indexCoins() {
			WeightedRandom.Builder<Collectable.BaseType> builder = new WeightedRandom.Builder<Collectable.BaseType>();
			if (coins == null) {
				// If not defined, no powerups at all! :(
				coins_wr = builder.add(1, null).build();
				return;
			}
			for (CoinChance cc : coins) {
				builder.add(cc.chance, cc.coin);
			}
			coins_wr = builder.build();
		}

		private void indexPowerups() {
			WeightedRandom.Builder<Collectable.Type> builder = new WeightedRandom.Builder<Collectable.Type>();
			if (powerups == null) {
				// If not defined, no powerups at all! :(
				powerup_wr = builder.add(1, null).build();
				return;
			}
			float total_chance = 0;
			for (PowerupChance pc : powerups) {
				total_chance += pc.chance;
				builder.add(pc.chance, pc.powerup);
			}
			if (total_chance > 1) {
				throw new RuntimeException("Powerup chances exceeds 1 !");
			}
			// Add null as default value.
			builder.add(1 - total_chance, null);
			
			powerup_wr = builder.build();
		}
	}
	
	public List<LevelDefinition> levels;
	public float start_time = 0;
	
	public static Levels loadFromDefault() {
		FileHandle file = Gdx.files.local(PATH);
		if (!file.exists()) {
			Gdx.app.log("INFO", "local file missing; using internal.");
			file = Gdx.files.internal(PATH);
		}
		return loadFromFile(file);
	}
	
	public static Levels loadFromFile(FileHandle file) {
		Levels levels = json.fromJson(Levels.class, file);
		levels.index();
		return levels;
	}
	
	private void index() {
		for (LevelDefinition ld : levels) {
			ld.index();
		}
	}

	public static void writeExample() {
		Levels levels = new Levels();
		levels.levels = new ArrayList<LevelDefinition>();
		
		{
			LevelDefinition ld = new LevelDefinition();
			ld.start_time = 7;
			ld.groups = new ArrayList<ProbabilityGroup>();
			
			ProbabilityGroup pg1 = new ProbabilityGroup();
			pg1.chance = 0.3f;
			pg1.sequences = new ArrayList<String>();
			pg1.sequences.add("a");
			pg1.sequences.add("b");
			
			ProbabilityGroup pg2 = new ProbabilityGroup();
			pg2.chance = 0.7f;
			pg2.sequences = new ArrayList<String>();
			pg2.sequences.add("c");
			pg2.sequences.add("d");
			pg2.sequences.add("e");
			
			ld.groups.add(pg1);
			ld.groups.add(pg2);
			
			levels.levels.add(ld);
		}
		{
			LevelDefinition ld = new LevelDefinition();
			ld.start_time = 21;
			ld.groups = new ArrayList<ProbabilityGroup>();
			
			ProbabilityGroup pg1 = new ProbabilityGroup();
			pg1.chance = 0.4f;
			pg1.sequences = new ArrayList<String>();
			pg1.sequences.add("f");
			pg1.sequences.add("g");
			pg1.sequences.add("h");
			
			ProbabilityGroup pg2 = new ProbabilityGroup();
			pg2.chance = 0.6f;
			pg2.sequences = new ArrayList<String>();
			pg2.sequences.add("i");
			pg2.sequences.add("j");
			
			ld.groups.add(pg1);
			ld.groups.add(pg2);
			
			levels.levels.add(ld);
		}
		
		String s = json.prettyPrint(levels);
		Gdx.files.local(PATH).writeString(s, false);
	}
	
	public static void readExample() {
		Levels levels = json.fromJson(Levels.class, Gdx.files.internal(PATH));
		System.out.println("read:");
		System.out.println(json.prettyPrint(levels));
	}
}
