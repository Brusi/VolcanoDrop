package com.retrom.volcano.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.assets.SoundAssets;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.shop.GraphicObject;
import com.retrom.volcano.shop.Shaman;
import com.retrom.volcano.shop.ShopFire;
import com.retrom.volcano.shop.ShopMenu;
import com.retrom.volcano.shop.ShopPlayer;
import com.retrom.volcano.shop.StaticGraphicObject;
import com.retrom.volcano.utils.BatchUtils;

public class ShopScreen extends ScreenAdapter implements Screen {
	
	SpriteBatch batch_ = new SpriteBatch();
	private final Camera cam_ = new OrthographicCamera(
			WorldRenderer.FRUSTUM_WIDTH, WorldRenderer.FRUSTUM_HEIGHT);
	
	GraphicObject shopBg = new StaticGraphicObject(Assets.shopBg, 0, 0);
	GraphicObject shopFg = new StaticGraphicObject(Assets.shopFg, 207, -274);
	
	ShopPlayer player = new ShopPlayer(-210,-350);
	Shaman shaman = new Shaman(185,-280);
	ShopFire fire = new ShopFire(0, -322);
	
	ShopMenu shopMenu = new ShopMenu();
	
	@Override
	public void show() {
		batch_.setProjectionMatrix(cam_.combined);
		SoundAssets.startShopMusic();
	}

	@Override
	public void render(float delta) {
		update(delta);
		
		Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		if (shopMenu.getBuy() || Gdx.input.isKeyJustPressed(Input.Keys.B)) {
			shaman.buy();
			SoundAssets.playSound(SoundAssets.shopClick);
		}
		
		batch_.begin();
		BatchUtils.setBlendFuncNormal(batch_);
		shopBg.render(batch_);
		player.render(batch_);
		shaman.render(batch_);
		shopFg.render(batch_);
		shopMenu.render(batch_);
		
		BatchUtils.setBlendFuncAdd(batch_);
		fire.render(batch_);
		batch_.end();
	}

	private void update(float delta) {
		player.update(delta);
		shaman.update(delta);
		fire.update(delta);
		shopMenu.update(delta);
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.HOME)) {
			Game x = ((Game)(Gdx.app.getApplicationListener()));
			x.setScreen(new GameScreen());
		}
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() 
	{
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}

}
