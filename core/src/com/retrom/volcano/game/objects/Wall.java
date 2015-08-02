package com.retrom.volcano.game.objects;

import java.util.Random;

import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.World;

public class Wall extends DynamicGameObject {
	
	public static final float SIZE = 80f;
	private static final int NUM_COLS = 6;
	private static final float GRAVITY_RATIO = 0.7f;
	
	public static int STATUS_ACTIVE = 1;
	public static int STATUS_INACTIVE = 2;
	public static int STATUS_GONE = 3;
	
	public int status;
	
	public final int graphic_;
	
	private final int col_;
	
	private static final Random rand = new Random();
	
	/**
	 * Get the x value of the wall which fits the given column.
	 */
	public static float xOfCol(int col) {
		return (col - NUM_COLS/2) * SIZE + SIZE/2; 
	}
	
	public Wall(int col, float y) {
		super(xOfCol(col), y, SIZE, SIZE);
		col_ = col;
		status = STATUS_ACTIVE;
		graphic_ = rand.nextInt(Assets.walls1.size);
	}

	public void update(float deltaTime) {
		if (status != STATUS_ACTIVE) {
			return;
		}
		velocity.add(0, World.gravity.y * deltaTime * GRAVITY_RATIO);
		bounds.y += velocity.y * deltaTime;
		bounds.getCenter(position);
	}

	public int col() {
		return col_;
	}
}
