package com.retrom.volcano.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;

/**
 * A class for generating the rectangles only the active floors.
 * Active floors are blocks which are not covered by other blocks and are possible to be collided.
 * (Non active floors are blocks that can never be touched by another object)
 */
public class ActiveFloors {
	private static final int NUM_COLS = 6;
	
	private final int[] hist_ = new int[NUM_COLS];
	private int totalBlocks_ = 0; 
	private final List<Rectangle> rects_ = new ArrayList<Rectangle>();
	
	private final float SIZE = 80f;
	private final float BOTTOM = 0f;
	private final float LEFT = -3 * SIZE;
	
	ActiveFloors() {
		for (int i = 0; i < NUM_COLS; i++) {
			hist_[i] = 0;
		}
	}
	
	public List<Rectangle> getRects() {
		return rects_;
	}
	
	public void addToColumn(int col) {
		totalBlocks_++;
		hist_[col]++;
		generateRects();
	}
	
	private Rectangle rectAt(int col, int row) {
		return new Rectangle(LEFT + col * SIZE, BOTTOM + (row-1) * SIZE, SIZE, SIZE);
	}

	private void generateRects() {
		rects_.clear();
		for (int col = 0; col < NUM_COLS; col++) {
			int row = hist_[col];
			while (row != 0) {
				rects_.add(rectAt(col, row));
				--row;
				if ((col == 0 || hist_[col-1] >= row) && (col == NUM_COLS-1 || hist_[col+1] >= row)) {
					break;
				}
			}
		}
	}

	public int getHighestFloor() {
		int maxval = 0;
		for (int col = 0; col < NUM_COLS; col++) {
			maxval = Math.max(maxval, hist_[col]);
		}
		return maxval;
	}
	
	public int getTotalBlocks() {
		return totalBlocks_;
	}
	
	
}
