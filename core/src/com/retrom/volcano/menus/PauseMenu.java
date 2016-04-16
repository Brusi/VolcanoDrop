package com.retrom.volcano.menus;

import com.badlogic.gdx.Gdx;
import com.retrom.volcano.assets.Assets;

public class PauseMenu extends Menu {
	
	public enum Command {
		RESUME, RESTART, SHOP,
	}
	
	public interface Listener {
		public void act(Command cmd);
	}
	
	
	private final Listener listener_;
	
	// TODO: remove when no longer needed.
	static private final MenuButton.Action NOT_IMPLEMENTED = new MenuButton.Action() {
		@Override
		public void act() {
			Gdx.app.error("ERROR", "Menu action not implemented.");
		}
	};
	
	MenuButton.Action commandAction(final Command cmd) {
		return new MenuButton.Action() {
			@Override
			public void act() {
				listener_.act(cmd);
			}
		};
	}
	
	public PauseMenu(final Listener listener) {
		this.listener_ = listener;
		graphics.add(new StaticGraphicObject(Assets.pauseMenuBG, 0, 97));
		graphics.add(new StaticGraphicObject(Assets.pauseMenuTitle, 0, 266));
		
		buttons.add(new SimpleMenuButton(-223, 330, 140, 140,
				Assets.pauseOptionsButton, Assets.pauseOptionsButtonClicked,
				NOT_IMPLEMENTED));
		buttons.add(new SimpleMenuButton(223, 330, 140, 140,
				Assets.pauseShopButton, Assets.pauseShopButtonClicked,
				commandAction(Command.SHOP)));
		
		buttons.add(new SimpleMenuButton(0, 127, 230, 100,
				Assets.pauseResumeButton, Assets.pauseResumeButtonClicked,
				commandAction(Command.RESUME)));
		
		buttons.add(new SimpleMenuButton(0, -9, 230, 100,
				Assets.pauseRetryButton, Assets.pauseRetryButtonClicked,
				commandAction(Command.RESTART)));
	}
}
