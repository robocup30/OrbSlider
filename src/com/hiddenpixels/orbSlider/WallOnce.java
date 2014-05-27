package com.hiddenpixels.orbSlider;

import com.hiddenpixels.framework.Graphics;

import android.graphics.Color;
import android.graphics.Rect;

public class WallOnce extends Wall {

	int x, y;
	int size = 40;
	boolean activated = false;
	Ball b = null;
	int animationSquareAlpha = 0;
	int animationSquareSize = 0;

	public WallOnce(int x, int y) {
		this.x = x * size;
		this.y = y * size;
		rect = new Rect(this.x - size / 2, this.y - size / 2,
				this.x + size / 2, this.y + size / 2);
	}

	public void update() {
		if (b != null && !Rect.intersects(rect, b.rect)) {
			if (!activated) {
				animationSquareAlpha = 255;
				animationSquareSize = 0;
				Assets.oneWay.play(.6f);
			}
			activated = true;
		}
		animationSquareAlpha -= 10;
		animationSquareSize += 3;
	}

	void draw(Graphics g) {
		if (activated) {
			if (animationSquareSize < size) {
				g.fillRect(x - animationSquareSize / 2, y - animationSquareSize
						/ 2, animationSquareSize, animationSquareSize,
						Color.argb(255, 255, 255, 255));
			} else {
				g.fillRect(x - size / 2, y - size / 2, size, size,
						Color.rgb(255, 255, 255));
			}
		} else {
			g.fillRect(x - 15 / 2, y - 15 / 2, 15, 15, Color.rgb(255, 255, 255));
		}
	}

}
