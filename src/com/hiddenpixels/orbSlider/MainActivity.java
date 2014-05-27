package com.hiddenpixels.orbSlider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.hiddenpixels.framework.Screen;
import com.hiddenpixels.framework.implementation.AndroidGame;

public class MainActivity extends AndroidGame {

	public static String map;
	boolean firstTimeCreated = true;
	public static SharedPreferences settings;
	public static SharedPreferences.Editor editor;
	public static float soundVolume = 0.5f;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = getSharedPreferences("prefFile", 0);
		editor = settings.edit();

//		 editor.clear();
//		 editor.commit();

	}

	@Override
	public Screen getInitScreen() {
		if (firstTimeCreated) {
			Assets.load(this);
			firstTimeCreated = false;
		}
		return new SplashLoadingScreen(this);
	}

	public static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append((line + "\n"));
			}
		} catch (IOException e) {
			Log.w("LOG", e.getMessage());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				Log.w("LOG", e.getMessage());
			}
		}
		return sb.toString();
	}

	@Override
	public void onBackPressed() {
		getCurrentScreen().backButton();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

}
