package com.retrom.volcano.menus;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.retrom.volcano.utils.TouchToPoint;
import com.sun.org.apache.xml.internal.utils.UnImplNode;



public abstract class MenuButton extends GraphicObject{
	
	protected static TouchToPoint ttp = TouchToPoint.create();
	
	public interface Action {
		public void act();
	}
	
	// Button rectangle in world/cam coordinates.
	protected final Rectangle rect;
	private final Action action;
	
	private boolean pressed = false;
	
	private boolean visible = true;
	protected float alpha_ = 1;
	protected float scale_ = 1;
	
	public MenuButton(Rectangle rect, Action action) {
		super(rect.x + rect.width / 2, rect.y + rect.height / 2);
		this.rect = rect;
		this.action = action;
	}
	
	// Check if button is clicked, if it does, invoke action and return true.
	public boolean checkClick() {
		if (!visible || alpha_ == 0) {
			pressed = false; 
			return false;
		}
		
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
	
	@Override
	public void render(Batch batch) {
		// A button does not have to be visible :)
	}

	public boolean isPressed() {
		return pressed;
	}
	
	public boolean isVisible() { return visible; }
	public void show() { visible = true; } 
	public void hide() { visible = false; }
	
	public float getX() {
		return rect.x + rect.width / 2;
	}
	public float getY() {
		return rect.y + rect.height / 2;
	}

	public void setAlpha(float alpha) {
		alpha_  = alpha;
	}
	
	public void setScale(float scale) {
		scale_ = scale;
	}
	
	@Override
	protected Sprite getSprite() {
		// Buttons are not using the getSprite mechanism.
		throw new NotImplementedException();
	}
	
}
