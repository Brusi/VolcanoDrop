package com.retrom.volcano.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.assets.SoundAssets;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.menus.Fade;
import com.retrom.volcano.menus.GraphicObject;
import com.retrom.volcano.menus.StaticGraphicObject;
import com.retrom.volcano.shop.Shaman;
import com.retrom.volcano.shop.ShopFire;
import com.retrom.volcano.shop.ShopMenu;
import com.retrom.volcano.shop.ShopPlayer;
import com.retrom.volcano.utils.BatchUtils;
import com.retrom.volcano.utils.Tween;
import com.retrom.volcano.utils.TweenQueue;

public class ShopScreen extends ScreenAdapter implements Screen {
	
	TweenQueue tweens_ = new TweenQueue();
	
	private final SpriteBatch batch_ = new SpriteBatch();
	private final ShapeRenderer shapes_ = new ShapeRenderer();
	Fade fade_ = new Fade();
	private final Camera cam_ = new OrthographicCamera(
			WorldRenderer.FRUSTUM_WIDTH, WorldRenderer.FRUSTUM_HEIGHT);
	
	GraphicObject shopBg = new StaticGraphicObject(Assets.shopBg, 0, 0);
	GraphicObject shopFg = new StaticGraphicObject(Assets.shopFg, 207, -274);
	
	ShopPlayer player = new ShopPlayer(-410,-350);
	Shaman shaman = new Shaman(385,-280);
	ShopFire fire = new ShopFire(0, -322);
	
	ShopMenu shopMenu = new ShopMenu();
	
	@Override
	public void show() {
		batch_.setProjectionMatrix(cam_.combined);
		shapes_.setProjectionMatrix(cam_.combined);
		SoundAssets.startShopMusic();
		
		tweens_.addTweenFromNow(0, 1, fade_.in);
		tweens_.addTweenFromNow(0.5f, 3, new Tween.EaseOut(new Tween.MovePoint(
				shaman.position_).from(385, -280).to(185, -280)));
		tweens_.addTweenFromNow(0.5f, 2, new Tween.EaseOut(new Tween.MovePoint(
				player.position_).from(-410,-350).to(-210,-350)));
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
		
		fade_.render(shapes_);
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
		
		tweens_.update(delta);
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
