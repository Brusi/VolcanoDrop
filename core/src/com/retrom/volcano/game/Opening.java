package com.retrom.volcano.game;

import sun.java2d.loops.RenderLoops;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.menus.StaticGraphicObject;
import com.retrom.volcano.shop.LoopingGraphicObject;
import com.retrom.volcano.utils.BatchUtils;
import com.retrom.volcano.utils.Tween;
import com.retrom.volcano.utils.TweenQueue;

public class Opening {
	
	TweenQueue queue = new TweenQueue();
	
	private static final float DOOR_X = -281;
	private static final float DOOR_FINAL_Y = 76;
	private static final float DOOR_INITIAL_Y = DOOR_FINAL_Y + 144;
	
	private final StaticGraphicObject door = new StaticGraphicObject(Assets.openingDoor, DOOR_X, DOOR_INITIAL_Y);
	private final StaticGraphicObject door_back = new StaticGraphicObject(
			Assets.openingDoorBg, DOOR_X, Assets.openingDoorBg.getHeight() / 2);
	private final StaticGraphicObject floor = new StaticGraphicObject(Assets.openingFloor, 0, -102);
	
	private final StaticGraphicObject shrineOn = new StaticGraphicObject(Assets.openingShrineOn, 0, 52);
	private final StaticGraphicObject shrineOff = new StaticGraphicObject(Assets.openingShrineOff, 0, 52);
	
	private final StaticGraphicObject leftTorch;
	private final StaticGraphicObject rightTorch;
	
	private final LoopingGraphicObject leftTorchFire = new LoopingGraphicObject(Assets.openingTorchFire, -210, 450);
	private final LoopingGraphicObject rightTorchFire = new LoopingGraphicObject(Assets.openingTorchFire, 210, 450);
	
	private final StaticGraphicObject bossSleeps = new StaticGraphicObject(Assets.openingBossSleeps, 140, 94);
	private final StaticGraphicObject bossSleepsGlow = new StaticGraphicObject(Assets.openingBossSleepsGlow, 140, 94);
	private final StaticGraphicObject bossSleepsRoots = new StaticGraphicObject(Assets.openingBossSleepsRoots, 140, 94);
	
	private final StaticGraphicObject fgRoots1 = new StaticGraphicObject(Assets.openingForegroundRoots1, 0, 292); 
	private final StaticGraphicObject fgRoots2 = new StaticGraphicObject(Assets.openingForegroundRoots2, -115, -92);
	
	float doorLightScale = 1;
	
	public Opening() {
		leftTorch = new StaticGraphicObject(Assets.openingTorch, -217, 349);
		rightTorch = new StaticGraphicObject(Assets.openingTorch, 217, 349) {
			@Override
			public void render(SpriteBatch batch) {
				Sprite s = getSprite();
				s.setFlip(true,  false);
				Utils.drawCenter(batch, s, position_.x, position_.y);
			}
		};
	}
	
	public void update(float deltaTime) {
		queue.update(deltaTime);
		leftTorchFire.update(deltaTime);
		rightTorchFire.update(deltaTime);
	}
	
	private boolean isRelicTaken = false;
	
	public void render(SpriteBatch batch) {
		bossSleeps.render(batch);
		bossSleepsGlow.render(batch);
		bossSleepsRoots.render(batch);
		
		if (isRelicTaken) {
			shrineOff.render(batch);
		} else {
			shrineOn.render(batch);
		}
		door_back.render(batch);
		renderDoorLight(batch);
		leftTorch.render(batch);
		rightTorch.render(batch);
		if (!isRelicTaken) {
			BatchUtils.setBlendFuncAdd(batch);
			leftTorchFire.render(batch);
			rightTorchFire.render(batch);
			BatchUtils.setBlendFuncNormal(batch);
		}
	}
	
	public void renderTop(SpriteBatch batch) {
		door.render(batch);
		floor.render(batch);
	}
	
	public void renderForeground(SpriteBatch batch) {
		fgRoots1.render(batch);
		fgRoots2.render(batch);
	}

	private void renderDoorLight(SpriteBatch batch) {
		BatchUtils.setBlendFuncAdd(batch);
		Sprite s = Assets.openingDoorLight;
		s.setScale(1, doorLightScale);
		Utils.drawCenter(batch, s, -114f, (s.getHeight() / 2 -28) * doorLightScale);
		BatchUtils.setBlendFuncNormal(batch);
	}
	
	public void startScene() {
		queue.addTweenFromNow(
				0, 1,
				new Tween.EaseBoth(new Tween.MovePoint(door.position_)
				  .from(DOOR_X, DOOR_INITIAL_Y).to(DOOR_X, DOOR_FINAL_Y)));
		queue.addTweenFromNow(
				0, 1,
				new Tween.EaseBoth(new Tween() {
					@Override
					public void invoke(float t) {
						doorLightScale = 1-t;
					}
				}));
		
		isRelicTaken = true;
	}
}
