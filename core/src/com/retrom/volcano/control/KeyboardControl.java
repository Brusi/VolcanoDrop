package com.retrom.volcano.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyboardControl extends AbstractControl {
	
	private boolean leftWasTouched;
	private boolean left_just_touched;
	private boolean rightWasTouched;
	private boolean right_just_touched;

	private boolean leftNewTouch() {
		boolean leftTouchedNow = getXImpl() < 0;
		boolean $ = false;
		if (!leftWasTouched && leftTouchedNow) {
			$ = true;
		}
		leftWasTouched = leftTouchedNow;
		left_just_touched = $;
		return $;
	}
	
	private boolean rightNewTouch() {
		boolean rightTouchedNow = getXImpl() > 0;
		boolean $ = false;
		if (!rightWasTouched && rightTouchedNow) {
			$ = true;
		}
		rightWasTouched = rightTouchedNow;
		
		right_just_touched = $;
		return $;
	}
	
	@Override
	public boolean isAnalog() {
		return false;
	}

	@Override
	public boolean isJumpPressed() {
		return Gdx.input.isKeyJustPressed(Input.Keys.UP); 
	}
	
	public boolean isJumpPressedContinuously() {
		return Gdx.input.isKeyPressed(Input.Keys.UP);
	}
	
	@Override
	public float getDigitalXDir() {
		updateJustTouched();
		
		return getXImpl();
	}

	private float getXImpl() {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			return -1;
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			return +1;
		}
		return 0;
	}

	private void updateJustTouched() {
		leftNewTouch();
		rightNewTouch();
	}
	
	@Override
	public boolean isLeftJustPressed() {
		return left_just_touched;
	}
	
	@Override
	public boolean isRightJustPressed() {
		return right_just_touched;
	}
}
