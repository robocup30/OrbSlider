package com.hiddenpixels.orbSlider;

import android.graphics.Color;
import android.graphics.Paint;

import com.hiddenpixels.framework.Game;
import com.hiddenpixels.framework.Graphics;
import com.hiddenpixels.framework.Image;

public class LevelButton extends Button {
	int level;
	Paint p;
	boolean unlocked = false;
	MainActivity main;

	public LevelButton(int x, int y, Image image, int level, Game game) {
		super(x, y, image);
		this.level = level;
		main = (MainActivity) game;
		if (MainActivity.settings.getInt("level", 1) >= level) {
			unlocked = true;
		}
		p = new Paint();
		p.setTextSize(25);
		p.setTextAlign(Paint.Align.CENTER);
		p.setAntiAlias(true);
		p.setColor(Color.BLACK);
	}

	@Override
	public void draw(Graphics g) {
		if (unlocked) {
			g.drawImage(Assets.levelButton, x - Assets.levelButton.getWidth()
					/ 2, y - Assets.levelButton.getHeight() / 2);
			g.drawString(String.valueOf(level), x, y + 25 / 2 - 25 / 8, p);
		} else {
			g.drawImage(Assets.levelButtonLocked, x - Assets.levelButtonLocked.getWidth()
					/ 2, y - Assets.levelButtonLocked.getHeight() / 2);
			g.drawString(String.valueOf(level), x, y + 25 / 2 - 25 / 8, p);
		}
	}
}
