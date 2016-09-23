package com.retrom.volcano.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BatchUtils {

	private BatchUtils() {}
	
	public static void setBlendFuncNormal(Batch batch) {
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void setBlendFuncAdd(Batch batch) {
		batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
	}

	public static void setBlendFuncScreen(Batch batch) {
		batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_COLOR);
	}

	public static void drawQuad(ShapeRenderer shapes,
								float x1, float y1,
                                float x2, float y2,
                                float x3, float y3,
                                float x4, float y4) {
		shapes.triangle(x1, y1, x2, y2, x3, y3);
		shapes.triangle(x1, y1, x3, y3, x4, y4);
	}

	public static void drawQuad(ShapeRenderer shapes,
                                float x1, float y1,
                                float x2, float y2,
                                float x3, float y3,
                                float x4, float y4,
                                Color bottomColor, Color topColor) {
		shapes.triangle(x1, y1, x2, y2, x3, y3, bottomColor, bottomColor, topColor);
		shapes.triangle(x1, y1, x3, y3, x4, y4, bottomColor, topColor, topColor);
	}
}
