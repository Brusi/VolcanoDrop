package com.retrom.volcano.data;

import java.util.List;

public class Sequence {
	public List<SpawnerAction> seq;
	public String name;
	public String hotkey;
	
	public Sequence() {}
	public Sequence(String name, List<SpawnerAction> seq) {
		this.name = name;
		this.seq = seq;
	}
}
