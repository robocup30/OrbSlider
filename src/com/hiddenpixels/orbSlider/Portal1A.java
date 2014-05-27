package com.hiddenpixels.orbSlider;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;

import com.hiddenpixels.framework.Graphics;
import com.hiddenpixels.framework.implementation.AndroidGraphics;

public class Portal1A extends Portal {

	Class<Portal1B> exit = Portal1B.class;
	AndroidGraphics ag;
	Canvas c;
	Paint p = new Paint();
	PortalAnimation1 pa;
	RectF rectF = new RectF();

	public Portal1A(int x, int y) {
		super(x, y);
		p.setAntiAlias(true);
		p.setColor(Color.MAGENTA);
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(3);
		pa = new PortalAnimation1(this.x, this.y);
	}

	public void update() {
		pa.update();
	}

	@Override
	public void draw(Graphics g) {
		ag = (AndroidGraphics) g;
		c = ag.canvas;
		rectF.set(x - size / 2 + 2, y - size / 2 + 2, x + size / 2
				- 4, y + size / 2 - 4);
		c.drawOval(rectF, p);
		pa.draw(c);
	}

	public void createParticle() {
		Particle p = new Particle(x, y, Assets.ballParticle);
		ArrayList<Integer> colors = new ArrayList<Integer>();
		p.setSize(.4, .4, 0.01, 1, 1);
		p.setDirection(0, 360, 0);
		p.setSpeed(.5, .7, -.02);
		p.setLifespan(35, 35);
		colors.add(Color.argb(0, 200, 80, 80));
		colors.add(Color.argb(255, 80, 200, 80));
		colors.add(Color.argb(0, 80, 80, 200));
		p.setColor(colors);
	}

	@Override
	public void teleportBall(Ball b) {
		for (Portal p2 : GameScreen.portalArray) {
			if (p2.getClass() == Portal1B.class) {
				p2.available = true;
				for (Ball b2 : GameScreen.ballArray) {
					if (Rect.intersects(p2.rect, b2.rect)) {
						p2.available = false;
					}
				}
				if (p2.available) {
					b.x = p2.x;
					b.y = p2.y;
					p2.ball = b;
					Assets.portal.play(MainActivity.soundVolume);
				} else {
					ball = b;
				}
			}
		}
	}
}

class PortalAnimation1 {
	int x, y;
	int size = 6;
	Paint p = new Paint();
	RectF rectF = new RectF();
	
	public PortalAnimation1(int x, int y) {
		this.x = x;
		this.y = y;
		p.setAntiAlias(true);
		p.setColor(Color.MAGENTA);
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(3);
	}

	public void update() {
		size += 1;
		if (size > 40) {
			size = 6;
		}
	}

	public void draw(Canvas c) {
		rectF.set(x - size / 2 + 2, y - size / 2 + 2, x + size / 2
				- 4, y + size / 2 - 4);
		c.drawOval(rectF, p);
	}

}