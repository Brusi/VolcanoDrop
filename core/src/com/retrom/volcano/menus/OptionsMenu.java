package com.retrom.volcano.menus;

import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Settings;

public class OptionsMenu extends Menu {
	
	public enum Command {
		BACK, SHOP,
	}
	
	public interface Listener {
		public void act(Command cmd);
	}
	
	private final Listener listener_;
	
	MenuButton.Action commandAction(final Command cmd) {
		return new MenuButton.Action() {
			@Override
			public void act() {
				listener_.act(cmd);
			}
		};
	}
	
	public OptionsMenu(final Listener listener) {
		this.listener_ = listener;
		graphics.add(new StaticGraphicObject(Assets.optionsMenuBG, 4, 97));
		graphics.add(new StaticGraphicObject(Assets.pauseMenuTitle, 0, 266));
		
		buttons.add(new SimpleMenuButton(-223, 330, 140, 140,
				Assets.optionsMenuBack, Assets.optionsMenuBackClicked,
				commandAction(Command.BACK)));
		buttons.add(new SimpleMenuButton(223, 330, 140, 140,
				Assets.pauseShopButton, Assets.pauseShopButtonClicked,
				commandAction(Command.SHOP)));
		
		buttons.add(new OnOffButton(-144, -7, 100, 100, Settings.soundEnabled));
		buttons.add(new OnOffButton(0, -7, 100, 100, Settings.vibrationEnabled));
	}
}
