package com.retrom.volcano.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.assets.PreloaderAssets;
import com.retrom.volcano.assets.SoundAssets;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.menus.Fade;
import com.retrom.volcano.utils.BatchUtils;

public class LoadingScreen implements Screen {

    Fade fade = new Fade();
    float fadeTime = 0;
    private boolean isDoneLoading = false;

	private SpriteBatch batch;
    private ShapeRenderer shapes;

    private Camera cam = new OrthographicCamera(
            WorldRenderer.FRUSTUM_WIDTH, WorldRenderer.FRUSTUM_HEIGHT);
	private final PreloaderAssets preloaderAssets = new PreloaderAssets();

    private Sprite tip_sprite;

    private float progress = 0;

	@Override
	public void show() {
        fade.setAlpha(0);

        Assets.startLoad();
        SoundAssets.preload(Assets.assetManager);
		batch = new SpriteBatch();
        batch.setProjectionMatrix(cam.combined);
        shapes = new ShapeRenderer();
        shapes.setProjectionMatrix(cam.combined);

        preloaderAssets.load();
        tip_sprite = preloaderAssets.tips.random();
    }

    private void afterDoneLoading(float deltaTime) {

    }

	@Override
	public void render(float delta) {
        if (isDoneLoading) {
            fadeTime += delta * 2;
            fade.setAlpha(fadeTime);
            if (fadeTime >= 1) {
                Game x = ((Game)(Gdx.app.getApplicationListener()));
                x.setScreen(new GameScreen());
                return;
            }
            renderGraphics();
            return;
        }

        if (Assets.assetManager.update()) {
            doneLoading();
        }
        float actualProgress = Assets.assetManager.getProgress();
        float vel = (actualProgress - progress) * 10;
        progress = Math.min(actualProgress, progress + vel * delta);

        renderGraphics();
	}

    private void doneLoading() {
        Assets.initAssets();
        SoundAssets.load();
        isDoneLoading = true;
    }

    private void renderGraphics() {
        Gdx.graphics.getGL20().glClearColor(0, 0, 0, 1);
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        batch.begin();
        Utils.drawCenter(batch, preloaderAssets.background, 0, 0);
        Utils.drawCenter(batch, preloaderAssets.bar_body, -3, -389);
        Utils.drawCenter(batch, tip_sprite, 0, -30);
        batch.end();

        //2. clear our depth buffer with 1.0
        Gdx.gl.glClearDepthf(1f);
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

        //3. set the function to LESS
        Gdx.gl.glDepthFunc(GL20.GL_LESS);

        //4. enable depth writing
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        //5. Enable depth writing, disable RGBA color writing
        Gdx.gl.glDepthMask(true);
        Gdx.gl.glColorMask(false, false, false, false);

        // 6. render your primitive shapes
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        shapes.setColor(1f, 0f, 0f, 0.5f);
        // shapes.circle(200, 200, 100);
        shapes.setColor(0f, 1f, 0f, 0.5f);
        // shapes.rect(200, 200, 100, 100);


        shapes.rect(-3 - 93, -389 - 20, progress * 190, 40);

        shapes.end();

//        ///////////// Draw sprite(s) to be masked
        batch.begin();

        //8. Enable RGBA color writing
        //   (SpriteBatch.begin() will disable depth mask)
        Gdx.gl.glColorMask(true, true, true, true);
//
//        //9. Make sure testing is enabled.
//        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
//
//        //10. Now depth discards pixels outside our masked shapes
        Gdx.gl.glDepthFunc(GL20.GL_EQUAL);

        //push to the batch
        Utils.drawCenter(batch, preloaderAssets.bar_fill, -3, -389);
        //end/flush your batch
        batch.end();

        Gdx.gl.glDepthFunc(GL20.GL_ALWAYS);
        BatchUtils.setBlendFuncNormal(batch);

        fade.render(shapes);
    }

    @Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		batch.dispose();
        shapes.dispose();
        preloaderAssets.dispose();
	}
}
