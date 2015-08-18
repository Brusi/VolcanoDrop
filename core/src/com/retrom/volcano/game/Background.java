package com.retrom.volcano.game;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;

import com.retrom.volcano.game.objects.Wall;

/**
 * A class for generating environment images.
 * Generates random side pillars and background. 
 * @author Ori
 *
 */
public class Background {
	public enum Element {
		PILLAR_1(76f, 0),
		PILLAR_2(76f, 1),
		PILLAR_3(76f, 2),
		PILLAR_START(76f),
		PILLAR_END(76f),
		PILLAR_BIG_1(111f, 0),
		PILLAR_BIG_2(111f, 1),
		PILLAR_HOLE(76f),
		PILLAR_HOLE_BG(0),
		
		BACKGROUND_BASE(191f),
		BACKGROUND_WORLD1_1(191f, 0),
		BACKGROUND_WORLD1_2(191f, 1),
		BACKGROUND_WORLD1_3(191f, 2),
		BACKGROUND_WORLD1_4(191f, 3),
		BACKGROUND_WORLD1_5(191f, 4),
		BACKGROUND_WORLD1_6(191f, 5);
		
		private float height_;
		private int index_;
		
		Element(float height, int index) {
			height_ = height;
			index_ = index;
		}
		
		Element(float height) {
			this(height, 0);
		}
		public float height() {
			return height_;
		}
		
		public int index() {
			return index_;
		}
		
		public static Element pillar() {
			int index = rand.nextInt(3);
			switch (index) {
			case 0:	return PILLAR_1;
			case 1: return PILLAR_2;
			case 2: return PILLAR_3;
			}
			return null;
		}
		
		public static Element bigPillar() {
			return rand.nextInt(2) == 0 ? PILLAR_BIG_1 : PILLAR_BIG_2;
		}
	}
	
	private static final Random rand = new Random();
	
	public static final float HEIGHT = 1000f; 
	public static final float BASE = -3 * Wall.SIZE;
	
	// The minimal y value from which holes start to appear on walls.
	private static final float HOLE_MIN_HEIGHT = 900f;
	
	public Deque<Element> leftPillar = new LinkedList<Element>();
	private float leftHeight = 0f;
	
	public Deque<Element> rightPillar = new LinkedList<Element>();
	private float rightHeight = 0f;
	
	public Deque<Element> bgPillar = new LinkedList<Element>();
	private float bgHeight;
	
	public Deque<Float> leftHoleList = new LinkedList<Float>();
	public Deque<Float> rightHoleList = new LinkedList<Float>();
	
	
	private float leftBaseY_ = BASE;
	private float rightBaseY_ = BASE;
	private float bgBaseY_ = BASE;
	private float y_ = 0f;

	public void setY(float y) {
		y_ = y;
		// Add to the top of the pillars.
		while (leftHeight < y_ + HEIGHT) {
			float heightAdded = addToPillar(leftPillar, leftHoleList, leftHeight);
			leftHeight += heightAdded;
		}
		while (rightHeight < y_ + HEIGHT) {
			float heightAdded = addToPillar(rightPillar, rightHoleList, rightHeight);
			rightHeight += heightAdded;
		}
		
		while(bgHeight < y_ + HEIGHT) {
			Element e;
			if (Math.random() < 0.8) {
				e = Element.BACKGROUND_BASE;
			} else {
				e = Element.values()[Element.BACKGROUND_BASE.ordinal() + 1 + rand.nextInt(6)];
			}
			
			float heightAdded = e.height_;
			bgPillar.addLast(e);
			bgHeight += heightAdded;
		}
		
		// Remove from the bottom of the pillars.
		if (y_ > leftBaseY_ + HEIGHT) {
			leftBaseY_ = leftBaseY_ + leftPillar.getFirst().height();
			leftPillar.removeFirst();
		}
		
		if (y_ > rightBaseY_ + HEIGHT) {
			rightBaseY_ += rightPillar.getFirst().height();
			rightPillar.removeFirst();
		}
		
		if (y_ > bgBaseY_ + HEIGHT) {
			bgBaseY_ += bgPillar.getFirst().height();
			bgPillar.removeFirst();
		}
		
		
		if (leftHoleList.peek() != null && y_ > leftHoleList.peek() + HEIGHT) {
			System.out.println("Hole removed!");
			leftHoleList.removeFirst();
		}
	}

	private float addToPillar(Deque<Element> pillar, Deque<Float> holeList, float height) {
		float heightAdded = 0f;
		if (height >= HOLE_MIN_HEIGHT && rand.nextInt(10) == 0) {
//			pillar.addLast(Element.PILLAR_HOLE_BG);
			pillar.addLast(Element.PILLAR_HOLE);
			heightAdded += Element.PILLAR_HOLE.height();
			holeList.addLast(height + BASE);
			return heightAdded;
		}
		
		if (rand.nextInt(10) == 0) {
			pillar.addLast(Element.PILLAR_END);
			heightAdded += Element.PILLAR_END.height();
			
			// Possibly 2 big pillars in a row.
			int numBigPullars = rand.nextInt(2);
			for (int i=0; i <= numBigPullars; i++) {
				Element bigPillar = Element.bigPillar(); 
				pillar.addLast(bigPillar);
				heightAdded += bigPillar.height();
			}
			
			pillar.addLast(Element.PILLAR_START);
			heightAdded += Element.PILLAR_START.height();
			
			return heightAdded;
		}
		
		Element smallPillar = Element.pillar(); 
		pillar.addLast(smallPillar);
		heightAdded += smallPillar.height();
		return heightAdded;
	}


	public float leftBaseY() {
		return leftBaseY_;
	}
	
	public float rightBaseY() {
		return rightBaseY_;
	}
	
	public float bgBaseY() {
		return bgBaseY_;
	}
}
