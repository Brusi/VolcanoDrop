package com.retrom.volcano.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class PreloaderAssets implements Disposable {

    private TextureAtlas atlas;

    public Sprite background;
    public Sprite bar_body;
    public Sprite bar_fill;

    public Array<Sprite> tips;

    public void load() {
        atlas = new TextureAtlas("loading/loading.txt");

        background = atlas.createSprite("loadingmenu_bg");
        bar_body = atlas.createSprite("loadingmenu_loadingbar_body");
        bar_fill = atlas.createSprite("loadingmenu_loadingbar_fill");

        tips = atlas.createSprites("loadingmenu_loadingbar_tip");
    }


    @Override
    public void dispose() {
        atlas.dispose();
    }
}
