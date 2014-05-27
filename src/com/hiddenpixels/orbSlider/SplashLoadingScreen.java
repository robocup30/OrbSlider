package com.hiddenpixels.orbSlider;

import com.hiddenpixels.framework.Game;
import com.hiddenpixels.framework.Graphics;
import com.hiddenpixels.framework.Screen;
import com.hiddenpixels.framework.Graphics.ImageFormat;

public class SplashLoadingScreen extends Screen {

	public SplashLoadingScreen(Game game) {
		super(game);
	}

	@Override
	public void update() {
		Graphics g = game.getGraphics();
		g.drawARGB(255, 200, 200, 200);
		game.setScreen(new LoadingScreen(game));
	}

	@Override
	public void paint() {
		Graphics g = game.getGraphics();
		g.drawARGB(255, 200, 200, 200);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {

	}

	@Override
	public void paintGUI() {
		// TODO Auto-generated method stub
		
	}

}
