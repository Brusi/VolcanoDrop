package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.Lava;
import com.retrom.volcano.game.Utils;
import com.retrom.volcano.game.World;

public class LavaSurfaceBubbleParticle extends Particle {
	
	private static float SPEED = 600f;
	
	private static float DURATION = 4;
	
	private float rotation_;

	public LavaSurfaceBubbleParticle(Vector2 position) {
		super(Assets.bubbleParticle.random(), DURATION, position.cpy(), getInitVel());
		rotation_ = Utils.randomRange(0, 360f);
	}
	
	private static Vector2 getInitVel() {
		Vector2 vec = Utils.randomDirOnlyUp();
		vec.x *= SPEED * Math.random();
		vec.y *= SPEED * Math.random();
		return vec;
	}
	
	@Override
	protected void childSpecificUpdating(float deltaTime) {
		velocity_.y += World.gravity.y * deltaTime * 0.5;
//		rotation_ += rotationSpeed_ * deltaTime;
	}

//	@Override
//	public void update(float deltaTime) {
//		super.update(deltaTime);
//		this.position_.x = (node1.x + node2.x) / 2;
//		this.position_.y = (node1.y + node2.y) / 2 + lava_.finalY() + 6;
//		
//		rotation_ = Utils.getDir(node1.x, node1.y, node2.x, node2.y);
//		if (Math.abs(rotation_) > 25) {
//			this.destroy();
//		}
//	}
//	
//	@Override
//	public float getAlpha() {
//		return Math.min(1, Math.max(0, (duration() - stateTime()) * 5));
//	}
//	
//	@Override
//	public float getScale() {
//		return stateTime() / duration();
//	}
//	
	@Override
	public float getRotation() {
		return rotation_;
	}
}
