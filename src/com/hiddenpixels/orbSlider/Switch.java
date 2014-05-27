package com.hiddenpixels.orbSlider;

import android.graphics.Color;

import com.hiddenpixels.framework.Graphics;

public class Switch{

	int x, y;
	int size = 40;
	boolean isOpen = false;
	Ball ball;
	
	public Switch(int x, int y) {
		this.x = x * size;
		this.y = y * size;
		ball = null;
	}

	void draw(Graphics g) {
		g.fillOval(x - 10, y - 10, 20, 20, Color.BLACK);
	}

}
