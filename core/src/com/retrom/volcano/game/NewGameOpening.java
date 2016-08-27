package com.retrom.volcano.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.objects.BossGraphic;
import com.retrom.volcano.menus.StaticGraphicObject;
import com.retrom.volcano.shop.LoopingGraphicObject;
import com.retrom.volcano.utils.BatchUtils;
import com.retrom.volcano.utils.EventQueue;
import com.retrom.volcano.utils.Tween;
import com.retrom.volcano.utils.TweenQueue;

public class NewGameOpening implements Opening {
	
	private static final float BOSS_X = 140;
	private static final float BOSS_Y = 94;

	TweenQueue queue = new TweenQueue();
	
	private static final float DOOR_X = -284;
	private static final float DOOR_FINAL_Y = 80;
	private static final float DOOR_INITIAL_Y = DOOR_FINAL_Y + 136;
	
	private static final float SHRINE_INITIAL_Y = 52;
	private static final float SHRINE_FINAL_Y = SHRINE_INITIAL_Y - 105;
	
	private static final float BOSS_AWAKE_INITIAL_X = 179;
	private static final float BOSS_AWAKE_INITIAL_Y = 92;
	private static final float BOSS_AWAKE_TOP_Y = 678;
	private static final float BOSS_AWAKE_INITIAL_ROTATION = 8f;
	
	public final StaticGraphicObject door = new StaticGraphicObject(Assets.openingDoor, DOOR_X, DOOR_INITIAL_Y);
	private final StaticGraphicObject door_back = new StaticGraphicObject(
			Assets.openingDoorBg, DOOR_X - 10, Assets.openingDoorBg.getHeight() / 2 + 6);
	private final StaticGraphicObject floor = new StaticGraphicObject(Assets.openingFloor, 0, -103);
	
	
	private final StaticGraphicObject shrineOn = new StaticGraphicObject(Assets.openingShrineOn, 0, SHRINE_INITIAL_Y);
	private final StaticGraphicObject shrineOff = new StaticGraphicObject(Assets.openingShrineOff, 0, SHRINE_INITIAL_Y);
	
	private final StaticGraphicObject leftTorch;
	private final StaticGraphicObject rightTorch;
	
	private final StaticGraphicObject leftTorchGlow = new StaticGraphicObject(Assets.burningWallGlow, -210, 450-80);
	private final StaticGraphicObject rightTorchGlow = new StaticGraphicObject(Assets.burningWallGlow, 210, 450-80);
	
	private final LoopingGraphicObject leftTorchFire = new LoopingGraphicObject(Assets.openingTorchFire, -210, 450);
	private final LoopingGraphicObject rightTorchFire = new LoopingGraphicObject(Assets.openingTorchFire, 210, 450);
	
	private final StaticGraphicObject bossSleeps = new StaticGraphicObject(Assets.openingBossSleeps, BOSS_X, BOSS_Y);
	private final StaticGraphicObject bossSleepsGlow = new StaticGraphicObject(Assets.openingBossSleepsGlow, BOSS_X, BOSS_Y);
	private final StaticGraphicObject bossSleepsRoots = new StaticGraphicObject(Assets.openingBossSleepsRoots, BOSS_X, BOSS_Y);
	
	private final StaticGraphicObject bossAngryGlow = new StaticGraphicObject(Assets.openingBossAngryGlow, BOSS_X, BOSS_Y);
	private final StaticGraphicObject bossAngryGlowAdd = new StaticGraphicObject(Assets.openingBossAngryGlow, BOSS_X, BOSS_Y);
	
	private final StaticGraphicObject fgRoots1 = new StaticGraphicObject(Assets.openingForegroundRoots1, 0, 280); 
	private final StaticGraphicObject fgRoots2 = new StaticGraphicObject(Assets.openingForegroundRoots2, -115, -92);
	
	private final BossGraphic bossAwake = new BossGraphic(BOSS_AWAKE_INITIAL_X ,BOSS_AWAKE_INITIAL_Y);
	
	private float doorLightScale = 1;
	private float shrineGlowAlpha = 1;

	private float stateTime = 0;
	private float bossStateTime = 0;

	private boolean relicTaken = false;
	
	public NewGameOpening() {
		leftTorch = new StaticGraphicObject(Assets.openingTorch, -217, 349);
		// TODO: offset phase of animation.
		rightTorch = new StaticGraphicObject(Assets.openingTorch, 217, 349) {
			@Override
			public void render(Batch batch) {
				Sprite s = getSprite();
				s.setFlip(true,  false);
				Utils.drawCenter(batch, s, position_.x, position_.y);
			}
		};
		
		bossAngryGlow.setAlpha(0);
		bossAngryGlowAdd.setAlpha(0);
		
		bossAwake.setAlpha(0);
	}
	
	@Override
	public void update(float deltaTime) {
		stateTime += deltaTime;
		bossStateTime += deltaTime;
				
		queue.update(deltaTime);
		leftTorchFire.update(deltaTime);
		rightTorchFire.update(deltaTime);
		bossAwake.update(deltaTime);
	}
	
	@Override
	public void render(ShapeRenderer shapes, SpriteBatch batch) {
		
		bossSleeps.render(batch);
		renderBossEyes(batch);
		bossSleepsGlow.render(batch);
		
		bossAngryGlow.render(batch);
		bossAngryGlowAdd.render(batch);
		bossAwake.render(batch);
		bossSleepsRoots.render(batch);
		
		renderShrine(shapes, batch);
		
		door_back.render(batch);
		leftTorch.render(batch);
		rightTorch.render(batch);
		
		BatchUtils.setBlendFuncAdd(batch);
		
		leftTorchFire.setTint(shrineGlowAlpha);
		leftTorchFire.render(batch);
		leftTorchGlow.setTint((shrineGlowAlpha) * (doorLightScale - (float)Math.random() * 0.1f));
		leftTorchGlow.render(batch);
		
		rightTorchFire.setTint(shrineGlowAlpha);
		rightTorchFire.render(batch);
		rightTorchGlow.setTint((shrineGlowAlpha) * (doorLightScale - (float)Math.random() * 0.1f));
		rightTorchGlow.render(batch);
		BatchUtils.setBlendFuncNormal(batch);
		
	}

	private void renderShrine(ShapeRenderer shapes, SpriteBatch batch) {
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
		shapes.begin(ShapeType.Filled);
		
		// TODO: remove setColor if not needed.
		shapes.setColor(1f, 0f, 0f, 0.5f);
		// shapes.circle(200, 200, 100);
		shapes.setColor(0f, 1f, 0f, 0.5f);
		// shapes.rect(200, 200, 100, 100);
		shapes.rect(-300, 0, 600, 500);
		
		shapes.rect(100,100,100,100);
		
		shapes.end();
		
		batch.begin();
		//8. Enable RGBA color writing
		//   (SpriteBatch.begin() will disable depth mask)
		Gdx.gl.glColorMask(true, true, true, true);
		
		//9. Make sure testing is enabled.
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		
		//10. Now depth discards pixels outside our masked shapes
		Gdx.gl.glDepthFunc(GL20.GL_EQUAL);
		
		shrineOff.render(batch);
		shrineOn.setAlpha(shrineGlowAlpha);
		shrineOn.render(batch);
		
		batch.end();
		Gdx.gl.glDepthFunc(GL20.GL_ALWAYS);
		batch.begin();
	}

	private void renderBossEyes(SpriteBatch batch) {
		if (bossStateTime > 7) {
			bossStateTime -= 7;
		}
		
		float alpha = 0;
		if (bossStateTime < 4) {
			if (relicTaken) bossStateTime = 0; 
			alpha = 0;
		} else if (bossStateTime < 4.333f) {
			alpha = (bossStateTime - 4) / 0.333f; 
		} else if (bossStateTime < 7f) {
			alpha = 1 - (bossStateTime - 4.333f) / 2.667f;
		}
		
		bossSleepsGlow.setAlpha(alpha);
		bossSleepsGlow.render(batch);
	}
	
	@Override
	public void renderTop(SpriteBatch batch) {
		door.render(batch);
	}
	
	@Override
	public void renderForeground(SpriteBatch batch) {
		floor.render(batch);
		fgRoots1.render(batch);
		fgRoots2.render(batch);
		renderDoorLight(batch);
	}

	private void renderDoorLight(SpriteBatch batch) {
		BatchUtils.setBlendFuncAdd(batch);
		Sprite s = Assets.openingDoorLight;
		s.setColor(1,1,1,1);
		s.setScale(1, doorLightScale);
		Utils.drawCenter(batch, s, -114f, (s.getHeight() / 2 -28) * doorLightScale);
		
		float tint = ((float) Math.sin(stateTime * 2) + 1) / 2 * 0.5f + 0.0f;
		tint *= doorLightScale;
		s.setColor(tint, tint, tint, 1);
		Utils.drawCenter(batch, s, -114f, (s.getHeight() / 2 -28) * doorLightScale);
		BatchUtils.setBlendFuncNormal(batch);
	}
	
	@Override
	public void startScene() {
		relicTaken = true;
		
		closdDoor();
		takeShrineDown();
		
		bossWakesUp();
	}
	
	private Tween scaleSleepingBossTween(final float begin, final float end) {
		return new Tween() {
			@Override
			public void invoke(float t) {
				float scale = (1 - t) * begin + t * end;
				bossSleeps.setScale(scale);
				bossAngryGlow.setScale(scale);
				bossAngryGlowAdd.setScale(scale);
				
				float movedX = BOSS_X + (1 - scale) * 73 / 2;
				bossSleeps.position_.x = movedX;
				bossAngryGlow.position_.x = movedX;
				bossAngryGlowAdd.position_.x = movedX;
			}
		};
	}

	private void bossWakesUp() {
		bossAwake.setRotation(BOSS_AWAKE_INITIAL_ROTATION);
		
		queue.addTweenFromNow(1.833f, 0.666f, new Tween() {
			@Override
			public void invoke(float t) {
				bossAngryGlow.setAlpha(t);
			}
		});
		queue.addTweenFromNow(1.833f, 0.666f, new Tween() {
			@Override
			public void invoke(float t) {
				bossAngryGlow.setAlpha(t);
				bossAngryGlowAdd.setAlpha(t);
			}
		});
		queue.addTweenFromNow(1.833f + 0.666f, 0.666f, bossEyesAddTween);
		
		queue.addTweenFromNow(3f, 0.233f, scaleSleepingBossTween(1, 0.97f));
		queue.addTweenFromNow(3.233f, 0.067f, scaleSleepingBossTween(0.97f, 1.02f));
		queue.addTweenFromNow(3.3f, 0.7f, scaleSleepingBossTween(1.02f, 1f));
		queue.addTweenFromNow(4f, 0.233f, scaleSleepingBossTween(1, 0.97f));
		queue.addTweenFromNow(4.233f, 0.067f, scaleSleepingBossTween(0.97f, 1.05f));
		queue.addTweenFromNow(4.3f, 0.7f, scaleSleepingBossTween(1.05f, 1f));
		queue.addTweenFromNow(5.333f, 0.233f, scaleSleepingBossTween(1, 0.80f));
		queue.addTweenFromNow(5.567f, 0.067f, scaleSleepingBossTween(0.80f, 1.20f));
		
		queue.addTweenFromNow(3.3f, 0.7f, bossEyesAddTween);
		queue.addTweenFromNow(4.3f, 0.7f, bossEyesAddTween);
		queue.addTweenFromNow(5.567f, 0.5f, bossEyesAddTween);
		
		// From here the boss is released.
		queue.addEventFromNow(5.633f + 0.5f, new EventQueue.Event() {
			@Override
			public void invoke() {
				bossSleeps.setAlpha(0);
				bossAngryGlow.setAlpha(0);
				bossAngryGlowAdd.setAlpha(0);
			}
		});
		queue.addTweenFromNow(5.633f, 0.5f, scaleSleepingBossTween(1.2f, 1));
		queue.addTweenFromNow(5.633f, 0.5f, Tween.alpha(bossAwake));
		queue.addTweenFromNow(5.633f, 0.5f, Tween.tint(bossAwake));
		
		queue.addTweenFromNow(5.633f, 0.5f, Tween.easeBothSin(Tween.tSpan(1.2f, 1, Tween.scale(bossAwake))));
		queue.addTweenFromNow(
				6.25f, 1f,
				Tween.easeBothQuad(Tween.movePoint(bossAwake.position_)
						.fromX(BOSS_AWAKE_INITIAL_X).toX(0)));
		
		queue.addTweenFromNow(
				6.25f, 1.7f,
				Tween.easeBothSin(Tween.movePoint(bossAwake.position_)
						.fromY(BOSS_AWAKE_INITIAL_Y).toY(BOSS_AWAKE_TOP_Y)));
		
		queue.addTweenFromNow(
				6.25f, 0.5f,
				Tween.tSpan(BOSS_AWAKE_INITIAL_ROTATION, 0,
						Tween.rotate(bossAwake)));
		
		queue.addEventFromNow(7.0f, new EventQueue.Event() {
			@Override public void invoke() {
				bossAwake.setState_(BossGraphic.State.DARK);
			}
		});
		queue.addEventFromNow(9.25f, new EventQueue.Event() {
			@Override public void invoke() {
				bossAwake.setState_(BossGraphic.State.HIDDEN);
			}
		});
		
		queue.addTweenFromNow(
				8.25f, 1f,
				Tween.easeIn(Tween.movePoint(bossAwake.position_)
						.fromY(BOSS_AWAKE_TOP_Y).toY(WorldRenderer.FRUSTUM_HEIGHT + 200)));
		
		// Roots shake.
		Tween rootsShake = new Tween() {
			@Override
			public void invoke(float t) {
				bossSleepsRoots.position_.x = BOSS_X + Utils.random2Range(7 * (1-t));
			}
		};
		queue.addTweenFromNow(3.3f, 0.5f, rootsShake);
		queue.addTweenFromNow(4.3f, 0.5f, rootsShake);
		queue.addTweenFromNow(5.567f, 0.5f, rootsShake);
		queue.addTweenFromNow(5.567f, 0.5f, new Tween() {
			@Override public void invoke(float t) {
				bossSleepsRoots.setScaleY(1-t);
				bossSleepsRoots.position_.y = BOSS_Y - t * 109; 
			}
		});
	}

	private final Tween bossEyesAddTween = new Tween() {
			@Override
			public void invoke(float t) {
				bossAngryGlowAdd.setAlpha(1-t);
			}
		};

	private void takeShrineDown() {
		queue.addTweenFromNow(
				0.333f, 2.666f,
				Tween.movePoint(shrineOff.position_).from(0,
						SHRINE_INITIAL_Y).to(0, SHRINE_FINAL_Y));
		queue.addTweenFromNow(0.333f, 2.666f, new Tween() {
			@Override
			public void invoke(float t) {
				shrineOff.position_.x = Utils.random2Range(6 * t);
			}
		});
		// TODO: add shrine mask!
		
		queue.addTweenFromNow(
				0, 0.5f,
				Tween.easeBothSin(new Tween() {
					@Override
					public void invoke(float t) {
						shrineGlowAlpha = 1-t;
					}
				}));
	}

	private void closdDoor() {
		queue.addTweenFromNow(
				0.333f, 2.666f,
				new Tween.EaseBoth2(Tween.movePoint(door.position_)
				  .from(DOOR_X, DOOR_INITIAL_Y).to(DOOR_X, DOOR_FINAL_Y)));
		queue.addTweenFromNow(
				0.333f, 2.666f,
				new Tween.EaseBoth2(new Tween() {
					@Override
					public void invoke(float t) {
						doorLightScale = 1-t;
					}
				}));
	}
}
