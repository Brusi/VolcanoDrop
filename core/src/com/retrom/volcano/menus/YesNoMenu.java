package com.retrom.volcano.menus;

import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Settings;
import com.retrom.volcano.menus.MenuButton.Action;
import com.retrom.volcano.menus.OptionsMenu.Command;

public class YesNoMenu extends Menu {

	private Listener listener_;

	public enum Command {
		YES, NO
	}

	public interface Listener {
		public void act(Command cmd);
	}

	MenuButton.Action commandAction(final Command cmd) {
		return new MenuButton.Action() {
			@Override
			public void act() {
				listener_.act(cmd);
			}
		};
	}

	public YesNoMenu(Listener listener) {
		this.listener_ = listener;
		graphics.add(new StaticGraphicObject(Assets.yesNoMenuBg, -2, 71));
		graphics.add(new StaticGraphicObject(Assets.yesNoMenuTitle, 0, 118));

		buttons.add(new SimpleMenuButton(-95, -15, 140, 100,
				Assets.noButton, Assets.noButtonClicked,
				commandAction(Command.NO)));
		buttons.add(new SimpleMenuButton(92, -15, 140, 100,
				Assets.yesButton, Assets.yesButtonClicked,
				commandAction(Command.YES)));
	}
}
