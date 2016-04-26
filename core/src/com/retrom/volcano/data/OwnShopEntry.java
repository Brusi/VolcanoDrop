package com.retrom.volcano.data;

import com.badlogic.gdx.Preferences;

public class OwnShopEntry extends ShopEntry {
	final public int price;
	protected boolean own = false;
	
	public OwnShopEntry(String name, int price) {
		super(name);
		this.price = price;
	}
	
	@Override
	public void save(Preferences prefs) {
		prefs.putBoolean(name, own);
	}
	
	@Override
	public void load(Preferences prefs) {
		own = prefs.getBoolean(name, false);
	}

	@Override
	public void buy(Preferences prefs) {
		own = true;
		save(prefs);
	}

	@Override
	public int getPrice() {
		return price;
	}

	@Override
	public boolean isOwn() {
		return own;
	}
}
