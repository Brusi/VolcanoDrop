package com.retrom.volcano.control;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class AbstractControl {
	
	// There are two types of controls:
	// - digital control: either left/right is pressed or not, and has acceleration effect.
	// - analog control: left/right is continuous, and speed is set to an absolute value.
	
	abstract public boolean isAnalog();
	
	public float getAnalogXVel() {
		throw new UnsupportedOperationException("Not implemented or not supported by this controller.");
	}
	
	public float getDigitalXDir() {
		throw new UnsupportedOperationException("Not implemented or not supported by this controller.");
	}
	
	abstract public boolean isJumpPressed();
	abstract public boolean isJumpPressedContinuously();
	
	public void render(SpriteBatch batch) {
		// If controls needs rendering, they will override this.
		return;
	}
}
