package com.hiddenpixels.orbSlider;

import com.hiddenpixels.framework.Graphics;
import com.hiddenpixels.framework.Image;
import com.hiddenpixels.framework.Input.TouchEvent;

public class Button {

	int x, y;
	Image image;

	public Button(int x, int y, Image image) {
		this.x = x;
		this.y = y;
		this.image = image;
	}

	public void draw(Graphics g) {
		g.drawImage(image, x - image.getWidth() / 2, y - image.getHeight() / 2);
	}

	public boolean checkTouch(TouchEvent event) {
		if (inBounds(event, x - image.getWidth() / 2,
				y - image.getHeight() / 2, image.getWidth(), image.getHeight())) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}
}
