package com.retrom.volcano.data;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ShopData {
	
	private static final String GOLD_PREFS_NAME = "gold"; 
	private static final String PREFS_NAME = "volcanoprefs";
	
	
	public static final List<ShopEntry> allShopEntries = new ArrayList<ShopEntry>();
	
	public static ShopEntry airStep;
	public static ShopEntry wallFoot;
	
	private static final Preferences prefs = Gdx.app
			.getPreferences(PREFS_NAME);
	
	public static void init() {
		airStep = createAndRegister("powers_airstep", 1500);
		wallFoot = createAndRegister("powers_wallfoot", 1500);
		
		load();
	}
	
	public static void save() {
		for (ShopEntry entry : allShopEntries) {
			entry.save(prefs);
		}
		prefs.flush();
	}
	
	public static void load() {
		for (ShopEntry entry : allShopEntries) {
			entry.load(prefs);
		}
	}
	
	public static void reset() {
		prefs.clear();
		prefs.flush();
		Gdx.app.log("INFO", "Resetting preferences.");
		load();
	}
	
	public static void buyFromShop(ShopEntry entry) {
		entry.buy(prefs);
		prefs.flush();
	}
	
	private static ShopEntry createAndRegister(String name, int price) {
		ShopEntry entry = new ShopEntry(name, price);
		allShopEntries.add(entry);
		return entry;
	}
	
	public static int getGold() {
		return prefs.getInteger(GOLD_PREFS_NAME, 0);
	}
	
	public static void setGold(int value) {
		prefs.putInteger(GOLD_PREFS_NAME, value);
		prefs.flush();
	}

	public static void addGold(int amount) {
		int gold = getGold();
		gold += amount;
		setGold(gold);
	}
	
	public static void reduceGold(int amount) {
		int gold = getGold();
		gold -= amount;
		setGold(gold);
	}

}
