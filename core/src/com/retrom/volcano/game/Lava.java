package com.retrom.volcano.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Lava {
	
	private static final float LAVA_HEIGHT_NONE = -WorldRenderer.FRUSTUM_HEIGHT / 2 + 250;
	public static final float SPACING = 14;
	public static final float WIDTH = WorldRenderer.FRUSTUM_WIDTH - 150;
	private static final float FEEDBACK = 20f;
	private static final float NEIGHBOR_FORCE = 60f / 3 * 2;
	private static final float LINE_WIDTH = 8;
	private static final float CONST_OFFSET = -400f;
	
	public static enum State {
		OPENING,  // Only to see the lava at the bottom of the opening.
		AFTER_OPENING,    // Rise a little after the opening scene.
		NONE,     // Invisible. Out of screen.
		HARMLESS, // Visible but too low to cause (extreme cases are possible). 
		LOW,      // May harm you only if you are very low.
		MEDIUM,   // You better stay on high ground, always possible.
		HIGH;     // For hand-sewn sequences!
	}
	
	public static enum Level {
		
	}
	
	public static class Node {
		public final float x;
		public float y = 0;
		public float vel_ = 0;
		
		private float force_ = 0;
		
		Node(float x) {
			this.x = x;
		}
		
		void setForce(float force) {
			force_ = force;
		}
		
		void update(float deltaTime) {
			vel_ += force_ * deltaTime;
			y += vel_ * deltaTime;
			vel_ *= Math.pow(0.4, deltaTime);
		}
	}
	
	// TODO: do not render lava if out of screen.
	
	private final Node[] nodes;
	private float height_ = LAVA_HEIGHT_NONE;
	private float target_height_ = LAVA_HEIGHT_NONE;
	
	public float cam_y;
	
	State state_ = State.OPENING;
	
	public Lava() {
		nodes = new Node[(int)(WIDTH / SPACING) + 1];
		for (int i=0; i < nodes.length; i++) {
			nodes[i] = new Node(i * SPACING - WIDTH/2);
		}
	}
	
	public void update(float deltaTime) {
		for (int i=0; i < nodes.length; i++) {
			float force = -nodes[i].y * FEEDBACK;
			force += (((i > 0) ? (nodes[i-1].y) : 0) - nodes[i].y) * NEIGHBOR_FORCE; 
			force += (((i < nodes.length-1) ? (nodes[i+1].y) : 0) - nodes[i].y) * NEIGHBOR_FORCE;
			
			// Add ripple
//			force += Utils.random2Range(10000 * deltaTime);
//			force += Math.sin(i) * 10000 * deltaTime;
			
			nodes[i].setForce(force);
		}
		
		for (int i=0; i < nodes.length; i++) {
			nodes[i].update(deltaTime);
		}
		height_ += (target_height_ - height_) * deltaTime / 2;
	}
	
	public float finalY() {
		if (state_ == State.OPENING) {
			return -170;
		}
		if (state_ == State.AFTER_OPENING) {
			return -20;
		}
		return cam_y + CONST_OFFSET + height_;
	}
	
	private void drawQuad(ShapeRenderer shapes, float x1, float y1, float x2, float y2, float x3,
			float y3, float x4, float y4) {
		shapes.triangle(x1, y1, x2, y2, x3, y3);
    	shapes.triangle(x1, y1, x3, y3, x4, y4);
	}
	
	private void drawQuad(ShapeRenderer shapes, float x1, float y1, float x2, float y2, float x3,
			float y3, float x4, float y4, Color bottomColor, Color topColor) {
		shapes.triangle(x1, y1, x2, y2, x3, y3, bottomColor, bottomColor, topColor);
    	shapes.triangle(x1, y1, x3, y3, x4, y4, bottomColor, topColor, topColor);
	}
	
	public void render(SpriteBatch batch, ShapeRenderer shapes) {
		
//		Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE);
//		Color bottomColor = new Color(1, 0.2f, 0, 1);
//		Color topColor = new Color(1, 0.2f, 0, 0);
//		shapes.rect(-WIDTH/2, finalY()-50, WIDTH, 100, bottomColor, bottomColor, topColor, topColor);
		
		for (int i=0; i < nodes.length - 1; i++) {
			{
				// Draw topline outerglow.
				Color topColor = new Color(.61f, .04f, .02f, 0); 
				Color bottomColor = new Color(.61f, .04f, .02f, 1);
	        	drawQuad(shapes, nodes[i].x, nodes[i].y + finalY(), nodes[i + 1].x, nodes[i + 1].y + finalY(), nodes[i+1].x, finalY() + 36, nodes[i].x, finalY() + 36, bottomColor, topColor);
			}
        }
		
        // Draw 75% redbg.
		for (int i=0; i < nodes.length - 1; i++) {
        	shapes.setColor(1, 0.2f, 0, 0.75f);
        	drawQuad(shapes, nodes[i].x, nodes[i].y + finalY(), nodes[i + 1].x, nodes[i + 1].y + finalY(), nodes[i+1].x, -1000, nodes[i].x, -1000);
        	// Orange ripple line.
        	shapes.setColor(0.98f, 0.53f, 0.14f, 1);
			drawRippleLine(shapes, i, LINE_WIDTH, 0);
        }
		
		for (int i=0; i < nodes.length - 1; i++) {
	    	// Draw orange line.
	    	shapes.setColor(0.98f, 0.53f, 0.14f, 1);
			drawRippleLine(shapes, i, LINE_WIDTH, 0);
			
			{
				// Draw orange line glows.
				Color bottomColor = new Color(1f, .47f, 0, 0.5f);
				Color topColor = new Color(1f, .47f, 0, 0);
				drawQuad(shapes, nodes[i].x, nodes[i].y + finalY() + 4,
				         nodes[i + 1].x, nodes[i + 1].y + finalY() + 4,
				         nodes[i + 1].x, nodes[i+1].y + finalY() + 12,
				         nodes[i].x, nodes[i].y + finalY() + 12,
				         bottomColor, topColor);
		
				drawQuad(shapes, nodes[i].x, nodes[i].y + finalY() - 4,
				         nodes[i + 1].x, nodes[i + 1].y + finalY() - 4,
				         nodes[i + 1].x, nodes[i+1].y + finalY() - 19,
				         nodes[i].x, nodes[i].y + finalY() -19,
				         bottomColor, topColor);
			}
		}
		
		
		// Draw yellow line.
        for (int i=0; i < nodes.length - 1; i++) {
        	shapes.setColor(1, 0.8f, 0, 1);
        	{
        		// Yellow line glow:
	        	Color topColor = new Color(1f, .61f, .02f, 0); 
	        	Color bottomColor = new Color(1f, .61f, .02f, 0.5f);
				drawQuad(shapes, nodes[i].x, nodes[i].y + finalY() + 2 + LINE_WIDTH / 4,
						         nodes[i + 1].x, nodes[i + 1].y + finalY() + 2 + LINE_WIDTH / 4,
						         nodes[i + 1].x, nodes[i+1].y + finalY() + 5 + LINE_WIDTH / 4,
						         nodes[i].x, nodes[i].y + finalY() + 5 + LINE_WIDTH / 4,
						         bottomColor, topColor);
				
				drawQuad(shapes, nodes[i].x, nodes[i].y + finalY() - 2 + LINE_WIDTH / 4,
						         nodes[i + 1].x, nodes[i + 1].y + finalY() - 2 + LINE_WIDTH / 4,
						         nodes[i + 1].x, nodes[i+1].y + finalY() - 9 + LINE_WIDTH / 4,
						         nodes[i].x, nodes[i].y + finalY() - 9 + LINE_WIDTH / 4,
						         bottomColor, topColor);
        	}
			drawRippleLine(shapes, i, LINE_WIDTH / 2, LINE_WIDTH / 4);
        }
	}
	
	public void renderTop(SpriteBatch batch, ShapeRenderer shapes) {
		// Draw bottomfade.
		{
			Color bottomColor = new Color(0.53f, 0, 0, 1);
			Color topColor = new Color(0.53f, 0, 0, 0);
			shapes.rect(-WIDTH / 2, finalY() - 150, WIDTH, 150, bottomColor, bottomColor, topColor, topColor);
			shapes.rect(-WIDTH / 2, finalY() - 1150, WIDTH, 1000, bottomColor, bottomColor, bottomColor, bottomColor);
		}
	}
	
	private void drawRippleLine(ShapeRenderer shapes, int i, float width, float offset) {
		shapes.rectLine(nodes[i].x, nodes[i].y + finalY() + offset,
				        nodes[i + 1].x, nodes[i + 1].y + finalY() + offset, width);
		if (i != 0) {
			shapes.circle(nodes[i].x, nodes[i].y + finalY() + offset, width / 2);
		}
	}

	// Hit lava at some point and make a ripple in it.
	public void hitAt(float x, float strength, float width) {
		int first_i = Math.max(0,
				(int) Math.round((x - width / 2 + WIDTH / 2) / SPACING));
		int last_i = Math.min(nodes.length - 1,
				(int) Math.round((x + width / 2 + WIDTH / 2) / SPACING));
		for (int i = first_i; i <= last_i; i++) {
			//int i = (int)Math.round((x + WIDTH / 2) / SPACING);
			nodes[i].vel_ = strength / 2;
//			if (i > 0) nodes[i-1].vel_ = strength / 3;
//			if (i < nodes.length - 1) nodes[i+1].vel_ = strength / 3;
		}
	}

	public void hitRandom() {
		int i = Utils.randomInt(nodes.length);
		nodes[i].vel_ = Utils.random2Range(120);
	}

	public int getRandomSegmentIndexForSurfaceBubbles() {
		return Utils.randomInt(nodes.length-5) + 2;
	}
	
	public Node getNode(int i) {
		return nodes[i];
	}

	public void setState(Lava.State state) {
		state_ = state;
		switch(state_) {
		case NONE:
			target_height_ = LAVA_HEIGHT_NONE;
			break;
		case HARMLESS:
			target_height_ = -80;
			break;
		case LOW:
			target_height_ = 0;
			break;
		case MEDIUM:
			target_height_ = 50;
			break;
		case HIGH:
			target_height_ = 100;
			break;
		case OPENING:
			break;
		default:
			break;
		
		}
		
	}
}
