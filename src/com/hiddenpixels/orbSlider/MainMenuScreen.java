package com.hiddenpixels.orbSlider;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;

import com.hiddenpixels.framework.Game;
import com.hiddenpixels.framework.Graphics;
import com.hiddenpixels.framework.Screen;
import com.hiddenpixels.framework.Input.TouchEvent;
import com.hiddenpixels.framework.implementation.AndroidFastRenderView;
import com.hiddenpixels.framework.implementation.AndroidGame;
import com.hiddenpixels.framework.implementation.AndroidGraphics;
import com.hiddenpixels.framework.implementation.AndroidImage;

public class MainMenuScreen extends Screen {

	static AndroidGame androidGame;
	static Paint paint = new Paint();
	public static List<LevelBox> levelBoxes1 = new ArrayList<LevelBox>();
	public static List<LevelBox> levelBoxes2 = new ArrayList<LevelBox>();
	public static List<LevelPack> levelPacks = new ArrayList<LevelPack>();
	public static int levelPackSelected = 1;
	int levelSelectTimer = 0;
	public static int parsCleared1 = 0;
	public static int parsCleared2 = 0;
	public static int stars1 = 0;
	public static int stars2 = 0;
	public static MessageBox message = new MessageBox();

	// settings and level select I guess
	public enum GameState {
		Main, LevelSelect, About, Donate, Settings
	}

	public static GameState state = GameState.Main;

	public MainMenuScreen(Game game) {
		super(game);
		androidGame = (AndroidGame) game;
		int level = 1;
		levelBoxes1.clear();
		levelBoxes2.clear();
		levelPacks.clear();
		parsCleared1 = 0;
		parsCleared2 = 0;
		stars1 = 0;
		stars2 = 0;
		levelSelectTimer = 0;
		levelPacks.add(new LevelPack(1, 0, 0, game));
		levelPacks.add(new LevelPack(2, 1, 0, game));
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				levelBoxes1.add(new LevelBox(1, level, j, i, game,
						MainActivity.settings.getInt("stars" + 1 + "-" + level,
								0)));
				levelBoxes2.add(new LevelBox(2, level, j, i, game,
						MainActivity.settings.getInt("stars" + 2 + "-" + level,
								0)));
				level++;
			}
		}
		for (int i = 0; i < 25; i++) {
			if ((Integer.parseInt(Assets.levelPack1Par.get(i)) >= MainActivity.settings
					.getInt("best1-" + (i + 1), 0))
					&& (MainActivity.settings.getInt("best1-" + (i + 1), 0) != 0)) {
				parsCleared1 += 1;
			}

			if ((Integer.parseInt(Assets.levelPack2Par.get(i)) >= MainActivity.settings
					.getInt("best2-" + (i + 1), 0))
					&& (MainActivity.settings.getInt("best2-" + (i + 1), 0) != 0)) {
				parsCleared2 += 1;
			}
			stars1 += MainActivity.settings.getInt("stars1-" + (i + 1), 0);
			stars2 += MainActivity.settings.getInt("stars2-" + (i + 1), 0);
		}
		// System.out.println(MainActivity.settings.getString("solution2-5", "NO SOLUTION"));
	}

	@Override
	public void update() {
		if (state == GameState.Main) {
			updateMain();
		} else if (state == GameState.LevelSelect) {
			updateLevelSelect();
		} else if (state == GameState.About) {
			updateAbout();
		} else if (state == GameState.Donate) {
			updateDonate();
		} else if (state == GameState.Settings) {
			updateSettings();
		}
	}

	private void updateSettings() {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				if (inAbsBounds(event, 0, AndroidFastRenderView.dstRect.bottom
						/ 2 - AndroidFastRenderView.dstRect.bottom / 30
						- AndroidFastRenderView.dstRect.bottom / 30,
						AndroidFastRenderView.dstRect.right,
						AndroidFastRenderView.dstRect.bottom / 30)) {
					if (MainActivity.settings.getBoolean("sound", true)) {
						MainActivity.editor.putBoolean("sound", false);
						MainActivity.editor.commit();
						MainActivity.soundVolume = 0;
					} else {
						MainActivity.editor.putBoolean("sound", true);
						MainActivity.editor.commit();
						MainActivity.soundVolume = 0.5f;
						Assets.colorSelectSound.play(MainActivity.soundVolume);
					}
				} else if (inAbsBounds(event, 0,
						AndroidFastRenderView.dstRect.bottom / 2,
						AndroidFastRenderView.dstRect.right,
						AndroidFastRenderView.dstRect.bottom / 30)) {
					if (MainActivity.settings.getBoolean("tap", true)) {
						MainActivity.editor.putBoolean("tap", false);
						MainActivity.editor.commit();
					} else {
						MainActivity.editor.putBoolean("tap", true);
						MainActivity.editor.commit();
					}
					if (MainActivity.settings.getBoolean("sound", true)) {
						Assets.colorSelectSound.play(MainActivity.soundVolume);
					}
				}

			}
		}

	}

	private void updateDonate() {

	}

	private void updateAbout() {

	}

	private void updateLevelSelect() {
		levelSelectTimer++;
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		boolean changingScreen = false;
		try {
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);
				if (event.type == TouchEvent.TOUCH_UP && !changingScreen) {

					if (levelPackSelected == 1 && levelSelectTimer > 25) {
						for (int j = 0; j < 25; j++) {
							levelBoxes1.get(j).checkTouch(event);
						}
					} else if (levelPackSelected == 2 && levelSelectTimer > 25) {
						for (int j = 0; j < 25; j++) {
							levelBoxes2.get(j).checkTouch(event);
						}
					}
					if (levelSelectTimer > 25) {
						for (LevelPack lp : levelPacks) {
							lp.checkTouch(event);
						}
					}
				}
			}
		} catch (Exception e) {
			
		}
	}

	private void updateMain() {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inAbsBounds(event, 0, AndroidFastRenderView.dstRect.bottom
						/ 2 - AndroidFastRenderView.dstRect.bottom / 10
						- AndroidFastRenderView.dstRect.bottom / 20,
						AndroidFastRenderView.dstRect.right,
						AndroidFastRenderView.dstRect.bottom / 20)) {
					state = GameState.LevelSelect;
					Assets.colorSelectSound.play(MainActivity.soundVolume);
				} else if (inAbsBounds(event, 0,
						AndroidFastRenderView.dstRect.bottom / 2
								- AndroidFastRenderView.dstRect.bottom / 20,
						AndroidFastRenderView.dstRect.right,
						AndroidFastRenderView.dstRect.bottom / 20)) {
					state = GameState.Settings;
					Assets.colorSelectSound.play(MainActivity.soundVolume);
				} else if (inAbsBounds(event, 0,
						AndroidFastRenderView.dstRect.bottom / 2
								+ AndroidFastRenderView.dstRect.bottom / 10
								- AndroidFastRenderView.dstRect.bottom / 20,
						AndroidFastRenderView.dstRect.right,
						AndroidFastRenderView.dstRect.bottom / 20)) {
					state = GameState.About;
					Assets.colorSelectSound.play(MainActivity.soundVolume);
					// state = GameState.About;
					// Assets.colorSelectSound.play(0.3f);
				}
			}
		}
	}

	public static boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	public static boolean inAbsBounds(TouchEvent event, int x, int y,
			int width, int height) {
		if (event.absX > x && event.absX < x + width - 1 && event.absY > y
				&& event.absY < y + height - 1)
			return true;
		else
			return false;
	}

	@Override
	public void paint() {
		if (state == GameState.Main) {
			paintMain();
		} else if (state == GameState.LevelSelect) {
			paintLevelSelect();
		} else if (state == GameState.About) {
			paintAbout();
		} else if (state == GameState.Donate) {
			paintDonate();
		} else if (state == GameState.Settings) {
			paintSettings();
		}
	}

	private void paintSettings() {

	}

	private void paintDonate() {

	}

	private void paintAbout() {

	}

	private void paintLevelSelect() {
		Graphics g = game.getGraphics();
		g.drawARGB(255, 200, 200, 200);
	}

	private void paintMain() {
		AndroidGraphics ag = (AndroidGraphics) game.getGraphics();
		ag.drawARGB(255, 200, 200, 200);
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
		game.getInput().getTouchEvents().clear();
		if (state == GameState.Main) {
			android.os.Process.killProcess(android.os.Process.myPid());
		} else if (state == GameState.LevelSelect) {
			if (message.activated) {
				message.activated = false;
			} else {
				levelSelectTimer = 0;
				state = GameState.Main;
			}
		} else if (state == GameState.About) {
			if (message.activated) {
				message.activated = false;
			} else {
				levelSelectTimer = 0;
				state = GameState.Main;
			}
		} else if (state == GameState.Donate) {
			if (message.activated) {
				message.activated = false;
			} else {
				levelSelectTimer = 0;
				state = GameState.Main;
			}
		} else if (state == GameState.Settings) {
			state = GameState.Main;
		}
	}

	@Override
	public void paintGUI() {
		if (state == GameState.Main) {
			paint.setColor(Color.rgb(150, 150, 150));
			paint.setTextSize(AndroidFastRenderView.dstRect.bottom / 20);
			paint.setTextAlign(Align.CENTER);
			paint.setStyle(Style.FILL);
			paint.setStrokeWidth(1);
			AndroidFastRenderView.canvas.drawText("Play",
					AndroidFastRenderView.dstRect.right / 2,
					AndroidFastRenderView.dstRect.bottom / 2
							- AndroidFastRenderView.dstRect.bottom / 10, paint);
			AndroidFastRenderView.canvas.drawText("Settings",
					AndroidFastRenderView.dstRect.right / 2,
					AndroidFastRenderView.dstRect.bottom / 2, paint);
			AndroidFastRenderView.canvas.drawText("About",
					AndroidFastRenderView.dstRect.right / 2,
					AndroidFastRenderView.dstRect.bottom / 2
							+ AndroidFastRenderView.dstRect.bottom / 10, paint);
		} else if (state == GameState.LevelSelect) {
			paint.setStrokeWidth(2);
			for (LevelPack lp : levelPacks) {
				lp.draw();
			}
			if (levelPackSelected == 1) {
				for (LevelBox lb : levelBoxes1) {
					lb.draw();
				}
			} else if (levelPackSelected == 2) {
				for (LevelBox lb : levelBoxes2) {
					lb.draw();
				}
			}
			message.draw();
		} else if (state == GameState.About) {
			paint.setColor(Color.rgb(150, 150, 150));
			paint.setTextSize(AndroidFastRenderView.dstRect.bottom / 50);
			paint.setTextAlign(Align.CENTER);
			paint.setStyle(Style.FILL);
			paint.setStrokeWidth(1);
			AndroidFastRenderView.canvas.drawText("Orb Slider",
					AndroidFastRenderView.dstRect.right / 2,
					AndroidFastRenderView.dstRect.bottom / 2
							- AndroidFastRenderView.dstRect.bottom * 2 / 25,
					paint);
			AndroidFastRenderView.canvas.drawText("Created by: robocup30",
					AndroidFastRenderView.dstRect.right / 2,
					AndroidFastRenderView.dstRect.bottom / 2
							- AndroidFastRenderView.dstRect.bottom / 25, paint);
			AndroidFastRenderView.canvas.drawText(
					"Contact: robocup30@hotmail.com",
					AndroidFastRenderView.dstRect.right / 2,
					AndroidFastRenderView.dstRect.bottom / 2, paint);
			AndroidFastRenderView.canvas.drawText("Version: 1.0.7",
					AndroidFastRenderView.dstRect.right / 2,
					AndroidFastRenderView.dstRect.bottom / 2
							+ AndroidFastRenderView.dstRect.bottom / 25, paint);
		} else if (state == GameState.Donate) {
			paint.setColor(Color.rgb(150, 150, 150));
			paint.setTextSize(AndroidFastRenderView.dstRect.bottom / 50);
			paint.setTextAlign(Align.CENTER);
			paint.setStyle(Style.FILL);
			paint.setStrokeWidth(1);
			AndroidFastRenderView.canvas.drawText(
					"If you want to support me please donate",
					AndroidFastRenderView.dstRect.right / 2,
					AndroidFastRenderView.dstRect.bottom / 2, paint);
		} else if (state == GameState.Settings) {
			paint.setColor(Color.rgb(150, 150, 150));
			paint.setTextSize(AndroidFastRenderView.dstRect.bottom / 30);
			paint.setTextAlign(Align.CENTER);
			paint.setStyle(Style.FILL);
			paint.setStrokeWidth(1);
			if (MainActivity.settings.getBoolean("sound", true)) {
				AndroidFastRenderView.canvas.drawText("Sound ON",
						AndroidFastRenderView.dstRect.right / 2,
						AndroidFastRenderView.dstRect.bottom / 2
								- AndroidFastRenderView.dstRect.bottom / 30,
						paint);
			} else {
				AndroidFastRenderView.canvas.drawText("Sound OFF",
						AndroidFastRenderView.dstRect.right / 2,
						AndroidFastRenderView.dstRect.bottom / 2
								- AndroidFastRenderView.dstRect.bottom / 30,
						paint);
			}
			if (MainActivity.settings.getBoolean("tap", true)) {
				AndroidFastRenderView.canvas.drawText(
						"Tap orb to switch color ON",
						AndroidFastRenderView.dstRect.right / 2,
						AndroidFastRenderView.dstRect.bottom / 2
								+ AndroidFastRenderView.dstRect.bottom / 30,
						paint);
			} else {
				AndroidFastRenderView.canvas.drawText(
						"Tap orb to switch color OFF",
						AndroidFastRenderView.dstRect.right / 2,
						AndroidFastRenderView.dstRect.bottom / 2
								+ AndroidFastRenderView.dstRect.bottom / 30,
						paint);
			}
		}
	}
}

class LevelBox {

	int pack, level, x, y;
	int size;
	Game game;
	int stars;

	public LevelBox(int pack, int level, int x, int y, Game game, int stars) {
		this.pack = pack;
		this.level = level;
		this.x = x;
		this.y = y + 1;
		this.game = game;
		this.stars = stars;
	}

	public boolean checkTouch(TouchEvent event) {
		if (MainMenuScreen.inAbsBounds(event, x * size, y * size, size, size)) {
			if (!MainMenuScreen.message.activated) {
				Assets.colorSelectSound.play(MainActivity.soundVolume);
				game.setScreen(new GameScreen(game, pack, level));
				return true;
			}
		}
		return false;
	}

	public void draw() {
		if (MainActivity.settings.getInt("best" + pack + "-" + level, 0) != 0) {
			MainMenuScreen.paint.setStyle(Style.STROKE);
			MainMenuScreen.paint.setColor(Color.rgb(200, 200, 200));
		} else {
			MainMenuScreen.paint.setStyle(Style.FILL);
			MainMenuScreen.paint.setColor(Color.rgb(175, 175, 175));
		}
		size = AndroidFastRenderView.dstRect.width() / 5;
		AndroidFastRenderView.canvas.drawRect(x * size, y * size, (x + 1)
				* size, (y + 1) * size, MainMenuScreen.paint);
		MainMenuScreen.paint.setStyle(Style.FILL_AND_STROKE);
		MainMenuScreen.paint.setTextAlign(Align.CENTER);
		MainMenuScreen.paint.setTextSize(size / 3);
		MainMenuScreen.paint.setColor(Color.rgb(150, 150, 150));
		AndroidFastRenderView.canvas.drawText(String.valueOf(level), x * size
				+ size / 2, y * size + size / 2 + size / 6,
				MainMenuScreen.paint);
		MainMenuScreen.paint.setTextSize(size / 6);
		MainMenuScreen.paint.setStyle(Style.STROKE);
		int starSize = size / 5;
		if (stars == 0) {
			AndroidFastRenderView.canvas.drawBitmap(
					((AndroidImage) Assets.starEmptyDark).bitmap, null,
					new Rect(x * size + starSize,
							(int) ((y + 1) * size - 1.5 * starSize), x * size
									+ 2 * starSize,
							(int) ((y + 1) * size - .5 * starSize)),
					MainMenuScreen.paint);
			AndroidFastRenderView.canvas.drawBitmap(
					((AndroidImage) Assets.starEmptyDark).bitmap, null,
					new Rect(x * size + size / 2 - starSize / 2, (int) ((y + 1)
							* size - 1.5 * starSize), x * size + size / 2
							+ starSize / 2,
							(int) ((y + 1) * size - .5 * starSize)),
					MainMenuScreen.paint);
			AndroidFastRenderView.canvas.drawBitmap(
					((AndroidImage) Assets.starEmptyDark).bitmap, null,
					new Rect((x + 1) * size - 2 * starSize, (int) ((y + 1)
							* size - 1.5 * starSize),
							(x + 1) * size - starSize,
							(int) ((y + 1) * size - .5 * starSize)),
					MainMenuScreen.paint);
		} else if (stars == 1) {
			AndroidFastRenderView.canvas.drawBitmap(
					((AndroidImage) Assets.starFullDark).bitmap, null,
					new Rect(x * size + starSize,
							(int) ((y + 1) * size - 1.5 * starSize), x * size
									+ 2 * starSize,
							(int) ((y + 1) * size - .5 * starSize)),
					MainMenuScreen.paint);
			AndroidFastRenderView.canvas.drawBitmap(
					((AndroidImage) Assets.starEmptyDark).bitmap, null,
					new Rect(x * size + size / 2 - starSize / 2, (int) ((y + 1)
							* size - 1.5 * starSize), x * size + size / 2
							+ starSize / 2,
							(int) ((y + 1) * size - .5 * starSize)),
					MainMenuScreen.paint);
			AndroidFastRenderView.canvas.drawBitmap(
					((AndroidImage) Assets.starEmptyDark).bitmap, null,
					new Rect((x + 1) * size - 2 * starSize, (int) ((y + 1)
							* size - 1.5 * starSize),
							(x + 1) * size - starSize,
							(int) ((y + 1) * size - .5 * starSize)),
					MainMenuScreen.paint);
		} else if (stars == 2) {
			AndroidFastRenderView.canvas.drawBitmap(
					((AndroidImage) Assets.starFullDark).bitmap, null,
					new Rect(x * size + starSize,
							(int) ((y + 1) * size - 1.5 * starSize), x * size
									+ 2 * starSize,
							(int) ((y + 1) * size - .5 * starSize)),
					MainMenuScreen.paint);
			AndroidFastRenderView.canvas.drawBitmap(
					((AndroidImage) Assets.starFullDark).bitmap, null,
					new Rect(x * size + size / 2 - starSize / 2, (int) ((y + 1)
							* size - 1.5 * starSize), x * size + size / 2
							+ starSize / 2,
							(int) ((y + 1) * size - .5 * starSize)),
					MainMenuScreen.paint);
			AndroidFastRenderView.canvas.drawBitmap(
					((AndroidImage) Assets.starEmptyDark).bitmap, null,
					new Rect((x + 1) * size - 2 * starSize, (int) ((y + 1)
							* size - 1.5 * starSize),
							(x + 1) * size - starSize,
							(int) ((y + 1) * size - .5 * starSize)),
					MainMenuScreen.paint);
		} else if (stars == 3) {
			AndroidFastRenderView.canvas.drawBitmap(
					((AndroidImage) Assets.starFullDark).bitmap, null,
					new Rect(x * size + starSize,
							(int) ((y + 1) * size - 1.5 * starSize), x * size
									+ 2 * starSize,
							(int) ((y + 1) * size - .5 * starSize)),
					MainMenuScreen.paint);
			AndroidFastRenderView.canvas.drawBitmap(
					((AndroidImage) Assets.starFullDark).bitmap, null,
					new Rect(x * size + size / 2 - starSize / 2, (int) ((y + 1)
							* size - 1.5 * starSize), x * size + size / 2
							+ starSize / 2,
							(int) ((y + 1) * size - .5 * starSize)),
					MainMenuScreen.paint);
			AndroidFastRenderView.canvas.drawBitmap(
					((AndroidImage) Assets.starFullDark).bitmap, null,
					new Rect((x + 1) * size - 2 * starSize, (int) ((y + 1)
							* size - 1.5 * starSize),
							(x + 1) * size - starSize,
							(int) ((y + 1) * size - .5 * starSize)),
					MainMenuScreen.paint);
		}

		if (pack == 1) {
			// AndroidFastRenderView.canvas
			// .drawText(String.valueOf(MainActivity.settings.getInt(
			// "best" + pack + "-" + level, 0)
			// + " ("
			// + Assets.levelPack1Par.get(level - 1) + ")"), x
			// * size + size / 2, y * size + size / 2 + size / 3,
			// MainMenuScreen.paint);
		} else if (pack == 2) {
			// AndroidFastRenderView.canvas
			// .drawText(String.valueOf(MainActivity.settings.getInt(
			// "best" + pack + "-" + level, 0)
			// + " ("
			// + Assets.levelPack2Par.get(level - 1) + ")"), x
			// * size + size / 2, y * size + size / 2 + size / 3,
			// MainMenuScreen.paint);
		}
		// AndroidFastRenderView.canvas.drawBitmap(
		// ((AndroidImage) Assets.starEmpty).bitmap, null, new Rect(90,
		// 90, 150, 150), MainMenuScreen.paint);
		// AndroidFastRenderView.canvas.drawBitmap(
		// ((AndroidImage) Assets.starFull).bitmap, null, new Rect(200,
		// 200, 240, 240), MainMenuScreen.paint);

	}
}

class LevelPack {

	int pack, x, y;
	int xSize, ySize;
	Game game;

	public LevelPack(int pack, int x, int y, Game game) {
		this.pack = pack;
		this.x = x;
		this.y = y;
		this.game = game;
	}

	public void checkTouch(TouchEvent event) {
		if (MainMenuScreen.inAbsBounds(event, x * xSize, y * ySize, xSize,
				ySize)) {
			Assets.colorSelectSound.play(MainActivity.soundVolume);
			if (pack == 1) {
				MainMenuScreen.levelPackSelected = pack;
			} else if (pack == 2) {
				MainMenuScreen.levelPackSelected = pack;
				// if (MainMenuScreen.stars1 < 60) {
				// MainMenuScreen.message.text1 = "Collect at least 60 stars";
				// MainMenuScreen.message.text2 = "to unlock this level pack";
				// MainMenuScreen.message.activated = true;
				// } else {
				// MainMenuScreen.levelPackSelected = pack;
				// }
			}
		}
	}

	public void draw() {
		xSize = AndroidFastRenderView.dstRect.width() / 2;
		ySize = AndroidFastRenderView.dstRect.width() / 5;
		if (MainMenuScreen.levelPackSelected != pack) {
			MainMenuScreen.paint.setStyle(Style.FILL);
			MainMenuScreen.paint.setColor(Color.rgb(175, 175, 175));
		} else {
			MainMenuScreen.paint.setStyle(Style.STROKE);
		}
		AndroidFastRenderView.canvas.drawRect(x * xSize, y * ySize, (x + 1)
				* xSize, (y + 1) * ySize, MainMenuScreen.paint);
		MainMenuScreen.paint.setStyle(Style.FILL_AND_STROKE);
		MainMenuScreen.paint.setTextAlign(Align.CENTER);
		MainMenuScreen.paint.setTextSize(ySize / 3);
		MainMenuScreen.paint.setColor(Color.rgb(150, 150, 150));
		if (pack == 1) {
			AndroidFastRenderView.canvas.drawText(String.valueOf(pack) + " ("
					+ MainMenuScreen.stars1 + "/" + "75)", x * xSize + xSize
					/ 2, y * ySize + ySize / 2 + ySize / 6,
					MainMenuScreen.paint);
		} else if (pack == 2) {
			AndroidFastRenderView.canvas.drawText(String.valueOf(pack) + " ("
					+ MainMenuScreen.stars2 + "/" + "75)", x * xSize + xSize
					/ 2, y * ySize + ySize / 2 + ySize / 6,
					MainMenuScreen.paint);
		}
	}
}

class MessageBox {

	public String text1 = "";
	public String text2 = "";
	public String text3 = "";
	public boolean activated = false;

	public MessageBox() {

	}

	public void draw() {
		if (activated) {
			AndroidFastRenderView.canvas.drawARGB(100, 0, 0, 0);
			MainMenuScreen.paint.setStyle(Style.FILL_AND_STROKE);
			MainMenuScreen.paint.setColor(Color.rgb(255, 255, 255));
			AndroidFastRenderView.canvas.drawRect(0,
					AndroidFastRenderView.dstRect.bottom / 3,
					AndroidFastRenderView.dstRect.right,
					AndroidFastRenderView.dstRect.bottom * 2 / 3,
					MainMenuScreen.paint);
			MainMenuScreen.paint.setColor(Color.rgb(200, 200, 200));
			MainMenuScreen.paint
					.setTextSize(AndroidFastRenderView.dstRect.bottom / 40);
			MainMenuScreen.paint.setStrokeWidth(1);
			AndroidFastRenderView.canvas.drawText(text1,
					AndroidFastRenderView.dstRect.right / 2,
					AndroidFastRenderView.dstRect.bottom / 2
							- AndroidFastRenderView.dstRect.bottom / 40,
					MainMenuScreen.paint);
			AndroidFastRenderView.canvas.drawText(text2,
					AndroidFastRenderView.dstRect.right / 2,
					AndroidFastRenderView.dstRect.bottom / 2,
					MainMenuScreen.paint);
			AndroidFastRenderView.canvas.drawText(text3,
					AndroidFastRenderView.dstRect.right / 2,
					AndroidFastRenderView.dstRect.bottom / 2
							+ AndroidFastRenderView.dstRect.bottom / 40,
					MainMenuScreen.paint);
		}
	}
}