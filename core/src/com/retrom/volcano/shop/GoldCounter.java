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
	private float scale_;
	
	public GoldCounter(float y) {
		this.y = y;
		score_text.setY(y - score_text.getHeight() / 2);
	}
	
	public void update() {
		score_text.setText("" + ShopData.getGold());
		if (scale_ > 0) {
			Assets.scoreFont.getData().setScale(scale_);
			LabelStyle style = new LabelStyle(Assets.scoreFont, Color.WHITE);
			score_text.setStyle(style);
		}
	}
	
	public void render(Batch batch) {
		float x = - score_text.getWidth() * score_text.getText().length / 2;
		if (scale_ > 0) {
			score_text.setX(x * scale_);
			score_text.draw(batch, 1);
		}
		
		Sprite coin = Assets.scoreIcon; 
		coin.setScale(scale_);
		Utils.drawCenter(batch, coin, (x - coin.getWidth() / 2 - 8) * scale_, y);
	}

	public void setScale(float scale) {
		scale_ = scale;
	}
}
