package com.retrom.volcano.data;

import com.badlogic.gdx.Preferences;

// Shop entries for items you can buy and upgrade several times.
public class IncShopEntry extends ShopEntry {
	final public int[] prices;
	private final int max_level;
	private int level = 0;
	
	public IncShopEntry(String name, int[] prices) {
		super(name);
		this.prices = prices;
		this.max_level = prices.length;
	}
	
	@Override
	public void save(Preferences prefs) {
		prefs.putInteger(name, level);
	}
	
	@Override
	public void load(Preferences prefs) {
		level = prefs.getInteger(name, 0);
	}

	@Override
	public void buy(Preferences prefs) {
		level++;
		save(prefs);
	}

	@Override
	public int getPrice() {
		if (isOwn()) {
			// The current price of something owned is irrelevant.
			return 0;
		}
		// Returns the price of the next available 
		return prices[level];
	}

	@Override
	public boolean isOwn() {
		// Decision: to return isOwn only if all levels are owned.
		// The rational behind it is that something owned cannot be furtherly baught.
		return level == max_level;
	}

	public int getLevel() {
		return level;
	}
}
