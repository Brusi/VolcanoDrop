package com.retrom.volcano.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.retrom.volcano.utils.TouchToPoint;



public abstract class MenuButton {
	
	protected static TouchToPoint ttp = TouchToPoint.create();
	
	public interface Action {
		public void act();
	}
	
	// Button rectangle in world/cam coordinates.
	protected final Rectangle rect;
	private final Action action;
	
	private boolean pressed = false;
	
	public MenuButton(Rectangle rect, Action action) {
		this.rect = rect;
		this.action = action;
	}
	
	// Check if button is clicked, if it does, invoke action and return true.
	public boolean checkClick() {
		if (Gdx.input.justTouched()) {
			if (rect.contains(ttp.toPoint(Gdx.input.getX(), Gdx.input.getY()))) {
				pressed = true;
			} else {
				pressed = false;
			}
		}
		if (Gdx.input.isTouched()) {
			if (!rect.contains(ttp.toPoint(Gdx.input.getX(), Gdx.input.getY()))) {
				pressed = false;
			}
		} else {
			if (isPressed()) {
				pressed = false;
				action.act();
				return true;
			}
		}
		return false;
	}
	
	public void render(Batch batch) {
		// A button does not have to be visible :)
	}

	public boolean isPressed() {
		return pressed;
	}
}
