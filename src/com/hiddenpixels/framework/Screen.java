package com.hiddenpixels.framework;

public abstract class Screen {
	protected final Game game;

	public Screen(Game game) {
		this.game = game;
	}

	public abstract void update();

	public abstract void paint();

	public abstract void pause();

	public abstract void resume();

	public abstract void dispose();

	public abstract void backButton();

	public abstract void paintGUI();
}