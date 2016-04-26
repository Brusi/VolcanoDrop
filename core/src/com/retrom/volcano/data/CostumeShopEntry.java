package com.retrom.volcano.data;

import com.badlogic.gdx.Preferences;

public class CostumeShopEntry extends OwnShopEntry {
	
	private boolean equipped;
	public final boolean defaultCostume;

	public CostumeShopEntry(String name, int price, boolean defaultCostume) {
		super(name, price);
		this.defaultCostume = defaultCostume;
	}
	
	public void equip() {
		equipped = true;
	}
	
	public void unEquip() {
		equipped = false;
	}
	
	public boolean isEquipped() {
		return equipped;
	}
	
	@Override
	public void save(Preferences prefs) {
		prefs.putBoolean(equippedKey(), equipped);
		super.save(prefs);
	}
	
	@Override
	public void load(Preferences prefs) {
		equipped = prefs.getBoolean(equippedKey(), defaultCostume);
		super.load(prefs);
		if (!isOwn() && defaultCostume) {
			own = true;
		}
	}
	
	private String equippedKey() {
		return name + "_equipped"; 
	}
}
