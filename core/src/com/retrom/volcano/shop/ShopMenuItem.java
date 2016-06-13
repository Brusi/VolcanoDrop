package com.retrom.volcano.shop;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.data.CostumeShopEntry;
import com.retrom.volcano.data.IncShopEntry;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.data.ShopEntry;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.menus.MenuButton;

public class ShopMenuItem extends MenuButton {
	
	public static final float ITEM_HEIGHT = 100;
	
	private enum State {
		CAN_BUY,
		CANT_BUY,
		BUYING,
		OWN;
	}
	
	boolean active;
	
	State state = State.CANT_BUY;
	float stateTime;
	float alpha = 1;
	
	private static final float baseY = 290;
	
	private static final float iconWidth = 190;
	
	private static final float RECT_HEIGHT = 100;
	
	private float scrollY = 0;
	
	private final int indexInMenu_;
	private final Sprite icon;
	private final Sprite title;
	private final ShopEntry entry;

	public ShopMenuItem(int indexInMenu, Sprite icon, Sprite title, ShopEntry entry, MenuButton.Action action) {
		this(indexInMenu, icon, title, entry, action, false);
	}
	
	public ShopMenuItem(int indexInMenu, Sprite icon, Sprite title, ShopEntry entry, MenuButton.Action action, boolean costume) {
		super(new Rectangle(iconWidth - RECT_HEIGHT / 2, 0, RECT_HEIGHT, RECT_HEIGHT), action);
		this.indexInMenu_ = indexInMenu;
		this.icon = icon;
		this.title = title;
		this.entry = entry;
		
		initState();
	}
	
	public void initState() {
		if (entry.isOwn()) {
			state = State.OWN;
			return;
		}
		if (ShopData.getGold() < entry.getPrice()) {
			state = State.CANT_BUY;
		} else {
			state = State.CAN_BUY;
		}
	}
	
	public void buy() {
		state = State.BUYING;
		stateTime = 0;
	}
	
	public float getY() {
		return baseY + scrollY - indexInMenu_ * ITEM_HEIGHT;
	}

	public void update(float deltaTime) {
		if (state == State.CAN_BUY || (entry instanceof CostumeShopEntry)
				&& !((CostumeShopEntry) entry).isEquipped()) {
			enable();
		} else {
			disable();
		}
		
		stateTime += deltaTime;
		rect.y = getY() - RECT_HEIGHT / 2;
		
		if (state == State.BUYING && stateTime >= 1) {
			state = State.OWN;
		}
		
		if (checkClick()) {
			if (state == State.CAN_BUY) {
				buy();
			} else if (state == State.OWN && entry instanceof CostumeShopEntry){
				CostumeShopEntry cse = (CostumeShopEntry)entry;
				ShopData.equipCostume(cse);
			}
		}
	}

	public void setScrollY(float scrollY) {
		this.scrollY = scrollY;
		if (getY() > 380 || getY() < -50) this.hide(); else this.show();
	}
	
	public void render(Batch batch) {
		if (!isVisible()) {
			return;
		}
		float y = getY();
		icon.setY(y - icon.getHeight() / 2);
		icon.setX(-iconWidth - icon.getWidth() / 2);
		icon.setAlpha(alpha_);
		icon.draw(batch);
		
		title.setY(y - title.getHeight() / 2);
		title.setX(0 - 160);
		title.setAlpha(alpha_);
		title.draw(batch);
		
		if (clickable()) {
			Sprite bg = Assets.shopItemButtonBg;
			bg.setY(y - bg.getHeight() / 2);
			bg.setX(iconWidth - bg.getWidth() / 2);
			bg.setAlpha(alpha_);
			bg.draw(batch);
		} else if (state == State.BUYING) {
			Sprite bg = WorldRenderer.getFrameStopAtLastFrame(Assets.shopItemButtonBuy, stateTime);
			bg.setY(y - bg.getHeight() / 2);
			bg.setX(iconWidth - bg.getWidth() / 2);
			bg.setAlpha(alpha_);
			bg.draw(batch);
		}
		
		if (state == State.CAN_BUY || state == State.CANT_BUY) {
			if (entry.getPrice() > 0) {
				Sprite price = Assets.prices.get(entry.getPrice());
				price.setY(y - 27);
				price.setX(iconWidth - price.getWidth() / 2 - 7);
				price.setAlpha(alpha_);
				price.draw(batch);
			}
		}
		
		{
			Sprite sack = Assets.shopItemButtonGoldSack;
			sack.setY(y - 5);
			sack.setX(iconWidth - sack.getWidth() / 2 + 5);
			sack.setAlpha(alpha_);
			sack.draw(batch);
		}
		if (state == State.BUYING || state == State.OWN) {
			if (entry instanceof CostumeShopEntry) {
				CostumeShopEntry cse = (CostumeShopEntry)entry;
				if (!cse.isEquipped()) {
					Sprite equip = Assets.shopItemButtonEquip;
					equip.setY(y - equip.getHeight() / 2 - 6);
					equip.setX(iconWidth - equip.getWidth() / 2);
					equip.setAlpha(alpha_);
					equip.draw(batch);
				} else {
					Sprite equiped = Assets.shopItemButtonEquipped;
					equiped.setY(y - equiped.getHeight() / 2 - 4);
					equiped.setX(iconWidth - equiped.getWidth() / 2 + 7);
					equiped.setAlpha(alpha_);
					equiped.draw(batch);
				}
			} else {
				Sprite own = Assets.shopItemButtonOwn;
				own.setY(y - own.getHeight() / 2);
				own.setX(iconWidth - own.getWidth() / 2);
				own.setAlpha(alpha_);
				own.draw(batch);
			}
		}
		if (isPressed()) {
			Utils.drawCenter(batch, Assets.shopItemButtonClicked, iconWidth, y);
		}
		if (entry instanceof IncShopEntry) {
			IncShopEntry ise = (IncShopEntry)entry;
			int level = ise.getLevel();
			if (level > 0) {
				Sprite s = Assets.shopBlessingsLevelNumber.get(level - 1);
				Utils.drawCenter(batch, s, -223, y-18);  // TODO: place correctly on screen.
			}
		}
	}

	// Whether an item button can be clicked (can be bought or equipped).
	private boolean clickable() {
		return state == State.CAN_BUY || 
    	   (entry instanceof CostumeShopEntry && !((CostumeShopEntry)entry).isEquipped());
	}

	public void updateState() {
		if (state == State.BUYING) {
			return;
		}
		initState();
	}
}
