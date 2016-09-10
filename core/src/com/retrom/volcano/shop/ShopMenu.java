package com.retrom.volcano.shop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.menus.BackMenuButton;
import com.retrom.volcano.menus.ExitMenuButton;
import com.retrom.volcano.menus.GraphicObject;
import com.retrom.volcano.menus.MenuButton;
import com.retrom.volcano.menus.StaticGraphicObject;
import com.retrom.volcano.screens.GameScreen;
import com.retrom.volcano.utils.Tween;
import com.retrom.volcano.utils.TweenQueue;

public class ShopMenu {

	private static final float menuStartYPos = 1000;
	private static final float menuFinalYPos = 176;
	
	TweenQueue tweens = new TweenQueue();

	GraphicObject menuBg = new StaticGraphicObject(Assets.shopMenuBg, 0, menuStartYPos - 10);
	GraphicObject menuFg = new StaticGraphicObject(Assets.shopMenuFg, 0, menuStartYPos);
	
	private final MenuButton exitButton;
	private final MenuButton backButton;

	private final MenuButton scrollUpButton;
	private final MenuButton scrollDownButton;
	
	enum Command {
		EXIT, BACK, POWERS, BLESSINGS, COSTUMES, BUY
	}
	
	public interface Listener {
		void act(Command cmd);
	}
	
	private Listener listener = new Listener() {
		@Override
		public void act(Command cmd) {
			switch (cmd) {
			case BACK:
				content = mainContent;
                hideControlButtons();
				break;
			case BLESSINGS:
				content = blessingsContent;
                showControlButtons();
				break;
			case COSTUMES:
				content = costumesContent;
                showControlButtons();
				break;
			case POWERS:
				content = powersContent;
                showControlButtons();
				break;
			case BUY:
				buy = true;
				break;
			case EXIT:
				Game x = ((Game)(Gdx.app.getApplicationListener()));
				x.setScreen(new GameScreen());
				break;
			default:
				break;
			}
			content.refresh();
            updateScrollButtons();
		}
	};

    private void hideControlButtons() {
        backButton.hide();
        scrollUpButton.hide();
        scrollDownButton.hide();
    }

    private void showControlButtons() {
        backButton.show();
        scrollUpButton.show();
        scrollDownButton.show();
    }

    private ShopMenuContent mainContent = new MainShopContent(listener);
	private ShopMenuContent powersContent = new PowersShopMenuContent(listener);
	private ShopMenuContent blessingsContent = new BlessingsShopMenuContent(listener);
	private ShopMenuContent costumesContent = new CostumesShopMenuContent(listener);
	
	private ShopMenuContent content;
			

	private boolean buy = false;
	
	private GoldCounter goldCounter = new GoldCounter(374);
	
	public ShopMenu() {
		content = mainContent;
		content.refresh();
		exitButton = new ExitMenuButton(ExitMenuButton.DEFAULT_X,
				ExitMenuButton.DEFAULT_Y, new MenuButton.Action() {
					@Override
					public void act() {
						listener.act(ShopMenu.Command.EXIT);
					}
				});
		backButton = new BackMenuButton(BackMenuButton.DEFAULT_X,
				BackMenuButton.DEFAULT_Y, new MenuButton.Action() {
					@Override
					public void act() {
						listener.act(ShopMenu.Command.BACK);
					}
				});
        scrollUpButton = ScrollButton.UpScrollButton(-172, -88, new MenuButton.Action() {
            @Override
            public void act() {
                if (!(content instanceof ItemsListShopMenuContent)) {
                    Gdx.app.error("ERROR", "Scroll buttons should not be pressed when contemt menu is not items list.");
                    return;
                }
                ItemsListShopMenuContent items_content = (ItemsListShopMenuContent) content;
                items_content.scrollUp();
                updateScrollButtons();
            }
        });
        scrollDownButton = ScrollButton.DownScrollButton(172, -88, new MenuButton.Action() {
            @Override
            public void act() {
                if (!(content instanceof ItemsListShopMenuContent)) {
                    Gdx.app.error("ERROR", "Scroll buttons should not be pressed when contemt menu is not items list.");
                    return;
                }
                ItemsListShopMenuContent items_content = (ItemsListShopMenuContent) content;
                items_content.scrollDown();
                updateScrollButtons();
            }
        });
        backButton.hide();
        scrollUpButton.hide();
        scrollDownButton.hide();

		tweens.addTweenFromNow(1, 1f, Tween.bounce(Tween.movePoint(menuBg.position_).from(1, menuStartYPos).to(1, menuFinalYPos)));
		tweens.addTweenFromNow(1, 1f, Tween.bounce(Tween.movePoint(menuFg.position_).from(1, menuStartYPos-10).to(1, menuFinalYPos)));
		tweens.addTweenFromNow(1.5f, 0.6f, Tween.bubble(new Tween() {
			@Override
			public void invoke(float t) {
				goldCounter.setScale(t);
			}}));
	}

    private void updateScrollButtons() {
        if (!(content instanceof ItemsListShopMenuContent)) {
            return;
        }
        ItemsListShopMenuContent items_content = (ItemsListShopMenuContent) content;
        if (items_content.canScrollUp()) {
            scrollUpButton.enable();
        } else  {
            scrollUpButton.disable();
        }

        if (items_content.canScrollDown()) {
            scrollDownButton.enable();
        } else  {
            scrollDownButton.disable();
        }
    }

    public void update(float deltaTime) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			ShopData.reset();
			content.refresh();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
			ShopData.addGold(1000);
			content.refresh();
		}
		
		tweens.update(deltaTime);
		content.update(deltaTime);
		goldCounter.update();
		exitButton.checkClick();
		backButton.checkClick();
        scrollUpButton.checkClick();
        scrollDownButton.checkClick();
	}
	
	public void render(SpriteBatch batch) {
		menuBg.render(batch);
		content.render(batch);
		renderBottomFade(batch);
		menuFg.render(batch);
		goldCounter.render(batch);
		exitButton.render(batch);
		backButton.render(batch);
        scrollUpButton.render(batch);
        scrollDownButton.render(batch);
	}
	
	private void renderBottomFade(SpriteBatch batch) {
		Sprite s = content.getBottomFade();
		if (s != null) Utils.drawCenter(batch, s, 0, 174);
	}

	public boolean getBuy() {
		boolean $ = buy;
		buy = false;
		return $;
	}
}
