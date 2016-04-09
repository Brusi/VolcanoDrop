package com.retrom.volcano.data;

import com.badlogic.gdx.Preferences;

public abstract class ShopEntry {
	final public String name;
	
	
	public ShopEntry(String name) {
		this.name = name;
	}
	
	abstract public void save(Preferences prefs);
	abstract public void load(Preferences prefs);
	abstract public void buy(Preferences prefs);
	
	abstract public int getPrice();
	abstract public boolean isOwn();
}