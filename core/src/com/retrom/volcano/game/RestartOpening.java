package com.retrom.volcano.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.menus.StaticGraphicObject;

public class RestartOpening implements Opening {
	
	private final StaticGraphicObject floor = new StaticGraphicObject(Assets.openingFloor, 0, -103);
	private final StaticGraphicObject fgRoots1 = new StaticGraphicObject(Assets.openingForegroundRoots1, 0, 280); 
	private final StaticGraphicObject fgRoots2 = new StaticGraphicObject(Assets.openingForegroundRoots2, -115, -92);

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(ShapeRenderer shapes, SpriteBatch batch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void renderTop(SpriteBatch batch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void renderForeground(SpriteBatch batch) {
		floor.render(batch);
		fgRoots1.render(batch);
		fgRoots2.render(batch);
	}

	@Override
	public void startScene() {
		// TODO Auto-generated method stub

	}

}
