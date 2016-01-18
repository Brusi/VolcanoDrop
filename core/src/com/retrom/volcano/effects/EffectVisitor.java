package com.retrom.volcano.effects;

public interface EffectVisitor<T> {
	public T visit(Score1Effect score1Effect);
	public T visit(Score3Effect score3Effect);
	public T visit(Score4Effect score4Effect);
	public T visit(Score5Effect score5Effect);
	public T visit(Score6Effect score6Effect);
	public T visit(Score10Effect score10Effect);
	public T visit(Score15GreenEffect score15GreenEffect);
	public T visit(Score15PurpleEffect score15PurpleEffect);
	public T visit(Score15TealEffect score15TealEffect);
	public T visit(Score25Effect score25Effect);
	public T visit(FiniteAnimationEffect finiteAnimationEffect);
	public T visit(OneFrameEffect oneFrameEffect);
	public T visit(FireballStartEffect oneFrameEffect);
	public T visit(FlameEffect flameEffect);
	public T visit(FireballAnimationEffect fireballAnimationEffect);
	public T visit(DiamondGlowEffect diamondGlowEffect);
	public T visit(PowerupGlow effect);
	public T visit(PlayerShieldEffect playerShieldEffect);
	public T visit(PlayerMagnetEffect playerMagnetEffect);
	public T visit(PlayerOnionSkinEffect playerOnionSkinEffect);
	public T visit(WarningSkullEffect effect);
	public T visit(WarningExclEffect effect);
}
