package com.retrom.volcano.control;

import com.badlogic.gdx.Gdx;

public class TiltAndTapPhoneControl extends AbstractControl {

	@Override
	public boolean isAnalog() {
		return true;
	}
	
	@Override
	public float getAnalogXVel() {
		float accel = Gdx.input.getAccelerometerX();
		if (Math.abs(accel) > 0.5) {
			return -200 * accel;
		}
		return 0;
	}
	
	@Override
	public boolean isJumpPressed() {
		return Gdx.input.justTouched();
	}
	
	@Override
	public boolean isJumpPressedContinuously() {
		return Gdx.input.isTouched();
	}
}
