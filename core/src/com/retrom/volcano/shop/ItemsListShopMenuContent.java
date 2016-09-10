package com.retrom.volcano.shop;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.data.ShopEntry;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.menus.BackMenuButton;
import com.retrom.volcano.menus.ExitMenuButton;
import com.retrom.volcano.menus.MenuButton;
import com.retrom.volcano.menus.MenuButton.Action;

public abstract class ItemsListShopMenuContent implements ShopMenuContent {
	
	private ShopMenu.Listener listener_;



    ItemsListShopMenuContent(ShopMenu.Listener listener) {
        this.listener_ = listener;
        initItems();
        refresh();
    }
	
	abstract protected void initItems();	
	
	List<ShopMenuItem> items = new ArrayList<ShopMenuItem>();
    private int scrollCount = 0;
	private float scrollY = 0;
	private float tgtY = 0;
	private int runningIndex = 0;
	private float alpha_ = 1;

    @Override
	public void update(float deltaTime) {
		for (ShopMenuItem item : items) {
			item.update(deltaTime);
		}

        tgtY = scrollCount * ShopMenuItem.ITEM_HEIGHT;
		float vel = (tgtY - scrollY) * 10f;
		scrollY += vel * deltaTime;
	}

	@Override
	public void render(SpriteBatch batch) {
		for (ShopMenuItem item : items) {
			item.setScrollY(scrollY);
			item.setAlpha(alpha_);
			item.render(batch);
		}
	}

	@Override
	public void refresh() {
		for (ShopMenuItem item : items) {
			item.initState();
		}
        scrollCount = 0;
	}
	
	protected void addMenuItem(Sprite icon, Sprite title, final ShopEntry entry) {
		Action action = new Action() {
			@Override
			public void act() {
				// TODO: move action to insude item. No need to pass it from outside mostly.
				if (!entry.isOwn()) {
					ShopData.reduceGold(entry.getPrice());
					ShopData.buyFromShop(entry);
				}
				
				listener_.act(ShopMenu.Command.BUY);
				for (ShopMenuItem item : items) {
					item.updateState();
				}
			}
		};
		items.add(new ShopMenuItem(runningIndex++, icon, title, entry, action));
	}
	
	@Override
	public void setAlpha(float alpha) {
		alpha_  = alpha;
	}

    public void scrollUp() {
        scrollCount--;
        scrollCount = Utils.clamp(scrollCount, 0, items.size() - 3);
    }
    public void scrollDown() {
        scrollCount++;
        scrollCount = Utils.clamp(scrollCount, 0, items.size() - 3);
    }

    public boolean canScrollUp() {
        return scrollCount > 0;
    }

    public boolean canScrollDown() {
        return scrollCount < items.size() - 3;
    }
}
