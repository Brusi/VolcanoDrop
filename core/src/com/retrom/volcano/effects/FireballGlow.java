package com.retrom.volcano.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.objects.TopFireball;

public class FireballGlow extends OneFrameEffect {
	
	private static float DURATION = 5f;
	private final TopFireball fireball_;

	public FireballGlow(TopFireball fireball) {
		super(Assets.burningWallGlow, DURATION, fireball.position.cpy());
		this.fireball_ = fireball;
	}
	
	@Override
	protected void childSpecificUpdating() {
		this.position_.y = fireball_.position.y - 30f;
	}
	
	@Override
	public Sprite sprite() {
		Sprite $ = super.sprite();
		setTint($, getGlowAlpha());
		return $;
	}

	private void setTint(Sprite $, float glowAlpha) {
		$.setColor(glowAlpha, glowAlpha, glowAlpha, glowAlpha);
		
	}
	
	@Override
	public float getScale() {
		return 0.5f;
	}
	
	private float getGlowAlpha() {
		if (fireball_.state() == Effect.STATE_DONE) {
			return 0;
		}
		return 1;
	}
}
