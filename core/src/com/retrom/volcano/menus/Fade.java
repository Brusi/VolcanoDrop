package com.retrom.volcano.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.retrom.volcano.utils.Tween;

public class Fade {
	private static final int FRUSTUM_WIDTH = Gdx.graphics.getWidth();
	private static final int FRUSTUM_HEIGHT = Gdx.graphics.getHeight();

	private final Color color_;
	
	private float alpha_;
	
	public final Tween out = new Tween() {
		@Override
		public void invoke(float t) {
			setAlpha(t);
		}
	}; 
	
	public final Tween in = new Tween() {
		@Override
		public void invoke(float t) {
			setAlpha(1-t);
		}
	};
	
	public Fade(Color color) {
		color_ = color;
	}
		
	// Black fade.
	public Fade() {
		this(new Color(0,0,0,1));
	}
	
	public void render(ShapeRenderer shapes) {
		if (alpha_ == 0) {
			return;
		}
		
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	    Gdx.gl.glEnable(GL20.GL_BLEND);
	    shapes.begin(ShapeType.Filled);
	    color_.set(color_.r, color_.g, color_.b, alpha_);
	    shapes.setColor(color_);
	    shapes.rect(- FRUSTUM_WIDTH, - FRUSTUM_HEIGHT, FRUSTUM_WIDTH*2, FRUSTUM_HEIGHT*2);
	    shapes.end();
	}
	
	public void setAlpha(float alpha) {
		alpha_ = alpha;
	}
}
