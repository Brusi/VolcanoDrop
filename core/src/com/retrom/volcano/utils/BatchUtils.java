package com.retrom.volcano.utils;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
}
