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
	
	public boolean isLeftJustPressed() {
		throw new UnsupportedOperationException("Not implemented or not supported by this controller.");
	}
	public boolean isRightJustPressed() {
		throw new UnsupportedOperationException("Not implemented or not supported by this controller.");
	}
	
	public void render(SpriteBatch batch) {
		// If controls needs rendering, they will override this.
		return;
	}
	
	public void show() {
		
	}
	
	public void hide() {
		
	}
	
	public void enable() {
		
	}
	
	public void disable() {
		
	}
	
	// Set slide position. 1 is completely in, 0 is completely out.
	public void setSlide(float slide) {
		// Do nothing; not all controls have graphics.
	}
	
	public void setSidesButtonsScale(float scale) {
		// Do nothing; not all controls have graphics.
	}
	
	public void setJumpButtonScale(float scale) {
		// Do nothing; not all controls have graphics.
	}

	public void reset() {
		// Do nothing... not all controls need resetting.
	}
}
