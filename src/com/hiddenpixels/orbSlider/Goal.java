package com.hiddenpixels.orbSlider;

import java.util.ArrayList;

import android.graphics.Color;

import com.hiddenpixels.framework.Graphics;

public class Goal {

	int size = 40;
	int x, y;
	String colorString;

	public Goal(int x, int y, String color) {
		this.x = x * size;
		this.y = y * size;
		this.colorString = color;
	}

	public void createParticle() {
		Particle p = new Particle(x, y, Assets.squareParticle);
		ArrayList<Integer> colors = new ArrayList<Integer>();
		p.setSize(.5, .5, 0, 1, .07);
		p.setDirection(0, 360, 0);
		p.setSpeed(.5, .7, -.02);
		p.setLifespan(30, 50);
		p.setOrientation(0, 0, 0, true);
		if (colorString == "red") {
			colors.add(Color.argb(0, 200, 70, 70));
			colors.add(Color.argb(255, 200, 70, 70));
			colors.add(Color.argb(0, 200, 70, 70));
			p.setColor(colors);
		} else if (colorString == "green") {
			colors.add(Color.argb(0, 70, 200, 70));
			colors.add(Color.argb(255, 70, 200, 70));
			colors.add(Color.argb(0, 70, 200, 70));
			p.setColor(colors);
		} else if (colorString == "blue") {
			colors.add(Color.argb(0, 70, 70, 200));
			colors.add(Color.argb(255, 70, 70, 200));
			colors.add(Color.argb(0, 70, 70, 200));
			p.setColor(colors);
		}
	}
	
	void draw(Graphics g) {

		if (colorString.equals("red")) {
			g.drawOval(x - size / 2 + 1, y - size / 2 + 1, size - 3, size - 3,
					Color.RED);
		} else if (colorString.equals("green")) {
			g.drawOval(x - size / 2 + 1, y - size / 2 + 1, size - 3, size - 3,
					Color.GREEN);
		} else if (colorString.equals("blue")) {
			g.drawOval(x - size / 2 + 1, y - size / 2 + 1, size - 3, size - 3,
					Color.BLUE);
		}
	}
}
