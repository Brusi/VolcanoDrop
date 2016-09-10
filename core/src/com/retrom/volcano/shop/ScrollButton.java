package com.retrom.volcano.shop;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.menus.SimpleMenuButton;

public class ScrollButton extends SimpleMenuButton{

    public static final int SIZE = 100;
    private final Sprite sprite_disabled_;

    public ScrollButton(float x, float y,
                        Sprite sprite,
                        Sprite sprite_pressed,
                        Sprite sprite_disabled_,
                        Action action) {
        super(x, y, SIZE, SIZE, sprite, sprite_pressed, action);
        this.sprite_disabled_ = sprite_disabled_;
    }

    public static ScrollButton UpScrollButton(float x,float y, Action action) {
        return new ScrollButton(x, y,
                                Assets.scrollUpButton,
                                Assets.scrollUpButtonPressed,
                                Assets.scrollUpButtonDisabled,
                                action);
    }

    public static ScrollButton DownScrollButton(float x,float y, Action action) {
        return new ScrollButton(x, y,
                                Assets.scrollDownButton,
                                Assets.scrollDownButtonPressed,
                                Assets.scrollDownButtonDisabled,
                                action);
    }

    @Override
    public void render(Batch batch) {
        if (!isVisible()) {
            return;
        }
        if (!isEnabled()) {
            Sprite s = sprite_disabled_;
            s.setAlpha(alpha_);
            s.setScale(scale_);
            Utils.drawCenter(batch, s, getX(), getY());
            return;
        }
        super.render(batch);
    }
}
