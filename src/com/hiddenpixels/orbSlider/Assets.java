package com.hiddenpixels.orbSlider;

import java.util.ArrayList;
import java.util.List;

import com.hiddenpixels.framework.Image;
import com.hiddenpixels.framework.Sound;

public class Assets {

	public static Image splash, menu, background, playButton, settingButton,
			levelButton, resetButton, menuButton, HUDBar, ballParticle,
			squareParticle, levelButtonLocked, circleParticle, starFull,
			starEmpty, starFullDark, starEmptyDark, starFullYellow, starEmptyYellow;

	public static String level1, level2, level3, level4;

	public static List<String> levelPack1 = new ArrayList<String>();
	public static List<String> levelPack2 = new ArrayList<String>();
	public static List<String> levelPack1Par = new ArrayList<String>();
	public static List<String> levelPack2Par = new ArrayList<String>();
	public static List<String> levelPack1Stars = new ArrayList<String>();
	public static List<String> levelPack2Stars = new ArrayList<String>();

	public static Sound colorSelectSound, clear, switchSound, portal, oneWay,
			glass, goal;

	public static void load(MainActivity mainActivity) {
		// TODO Auto-generated method stub

	}

}
