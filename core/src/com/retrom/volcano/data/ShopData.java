package com.retrom.volcano.data;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ShopData {
	
	private static final String BEST_TIME_PREFS_NAME = "best_time";
	private static final String GOLD_PREFS_NAME = "gold"; 
	private static final String PREFS_NAME = "volcanoprefs";
	
	
	public static final List<ShopEntry> allShopEntries = new ArrayList<ShopEntry>();
	public static final List<CostumeShopEntry> costumeEntries = new ArrayList<CostumeShopEntry>();
	
	public static ShopEntry airStep;
	public static ShopEntry wallFoot;
	public static ShopEntry charge;
	public static ShopEntry cheetahr;
	public static ShopEntry frogger;
	public static ShopEntry slowWalker;
	
	public static IncShopEntry airCharm;
//	public static IncShopEntry boulderFist;
//	public static IncShopEntry goldDigger;
//	public static IncShopEntry hazeBless;
//	public static IncShopEntry mightBless;
//	public static IncShopEntry reborn;
//	public static IncShopEntry templeHopper;
//	public static IncShopEntry timeBender;
//	public static IncShopEntry treasureRain;
//	public static IncShopEntry windBless;
	
	public static CostumeShopEntry defaultCostume;
	public static CostumeShopEntry goboCostume;
	public static CostumeShopEntry blikCostume;
	
	
	private static final Preferences prefs = Gdx.app
			.getPreferences(PREFS_NAME);
	
	public static void init() {
		// Powers
		airStep = createOwn("powers_airstep", 1500);
		wallFoot = createOwn("powers_wallfoot", 3000);
		charge = createOwn("powers_charge", 4500);
		cheetahr = createOwn("powers_cheetahr", 5000);
		frogger = createOwn("powers_frogger", 1000);
		slowWalker = createOwn("powers_slow_walker", 6000);
		
		// Blessings
		airCharm = createInc("air_charm", new int[] { 1000, 1500, 2000 });
		
		// Costumes
		defaultCostume = createCostume("costumes_default", 0, true);
		goboCostume = createCostume("costumes_gobo", 1000, false);
		blikCostume = createCostume("costumes_blik", 2000, false);
		
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
	
	public static void equipCostume(CostumeShopEntry entry) {
		for (CostumeShopEntry e : costumeEntries) {
			e.unEquip();
			e.save(prefs);
		}
		entry.equip();
		entry.save(prefs);
		prefs.flush();
		
	}
	
	private static ShopEntry createOwn(String name, int price) {
		ShopEntry entry = new OwnShopEntry(name, price);
		allShopEntries.add(entry);
		return entry;
	}
	
	private static IncShopEntry createInc(String name, int[] prices) {
		IncShopEntry entry = new IncShopEntry(name, prices);
		allShopEntries.add(entry);
		return entry;
	}
	
	private static ShopEntry createCostume(String name, int price) {
		return createCostume(name, price, false);
	}
	
	private static CostumeShopEntry createCostume(String name, int price, boolean defaultCostume) {
		CostumeShopEntry entry = new CostumeShopEntry(name, price, defaultCostume);
		allShopEntries.add(entry);
		costumeEntries.add(entry);
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
	
	// Returns whether the best time has been updated.
	public static boolean updateBestTime(float time) {
		int new_time = (int) Math.floor(time);
		int best_time = getBestTime();
		if (new_time > best_time) {
			prefs.putInteger(BEST_TIME_PREFS_NAME, new_time);
			prefs.flush();
			return true;
		}
		return false;
	}
	
	public static int getBestTime() {
		return prefs.getInteger(BEST_TIME_PREFS_NAME, 0);
	}
}
