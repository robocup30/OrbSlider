package com.hiddenpixels.orbSlider;

import com.hiddenpixels.framework.Graphics;
import com.hiddenpixels.framework.implementation.AndroidGraphics;

import android.graphics.Rect;

public abstract class Portal {

	int x, y;
	int size = 40;
	Ball ball = null;
	public Rect rect;
	public boolean available = true;

	public Portal(int x, int y) {
		this.x = x * size;
		this.y = y * size;
		rect = new Rect(this.x - size / 2, this.y - size / 2,
				this.x + size / 2, this.y + size / 2);
	}

	public abstract void teleportBall(Ball b);

	public void reset() {
		if (ball != null) {
			if (!Rect.intersects(rect, ball.rect)) {
				ball = null;
			}
		}
	}

	public void update() {
		
	}

	public void draw(Graphics g) {
		
	}
}
