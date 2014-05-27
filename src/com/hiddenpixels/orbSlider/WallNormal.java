package com.hiddenpixels.orbSlider;

import com.hiddenpixels.framework.Graphics;

import android.graphics.Rect;


public class WallNormal extends Wall {

	int x, y;
	int size = 40;

	public WallNormal(int x, int y) {
		this.x = x * size;
		this.y = y * size;
		rect = new Rect(this.x - size / 2, this.y - size / 2,
				this.x + size / 2, this.y + size / 2);
	}

	void draw(Graphics g) {
//		g.fillRect(x - size / 2, y - size / 2, size, size, Color.WHITE);
	}

}
