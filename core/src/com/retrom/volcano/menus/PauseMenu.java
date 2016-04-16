package com.retrom.volcano.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.menus.OptionsMenu.Command;

public class PauseMenu extends Menu {
	
	public enum Command {
		RESUME, RESTART, SHOP,
	}
	
	public interface Listener {
		public void act(Command cmd);
	}
	
	
	private final Listener listener_;

	private boolean optionsOn;
	private final OptionsMenu optionsMenu = new OptionsMenu(new OptionsMenu.Listener() {
		@Override
		public void act(OptionsMenu.Command cmd) {
			switch (cmd) {
			case BACK:
				optionsOn = false;
				break;
			case SHOP:
				optionsOn = false;
				listener_.act(Command.SHOP);
				break;
			default:
				Gdx.app.error("ERROR", "Unknown options menu command.");
				break;
			}
		}
	});
	
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
		graphics.add(new StaticGraphicObject(Assets.pauseMenuBG, 4, 97));
		graphics.add(new StaticGraphicObject(Assets.pauseMenuTitle, 0, 266));
		
		buttons.add(new SimpleMenuButton(-223, 330, 140, 140,
				Assets.pauseOptionsButton, Assets.pauseOptionsButtonClicked,
				new MenuButton.Action() {
					@Override
					public void act() {
						optionsOn = true;
					}
				}));
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
	
	@Override
	public void render(SpriteBatch batch) {
		if (!optionsOn) {
			super.render(batch);
			return;
		}
		optionsMenu.render(batch);
	}
	
	@Override
	public void update(float deltaTime) {
		if (!optionsOn) {
			super.update(deltaTime);
			return;
		}
		optionsMenu.update(deltaTime);
	}
}
