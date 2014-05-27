package com.hiddenpixels.orbSlider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.content.res.AssetManager;

import com.hiddenpixels.framework.Game;
import com.hiddenpixels.framework.Graphics;
import com.hiddenpixels.framework.Screen;
import com.hiddenpixels.framework.Graphics.ImageFormat;
import com.hiddenpixels.framework.implementation.AndroidGame;

public class LoadingScreen extends Screen {

	AssetManager assets;
	AndroidGame ag;

	public LoadingScreen(Game game) {
		super(game);
		ag = (AndroidGame) game;
		assets = ag.getAssets();
	}

	@Override
	public void update() {
		if(MainActivity.settings.getBoolean("sound", true)){
			MainActivity.soundVolume = 0.5f;
		} else {
			MainActivity.soundVolume = 0;
		}
		
		Graphics g = game.getGraphics();
		Assets.starFull = g.newImage("starFull.png", ImageFormat.ARGB8888);
		Assets.starEmpty = g.newImage("starEmpty.png", ImageFormat.ARGB8888);
		Assets.starFullDark = g.newImage("starFullDark.png", ImageFormat.ARGB8888);
		Assets.starEmptyDark = g.newImage("starEmptyDark.png", ImageFormat.ARGB8888);
		Assets.starFullYellow = g.newImage("starFullYellow.png", ImageFormat.ARGB8888);
		Assets.starEmptyYellow = g.newImage("starEmptyYellow.png", ImageFormat.ARGB8888);
		Assets.colorSelectSound = game.getAudio()
				.createSound("colorSelect.wav");
		Assets.switchSound = game.getAudio().createSound("switch.wav");
		Assets.portal = game.getAudio().createSound("portal.wav");
		Assets.oneWay = game.getAudio().createSound("oneWay.wav");
		Assets.glass = game.getAudio().createSound("glass.wav");
		Assets.goal = game.getAudio().createSound("goal.wav");

		for (int i = 1; i <= 25; i++) {
			Assets.levelPack1.add(newMap("levelPack1/" + i));
			Assets.levelPack2.add(newMap("levelPack2/" + i));
			Assets.levelPack1Stars.add(Integer.toString(MainActivity.settings
					.getInt("stars" + 1 + "-" + i, 0)));
			Assets.levelPack2Stars.add(Integer.toString(MainActivity.settings
					.getInt("stars" + 2 + "-" + i, 0)));

			InputStream in1 = null;
			InputStream in2 = null;
			try {
				in1 = assets.open("levelPack1/" + i);
				in2 = assets.open("levelPack2/" + i);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Scanner scanner = null;
			scanner = new Scanner(MainActivity.convertStreamToString(in1));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line == null) {
					break;
				}
				if (line.startsWith("@")) {
					line = line.replaceAll("@", "");
					line = line.replaceAll(" ", "");
					line.trim();

					int par = Integer.parseInt(line);
					Assets.levelPack1Par.add(line);
					break;
				}
			}
			scanner = new Scanner(MainActivity.convertStreamToString(in2));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line == null) {
					break;
				}

				if (line.startsWith("@")) {
					line = line.replaceAll("@", "");
					line = line.replaceAll(" ", "");
					line.trim();

					int par = Integer.parseInt(line);
					Assets.levelPack2Par.add(line);
					break;
				}
			}
		}
	}

	@Override
	public void paint() {
		Graphics g = game.getGraphics();
		g.drawARGB(255, 200, 200, 200);
		game.setScreen(new MainMenuScreen(game));
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

	public String newMap(String fileName) {
		InputStream in = null;
		try {
			in = assets.open(fileName);
			return MainActivity.convertStreamToString(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public void paintGUI() {

	}

}
