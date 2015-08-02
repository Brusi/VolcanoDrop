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
		
		BACKGROUND(213f);
		
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
	
	public Deque<Element> leftPillar = new LinkedList<Element>();
	private float leftHeight = 0f;
	
	public Deque<Element> rightPillar = new LinkedList<Element>();
	private float rightHeight = 0f;
	
	public Deque<Element> bgPillar = new LinkedList<Element>();
	private float bgHeight;
	
	
	private float leftBaseY_ = BASE;
	private float rightBaseY_ = BASE;
	private float bgBaseY_ = BASE;
	private float y_ = 0f;

	public void setY(float y) {
		y_ = y;
		// Add to the top of the pillars.
		while (leftHeight < y_ + HEIGHT) {
			float heightAdded = addToPillar(leftPillar);
			leftHeight += heightAdded;
		}
		while (rightHeight < y_ + HEIGHT) {
			float heightAdded = addToPillar(rightPillar);
			rightHeight += heightAdded;
		}
		
		while(bgHeight < y_ + HEIGHT) {
			float heightAdded = Element.BACKGROUND.height_;
			bgPillar.addLast(Element.BACKGROUND);
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
		
	}

	private float addToPillar(Deque<Element> pillar) {
		float heightAdded = 0f;
		if (rand.nextInt(10) == 0) {
			pillar.addLast(Element.PILLAR_END);
			heightAdded += Element.PILLAR_END.height();
			
			Element bigPillar = Element.bigPillar(); 
			pillar.addLast(bigPillar);
			heightAdded += bigPillar.height();
			
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
