package com.retrom.volcano.game;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Rectangle;

public class ActiveFloorsTest {
	
	ActiveFloors floors;
	
	private Rectangle rectAt(int col, int row) {
		return new Rectangle(80*(col-3), 80*(row-1), 80, 80);
	}
	
	private void checkRects(List<Rectangle> expected, List<Rectangle> actual) {
		assertEquals(expected.size(), actual.size());
		for (Rectangle rect : expected) {
			assertTrue(actual.contains(rect));
		}
	}
	
	@Before
	public void setUp() {
		floors = new ActiveFloors();
	}

	@Test
	public void zeroFloors() {
		assertEquals(0, floors.getRects().size());
	}
	
	@Test
	public void oneRect() {
		floors.addToColumn(0);
		assertEquals(rectAt(0,1), floors.getRects().get(0));
	}
	
	@Test
	public void anotherRect() {
		floors.addToColumn(1);
		assertEquals(rectAt(1,1), floors.getRects().get(0));
	}
	
	@Test
	public void someRects() {
		floors.addToColumn(0);
		floors.addToColumn(2);
		floors.addToColumn(4);
		
		checkRects(floors.getRects(), Arrays.asList(rectAt(0,1), rectAt(2,1), rectAt(4,1)));
	}
	
	@Test
	public void towerOfRects() {
		floors.addToColumn(3);
		floors.addToColumn(3);
		floors.addToColumn(3);
		
		checkRects(floors.getRects(), Arrays.asList(rectAt(3,1), rectAt(3,2), rectAt(3,3)));
	}
	
	@Test
	public void towerOfRectsMostLeft() {
		floors.addToColumn(0);
		floors.addToColumn(0);
		floors.addToColumn(0);
		
		checkRects(floors.getRects(), Arrays.asList(rectAt(0,1), rectAt(0,2), rectAt(0,3)));
	}
	
	@Test
	public void towerOfRectsMostRight() {
		floors.addToColumn(0);
		floors.addToColumn(0);
		floors.addToColumn(0);
		
		checkRects(floors.getRects(), Arrays.asList(rectAt(0,1), rectAt(0,2), rectAt(0,3)));
	}
	
	
	@Test
	public void PodiumShapedBottomNotCounted() {
		floors.addToColumn(1);
		floors.addToColumn(2);
		floors.addToColumn(2);
		floors.addToColumn(3);
		
		checkRects(floors.getRects(),
				Arrays.asList(rectAt(1, 1), rectAt(2, 2), rectAt(3, 1)));
	}
	
	@Test
	public void leftStepAllCount() {
		floors.addToColumn(1);
		floors.addToColumn(2);
		floors.addToColumn(2);
		
		checkRects(floors.getRects(),
				Arrays.asList(rectAt(1, 1), rectAt(2, 2), rectAt(2, 1)));
	}
	
	@Test
	public void cornerTrappedDoesNotCount() {
		floors.addToColumn(0);
		floors.addToColumn(0);
		floors.addToColumn(1);
		
		checkRects(floors.getRects(),
				Arrays.asList(rectAt(0, 2), rectAt(1, 1)));
	}
	
	@Test
	public void twoWholeRowsOnlyTowRowCounts() {
		floors.addToColumn(0);
		floors.addToColumn(1);
		floors.addToColumn(2);
		floors.addToColumn(3);
		floors.addToColumn(4);
		floors.addToColumn(5);
		floors.addToColumn(5);
		floors.addToColumn(4);
		floors.addToColumn(3);
		floors.addToColumn(2);
		floors.addToColumn(1);
		floors.addToColumn(0);
		
		checkRects(floors.getRects(),
				Arrays.asList(rectAt(0, 2), rectAt(1, 2), rectAt(2, 2), rectAt(3, 2), rectAt(4, 2), rectAt(5, 2)));
	}
	
	@Test
	public void wallsOfAPitAreCounted() {
		floors.addToColumn(0);
		floors.addToColumn(0);
		floors.addToColumn(1);
		floors.addToColumn(1);
		floors.addToColumn(2);
		floors.addToColumn(2);
		floors.addToColumn(4);
		floors.addToColumn(4);
		floors.addToColumn(5);
		floors.addToColumn(5);
		
		checkRects(Arrays.asList(rectAt(0, 2), rectAt(1, 2), rectAt(2, 2), rectAt(4, 2), rectAt(5, 2),
						rectAt(2,1), rectAt(4,1)), floors.getRects());
	}
	
	@Test
	public void highestFloorZero() {
		assertEquals(0, floors.getHighestFloor());
	}
	
	@Test public void highestFloorOne() {
		floors.addToColumn(3);
		assertEquals(1, floors.getHighestFloor());
	}
	
	@Test public void highestFloorMaxOfSomeColumns() {
		floors.addToColumn(2);
		floors.addToColumn(2);
		floors.addToColumn(2);
		floors.addToColumn(1);
		floors.addToColumn(1);
		floors.addToColumn(3);
		floors.addToColumn(4);
		
		assertEquals(3, floors.getHighestFloor());
	}
	
	@Test public void getMinMax() {
		floors.addToColumn(1);
		floors.addToColumn(1);
		floors.addToColumn(2);
		floors.addToColumn(2);
		floors.addToColumn(2);
		floors.addToColumn(2);
		floors.addToColumn(3);
		floors.addToColumn(4);
		floors.addToColumn(5);
		floors.addToColumn(0);
		
		assertEquals(4, floors.getMaxRow());
		assertEquals(1, floors.getMinRow());
	}
	
	@Test public void nextCols() {
		assertEquals(6, floors.getNextPossibleCols().size());
	}
	
	@Test public void nextColsWhenDiffIs1() {
		floors.addToColumn(0);
		floors.addToColumn(1);
		floors.addToColumn(4);
		assertEquals(6, floors.getNextPossibleCols().size());
	}
	
	@Test public void nextColsWhenDiffIs2() {
		floors.addToColumn(1);
		floors.addToColumn(1);
		floors.addToColumn(4);
		floors.addToColumn(4);
		assertEquals(4, floors.getNextPossibleCols().size());
	}
	
	@Test public void nextColsWhenDiffIs3() {
		floors.addToColumn(1);
		floors.addToColumn(1);
		floors.addToColumn(1);
		floors.addToColumn(4);
		floors.addToColumn(4);
		floors.addToColumn(3);
		assertEquals(3, floors.getNextPossibleCols().size());
	}
}
