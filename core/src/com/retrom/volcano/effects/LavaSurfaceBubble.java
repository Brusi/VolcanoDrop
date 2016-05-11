package com.retrom.volcano.effects;

import com.badlogic.gdx.math.Vector2;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.assets.SoundAssets;
import com.retrom.volcano.game.Lava;
import com.retrom.volcano.game.Utils;

public class LavaSurfaceBubble extends OneFrameEffect {
	
	public interface OnPopListener {
		public void pop(LavaSurfaceBubble bubble);
	}
	
	private Lava lava_;
	
	private final Lava.Node node1;
	private final Lava.Node node2;

	private float rotation_;
	
	private final float max_scale = Utils.randomRange(0.75f, 1f);

	private OnPopListener listener;
	
	private static float getDuration() {
		return Utils.randomRange(0.5f, 8);
	}
	
	public LavaSurfaceBubble(Lava lava, OnPopListener listener) {
		super(Assets.surfaceBubble.random(), getDuration(), new Vector2());
		lava_ = lava;
		this.listener = listener;
		
		int i = lava.getRandomSegmentIndexForSurfaceBubbles();
		node1 = lava_.getNode(i);
		node2 = lava_.getNode(i+1);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		this.position_.x = (node1.x + node2.x) / 2;
		this.position_.y = (node1.y + node2.y) / 2 + lava_.finalY() + 6;
		
		rotation_ = Utils.getDir(node1.x, node1.y, node2.x, node2.y);
		if (Math.abs(rotation_) > 25 || Math.abs(node1.vel_) > 150 || Math.abs(node1.vel_) > 150) {
			this.destroy();
		}
	}
	
	@Override
	public void destroy() {
		listener.pop(this);
		SoundAssets.playRandomSound(SoundAssets.lavaBubble, getScale() * 0.6f);
		lava_.hitAt(position_.x, -50, 20);
		super.destroy();
	}
	
//	@Override
//	public float getAlpha() {
//		return Math.min(1, Math.max(0, (duration() - stateTime()) * 5));
//	}
	
	@Override
	public float getScale() {
		return stateTime() / duration() * max_scale;
	}
	
	@Override
	public float getRotation() {
		return rotation_;
	}
}
