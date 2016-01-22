package com.retrom.volcano.shop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.data.ShopData;

public class GoldCounter {
	private Label score_text = new Label("0", new LabelStyle(Assets.scoreFont, Color.WHITE));;
	private final float y = 365;
	
	public GoldCounter() {
		score_text.setY(y);
	}
	
	public void update() {
		score_text.setText("" + ShopData.getGold());
		float scale = 1f;
//		if (stateTime_ < POP_TIME) {
//			scale = 1f + POP_ADDED_SCALE * (POP_TIME - stateTime_) / POP_TIME;
//		}
		
		Assets.scoreFont.getData().setScale(scale);
		LabelStyle style = new LabelStyle(Assets.scoreFont, Color.WHITE);
		score_text.setStyle(style);
	}
	
	public void render(Batch batch) {
		float x = - score_text.getWidth() * score_text.getText().length / 2;
		score_text.setX(x);
		score_text.draw(batch, 1);
		
		Sprite coin = Assets.scoreIcon; 
		batch.draw(coin, x - coin.getWidth() * 1.2f, y - coin.getHeight() / 2 + score_text.getHeight() / 2);
	}
}
