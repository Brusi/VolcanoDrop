package com.retrom.volcano.data;

import com.badlogic.gdx.Preferences;

public class ShopEntry {
	private boolean own = false;
	final public String name;
	final public int price;
	
	public ShopEntry(String name, int price) {
		this.name = name;
		this.price = price;
	}
	
	public boolean isOwn() {
		return own;
	}
	
	public void save(Preferences prefs) {
		prefs.putBoolean(name, own);
	}
	
	public void load(Preferences prefs) {
		own = prefs.getBoolean(name, false);
	}

	void buy(Preferences prefs) {
		own = true;
		prefs.putBoolean(name, own);
	}
}