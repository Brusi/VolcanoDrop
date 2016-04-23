package com.retrom.volcano.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

	private boolean optionsOn = false;
	private final OptionsMenu optionsMenu = new OptionsMenu(new OptionsMenu.Listener() {
		@Override
		public void act(OptionsMenu.Command cmd) {
			switch (cmd) {
			case BACK:
				optionsOn = false;
				break;
			case SHOP:
//				optionsOn = false;
				shopOn = true;
				break;
			default:
				Gdx.app.error("ERROR", "Unknown options menu command.");
				break;
			}
		}
	});
	
	private YesNoMenu makeYesNoMenu(final Command command) {
		return new YesNoMenu(new YesNoMenu.Listener() {
			@Override
			public void act(YesNoMenu.Command cmd) {
				restartOn = false;
				shopOn = false;
				switch (cmd) {
				case NO:
					// Do nothing.
					break;
				case YES:
					listener_.act(command);
					break;
				}
			}
		});
	}
	
	private boolean restartOn = false;
	private YesNoMenu restartGameMenu = makeYesNoMenu(Command.RESTART);
	
	private boolean shopOn = false;
	private YesNoMenu shopMenu = makeYesNoMenu(Command.SHOP);
	
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
				new MenuButton.Action() {
			@Override
			public void act() {
				shopOn = true;
			}
		}));
		
		buttons.add(new SimpleMenuButton(0, 127, 230, 100,
				Assets.pauseResumeButton, Assets.pauseResumeButtonClicked,
				commandAction(Command.RESUME)));
		
		buttons.add(new SimpleMenuButton(0, -9, 230, 100,
				Assets.pauseRetryButton, Assets.pauseRetryButtonClicked,
				new MenuButton.Action() {
					@Override
					public void act() {
						restartOn = true;
					}
				}));
	}
	
	@Override
	public void render(SpriteBatch batch, ShapeRenderer shapes) {
		if (optionsOn) {
			optionsMenu.render(batch, shapes);
		} else {
			super.render(batch, shapes);
		}
		
		if (restartOn) {
			restartGameMenu.render(batch, shapes);
			return;
		}
		if (shopOn) {
			shopMenu.render(batch, shapes);
			return;
		}
	}
	
	@Override
	public void update(float deltaTime) {
		if (shopOn) {
			shopMenu.update(deltaTime);
			return;
		}
		if (optionsOn) {
			optionsMenu.update(deltaTime);
			return;
		}
		if (restartOn) {
			restartGameMenu.update(deltaTime);
			return;
		}
		super.update(deltaTime);
	}
}
