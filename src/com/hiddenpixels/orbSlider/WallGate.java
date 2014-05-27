package com.hiddenpixels.orbSlider;

import com.hiddenpixels.framework.Graphics;

import android.graphics.Color;
import android.graphics.Rect;

public class WallGate extends Wall {

	int x, y;
	int size = 40;
	boolean isOpen = false;

	public WallGate(int x, int y) {
		this.x = x * size;
		this.y = y * size;
		rect = new Rect(this.x - size / 2, this.y - size / 2,
				this.x + size / 2, this.y + size / 2);
	}

	public WallGate(int x, int y, boolean isOpen) {
		this.x = x * size;
		this.y = y * size;
		rect = new Rect(this.x - size / 2, this.y - size / 2,
				this.x + size / 2, this.y + size / 2);
		this.isOpen = isOpen;
	}

	void draw(Graphics g) {
		if (isOpen) {
			g.fillRect(x - size / 2, y - size / 2, size, size, Color.GRAY);
		} else {
			g.fillRect(x - size / 2, y - size / 2, size, size, Color.BLACK);
		}
	}

	public void toggleGate() {
		if (isOpen) {
			isOpen = false;
			for (Ball b : GameScreen.ballArray) {
				if (Rect.intersects(rect, b.rect)) {
					isOpen = true;
				}
			}
		} else {
			isOpen = true;
		}

	}

}
