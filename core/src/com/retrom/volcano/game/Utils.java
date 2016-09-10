package com.retrom.volcano.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.data.ShopEntry;
import com.retrom.volcano.game.objects.Wall;

public class Utils {

	/**
	 * Get the x value of the wall which fits the given column.
	 */
	public static float xOfCol(int col) {
		return (col - Wall.NUM_COLS / 2) * Wall.SIZE + Wall.SIZE / 2;
	}

	public static float dualXOfCol(int col) {
		return (col - Wall.NUM_COLS/2) * Wall.SIZE + Wall.SIZE;
	}
	
	public static Vector2 randomDir() {
		double angle = Math.random() * 2 * Math.PI;
		return new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
	}

	public static Vector2 randomDirOnlyUp() {
		Vector2 vec = randomDir();
		vec.y = Math.abs(vec.y);
		return vec;
	}
	
	public static Vector2 randomDir45Up() {
		double angle = Utils.randomRange((float) Math.PI / 4, 3 * (float) Math.PI / 4);
		return new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
	}
	
	public static Vector2 randomDir30Up() {
		double angle = Utils.randomRange((float) Math.PI / 3, 2 * (float) Math.PI / 3);
		return new Vector2((float)Math.cos(angle), (float)Math.sin(angle));
	}
	
	// Returns a random float between -limit and +limit.
	public static float random2Range(float limit) {
		return randomRange(-limit, limit); 
	}
	
	// Returns a random float between bottom and top.
	public static float randomRange(float bottom, float top) {
		return (float) (bottom + Math.random() * (top - bottom)); 
	}
	
	// Returns a random boolean.
	public static boolean randomBool() {
		return Math.random() > 0.5;
	}
	
	public static int randomInt(int x) {
		return (int) Math.floor(Math.random() * x);
	}
	
	public static void shuffleArr(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			int j = randomInt(arr.length);
			{
				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}
	}
	
	// Returns an array { 0, 1, 2, ..., length }
	public static int[] rangeArr(int length) {
		int[] arr = new int[length];
		for (int i=0; i < arr.length; i++) {
			arr[i] = i;
		}
		return arr;
	}
	
	public static int[] shuffledRangeArr(int length) {
		int[] arr = rangeArr(length);
		shuffleArr(arr);
		return arr;
	}
	
	public static void drawCenter(Batch batch, Sprite sprite, float x, float y) {
		sprite.setPosition(x - sprite.getRegionWidth()/2, y - sprite.getRegionHeight()/2);
		sprite.draw(batch);
	}
	
	public static Map<Integer, Sprite> createSpritesIndexMap(
			TextureAtlas sheet, String name) {
		Map<Integer, Sprite> prices = new HashMap<Integer, Sprite>();
		for (ShopEntry entry : ShopData.allShopEntries) {
			Sprite s = sheet.createSprite(name, entry.getPrice());
			if (s != null) {
				prices.put(entry.getPrice(), s);
			}
		}
		return prices;
	}

	public static float getDir(float x1, float y1, float x2, float y2) {
		float x = x2 - x1;
		float y = y2 - y1;
		return (float) (Math.atan2(y, x) / 2 / Math.PI * 360);
	}

	public static float radToDeg(float rad) {
		return (float) (rad / (2 * Math.PI) * 360f);
	}

	public static float clamp01(float val) {
		return Math.max(0, Math.min(1, val));
	}

	public static float clamp(float val, float min, float max) {
		return Math.max(min, Math.min(max, val));
	}

	public static int clamp(int val, int min, int max) {
		return Math.max(min, Math.min(max, val));
	}
}
