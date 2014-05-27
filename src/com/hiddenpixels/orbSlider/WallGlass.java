package com.hiddenpixels.orbSlider;

import com.hiddenpixels.framework.Graphics;

import android.graphics.Color;
import android.graphics.Rect;

public class WallGlass extends Wall {

	int x, y;
	int size = 40;
	boolean activated = true;
	Ball b = null;
	int timeSinceShattered = 60;
	int sizeChange = 6;
	int accel = -1;

	public WallGlass(int x, int y) {
		this.x = x * size;
		this.y = y * size;
		rect = new Rect(this.x - size / 2, this.y - size / 2,
				this.x + size / 2, this.y + size / 2);
	}

	public void update() {
		
		if (!activated) {
			size += sizeChange;
			sizeChange += accel;
		}
	}

	void draw(Graphics g) {
		if (size > 0) {
			g.fillRect(x - size / 2, y - size / 2, size, size, Color.rgb(0, 255, 255));
		}
	}

	void shatter() {
		// GameScreen.wallArray.remove(this);
		activated = false;
		timeSinceShattered = 0;
		Assets.glass.play(.3f);
	}

}
