package com.retrom.volcano.shop;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.menus.MenuButton;

public class MainShopMenuButton extends MenuButton {
	
	private static final float WIDTH = 455;
	private static final float HEIGHT = 100;
	
	private Sprite sprite;
	public MainShopMenuButton(float y, Action action, Sprite sprite) {
		super(makeRect(y), action);
		this.sprite = sprite;
	}
	
	@Override
	public void render(Batch batch) {
		Sprite bgSprite = Assets.mainShopBg;
		if (isPressed()) {
			bgSprite.setScale(0.9f);
			sprite.setScale(0.95f);
		} else {
			bgSprite.setScale(1f);
			sprite.setScale(1f);
		}
		
		bgSprite.setAlpha(alpha_);
		sprite.setAlpha(alpha_);
		
		Utils.drawCenter(batch, bgSprite, 0, rect.y + rect.height / 2);
		Utils.drawCenter(batch, sprite,
				rect.x + rect.width / 2,
				rect.y + rect.height / 2);
	}
	
	private static Rectangle makeRect(float y) {
		Rectangle r = new Rectangle();
		r.width = WIDTH;
		r.height=HEIGHT;
		r.x = -WIDTH/2;
		r.y = y-HEIGHT/2;
		return r;
	}

}
