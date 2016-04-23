package com.retrom.volcano.menus;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Settings;
import com.retrom.volcano.game.Settings.Option;
import com.retrom.volcano.game.Utils;

public class OnOffButton extends MenuButton {
	
	private final Option option;

	public OnOffButton(float x, float y, float width, float height,
			final Settings.Option option) {
		super(new Rectangle(x - width / 2, y - height / 2, width, height),
				new MenuButton.Action() {
			@Override
			public void act() {
				option.toggle();
			}
		});
		this.option = option;
	}
	
	@Override
	public void render(Batch batch) {
		if (!isVisible()) {
			return;
		}
		Sprite s = option.on() ? Assets.optionsMenuOn : Assets.optionsMenuOff;
		s.setAlpha(alpha_);
		Utils.drawCenter(batch, s, getX(), getY());
	}
}
