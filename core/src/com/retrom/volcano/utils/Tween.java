package com.retrom.volcano.utils;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.menus.GraphicObject;

// Library for Tween.

public abstract class Tween {
	public abstract void invoke(float t);
	
	public abstract static class tTransformTween extends Tween {
		protected final Tween tween;

		public tTransformTween(Tween tween) {
			this.tween = tween;
		}

		// Override transform func to create T-transformation Tween.
		abstract float transformFunc(float t);

		@Override
		public void invoke(float t) {
			tween.invoke(transformFunc(t));
		}
	}
	
	public static Tween easeIn(final Tween tween) {
		return new Tween.tTransformTween(tween) {
			@Override
			float transformFunc(float t) {
				return t * t;
			}
		};
	}
	
	public static Tween easeOut(final Tween tween) {
		return new Tween.tTransformTween(tween) {
			@Override
			float transformFunc(float t) {
				return 1 - (1 - t) * (1 - t);
			}
		};
	}
	
	public static Tween easeBothSin(final Tween tween) {
		return new Tween.tTransformTween(tween) {
			@Override
			float transformFunc(float t) {
				return (float) ((Math.cos(t * Math.PI - Math.PI) + 1) / 2);
			}
		};
	}
	
	public static Tween easeBothQuad(final Tween tween) {
		return new Tween.tTransformTween(tween) {
			@Override
			float transformFunc(float t) {
				if (t < 0.5f) {
					float s = t * 2;
					return s*s / 2;
				} else {
					float s = (t - 0.5f) * 2;
					return (1-(1-s)*(1-s)) * 0.5f + 0.5f;
				}
			}
		};
	}
	
	// Is it used? probably for closing door.
	public static class EaseBoth2 extends Tween {
		private final Tween tween;

		public EaseBoth2(Tween tween) {
			this.tween = tween;
		}

		@Override
		public void invoke(float t) {
			if (t < 0.5f) {
				float s = t * 2;
				float r = (float) ((Math.cos(s * Math.PI - Math.PI) + 1) / 2);
				tween.invoke(r * 0.95f);
				return;
			} else {
				float s = (t - 0.5f) * 2;
				float r = s * 0.05f + 0.95f;
				tween.invoke(r);
			}
		}
	}

	// t: 0 --------- 1.2 --- 1
	public static Tween bubble(final Tween tween) {
		return new Tween.tTransformTween(tween) {
			@Override
			float transformFunc(float t) {
				if (t < 0.7f) {
					return t / 0.7f * 1.2f;
				}
				return 1 + (1 - (t - 0.7f) / 0.3f) * 0.2f;
			}
		};
	}
	
	public static Tween bounce(final Tween tween) {
		return new Tween.tTransformTween(tween) {
			@Override
			float transformFunc(float t) {
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
		};
	}
	
	public static class MovePointTween extends Tween {
		private Vector2 point;
		
		boolean move_x = false;
		boolean move_y = false;
		
		private float from_x;
		private float from_y;
		private float to_x;
		private float to_y;

		private MovePointTween(Vector2 point) {
			this.point = point;
		}
		
		public MovePointTween from(float x, float y) {
			fromX(x);
			fromY(y);
			return this;
		}
		
		public MovePointTween fromX(float x) {
			move_x = true;
			from_x = x;
			return this;
		}
		
		public MovePointTween fromY(float y) {
			move_y = true;
			from_y = y;
			return this;
		}
		
		public MovePointTween to(float x, float y) {
			toX(x);
			toY(y);
			return this;
		}
		
		public MovePointTween toX(float x) {
			to_x = x;
			return this;
		}
		
		public MovePointTween toY(float y) {
			to_y = y;
			return this;
		}

		@Override
		public void invoke(float t) {
			if (move_x) point.x = from_x * (1 - t) + to_x * t;
			if (move_y) point.y = from_y * (1 - t) + to_y * t;
		}
	}
	
	public static MovePointTween movePoint(Vector2 point) {
		return new MovePointTween(point);
	}
	
	// Graphic Object modificators.
	
	public abstract static class GraphicObjectTween extends Tween {
		protected final GraphicObject gobj_;
		public GraphicObjectTween(GraphicObject gobj) {
			this.gobj_ = gobj;
		}
	}
	
	public static Tween scale(final GraphicObject gobj) {
		return new Tween.GraphicObjectTween(gobj) {
			@Override
			public void invoke(float t) {
				gobj_.setScale(t);
			}
		};
	}
	
	public static Tween alpha(final GraphicObject gobj) {
		return new Tween.GraphicObjectTween(gobj) {
			@Override
			public void invoke(float t) {
				gobj_.setAlpha(t);
			}
		};
	}
}
