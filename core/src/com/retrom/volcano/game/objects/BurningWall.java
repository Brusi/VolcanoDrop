package com.retrom.volcano.game.objects;

import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.assets.SoundAssets;
import com.retrom.volcano.game.WorldRenderer;

public class BurningWall extends Wall {
	
	boolean start_sound_played;
	boolean end_sound_played;
	
	public static final float TIME_WITHOUT_BURN = 0.5f;
	private static final int START_NUM_FRAMES = 20;
	public static final float TIME_START =  TIME_WITHOUT_BURN + START_NUM_FRAMES * WorldRenderer.FRAME_TIME;

	private static float xOfCol(int col) {
		return (col - Wall.NUM_COLS/2) * SIZE + SIZE/2; 
	}
	
	public BurningWall(int col, float y) {
		super(xOfCol(col), y, Wall.SIZE, Wall.SIZE, col, rand.nextInt(Assets.walls1.size));
	}
	
	@Override
	protected void childSpecificUpdating() {
		if (!start_sound_played && stateTime() > TIME_WITHOUT_BURN) {
			start_sound_played = true;
			SoundAssets.playSound(SoundAssets.burningWallStart);
		} else if (!end_sound_played && status() == Wall.STATUS_INACTIVE) {
			end_sound_played = true;
			SoundAssets.playSound(SoundAssets.burningWallEnd);
		}
	}
}
