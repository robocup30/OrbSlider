package com.hiddenpixels.framework.implementation;

import android.graphics.Bitmap;

import com.hiddenpixels.framework.Image;
import com.hiddenpixels.framework.Graphics.ImageFormat;

public class AndroidImage implements Image {
	public Bitmap bitmap;
	ImageFormat format;

	public AndroidImage(Bitmap bitmap, ImageFormat format) {
		this.bitmap = bitmap;
		this.format = format;
	}

	@Override
	public int getWidth() {
		return bitmap.getWidth();
	}

	@Override
	public int getHeight() {
		return bitmap.getHeight();
	}

	@Override
	public ImageFormat getFormat() {
		return format;
	}

	@Override
	public void dispose() {
		bitmap.recycle();
	}
}
