package com.retrom.volcano.utils;

import com.badlogic.gdx.math.Vector2;

public interface Tween {
	public void invoke(float t);
	
	public static class EaseIn implements Tween {
		private final Tween tween;

		public EaseIn(Tween tween) {
			this.tween = tween;
		}

		@Override
		public void invoke(float t) {
			tween.invoke(t*t);
		}
	}
	
	public static class EaseOut implements Tween {
		private final Tween tween;

		public EaseOut(Tween tween) {
			this.tween = tween;
		}

		@Override
		public void invoke(float t) {
			tween.invoke(1-(1-t)*(1-t));
		}
	}
	
	public static class EaseBoth implements Tween {
		private final Tween tween;

		public EaseBoth(Tween tween) {
			this.tween = tween;
		}

		@Override
		public void invoke(float t) {
			float s = (float) ((Math.cos(t * Math.PI - Math.PI) + 1) / 2);
			System.out.println("s="+s);
			tween.invoke(s);
		}
	}
	
	public static class MovePoint implements Tween {
		private Vector2 point;
		private float from_x;
		private float from_y;
		private float to_x;
		private float to_y;

		public MovePoint(Vector2 point) {
			this.point = point;
		}
		
		public MovePoint from(float x, float y) {
			from_x = x;
			from_y = y;
			return this;
		}
		
		public MovePoint to(float x, float y) {
			to_x = x;
			to_y = y;
			return this;
		}

		@Override
		public void invoke(float t) {
			point.x = from_x * (1 - t) + to_x * t;
			point.y = from_y * (1 - t) + to_y * t;
		}
	}
	
	public static class Bounce implements Tween {
		private final Tween tween;

		public Bounce(Tween tween) {
			this.tween = tween;
		}
		
		private float getT(float t) {
			if (t < 0.5f) {
				float s = t * 2;
				return s*s;
			} else if (t < 0.75f) {
				float s = (t - 0.5f) * 4;
				return 1 - (s * (1-s) * 0.5f);
			} else if (t < 0.95) {
				float s = (t - 0.75f) / 0.20f;
				return 1 - (s * (1-s) * 0.20f);
			} else {
				float s = (t - 0.95f) / 0.05f;
				return 1 - (s * (1-s) * 0.04f);
			}
		}

		@Override
		public void invoke(float t) {
			tween.invoke(getT(t));
		}
	}
}
