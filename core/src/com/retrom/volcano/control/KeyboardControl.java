package com.retrom.volcano.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class KeyboardControl extends AbstractControl {
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
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			return -1;
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			return +1;
		}
		return 0;
	}
}
