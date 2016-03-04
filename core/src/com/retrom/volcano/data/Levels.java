package com.retrom.volcano.data;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

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
	}
	
	static public class LevelDefinition {
		// Starting time of the level;
		public float start_time;
		public float time_between_walls;
		// List of level sequence groups and their probabilities.
		public List<ProbabilityGroup> groups;
		
		public WeightedRandom<ProbabilityGroup> wr;
		public WeightedRandom<ProbabilityGroup> nswr;
		
		public void index() {
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
	}
	
	public List<LevelDefinition> levels;
	
	public static Levels loadFromDefault() {
		FileHandle file = Gdx.files.local(PATH);
		if (!file.exists()) {
			Gdx.app.log("INFO", "local file missing; using internal.");
			file = Gdx.files.internal(PATH);
		}
		System.out.println("file="+file);
		System.out.println(file.readString());
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
		
		System.out.println("Writing to: " + PATH);
		String s = json.prettyPrint(levels);
		Gdx.files.local(PATH).writeString(s, false);
	}
	
	public static void readExample() {
		Levels levels = json.fromJson(Levels.class, Gdx.files.internal(PATH));
		System.out.println("read:");
		System.out.println(json.prettyPrint(levels));
	}
}
