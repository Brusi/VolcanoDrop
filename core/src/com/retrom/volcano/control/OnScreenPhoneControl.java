package com.retrom.volcano.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.utils.TouchToPoint;

public class OnScreenPhoneControl extends AbstractControl {

	private boolean upWasTouched = false;
	private boolean leftWasTouched = false;
	private boolean rightWasTouched = false;
	// TODO: little vibration when touching left&right.
	
	private static final float SIDE_CONTROL_WIDTH = 244;
	private static final float SIDE_CONTROL_HEIGHT = 140;
	private static final float JUMP_RECT_SIZE = 140;
	final Rectangle leftRect = new Rectangle(
			-WorldRenderer.FRUSTUM_WIDTH / 2,
			-WorldRenderer.FRUSTUM_HEIGHT / 2,
			SIDE_CONTROL_WIDTH / 2,
			SIDE_CONTROL_HEIGHT);
	final Rectangle rightRect = new Rectangle(
			-WorldRenderer.FRUSTUM_WIDTH / 2 + SIDE_CONTROL_WIDTH / 2,
			-WorldRenderer.FRUSTUM_HEIGHT / 2,
			SIDE_CONTROL_WIDTH / 2,
			SIDE_CONTROL_HEIGHT);
	
	final Rectangle jumpRect = new Rectangle(
			WorldRenderer.FRUSTUM_WIDTH / 2 - JUMP_RECT_SIZE,
			-WorldRenderer.FRUSTUM_HEIGHT / 2,
			JUMP_RECT_SIZE, JUMP_RECT_SIZE);
	
	final TouchToPoint ttp = TouchToPoint.create();

	@Override
	public boolean isAnalog() {
		return false;
	}
	
	@Override
	public float getDigitalXDir() {
		vibrateSides();
		
		if (leftTouched() && rightTouched()) {
			return 0;
		}
		if (leftTouched()) {
			return -1;
		}
		if (rightTouched()) {
			return 1;
		}
		return 0;
	}
	
	private void vibrateSides() {
		if (leftNewTouch() || rightNewTouch()) {
			Gdx.input.vibrate(20);
		}
		
	}

	@Override
	public boolean isJumpPressed() {
		boolean newTouch = upNewTouch();
		if (newTouch) {
			Gdx.input.vibrate(20);
		}
		return newTouch;
	}
	
	private boolean touchInRect(int i, Rectangle rect) {
		Vector2 pnt = ttp.toPoint(Gdx.input.getX(i), Gdx.input.getY(i));
		System.out.println("Checking point: " + pnt);
		System.out.println("In rect: " + rect);
		
		return rect.contains(pnt);
	}
	
	
	private boolean isRectTouched(Rectangle rect) {
		for (int i=0; i < 20; i++) {
			if (!Gdx.input.isTouched(i)) {
				continue;
			}
			if (touchInRect(i, rect)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean leftTouched() {
		return isRectTouched(leftRect);
	}
	
	private boolean rightTouched() {
		return isRectTouched(rightRect);
	}
	
	// We don't want 'up' to be touched continuously, so we want to sample only
	// if 'up' started to be touched now.
	private boolean upNewTouch() {
		boolean upTouchedNow = upTouched();
		boolean $ = false;
		if (!upWasTouched && upTouchedNow) {
			$ = true;
		}
		upWasTouched = upTouchedNow;
		return $;
	}
	
	private boolean leftNewTouch() {
		boolean leftTouchedNow = leftTouched();
		boolean $ = false;
		if (!leftWasTouched && leftTouchedNow) {
			$ = true;
		}
		leftWasTouched = leftTouchedNow;
		return $;
	}
	
	private boolean rightNewTouch() {
		boolean rightTouchedNow = rightTouched();
		boolean $ = false;
		if (!rightWasTouched && rightTouchedNow) {
			$ = true;
		}
		rightWasTouched = rightTouchedNow;
		return $;
	}
	
	private boolean upTouched() {
		return isRectTouched(jumpRect);
	}
	
	static void drawAtCenterOfRect(SpriteBatch batch, Sprite sprite, Rectangle rect) {
		drawAtCenterOfRect(batch, sprite, rect, 0);
	}
	
	static void drawAtCenterOfRect(SpriteBatch batch, Sprite sprite, Rectangle rect, float xOffset) {
		sprite.setScale(1.2f);
		sprite.setPosition(rect.x + rect.width / 2 - sprite.getWidth() / 2 + xOffset, rect.y + rect.height / 2 - sprite.getHeight() / 2);
		sprite.draw(batch);
	}
	
	public void drawAtCenter(SpriteBatch batch, Sprite sprite, float x, float y) {
		sprite.setPosition(x - sprite.getWidth() / 2,
						   y - sprite.getHeight() / 2);
		sprite.draw(batch);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		{
			Sprite s = Assets.leftRightControl;
			s.setScale(1.2f);
			drawAtCenter(batch, s, leftRect.x + leftRect.width, leftRect.y + leftRect.height / 2);
		}
		{
			Sprite s = Assets.jumpControl;
			s.setScale(1.2f);
			drawAtCenter(batch, s, jumpRect.x + jumpRect.width / 2, leftRect.y + leftRect.height / 2);
		}
		
		if (leftTouched()) {
			drawAtCenterOfRect(batch, Assets.leftControlOn, leftRect, 11);
		}
		if (rightTouched()) {
			drawAtCenterOfRect(batch, Assets.rightControlOn, rightRect, -13);
		}
		if (upTouched()) {
			drawAtCenterOfRect(batch, Assets.jumpControlOn, jumpRect);
		}
	}
}
