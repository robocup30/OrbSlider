package com.hiddenpixels.orbSlider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;

import com.hiddenpixels.framework.Graphics;
import com.hiddenpixels.framework.implementation.AndroidGraphics;
import com.hiddenpixels.framework.implementation.AndroidImage;

public class ParticleSystem {

	List<Particle> particles = new ArrayList<Particle>();
	List<Particle> particlesToRemove = new ArrayList<Particle>();
	
	Bitmap tempBmp = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
	Canvas c = new Canvas(tempBmp);
	Paint paint = new Paint();
	
	public void updateParticles() {
		for (Particle p : particles) {
			p.update();
			if(p.lifespan <= p.age){
				particlesToRemove.add(p);
			}
		}
		for(Particle pr : particlesToRemove){
			particles.remove(pr);
		}	
		particlesToRemove.clear();
	}

	public void add(Particle p){
		particles.add(p);
	}
	
	public void draw(Graphics g) {
		AndroidGraphics ag = (AndroidGraphics) g;
		Canvas canvas = ag.getCanvas();
		
        if(tempBmp.isRecycled() || tempBmp.getWidth()!=canvas.getWidth() || tempBmp.getHeight()!=canvas.getHeight())
        {
            tempBmp.recycle();
            tempBmp = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Config.ARGB_8888);
            c.setBitmap(tempBmp);
        }
        
        c.drawColor(Color.TRANSPARENT, Mode.CLEAR);
        
//      paint.setXfermode(new PorterDuffXfermode(Mode.ADD));
//		paint.setColor(Color.argb(255, 255, 0, 0));
//		c.drawCircle(300, 300, 50, paint);
//		paint.setColor(Color.argb(255, 0, 255, 0));
//		c.drawCircle(325, 300, 50, paint);
//		paint.setColor(Color.argb(255, 0, 0, 255));
//		c.drawCircle(300, 325, 50, paint);
		
//        c.drawBitmap(((AndroidImage)Assets.ballParticle).bitmap, 100, 100, null);
//        c.drawColor(Color.argb(255, 255, 50, 50), Mode.SRC_ATOP);
//        c.drawBitmap(((AndroidImage)Assets.ballParticle).bitmap, 100, 100, null);
//        c.drawColor(Color.argb(255, 255, 50, 50), Mode.SRC_ATOP);
//        c.drawBitmap(((AndroidImage)Assets.ballParticle).bitmap, 100, 100, null);
//        c.drawColor(Color.argb(255, 255, 50, 50), Mode.SRC_ATOP);
        
        
		for (Particle p : particles) {
			p.draw(c);
		}
        
        
		canvas.drawBitmap(tempBmp, 0, 0, null);
		
	}

	public List<Particle> getParticles() {
		return particles;
	}

	public void setParticles(List<Particle> particles) {
		this.particles = particles;
	}

}
