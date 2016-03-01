package com.retrom.volcano.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class SequenceLib {
	
	private static final Json json = new Json();
	
	private static final String PATH = "levels/sequences.json";
	
	public List<Sequence> sequences = null;
	
	private Map<String, Sequence> nameMap;
	private Map<String, Sequence> hotkeyMap;
	
	public static SequenceLib loadFromDefault() {
		FileHandle file = Gdx.files.local(PATH);
		if (!file.exists()) {
			Gdx.app.log("INFO", "local file missing; using internal.");
			file = Gdx.files.internal(PATH);
		}
		System.out.println("file="+file);
		System.out.println(file.readString());
		return loadFromFile(file);
	}
	
	public static SequenceLib loadFromFile(FileHandle file) {
		SequenceLib lib = json.fromJson(SequenceLib.class, file);
		lib.index();
		return lib;
	}
	
	public static SequenceLib loadFromString(FileHandle file) {
		SequenceLib lib = json.fromJson(SequenceLib.class, file);
		lib.index();
		return lib;
	}
	
	public String toString() {
		return json.toJson(this);
	}
	
	public void toFile(FileHandle file) {
		json.toJson(this, file);
	}
	
	// Build 
	private void index() {
		nameMap = new HashMap<String, Sequence>();
		hotkeyMap = new HashMap<String, Sequence>();
		for (Sequence seq : sequences) {
			nameMap.put(seq.name, seq);
			hotkeyMap.put(seq.hotkey, seq);
		}
	}
	
	public Sequence getSequence(String name) {
		if (nameMap == null) {
			throw new IllegalStateException("SequenceLib not indexed. call slib.index().");
		}
		Sequence seq = nameMap.get(name);
		if (seq == null) {
			throw new IllegalStateException("No sequence named: " + name);
		}
		return seq;
	}
	
	public Set<String> hotkeys() {
		return hotkeyMap.keySet();
	}
	
	public static void writeExample() {
//		return;
//		Sequence sequence1 = new Sequence("3walls", new ArrayList<SpawnerAction>(Arrays.asList(
//				new SpawnerAction(Type.WALL, 0.1f, 1),
//				new SpawnerAction(Type.WALL, 0.2f, 2),
//				new SpawnerAction(Type.WALL, 0.3f, 3))));
//		
//		Sequence sequence2 = new Sequence("3walls", new ArrayList<SpawnerAction>(Arrays.asList(
//				new SpawnerAction(Type.BURN, 0.1f, 1),
//				new SpawnerAction(Type.WALL, 0.2f, 2),
//				new SpawnerAction(Type.WALL, 0.3f, 3))));
//		
//		SequenceLib slib = new SequenceLib();
//		slib.sequences = new ArrayList<Sequence>();
//		slib.sequences.add(sequence1);
//		slib.sequences.add(sequence2);
//		System.out.println("Writing to: " + PATH);
//		String s = json.prettyPrint(slib);
//		Gdx.files.local(PATH).writeString(s, false);
	}

	public Sequence getByHotkey(String keyString) {
		return hotkeyMap.get(keyString);
	}
}
