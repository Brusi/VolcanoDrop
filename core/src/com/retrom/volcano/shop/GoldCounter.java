package com.retrom.volcano.shop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.retrom.volcano.assets.Assets;
import com.retrom.volcano.data.ShopData;
import com.retrom.volcano.game.Utils;

public class GoldCounter {
	private Label score_text = new Label("0", new LabelStyle(Assets.scoreFont, Color.WHITE));;
	private final float y;
	private float alpha_ = 0;
	
	public GoldCounter(float y) {
		this.y = y;
		score_text.setY(y - score_text.getHeight() / 2);
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
		score_text.setColor(1, 1, 1, alpha_);
		score_text.draw(batch, 1);
		
		Sprite coin = Assets.scoreIcon; 
		coin.setAlpha(alpha_);
		Utils.drawCenter(batch, coin, x - coin.getWidth(), y);
	}

	public void setAlpha(float alpha) {
		alpha_ = alpha;
	}
}
