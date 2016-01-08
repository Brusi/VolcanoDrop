package com.retrom.volcano.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.World;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.utils.BatchUtils;

public class PowerupUiRenderer {
	
	private static float SPACING = 100f;
	private static float CENTER = 0;
	
	static enum UnitState {
		NONE,
		START,
		MIDDLE,
		END;
	}
	abstract class PowerupUnit {
		private final float y = WorldRenderer.FRUSTUM_HEIGHT / 2 - 55f;
		private final float x;
		
		private float ratio;
		
		
		UnitState state;
		float stateTime;
		
		public void setState(UnitState state) {
			this.state = state;
			stateTime = 0;
		}
		
		public void update(float deltaTime) {
			stateTime += deltaTime;
			if (state == UnitState.START && stateTime > World.PAUSE_EFFECT_DURATION) {
				setState(UnitState.MIDDLE);
			}
			if (state == UnitState.MIDDLE && ratio < 0) {
				setState(UnitState.END);
			}
		}
		
		PowerupUnit(float x) {
			this.x = x;
			setState(UnitState.NONE);
		}
		
		private float getScale(float StateTime) {
			if (state == UnitState.START) {
				if (stateTime < 1f / 8) {
					return 8 * stateTime * 1.2f;
				} else if (stateTime < 1f / 4) {
					return (1 - (stateTime - 1f / 8) / (1f / 8)) * 0.2f + 1f; 
				}
			}
			if (state == UnitState.END) {
				return Math.max(0, 1 - stateTime * 4);
			}
			return 1f;
		}
		
		public void renderBg(SpriteBatch batch) {
			if (state == UnitState.NONE) {
				return;
			}
			
			Sprite s = getBackSprite();
			float scale = getScale(stateTime);
			s.setScale(scale);
			drawSpriteAt(batch, s, x, y);
		}
		
		public void renderOver(SpriteBatch batch) {
			if (state == UnitState.NONE) {
				return;
			}
			Sprite s = getOverSprite();
			float scale = getScale(stateTime);
			s.setScale(scale);
			drawSpriteAt(batch, s, x, y);
		}
		
		public void renderMask() {
			if (state == UnitState.NONE || state == UnitState.END) {
				return;
			}
			drawMaskAt(x, y, 86f, ratio);
		}
		
		protected abstract Sprite getBackSprite();
		protected abstract Sprite getOverSprite();

		public void setRatio(float ratio) {
			this.ratio = ratio;
			if ((state == UnitState.NONE || state == UnitState.END) && ratio > 0) {
				setState(UnitState.START);
			}
		}
		
		public void start() {
			
		}
	}
	
	class MagnetUnit extends PowerupUnit{
		public MagnetUnit() {
			super(CENTER);
		}
		@Override protected Sprite getBackSprite() { return Assets.magnetGui; }
		@Override protected Sprite getOverSprite() { return Assets.magnetGuiOn; }
	}
	
	class SlomoUnit extends PowerupUnit{
		public SlomoUnit() {
			super(CENTER + SPACING);
		}

		@Override protected Sprite getBackSprite() { return Assets.slomoGui; }
		@Override protected Sprite getOverSprite() { return Assets.slomoGuiOn; }
	}
	
	class ShieldUnit extends PowerupUnit {
		public ShieldUnit() {
			super(CENTER - SPACING); 
		}
		@Override protected Sprite getBackSprite() { return Assets.shieldGui; }
		@Override protected Sprite getOverSprite() { return Assets.shieldGuiOn; }
	}
  
	SpriteBatch batch;
	Camera cam;
	static ShapeRenderer shapes;
	
	float ratio_ = 1;
	
	private PowerupUnit magnetUnit = new MagnetUnit();
	private PowerupUnit slomoUnit = new SlomoUnit();
	private PowerupUnit shieldUnit = new ShieldUnit();
	
	public PowerupUiRenderer(Camera cam) {
		batch = new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);
		this.cam = cam;
		shapes = new ShapeRenderer();
		shapes.setProjectionMatrix(cam.combined);
	}
	
	static private void drawSpriteAt(SpriteBatch batch, Sprite sprite, float x, float y) {
		sprite.setX(x - sprite.getWidth()/2);
		sprite.setY(y - sprite.getHeight() / 2);
		sprite.draw(batch);
	}
	
	private static void drawMaskAt(float x, float y, float radius, float ratio) {
		// 6. render your primitive shapes
		shapes.begin(ShapeType.Filled);

		shapes.setColor(1f, 0f, 0f, 0.5f);
		// shapes.circle(200, 200, 100);
		shapes.setColor(0f, 1f, 0f, 0.5f);
		// shapes.rect(200, 200, 100, 100);

		final float step = 0.001f;
		for (float i = 0; i < ratio; i+=step) {
			float anglepart1 = i;
			float angle1 = (float) (anglepart1 * 2 * Math.PI);
			float anglepart2 = (i + step);
			float angle2 = (float) (anglepart2 * 2 * Math.PI);

			float x1 = x;
			float y1 = y;
			float x2 = x - radius * (float) Math.sin(angle1);
			float y2 = y + radius * (float) Math.cos(angle1);
			float x3 = x - radius * (float) Math.sin(angle2);
			float y3 = y + radius * (float) Math.cos(angle2);
			shapes.triangle(x1, y1, x2, y2, x3, y3);
		}
		
		shapes.end();
	}
	
	public void update(float deltaTime) {
		PowerupUnit[] units = {magnetUnit, slomoUnit, shieldUnit};
		for (PowerupUnit unit : units) {
			if (unit == null) {
				continue;
			}
			unit.update(deltaTime);
		}
	}
	
	public void render() {
		PowerupUnit[] units = {magnetUnit, slomoUnit, shieldUnit};
		BatchUtils.setBlendFuncNormal(batch);
		batch.begin();
		for (PowerupUnit unit : units) {
			unit.renderBg(batch);
		}
		batch.end();
		
		for (PowerupUnit unit : units) {
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
			
			///////////// Draw mask shape(s)
			unit.renderMask();
			
			///////////// Draw sprite(s) to be masked
			BatchUtils.setBlendFuncAdd(batch);
			batch.begin();
			
			//8. Enable RGBA color writing
			//   (SpriteBatch.begin() will disable depth mask)
			Gdx.gl.glColorMask(true, true, true, true);
			
			//9. Make sure testing is enabled.
			Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
			
			//10. Now depth discards pixels outside our masked shapes
			Gdx.gl.glDepthFunc(GL20.GL_EQUAL);
			
			//push to the batch
			unit.renderOver(batch);
			//end/flush your batch
			batch.end();
		}
		
		Gdx.gl.glDepthFunc(GL20.GL_ALWAYS);
	}
	
	public void dispose() {
		batch.dispose();
		shapes.dispose();
	}

	static public void setUnitRatio(PowerupUnit unit, float ratio) {
		unit.setRatio(ratio);
	}
	
	public void setMagnetRatio(float ratio) {
		setUnitRatio(magnetUnit, ratio);
	}

	public void setSlomoRatio(float ratio) {
		setUnitRatio(slomoUnit, ratio);
	}

	public void setShieldRatio(float ratio) {
		setUnitRatio(shieldUnit, ratio);
	}
}