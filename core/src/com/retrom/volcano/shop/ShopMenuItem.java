package com.retrom.volcano.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.data.ShopEntry;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.menus.MenuButton;
import com.retrom.volcano.utils.TouchToPoint;

public class ShopMenuItem extends MenuButton {
	
	private enum State {
		CAN_BUY,
		CANT_BUY,
		BUYING,
		OWN;
	}
	
	State state = State.CANT_BUY;
	float stateTime;
	
	private static final float baseY = 290;
	private static final float itemHeight = 100;
	
	private static final float iconWidth = 190;
	
	private static final float RECT_HEIGHT = 100;
	
	private float scrollY = 0;
	
	private final int indexInMenu_;
	private final Sprite icon;
	private final Sprite title;
	private final ShopEntry entry;
	
	public ShopMenuItem(int indexInMenu, Sprite icon, Sprite title, ShopEntry entry, MenuButton.Action action) {
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
		if (ShopData.getGold() < entry.price) {
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
		return baseY + scrollY - indexInMenu_ * itemHeight;
	}

	public void update(float deltaTime) {
		stateTime += deltaTime;
		rect.y = getY() - RECT_HEIGHT / 2;
		
		if (state == State.BUYING && stateTime >= 1) {
			state = State.OWN;
		}
		
		if (state == State.CAN_BUY) {
			if (checkClick()) {
				buy();
			}
		}
	}
	
	public void setScrollY(float scrollY) {
		this.scrollY = scrollY;
	}
	
	public void render(Batch batch) {
		float y = getY();
		icon.setY(y - icon.getHeight() / 2);
		icon.setX(-iconWidth - icon.getWidth() / 2);
		icon.draw(batch);
		
		title.setY(y - title.getHeight() / 2);
		title.setX(0 - 160);
		title.draw(batch);
		
		if (state == State.CAN_BUY) {
			Sprite bg = Assets.shopItemButtonBg;
			bg.setY(y - bg.getHeight() / 2);
			bg.setX(iconWidth - bg.getWidth() / 2);
			bg.draw(batch);
		} else if (state == State.BUYING) {
				Sprite bg = WorldRenderer.getFrameStopAtLastFrame(Assets.shopItemButtonBuy, stateTime);
				bg.setY(y - bg.getHeight() / 2);
				bg.setX(iconWidth - bg.getWidth() / 2);
				bg.draw(batch);
		}
		
		if (state != State.BUYING && state != State.OWN) {
			Sprite price = Assets.shopPrice1500;
			price.setY(y - 27);
			price.setX(iconWidth - price.getWidth() / 2 - 7);
			price.draw(batch);
		}
		
		{
			Sprite sack = Assets.shopItemButtonGoldSack;
			sack.setY(y - 5);
			sack.setX(iconWidth - sack.getWidth() / 2 + 5);
			sack.draw(batch);
		}
		if (state == State.BUYING || state == State.OWN) {
			Sprite own = Assets.shopItemButtonOwn;
			own.setY(y - own.getHeight() / 2);
			own.setX(iconWidth - own.getWidth() / 2);
			own.draw(batch);
		}
	}

	public void updateState() {
		if (state == State.BUYING) {
			return;
		}
		initState();
	}
}
