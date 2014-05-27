package com.hiddenpixels.orbSlider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.widget.ImageView;

import com.hiddenpixels.framework.Graphics;
import com.hiddenpixels.framework.Image;
import com.hiddenpixels.framework.implementation.AndroidGraphics;
import com.hiddenpixels.framework.implementation.AndroidImage;
import com.hiddenpixels.orbSlider.R;

public class Particle {

	int lifespan, colorCount, age;
	double speed, size, sizeChange, direction, directionChange, xScale, yScale,
			x, y, speedChange, orientation, orientationChange, percentPerSegment;
	boolean orientationRelative;
	List<Integer> colorList;
	Image image;
	Paint paint;
	AndroidImage ai;
	Bitmap bitmap;
	// int color;
	Random rand = new Random();

	public Particle(int x, int y, Image image) {
		this.x = x;
		this.y = y;
		this.lifespan = 1;
		this.age = 0;
		this.image = image;
		this.speed = 0;
		// this.color = Color.rgb(255, 255, 255);
		this.colorCount = 0;
		this.yScale = 1;
		this.xScale = 1;
		this.size = 1;
		this.direction = 0;
		this.sizeChange = 0;
		this.directionChange = 0;
		this.speedChange = 0;
		this.orientationRelative = false;
		this.orientation = 0;
		this.colorList = new ArrayList<Integer>();
		this.percentPerSegment = 0;
		colorList.add(Color.argb(255, 255, 255, 255));
		paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(Mode.OVERLAY));
		ai = (AndroidImage) image;
		bitmap = ai.bitmap;
		ColorFilter f = new PorterDuffColorFilter(colorList.get(0), Mode.SRC_IN);
		paint.setColorFilter(f);
		paint.setAntiAlias(true);

	}

	public void setLifespan(int min, int max) {
		lifespan = min + rand.nextInt(max - min + 1);
	}

	public void setOrientation(double min, double max,
			double orientationChange, boolean relative) {
		this.orientation = min + rand.nextDouble() * (max - min);
		this.orientationRelative = relative;
		this.orientationChange = orientationChange;
		// System.out.println(orientation);
	}

	public void setColor(int color) {
		colorList.clear();
		colorList.add(color);
		colorCount = 1;
		percentPerSegment = 0;
	}

	public void setColor(ArrayList<Integer> colors) {
		colorList.clear();
		colorList = colors;
		colorCount = colors.size();
		if (colorCount == 1) {
			percentPerSegment = 0;
		} else {
			percentPerSegment = (double) 1 / (colorCount - 1);
		}
	}

	public void setSpeed(double min, double max, double accel) {
		this.speed = min + rand.nextDouble() * (max - min);
		this.speedChange = accel;
	}

	public void setSize(double min, double max, double sizeChange,
			double xScale, double yScale) {
		this.size = min + rand.nextDouble() * (max - min);
		this.xScale = xScale;
		this.yScale = yScale;
		this.sizeChange = sizeChange;
	}

	public void setDirection(double min, double max, double change) {
		this.direction = min + rand.nextDouble() * (max - min);
		this.directionChange = change;
	}

	public void update() {
		size += sizeChange;
		x += speed * Math.cos(Math.toRadians(direction));
		y -= speed * Math.sin(Math.toRadians(direction));
		direction += directionChange;
		speed += speedChange;
		orientation += orientationChange;
		age++;
	}

	public void draw(Canvas c) {
		double percentCompleteTotal = (double) age / lifespan;
		int before;
		int after;
		double percentComplete = 1;
		double percentToGo = 1;
		ColorFilter f;
//		System.out.println("PERCENT IS " + percentPerSegment);
		if (percentPerSegment == 0) {
			before = colorList.get(0);
			after = before;
			percentComplete = 1;
			percentToGo = 1;
			f = new PorterDuffColorFilter(
					Color.argb(Color.alpha(colorList.get(0)),
							Color.red(colorList.get(0)),
							Color.green(colorList.get(0)),
							Color.blue(colorList.get(0))), Mode.SRC_IN);
		} else {
			before = colorList.get((int) Math.floor(percentCompleteTotal
					/ percentPerSegment));
			after = colorList.get((int) Math.ceil(percentCompleteTotal
					/ percentPerSegment));
			percentComplete = (percentCompleteTotal % percentPerSegment)
					/ percentPerSegment;
			percentToGo = 1 - (percentCompleteTotal % percentPerSegment)
					/ percentPerSegment;
			f = new PorterDuffColorFilter(Color.argb(
					(int) (Color.alpha(before) * percentToGo + Color
							.alpha(after) * percentComplete),
					(int) (Color.red(before) * percentToGo + Color
							.red(after) * percentComplete),
					(int) (Color.green(before) * percentToGo + Color
							.green(after) * percentComplete),
					(int) (Color.blue(before) * percentToGo + Color
							.blue(after) * percentComplete)), Mode.SRC_IN);
		}
		paint.setColorFilter(f);

		Matrix matrix = new Matrix();
		matrix.postTranslate((float) x - image.getWidth() / 2, (float) y
				- image.getHeight() / 2);
		matrix.postScale((float) (size * xScale), (float) (size * yScale),
				(float) x, (float) y);
		if (orientationRelative) {
			matrix.postRotate((float) (-orientation - direction), (float) x,
					(float) y);
		} else {
			matrix.postRotate((float) -orientation, (float) x, (float) y);
			// System.out.println(orientation);
		}

		c.drawBitmap(bitmap, matrix, paint);

	}
}

// int[] pixels = new int[image.getWidth() * image.getHeight()];
// bitmap.getPixels(pixels, 0, image.getWidth(), 0, 0, image.getWidth(),
// image.getHeight());
//
//
// for(int i = 0; i < pixels.length; i++){
// int alpha = Color.alpha(pixels[i]);
// int red = Color.red(pixels[i]);
// int green = Color.green(pixels[i]);
// int blue= Color.blue(pixels[i]);
// pixels[i] = Color.argb(alpha, 255, 50, 50);
// }
//
// bitmap.setPixels(pixels, 0, image.getWidth(), 0, 0, image.getWidth(),
// image.getHeight());
//
