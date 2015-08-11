package com.retrom.volcano.effects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.game.objects.BurningWall;
import com.retrom.volcano.game.objects.Enemy;
import com.retrom.volcano.game.objects.TopFireball;
import com.retrom.volcano.game.objects.Wall;

public class FireballAnimationEffect extends Effect {
	
	private static float DURATION = 5f;
	private final TopFireball fireball_;

	public FireballAnimationEffect(TopFireball fireball) {
		super(DURATION, fireball.position);
		this.fireball_ = fireball;
	}
	
	@Override
	protected void childSpecificUpdating() {
		if (fireball_.state() == Enemy.STATE_DONE) {
			state_ = Effect.STATE_DONE;
		}
	}
	
	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
}
