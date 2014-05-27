package com.hiddenpixels.framework.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.StrictMode;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
	AndroidGame game;
	Bitmap framebuffer;
	Thread renderThread = null;
	SurfaceHolder holder;
	public static Canvas canvas;
	volatile boolean running = false;
	double deviceAspectRatio;
	double gameAspectRatio;
	static double viewPortWidth;
	static double viewPortHeight;
	public static Rect dstRect = new Rect();
	public static Rect srcRect = new Rect();
	// desired fps
	private final static int MAX_FPS = 60;
	// maximum number of frames to be skipped
	private final static int MAX_FRAME_SKIPS = 4;
	// the frame period
	private final static int FRAME_PERIOD = 1000 / MAX_FPS;
	static int extraTime = 0;

	public AndroidFastRenderView(AndroidGame game, Bitmap framebuffer) {
		super(game);
		this.game = game;
		this.framebuffer = framebuffer;
		this.holder = getHolder();
		// StrictMode.ThreadPolicy policy = new
		// StrictMode.ThreadPolicy.Builder()
		// .detectAll().penaltyFlashScreen().build();
		// StrictMode.setThreadPolicy(policy);

	}

	public void resume() {
		running = true;
		renderThread = new Thread(this);
		renderThread.start();

	}

	public void run() {
		long beginTime; // the time when the cycle begun
		long timeDiff; // the time it took for the cycle to execute
		int sleepTime; // ms to sleep (<0 if we're behind)
		int framesSkipped; // number of frames being skipped
		int totalFrameSkipped = 0;
		long absStartTime = System.currentTimeMillis();

		while (running) {
			if (!holder.getSurface().isValid())
				continue;

			beginTime = System.currentTimeMillis();
			framesSkipped = 0;

			game.getCurrentScreen().update();
			game.getCurrentScreen().paint();
			canvas = holder.lockCanvas();
			canvas.getClipBounds(dstRect);
			// THIS LINE IS WHERE I DO STUFF
			deviceAspectRatio = (double) dstRect.width() / dstRect.height();
			gameAspectRatio = (double) framebuffer.getWidth()
					/ framebuffer.getHeight();
			if (deviceAspectRatio == gameAspectRatio) {
				srcRect.set(0, 0, framebuffer.getWidth(),
						framebuffer.getHeight());
			} else if (deviceAspectRatio > gameAspectRatio) {
				// this means that device is wider than game
				viewPortHeight = framebuffer.getWidth() * deviceAspectRatio;
				viewPortWidth = framebuffer.getWidth();
				srcRect.set(
						0,
						(int) (framebuffer.getHeight() / 2 - viewPortHeight / 2),
						(int) viewPortWidth,
						(int) (framebuffer.getHeight() / 2 - viewPortHeight / 2)
								+ (int) viewPortHeight);
			} else if (deviceAspectRatio < gameAspectRatio) {
				// this means game is wider then device
				viewPortHeight = framebuffer.getHeight();
				viewPortWidth = framebuffer.getHeight() * deviceAspectRatio;
				srcRect.set(
						(int) (framebuffer.getWidth() / 2 - viewPortWidth / 2),
						0,
						(int) (framebuffer.getWidth() / 2 - viewPortWidth / 2)
								+ (int) viewPortWidth, (int) viewPortHeight);
			}
			// the important line where stuff gets drawn
			canvas.drawBitmap(framebuffer, srcRect, dstRect, null);

			game.getCurrentScreen().paintGUI();
			holder.unlockCanvasAndPost(canvas);

			timeDiff = System.currentTimeMillis() - beginTime;
			sleepTime = (int) (FRAME_PERIOD - timeDiff + extraTime);
			extraTime = 0;
			// System.out.println(sleepTime);
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {

				if ((sleepTime * -1) < FRAME_PERIOD) {
					beginTime = System.currentTimeMillis();
					game.getCurrentScreen().update();
					timeDiff = System.currentTimeMillis() - beginTime;
					extraTime = (int) (FRAME_PERIOD - timeDiff + sleepTime);
					sleepTime += FRAME_PERIOD;
					framesSkipped++;
//					totalFrameSkipped++;
//					System.out.println("FRAMES SKIPPED ARE " + framesSkipped);
//					System.out.println("Total Frame Skipped Are "
//							+ totalFrameSkipped);
//					System.out.println("Elapsed Time is "
//							+ (System.currentTimeMillis() - absStartTime)
//							/ 1000);
				} else {
					game.getCurrentScreen().update();
					sleepTime += FRAME_PERIOD;
					framesSkipped++;
//					totalFrameSkipped++;
//					System.out.println("FRAMES SKIPPED ARE " + framesSkipped);
//					System.out.println("Total Frame Skipped Are "
//							+ totalFrameSkipped);
//					System.out.println("Elapsed Time is "
//							+ (System.currentTimeMillis() - absStartTime)
//							/ 1000);
				}

				
				
				
				// beginTime = System.currentTimeMillis();
				// game.getCurrentScreen().update();
				// timeDiff = System.currentTimeMillis() - beginTime;
				// sleepTime += FRAME_PERIOD - timeDiff;
				// framesSkipped++;
				// //totalFrameSkipped++;
				// // System.out.println("FRAMES SKIPPED ARE " + framesSkipped);
				// // System.out.println("Total Frame Skipped Are "
				// // + totalFrameSkipped);
				// // System.out.println("Elapsed Time is "
				// // + (System.currentTimeMillis() - absStartTime) / 1000);
				// if (sleepTime > 0) {
				// try {
				// Thread.sleep(sleepTime);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				// }
			}

		}
	}

	public void pause() {
		running = false;
		while (true) {
			try {
				renderThread.join();
				break;
			} catch (InterruptedException e) {
			}
		}
	}

	public static float convertToAbsXCoordinate(float x) {
		float xx = (float) ((x * dstRect.width() / viewPortWidth) - srcRect.left
				* (dstRect.width() / viewPortWidth));
		return xx;
	}

	public static float convertToAbsYCoordinate(float y) {
		float yy = (float) ((y * dstRect.height() / viewPortHeight) - srcRect.top
				* (dstRect.height() / viewPortHeight));
		return yy;
	}
}