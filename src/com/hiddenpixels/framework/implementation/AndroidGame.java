package com.hiddenpixels.framework.implementation;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

import com.hiddenpixels.framework.Audio;
import com.hiddenpixels.framework.FileIO;
import com.hiddenpixels.framework.Game;
import com.hiddenpixels.framework.Graphics;
import com.hiddenpixels.framework.Input;
import com.hiddenpixels.framework.Screen;

public abstract class AndroidGame extends Activity implements Game {
	public AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	WakeLock wakeLock;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

		// game size
		int frameBufferWidth = isPortrait ? 600 : 854;
		int frameBufferHeight = isPortrait ? 854 : 600;
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
				frameBufferHeight, Config.RGB_565);
		// float scaleX = (float) frameBufferWidth
		// / getWindowManager().getDefaultDisplay().getWidth();
		// float scaleY = (float) frameBufferHeight
		// / getWindowManager().getDefaultDisplay().getHeight();

		renderView = new AndroidFastRenderView(this, frameBuffer);
		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		fileIO = new AndroidFileIO(this);
		audio = new AndroidAudio(this);

		double deviceAspectRatio = (double) getWindowManager()
				.getDefaultDisplay().getWidth()
				/ getWindowManager().getDefaultDisplay().getHeight();
		double gameAspectRatio = (double) frameBufferWidth / frameBufferHeight;

		if (deviceAspectRatio == gameAspectRatio) {
			float scaleX = (float) frameBufferWidth
					/ getWindowManager().getDefaultDisplay().getWidth();
			float scaleY = (float) frameBufferHeight
					/ getWindowManager().getDefaultDisplay().getHeight();
			input = new AndroidInput(this, renderView, scaleX, scaleY, 0, 0);
		} else if (deviceAspectRatio > gameAspectRatio) {
			// this means that device is wider than game
			double viewPortHeight = frameBufferWidth * deviceAspectRatio;
			double viewPortWidth = frameBufferWidth;
			float scaleX = (float) viewPortWidth
					/ getWindowManager().getDefaultDisplay().getWidth();
			float scaleY = (float) viewPortHeight
					/ getWindowManager().getDefaultDisplay().getHeight();
			input = new AndroidInput(this, renderView, scaleX, scaleY, 0,
					(int) (frameBufferHeight / 2 - viewPortHeight / 2));
		} else if (deviceAspectRatio < gameAspectRatio) {
			// this means game is wider then device
			double viewPortHeight = frameBufferHeight;
			double viewPortWidth = frameBufferHeight * deviceAspectRatio;
			float scaleX = (float) viewPortWidth
					/ getWindowManager().getDefaultDisplay().getWidth();
			float scaleY = (float) viewPortHeight
					/ getWindowManager().getDefaultDisplay().getHeight();
			input = new AndroidInput(this, renderView, scaleX, scaleY,
					(int) (frameBufferWidth / 2 - viewPortWidth / 2), 0);
		}

		// input = new AndroidInput(this, renderView, scaleX, scaleY;
		screen = getInitScreen();
		setContentView(renderView);

		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"MyGame");
	}

	@Override
	public void onResume() {
		super.onResume();
		wakeLock.acquire();
		screen.resume();
		renderView.resume();
	}

	@Override
	public void onPause() {
		super.onPause();
		wakeLock.release();
		renderView.pause();
		screen.pause();

		if (isFinishing())
			screen.dispose();
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public FileIO getFileIO() {
		return fileIO;
	}

	@Override
	public Graphics getGraphics() {
		return graphics;
	}

	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null");

		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update();
		this.screen = screen;
	}

	public Screen getCurrentScreen() {

		return screen;
	}
}
