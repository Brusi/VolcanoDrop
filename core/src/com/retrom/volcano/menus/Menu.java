package com.retrom.volcano.menus;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Menu {
	protected final List<MenuButton> buttons = new ArrayList<MenuButton>();
	protected final List<GraphicObject> graphics = new ArrayList<GraphicObject>();
	
	public void render(SpriteBatch batch) {
		for (GraphicObject graphic : graphics) {
			graphic.render(batch);
		}
		for (MenuButton button : buttons) {
			button.render(batch);
		}
	}
	
	public void update(float deltaTime) {
		for (MenuButton button : buttons) {
			button.checkClick();
		}		
	}
}
