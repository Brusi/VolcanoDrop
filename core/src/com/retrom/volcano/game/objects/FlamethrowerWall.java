package com.retrom.volcano.game.objects;

import com.retrom.volcano.assets.SoundAssets;

public class FlamethrowerWall extends Wall {
	
	public static final float TIME_UNTIL_FLAME_STARTS = 1.1f;
	private static final float WARNING_TIME = 0.0f;
	public Flame flame_;
	public boolean wasTurnedOff = false;
	private boolean warned = false;
	
	long soundId;
	
	
	private static float xOfCol(int col) {
		return (col - Wall.NUM_COLS/2) * SIZE + SIZE/2; 
	}
	
	public FlamethrowerWall(int col, float y) {
		super(xOfCol(col), y, Wall.SIZE, Wall.SIZE, col, -1);
	}
	
	public boolean isFlameOn() {
		return this.flame_ != null && stateTime_ < TIME_UNTIL_FLAME_STARTS + Flame.DURATION - Flame.OFF_ANIM_TIME;
	}

	public void turnOff() {
		wasTurnedOff = true;
		if (this.flame_ != null) {
			this.stateTime_ = Math.max(stateTime_, TIME_UNTIL_FLAME_STARTS + Flame.DURATION - Flame.OFF_ANIM_TIME);
			this.flame_.turnOff();
		} else {
			this.stateTime_ = Math.max(stateTime_, TIME_UNTIL_FLAME_STARTS + Flame.DURATION - 0.15f);
		}
		SoundAssets.stopSound(SoundAssets.flamethrowerStart);
		SoundAssets.playSound(SoundAssets.flamethrowerEnd);
	}
	
	public Flame flame() {
		return flame_;
	}

	public void addFlame(Flame flame) {
		flame_ = flame;
	}
	
	public boolean shouldAddFlame() {
		if (wasTurnedOff)
			return false;
		if (this.status() != Wall.STATUS_INACTIVE) {
			return false;
		}
		if (flame() != null) {
			return false;
		}
		if (stateTime() < FlamethrowerWall.TIME_UNTIL_FLAME_STARTS) {
			return false;
		}
		return true;
	}
	
	@Override
	public void setStatus(int status) {
		super.setStatus(status);
		if (status() == Wall.STATUS_INACTIVE) {
			soundId = SoundAssets.playSound(SoundAssets.flamethrowerStart);
		}
	}

	public boolean shouldWarn() {
		if (this.status() != Wall.STATUS_INACTIVE) {
			return false;
		}
		return !warned && stateTime_ > WARNING_TIME;
	}

	public void setWarned() {
		warned = true;
	}
}
