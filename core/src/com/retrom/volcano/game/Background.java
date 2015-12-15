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
	
	private static final float BG_HEIGHT = 191;
	
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
		
		BACKGROUND_BASE(BG_HEIGHT),
		
		BACKGROUND_WORLD1_1(BG_HEIGHT, 0),
		BACKGROUND_WORLD1_2(BG_HEIGHT, 1),
		BACKGROUND_WORLD1_3(BG_HEIGHT, 2),
		BACKGROUND_WORLD1_4(BG_HEIGHT, 3),
		BACKGROUND_WORLD1_5(BG_HEIGHT, 4),
		BACKGROUND_WORLD1_6(BG_HEIGHT, 5),
		BACKGROUND_OVERLAY1_1(0, 0, true),
		BACKGROUND_OVERLAY1_2(0, 1, true),
		BACKGROUND_OVERLAY1_3(0, 2, true),
		BACKGROUND_OVERLAY1_4(0, 3, true),
		BACKGROUND_OVERLAY1_5(0, 4, true),
		BACKGROUND_OVERLAY1_6(0, 5, true),
		BACKGROUND_OVERLAY1_7(0, 6, true),
		BACKGROUND_OVERLAY1_8(0, 7, true);
		
		private float height_;
		private int index_;
		private boolean is_overlay_;
		
		Element(float height, int index, boolean is_overlay) {
			height_ = height;
			index_ = index;
			is_overlay_ = is_overlay;
		}
		
		Element(float height, int index) {
			this(height, index, false);
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
		
		public boolean is_overlay() {
			return is_overlay_;
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
	
	boolean last_bg_is_overlay_ = false;
	
	private float leftBaseY_ = BASE;
	private float rightBaseY_ = BASE;
	private float bgBaseY_ = BASE;
	private float y_ = 0f;
	
	private void addElement(Element e) {
		float heightAdded = e.height_;
		bgPillar.addLast(e);
		bgHeight += heightAdded;
	}

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
		
		while (bgHeight < y_ + HEIGHT) {
			AddOneBackground();
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

	private void AddOneBackground() {
		
		boolean again = true;

		// World 1:
		while (again) {
			again = false;
			if (!last_bg_is_overlay_ || Math.random() < 0.5) {
				int[] arr = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
				Utils.shuffle(arr);
				int num_overlays = rand.nextInt(5);
				for (int i=0; i < num_overlays; i++) {
					addElement(Element.values()[Element.BACKGROUND_OVERLAY1_1.ordinal() + arr[i]]);
				}
				addElement(Element.BACKGROUND_BASE);
				last_bg_is_overlay_ = true;
			} else {
				addElement(Element.values()[Element.BACKGROUND_WORLD1_1.ordinal() + rand.nextInt(6)]);
				last_bg_is_overlay_ = false;
			}
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
