package com.retrom.volcano.game.objects;

public class FlamethrowerWall extends Wall {
	
	public static final float TIME_UNTIL_FLAME_STARTS = 1.1f;
	public Flame flame_;
	public boolean wasTurnedOff = false;

	
	private static float xOfCol(int col) {
		return (col - Wall.NUM_COLS/2) * SIZE + SIZE/2; 
	}
	
	public FlamethrowerWall(int col, float y) {
		super(xOfCol(col), y, Wall.SIZE, Wall.SIZE, col, -1);
	}
	
	public boolean isFlameOn() {
		System.out.println("stateTime_=" + stateTime_ );
		System.out.println("TIME=" + (TIME_UNTIL_FLAME_STARTS + Flame.DURATION - Flame.OFF_ANIM_TIME));
		
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
}
