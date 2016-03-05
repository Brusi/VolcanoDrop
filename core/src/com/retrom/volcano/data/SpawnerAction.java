package com.retrom.volcano.data;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.utils.Json;

public class SpawnerAction {
	
	public enum Type {
		// Actions which are part of a sequence:
		WALL,
		BURN,
		FLAME,
		FIREBALL,
		NOP,
		// Actions which are not of a sequence:
		WALL_NOT_DUAL(true),
		WALL_OR_DUAL(true),
		SINGLE_BURN(true),
		SINGLE_FLAME(true),
		SINGLE_FIREBALL(true);
		
		public final boolean random;
		
		Type(boolean random) {
			this.random = random; 
		}
		Type() {
			this(false);
		}
	}
	
	public Type type;
	public float time;
	public int col;
	
	public SpawnerAction() {}
	public SpawnerAction(Type type, float time, int col) {
		this.type = type;
		this.time = time;
		this.col = col;
	}
	
	public static void test() {
		
		Sequence sequence = new Sequence("3walls", new ArrayList<SpawnerAction>(Arrays.asList(
				new SpawnerAction(Type.WALL, 0.1f, 1),
				new SpawnerAction(Type.WALL, 0.2f, 2),
				new SpawnerAction(Type.WALL, 0.3f, 3))));
		
		Json json = new Json();
		String s = json.toJson(sequence);
		String t = "{name:3walls,seq:[{type:WALL,time:0.1,col:1},{type:WALL,time:0.2,col:2},{type:WALL,time:0.3,col:3}]}";
		Sequence seq  = json.fromJson(sequence.getClass(), t);
		System.out.println(json.prettyPrint(t));
		System.out.println("--------");
		System.out.println(json.toJson(seq));
//		System.out.println(seq.toString());
		// TODO Auto-generated method stub
	}
}