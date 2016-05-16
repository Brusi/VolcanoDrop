package com.retrom.volcano.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.game.WorldRenderer;
import com.retrom.volcano.utils.TouchToPoint;

public interface ControlInput {
	public boolean isJumpPressed();
	public boolean isLeftPressed();
	public boolean isRightPressed();
	
	enum Keyboard implements ControlInput {
		INSTANCE;

		@Override
		public boolean isJumpPressed() {
			return Gdx.input.isKeyPressed(Input.Keys.UP);
		}

		@Override
		public boolean isLeftPressed() {
			return Gdx.input.isKeyPressed(Input.Keys.LEFT);
		}

		@Override
		public boolean isRightPressed() {
			return Gdx.input.isKeyPressed(Input.Keys.RIGHT);
		}
	}
	
	public static final float MID_X_100 = 190;
	public static final float MID_X_90 = 178;
	
	
	enum Touch implements ControlInput {
		INSTANCE_100(MID_X_100),
		INSTANCE_90(MID_X_90);
		
		public static final float CONTROL_HEIGHT = 300;
		
		final Rectangle leftRect;
		final Rectangle rightRect;
		final Rectangle jumpRect;
		
		static final TouchToPoint ttp = TouchToPoint.create();
		
		Touch(float midPoint) {
			leftRect = new Rectangle(
					-WorldRenderer.FRUSTUM_WIDTH / 2,
					-WorldRenderer.FRUSTUM_HEIGHT / 2,
					midPoint,
					CONTROL_HEIGHT);
			rightRect = new Rectangle(
					-WorldRenderer.FRUSTUM_WIDTH / 2 + midPoint,
					-WorldRenderer.FRUSTUM_HEIGHT / 2,
					midPoint,
					CONTROL_HEIGHT);
			jumpRect = new Rectangle(
					WorldRenderer.FRUSTUM_WIDTH / 2 - midPoint,
					-WorldRenderer.FRUSTUM_HEIGHT / 2,
					midPoint, CONTROL_HEIGHT);
		}
		
		private static boolean touchInRect(int i, Rectangle rect) {
			Vector2 pnt = ttp.toPoint(Gdx.input.getX(i), Gdx.input.getY(i));
			return rect.contains(pnt);
		}
		
		
		private static boolean isRectTouched(Rectangle rect) {
			for (int i=0; i < 20; i++) {
				if (!Gdx.input.isTouched(i)) {
					continue;
				}
				if (touchInRect(i, rect)) {
					return true;
				}
			}
			return false;
		}
		

		@Override
		public boolean isJumpPressed() {
			return isRectTouched(jumpRect);
		}

		@Override
		public boolean isLeftPressed() {
			return isRectTouched(leftRect);
		}

		@Override
		public boolean isRightPressed() {
			return isRectTouched(rightRect);
		}
	}
	
	public class Combine implements ControlInput{
		private ControlInput[] inputs;

		public Combine(ControlInput... inputs) {
			this.inputs = inputs;
		}
		
		@Override
		public boolean isJumpPressed() {
			for (ControlInput input : inputs) {
				if (input.isJumpPressed()) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean isLeftPressed() {
			for (ControlInput input : inputs) {
				if (input.isLeftPressed()) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean isRightPressed() {
			for (ControlInput input : inputs) {
				if (input.isRightPressed()) {
					return true;
				}
			}
			return false;
		}
	}
}
