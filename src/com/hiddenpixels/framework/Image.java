package com.hiddenpixels.framework;

import com.hiddenpixels.framework.Graphics.ImageFormat;


public interface Image {
    public int getWidth();
    public int getHeight();
    public ImageFormat getFormat();
    public void dispose();
}