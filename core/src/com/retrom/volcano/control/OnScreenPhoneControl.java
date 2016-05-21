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
	
	private boolean left_just_touched = false;
	private boolean right_just_touched = false;
	
	private final ControlInput input;
	private float slide_ = 1;
	private float sides_scale = 1;
	private float jump_scale = 1;
	private boolean show;
	private boolean enabled;
	
	OnScreenPhoneControl(ControlInput input) {
		this.input = input;
	}

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
	
	@Override
	public boolean isJumpPressedContinuously() {
		return upTouched();
	}
	
	private boolean leftTouched() {
		if (!enabled) return false;
		return input.isLeftPressed();
	}
	
	private boolean rightTouched() {
		if (!enabled) return false;
		return input.isRightPressed();
	}
	
	private boolean upTouched() {
		if (!enabled) return false;
		return input.isJumpPressed();
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
		left_just_touched = $;
		return $;
	}
	
	private boolean rightNewTouch() {
		boolean rightTouchedNow = rightTouched();
		boolean $ = false;
		if (!rightWasTouched && rightTouchedNow) {
			$ = true;
		}
		rightWasTouched = rightTouchedNow;
		
		right_just_touched = $;
		return $;
	}
	
	static void drawAtCenterOfRect(SpriteBatch batch, Sprite sprite, Rectangle rect) {
		drawAtCenterOfRect(batch, sprite, rect, 0, 0);
	}
	
	static void drawAtCenterOfRect(SpriteBatch batch, Sprite sprite, Rectangle rect, float xOffset, float yOffset) {
		sprite.setScale(1.2f);
		sprite.setPosition(rect.x + rect.width / 2 - sprite.getWidth() / 2 + xOffset, rect.y + rect.height / 2 - sprite.getHeight() / 2 + yOffset);
		sprite.draw(batch);
	}
	
	public void drawAtCenter(SpriteBatch batch, Sprite sprite, float x, float y) {
		sprite.setPosition(x - sprite.getWidth() / 2,
						   y - sprite.getHeight() / 2);
		sprite.draw(batch);
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if (!show) {
			return;
		}
		final float mid_x = -WorldRenderer.FRUSTUM_WIDTH / 2 + ControlInput.MID_X_90;
		final float mid_y = -WorldRenderer.FRUSTUM_HEIGHT / 2 + 72;
		{
			Sprite s = Assets.leftRightControlBg;
			s.setScale(0.9f * sides_scale);
			drawAtCenter(batch, s, mid_x, mid_y);
		}
		{
			Sprite s = leftTouched() ? Assets.leftControlOn : Assets.leftControlOff;
			s.setScale(0.9f * sides_scale);
			drawAtCenter(batch, s, mid_x -78 * sides_scale, mid_y);
		}
		{
			Sprite s = rightTouched() ? Assets.rightControlOn : Assets.rightControlOff;
			s.setScale(0.9f * sides_scale);
			drawAtCenter(batch, s, mid_x + 78 * sides_scale, mid_y);
		}
		{
			Sprite s = Assets.leftRightControlOver;
			s.setScale(0.9f * sides_scale);
			drawAtCenter(batch, s, mid_x + 3, mid_y + 3);
		}
		
		{
			Sprite s = upTouched() ? Assets.jumpControlOn : Assets.jumpControlOff;
			s.setScale(0.9f * jump_scale);
			drawAtCenter(batch, s, WorldRenderer.FRUSTUM_WIDTH / 2 - 76, mid_y);
		}
	}
	
	@Override
	public boolean isLeftJustPressed() {
		return left_just_touched;
	}
	
	@Override
	public boolean isRightJustPressed() {
		return right_just_touched;
	}
	
	@Override
	public void setSlide(float slide) {
		this.slide_ = slide;
	}
	
	@Override
	public void setSidesButtonsScale(float scale) {
		sides_scale = scale;
	}
	
	@Override
	public void setJumpButtonScale(float scale) {
		jump_scale = scale; 
	}
	
	@Override
	public void show() {
		show = true;
	}
	
	@Override
	public void hide() {
		show = false;
	}
	
	@Override
	public void enable() {
		enabled = true;
	}
	
	@Override
	public void disable() {
		enabled = false;
	}
	
	@Override
	public void reset() {
		enabled = false;
		show = false;
		sides_scale = 0;
		jump_scale = 0;
		slide_ = 1;
	}
	
}
