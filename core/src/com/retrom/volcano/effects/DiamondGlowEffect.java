package com.retrom.volcano.effects;

import com.retrom.volcano.game.objects.Collectable;

public class DiamondGlowEffect extends Effect {
	
	private static float DURATION = 10f;
	public final Collectable diamond; 

	public DiamondGlowEffect(Collectable diamond) {
		super(DURATION, diamond.position);
		this.diamond = diamond;
	}

	@Override
	public <T> T accept(EffectVisitor<T> v) {
		return v.visit(this);
	}
	
	@Override
	protected void childSpecificUpdating(float deltaTime) {
		if (diamond.state() == Collectable.STATUS_CRUSHED || diamond.state() == Collectable.STATUS_TAKEN) {
			this.state_ = Effect.STATE_DONE;
		}
	}
}
