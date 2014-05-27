package com.hiddenpixels.orbSlider;

import android.graphics.Color;
import android.graphics.Rect;

import com.hiddenpixels.framework.Graphics;

public class WallColored extends Wall {

	int x, y;
	int size = 40;
	boolean activated = false;
	Ball b = null;
	String colorString;

	public WallColored(int x, int y) {
		this.x = x * size;
		this.y = y * size;
		rect = new Rect(this.x - size / 2, this.y - size / 2,
				this.x + size / 2, this.y + size / 2);
		colorString = "red";
	}

	public WallColored(int x, int y, String color) {
		this.x = x * size;
		this.y = y * size;
		rect = new Rect(this.x - size / 2, this.y - size / 2,
				this.x + size / 2, this.y + size / 2);
		colorString = color;
	}

	void draw(Graphics g) {
		if (colorString == "red") {
			g.fillRect(x - size / 2, y - size / 2, size, size, Color.RED);
		} else if (colorString == "green") {
			g.fillRect(x - size / 2, y - size / 2, size, size, Color.GREEN);
		} else if (colorString == "blue") {
			g.fillRect(x - size / 2, y - size / 2, size, size, Color.BLUE);
		}
	}

}
