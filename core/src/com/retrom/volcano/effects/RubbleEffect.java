package com.retrom.volcano.effects;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.ActiveFloors;
import com.retrom.volcano.game.Lava;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.game.World;

public class RubbleEffect extends OneFrameEffect {

	private float rotation_;
	private final float rotationSpeed_;
	private final ActiveFloors floors_;
	private boolean floorHit_ = false;
	private float scale_ = Utils.randomRange(0.4f, 0.7f);
	
	private static float MAX_ROTATION = 1600f;
	private static float DURATION = 10f;
	private Lava lava_;

	public RubbleEffect(Vector2 position, ActiveFloors floors, Lava lava_) {
		super(Assets.wallParticles.random(), DURATION, position, new Vector2());
		this.floors_ = floors;
		this.lava_ = lava_;
		rotation_ = (float) (Math.random() * 360);
		rotationSpeed_ = (float) (Math.random() * MAX_ROTATION - MAX_ROTATION / 2);
	}
	
	@Override
	protected void childSpecificUpdating(float deltaTime) {
		velocity_.y += World.gravity.y * deltaTime * 3 / 4f;
		rotation_ += rotationSpeed_ * deltaTime;
		
		checkFloorHit(deltaTime);
		checkLavaHit(deltaTime);
	}

	private void checkLavaHit(float deltaTime) {
		if (lava_ == null) {
			return;
		}
		
		if (this.position_.y < lava_.finalY()) {
			lava_.hitAt(position_.x, this.velocity_.y / 4, 20);
			this.destroy();
		}
	}

	private void checkFloorHit(float deltaTime) {
		if (floorHit_) {
			return;
		}
		if (this.position_.y <= 0) {
			hitFloor(deltaTime);
			return;
		}
		
		List<Rectangle> rects = floors_.getRects();
		for (Rectangle rect : rects) {
			if (rect.contains(this.position_)) {
				hitFloor(deltaTime);
				return;
			}
		}
	}

	private void hitFloor(float deltaTime) {
		this.position_.y -= this.velocity_.y * deltaTime / 2;
		floorHit_ = true;
		this.velocity_.y = -this.velocity_.y / 5;
	}

	@Override
	public float getRotation() {
		return rotation_;
	}
	
	@Override
	public float getScale() {
		return scale_;
	}
}
