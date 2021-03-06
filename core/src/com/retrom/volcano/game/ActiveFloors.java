package com.retrom.volcano.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.retrom.volcano.game.objects.Wall;

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
	
	private int maxrow = 0;
	private int minrow = 0;
	
	private static final List<Integer> allCols = Arrays.asList(0,1,2,3,4,5);
	private static final List<Integer> allColsButLast = Arrays.asList(0,1,2,3,4);
	
	private List<Integer> nextCols = allCols;
	private List<Integer> nextDualCols = allColsButLast;
	
	private final float SIZE = 80f;
	private final float BOTTOM = 0f;
	private final float LEFT = -3 * SIZE;

	ActiveFloors() {
		for (int i = 0; i < NUM_COLS; i++) {
			hist_[i] = 0;
		}
		generateRects();
	}
	
	public List<Rectangle> getRects() {
		return rects_;
	}
	
	public void addToColumn(int column) {
		totalBlocks_++;
		hist_[column]++;
		generateRects();
		
		maxrow = Math.max(maxrow, hist_[column]);
		minrow = maxrow;
		for (int val : hist_) {
			minrow = Math.min(minrow, val);
		}
		
		// Prepare nextCols.
		nextCols = new ArrayList<Integer>();
		int diff = maxrow - minrow;
		if (diff >= 3) {
			// Accept only mins
			for (int i=0; i < NUM_COLS; i++) {
				if (hist_[i] == minrow) {
					nextCols.add(i); 
				}
			}
		} else  if (diff == 2) {
			// Accept all but max
			for (int i=0; i < NUM_COLS; i++) {
				if (hist_[i] != maxrow) {
					nextCols.add(i); 
				}
			}
		} else {
			nextCols = allCols;
		}
		
		// Prepare next dual cols.
		nextDualCols = new ArrayList<Integer>();
		for (int col = 0; col < NUM_COLS - 1; col++) {
			if (hist_[col] == hist_[col+1] && nextCols.contains(col) && nextCols.contains(col+1)) {
				nextDualCols.add(col);
			}
		}
	}
	
	private Rectangle rectAt(int col, int row) {
		return new Rectangle(LEFT + col * SIZE, getRowY(row-1), SIZE, SIZE);
	}

	private float getRowY(int row) {
		return BOTTOM + (row) * SIZE;
	}

	private void generateRects() {
		rects_.clear();
		for (int col = 0; col < NUM_COLS; col++) {
			int row = hist_[col];
			while (row >= 0) {
				rects_.add(rectAt(col, row));
				--row;
				if ((col == 0 || hist_[col-1] >= row) && (col == NUM_COLS-1 || hist_[col+1] >= row)) {
					break;
				}
			}
		}
	}

	public int getTotalBlocks() {
		return totalBlocks_;
	}
	
	public float getBaseLine() {
		return getTotalBlocks() * Wall.SIZE / 6f;
	}
	
	/**
	 * Since we do not want a large difference between the heights, we allow
	 * each time only some cols to be dropped.
	 * 
	 * @return a list of the possible cols.
	 */
	public List<Integer> getNextPossibleCols() {
		return nextCols;
	}

	public int getMaxRow() {
		return maxrow;
	}
	
	public int getMinRow() {
		return minrow;
	}

	public float bottomLine() {
		return minrow * SIZE;
	}

	
	// Returns the next columns which are possible to drop a width-2 stones.
	public List<Integer> getNextPossibleDualCols() {
		return nextDualCols;
	}
	
	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < hist_.length; i++)
			s += hist_[i] + ",";
		return s;
	}

	public float getColY(int col) {
		return getRowY(hist_[col]);
	}
	
	public int getColumnFloor(int col) {
		return hist_[col];
	}
}
