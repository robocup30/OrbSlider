package com.hiddenpixels.orbSlider;

import android.graphics.Color;

import com.hiddenpixels.framework.Graphics;

class BallParticle {

	int x, y;
	String colorString;
	int size = 40;
	boolean delete = false;

	public BallParticle(int x, int y, String colorString) {
		this.x = x;
		this.y = y;
		this.colorString = colorString;
	}

	public void update() {
		size -= 3;
		if (size < 0) {
			delete = true;
		}
	}

	public void draw(Graphics g) {
		if (colorString.equals("red")) {
			g.fillOval(x - size / 2, y - size / 2, size, size,
					Color.RED);
		} else if (colorString.equals("green")) {
			g.fillOval(x - size / 2, y - size / 2, size, size,
					Color.GREEN);
		} else if (colorString.equals("blue")) {
			g.fillOval(x - size / 2, y - size / 2, size, size,
					Color.BLUE);
		}
	}
}