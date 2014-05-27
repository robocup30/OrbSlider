package com.hiddenpixels.orbSlider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;

import com.hiddenpixels.framework.Game;
import com.hiddenpixels.framework.Screen;
import com.hiddenpixels.framework.Input.TouchEvent;
import com.hiddenpixels.framework.implementation.AndroidFastRenderView;
import com.hiddenpixels.framework.implementation.AndroidGraphics;
import com.hiddenpixels.framework.implementation.AndroidImage;

public class GameScreen extends Screen {

	public enum GameState {
		GameScreen, ScoreScreen
	}

	public static GameState state = GameState.GameScreen;

	static List<Ball> ballArray = new ArrayList<Ball>();
	static List<Wall> wallArray = new ArrayList<Wall>();
	static List<Goal> goalArray = new ArrayList<Goal>();
	static List<Switch> switchArray = new ArrayList<Switch>();
	static List<Goal> goalsToRemove = new ArrayList<Goal>();
	static List<Ball> ballsToRemove = new ArrayList<Ball>();
	static List<Portal> portalArray = new ArrayList<Portal>();
	final static int GUITop = AndroidFastRenderView.srcRect.top;
	final static int GUIBot = AndroidFastRenderView.srcRect.bottom;
	final static int GUILeft = AndroidFastRenderView.srcRect.left;
	final static int GUIRight = AndroidFastRenderView.srcRect.right;
	final static int GUIWidth = GUIRight - GUILeft;
	final static int GUIHeight = GUIBot - GUITop;
	final static int color1 = Color.rgb(230, 230, 230);
	static String selectedColor;
	boolean canMove = true;
	static int currentLevel = 1;
	static boolean reset = false;
	boolean tapped = false;
	int xPointerStart = 0;
	int yPointerStart = 0;
	int xPointerEnd = 0;
	int yPointerEnd = 0;
	int xPointerDifference = 0;
	int yPointerDifference = 0;
	int totalDifference = 0;
	boolean goToMenu = false;
	MainActivity main;
	Paint paint = new Paint();
	static int currentLevelPack = 1;
	static int moves = 0;
	static boolean moved = false;
	static boolean completed = false;
	static int completedTimer = 0;
	public static List<BallParticle> ballParticles = new ArrayList<BallParticle>();
	static double scoreScreenHeight = AndroidFastRenderView.dstRect.bottom * 2 / 15;
	static double scoreScreenSpeed = -AndroidFastRenderView.dstRect.bottom / 100;
	static double scoreScreenAcceleration = 0;
	static int best = 0;
	static int threeStarMoves = 0;
	static int twoStarMoves = 0;
	static String solution = "";
	static boolean firstTimePainting = true;
	static Bitmap bufferBitmap;
	static Canvas bufferCanvas;
	static String message = "";
	static ArrayList<String> messages = new ArrayList<String>();
	static boolean hasRed = false;
	static boolean hasGreen = false;
	static boolean hasBlue = false;
	static int colorNumber = 0;
	static int redBar = 0;
	static int greenBar = 0;
	static int blueBar = 0;
	static int colorBarWidth = 0;

	public GameScreen(Game game) {
		super(game);
		main = (MainActivity) game;
		levelReset();
	}

	public GameScreen(Game game, int level) {
		super(game);
		currentLevel = level;
		main = (MainActivity) game;
		levelReset();
		paint.setColor(Color.argb(255, 200, 200, 200));
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(35);
	}

	public GameScreen(Game game, int levelPack, int level) {
		super(game);
		currentLevel = level;
		currentLevelPack = levelPack;
		main = (MainActivity) game;
		levelReset();
		paint.setColor(Color.argb(255, 200, 200, 200));
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(35);
		paint.setAntiAlias(false);
		best = MainActivity.settings.getInt("best" + currentLevelPack + "-"
				+ currentLevel, 0);
	}

	@Override
	public void update() {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				xPointerStart = event.x;
				yPointerStart = event.y;
				if (MainActivity.settings.getBoolean("tap", true)) {
					for (Ball b : ballArray) {
						if (b.checkTouch(event)) {
							selectedColor = b.colorString;
							if (b.colorString == "red") {
								solution += "R";
							} else if (b.colorString == "blue") {
								solution += "B";
							} else if (b.colorString == "green") {
								solution += "G";
							}
							Assets.colorSelectSound
									.play(MainActivity.soundVolume);
						}
					}
				}
			} else if (event.type == TouchEvent.TOUCH_UP) {
				xPointerEnd = event.x;
				yPointerEnd = event.y;
				xPointerDifference = xPointerEnd - xPointerStart;
				yPointerDifference = yPointerEnd - yPointerStart;
				totalDifference = xPointerDifference * xPointerDifference
						+ yPointerDifference * yPointerDifference;

				if (inAbsBounds(event, redBar,
						AndroidFastRenderView.dstRect.bottom
								- AndroidFastRenderView.dstRect.bottom / 15,
						colorBarWidth,
						AndroidFastRenderView.dstRect.bottom / 15)) {
					selectedColor = "red";
					solution += "R";
					Assets.colorSelectSound.play(MainActivity.soundVolume);
				} else if (inAbsBounds(event, greenBar,
						AndroidFastRenderView.dstRect.bottom
								- AndroidFastRenderView.dstRect.bottom / 15,
						colorBarWidth,
						AndroidFastRenderView.dstRect.bottom / 15)) {
					selectedColor = "green";
					solution += "G";
					Assets.colorSelectSound.play(MainActivity.soundVolume);
				} else if (inAbsBounds(event, blueBar,
						AndroidFastRenderView.dstRect.bottom
								- AndroidFastRenderView.dstRect.bottom / 15,
						colorBarWidth,
						AndroidFastRenderView.dstRect.bottom / 15)) {
					selectedColor = "blue";
					solution += "B";
					Assets.colorSelectSound.play(MainActivity.soundVolume);
				}
			}

			if (inAbsBounds(
					event,
					0,
					(int) AndroidFastRenderView
							.convertToAbsYCoordinate(AndroidFastRenderView.srcRect.top),
					AndroidFastRenderView.dstRect.width() / 2,
					(int) AndroidFastRenderView.dstRect.bottom / 10)) {
				goToMenu = true;

			} else if (inAbsBounds(
					event,
					AndroidFastRenderView.dstRect.width() / 2,
					(int) AndroidFastRenderView
							.convertToAbsYCoordinate(AndroidFastRenderView.srcRect.top),
					AndroidFastRenderView.dstRect.width() / 2,
					(int) AndroidFastRenderView.dstRect.bottom / 10)) {
				reset = true;
			}

			if (state == GameState.ScoreScreen && completedTimer > 60) {
				if (inAbsBounds(
						event,
						0,
						(int) (AndroidFastRenderView.dstRect.bottom - scoreScreenHeight),
						AndroidFastRenderView.dstRect.right / 2,
						AndroidFastRenderView.dstRect.bottom / 5)) {
					reset = true;
				} else if (inAbsBounds(
						event,
						AndroidFastRenderView.dstRect.right / 2,
						(int) (AndroidFastRenderView.dstRect.bottom - scoreScreenHeight),
						AndroidFastRenderView.dstRect.right,
						AndroidFastRenderView.dstRect.bottom / 5)) {
					currentLevel += 1;
					if (currentLevelPack == 1) {
						if (currentLevel > Assets.levelPack1.size()) {
							game.setScreen(new MainMenuScreen(game));
							MainMenuScreen.state = MainMenuScreen.GameState.LevelSelect;
						} else {
							levelReset();
						}
					} else if (currentLevelPack == 2) {
						if (currentLevel > Assets.levelPack2.size()) {
							game.setScreen(new MainMenuScreen(game));
							MainMenuScreen.state = MainMenuScreen.GameState.LevelSelect;
						} else {
							levelReset();
						}
					}
				}
			}
		}

		if (goToMenu) {
			game.setScreen(new MainMenuScreen(game));
		}

		if (canMove && xPointerDifference != 0 && yPointerDifference != 0
				&& totalDifference > 5000) {
			if (xPointerDifference * xPointerDifference > yPointerDifference
					* yPointerDifference
					&& xPointerDifference > 0) {
				for (Ball b : ballArray) {
					if (b.colorString == selectedColor) {
						b.direction = "right";
					}
				}
				solution += "r";
			} else if (xPointerDifference * xPointerDifference > yPointerDifference
					* yPointerDifference
					&& xPointerDifference < 0) {
				for (Ball b : ballArray) {
					if (b.colorString == selectedColor) {
						b.direction = "left";
					}
				}
				solution += "l";
			} else if (xPointerDifference * xPointerDifference < yPointerDifference
					* yPointerDifference
					&& yPointerDifference < 0) {
				for (Ball b : ballArray) {
					if (b.colorString == selectedColor) {
						b.direction = "up";
					}
				}
				solution += "u";
			} else if (xPointerDifference * xPointerDifference < yPointerDifference
					* yPointerDifference
					&& yPointerDifference > 0) {
				for (Ball b : ballArray) {
					if (b.colorString == selectedColor) {
						b.direction = "down";
					}
				}
				solution += "d";
			}
			xPointerDifference = 0;
			yPointerDifference = 0;
			totalDifference = 0;
			moved = false;
		}

		if (reset) {
			levelReset();
		}

		for (Portal p : portalArray) {
			p.reset();
		}

		for (Switch s : switchArray) {
			if (s.ball != null) {
				if (s.ball.x != s.x || s.ball.y != s.y) {
					s.ball = null;
				}
			}
		}

		for (Ball b : ballArray) {
			b.reset();
			if (b.direction != "still") {
				for (Wall w : wallArray) {
					b.checkWallCollision(w);
				}

				for (Ball b2 : ballArray) {
					b.checkBallCollision(b2);
				}
			}
		}

		for (BallParticle bp : ballParticles) {
			bp.update();
		}

		for (Ball b : ballArray) {
			b.move();
		}

		for (Wall w : wallArray) {
			w.update();
		}

		for (Portal p : portalArray) {
			p.update();
		}

		goalCollision();
		switchCollision();
		portalCollision();

		canMove = true;
		for (Ball b : ballArray) {
			if (b.getDirection() != "still") {
				canMove = false;
				if (!moved) {
					moves++;
					// Assets.slide.play(.2f);
				}
				moved = true;
			}
		}

		if (state == GameState.ScoreScreen) {
			if (scoreScreenHeight < AndroidFastRenderView.dstRect.bottom * 2 / 3) {
				scoreScreenHeight += scoreScreenSpeed;
			}
			if (scoreScreenSpeed < AndroidFastRenderView.dstRect.bottom / 100) {
				scoreScreenSpeed += AndroidFastRenderView.dstRect.bottom / 800;
			}
		}
	}

	private void portalCollision() {
		for (Portal p : portalArray) {
			for (Ball b : ballArray) {
				if (p.x == b.x && p.y == b.y && p.ball != b) {
					p.teleportBall(b);
					b.updateRect();
				}
			}
		}
	}

	public void goalCollision() {
		for (Ball b : ballArray) {
			for (Goal go : goalArray) {
				if (b.x == go.x && b.y == go.y
						&& b.colorString == go.colorString) {
					goalsToRemove.add(go);
					ballsToRemove.add(b);
					Assets.goal.play(MainActivity.soundVolume);
				}
			}
		}

		for (Goal go : goalsToRemove) {
			goalArray.remove(go);
		}
		for (Ball b : ballsToRemove) {
			ballArray.remove(b);
		}
		if (goalArray.size() == 0) {
			completed = true;
			// System.out.println(solution);
			if (moves < best || best == 0) {
				MainActivity.editor.putInt("best" + currentLevelPack + "-"
						+ currentLevel, moves);
				MainActivity.editor.putString("solution" + currentLevelPack
						+ "-" + currentLevel, solution);
				if (moves <= threeStarMoves) {
					MainActivity.editor.putInt("stars" + currentLevelPack + "-"
							+ currentLevel, 3);
				} else if (moves <= twoStarMoves) {
					MainActivity.editor.putInt("stars" + currentLevelPack + "-"
							+ currentLevel, 2);
				} else {
					MainActivity.editor.putInt("stars" + currentLevelPack + "-"
							+ currentLevel, 1);
				}
				MainActivity.editor.commit();
				best = moves;
			}
		}

		if (completed) {
			completedTimer++;
		}

		if (completedTimer == 3) {
			// Assets.clear.play(0.05f);
		} else if (completedTimer == 30) {
			state = GameState.ScoreScreen;
		}
		goalsToRemove.clear();
		ballsToRemove.clear();
	}

	public void switchCollision() {
		for (Switch s : switchArray) {
			for (Ball b : ballArray) {
				if (b.x == s.x && b.y == s.y && s.ball != b) {
					for (Wall w : wallArray) {
						if (w.getClass() == WallGate.class) {
							((WallGate) w).toggleGate();
						}
					}
					s.ball = b;
					Assets.switchSound.play(MainActivity.soundVolume);
				}
			}
		}
	}

	public static void levelReset() {
		reset = false;
		selectedColor = "red";
		ballArray.clear();
		wallArray.clear();
		goalArray.clear();
		ballsToRemove.clear();
		goalsToRemove.clear();
		switchArray.clear();
		portalArray.clear();
		ballParticles.clear();
		moves = 0;
		moved = false;
		completed = false;
		solution = "";
		completedTimer = 0;
		state = GameState.GameScreen;
		scoreScreenHeight = AndroidFastRenderView.dstRect.bottom * 2 / 15;
		scoreScreenSpeed = -AndroidFastRenderView.dstRect.bottom / 100;
		scoreScreenAcceleration = 0;
		best = MainActivity.settings.getInt("best" + currentLevelPack + "-"
				+ currentLevel, 0);
		threeStarMoves = 0;
		twoStarMoves = 0;
		firstTimePainting = true;
		message = "";
		messages.clear();
		redBar = 0;
		greenBar = 0;
		blueBar = 0;
		hasRed = false;
		hasGreen = false;
		hasBlue = false;
		colorBarWidth = 0;
		try {
			parseLevel();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void parseLevel() throws IOException {

		ArrayList<String> lines = new ArrayList<String>();
		int width = 0;
		int height = 0;

		Scanner scanner;
		if (currentLevelPack == 1) {
			scanner = new Scanner(Assets.levelPack1.get(currentLevel - 1));
		} else if (currentLevelPack == 2) {
			scanner = new Scanner(Assets.levelPack2.get(currentLevel - 1));
		} else {
			scanner = new Scanner(Assets.levelPack1.get(currentLevel - 1));
		}

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			// no more lines to read
			if (line == null) {
				break;
			}

			if (!line.startsWith("!")) {
				if (line.startsWith("@")) {
					line = line.replaceAll("@", "");
					line = line.replaceAll(" ", "");
					line = line.trim();

					threeStarMoves = Integer.parseInt(line);
				} else if (line.startsWith("#")) {
					line = line.replaceAll("#", "");
					line = line.replaceAll(" ", "");
					line = line.trim();
					twoStarMoves = Integer.parseInt(line);
				} else if (line.startsWith("$")) {
					line = line.replace('$', ' ');
					line = line.trim();
					message = line;
					messages.add(message);
				} else {
					lines.add(line);
					width = Math.max(width, line.length());
				}
			}

		}
		height = lines.size();

		int xx = 2;
		int yy = 3;
		for (int j = 0; j < height; j++) {
			String line = (String) lines.get(j);
			for (int i = 0; i < width; i++) {

				if (i < line.length()) {
					// char ch = line.charAt(i);
					switch (line.charAt(i)) {
					case 'r':
						ballArray.add(new Ball(i + xx, j + yy, "red"));
						hasRed = true;
						break;
					case 'g':
						ballArray.add(new Ball(i + xx, j + yy, "green"));
						hasGreen = true;
						break;
					case 'b':
						ballArray.add(new Ball(i + xx, j + yy, "blue"));
						hasBlue = true;
						break;
					case 'w':
						wallArray.add(new WallNormal(i + xx, j + yy));
						break;
					case 'a':
						wallArray.add(new WallGate(i + xx, j + yy, true));
						break;
					case 'A':
						wallArray.add(new WallGate(i + xx, j + yy, false));
						break;
					case 'o':
						wallArray.add(new WallOnce(i + xx, j + yy));
						break;
					case 'l':
						wallArray.add(new WallGlass(i + xx, j + yy));
						break;
					case '1':
						wallArray.add(new WallColored(i + xx, j + yy, "red"));
						break;
					case '2':
						wallArray.add(new WallColored(i + xx, j + yy, "green"));
						break;
					case '3':
						wallArray.add(new WallColored(i + xx, j + yy, "blue"));
						break;
					case 'p':
						portalArray.add(new Portal1A(i + xx, j + yy));
						break;
					case 'P':
						portalArray.add(new Portal1B(i + xx, j + yy));
						break;
					case 's':
						switchArray.add(new Switch(i + xx, j + yy));
						break;
					case 'R':
						goalArray.add(new Goal(i + xx, j + yy, "red"));
						break;
					case 'G':
						goalArray.add(new Goal(i + xx, j + yy, "green"));
						break;
					case 'B':
						goalArray.add(new Goal(i + xx, j + yy, "blue"));
						break;
					case 'Q':
						portalArray.add(new Portal2A(i + xx, j + yy));
						break;
					case 'q':
						portalArray.add(new Portal2B(i + xx, j + yy));
						break;
					}

					// if (ch == 'r') {
					// ballArray.add(new Ball(i + xx, j + yy, "red"));
					// hasRed = true;
					// } else if (ch == 'g') {
					// ballArray.add(new Ball(i + xx, j + yy, "green"));
					// hasGreen = true;
					// } else if (ch == 'b') {
					// ballArray.add(new Ball(i + xx, j + yy, "blue"));
					// hasBlue = true;
					// } else if (ch == 'w') {
					// wallArray.add(new WallNormal(i + xx, j + yy));
					// } else if (ch == 'a') {
					// wallArray.add(new WallGate(i + xx, j + yy, true));
					// } else if (ch == 'A') {
					// wallArray.add(new WallGate(i + xx, j + yy, false));
					// } else if (ch == 'o') {
					// wallArray.add(new WallOnce(i + xx, j + yy));
					// } else if (ch == 'l') {
					// wallArray.add(new WallGlass(i + xx, j + yy));
					// } else if (ch == '1') {
					// wallArray.add(new WallColored(i + xx, j + yy, "red"));
					// } else if (ch == '2') {
					// wallArray.add(new WallColored(i + xx, j + yy, "green"));
					// } else if (ch == '3') {
					// wallArray.add(new WallColored(i + xx, j + yy, "blue"));
					// } else if (ch == 'p') {
					// portalArray.add(new Portal1A(i + xx, j + yy));
					// } else if (ch == 'P') {
					// portalArray.add(new Portal1B(i + xx, j + yy));
					// } else if (ch == 's') {
					// switchArray.add(new Switch(i + xx, j + yy));
					// } else if (ch == 'R') {
					// goalArray.add(new Goal(i + xx, j + yy, "red"));
					// } else if (ch == 'G') {
					// goalArray.add(new Goal(i + xx, j + yy, "green"));
					// } else if (ch == 'B') {
					// goalArray.add(new Goal(i + xx, j + yy, "blue"));
					// } else if (ch == 'Q') {
					// portalArray.add(new Portal2A(i + xx, j + yy));
					// } else if (ch == 'q') {
					// portalArray.add(new Portal2B(i + xx, j + yy));
					// }
				}

			}
		}
		scanner.close();
		if (hasRed) {
			colorNumber += 1;
		}
		if (hasGreen) {
			colorNumber += 1;
		}
		if (hasBlue) {
			colorNumber += 1;
		}

		switch (getColors()) {
		case 1:
			redBar = 0;
			greenBar = AndroidFastRenderView.dstRect.right / 3;
			blueBar = AndroidFastRenderView.dstRect.right * 2 / 3;
			break;
		case 2:
			redBar = 0;
			greenBar = AndroidFastRenderView.dstRect.right / 2;
			blueBar = AndroidFastRenderView.dstRect.right;
			break;
		case 3:
			redBar = 0;
			greenBar = AndroidFastRenderView.dstRect.right;
			blueBar = AndroidFastRenderView.dstRect.right / 2;
			break;
		case 4:
			redBar = AndroidFastRenderView.dstRect.right;
			greenBar = 0;
			blueBar = AndroidFastRenderView.dstRect.right / 2;
			break;
		case 5:
			redBar = 0;
			greenBar = AndroidFastRenderView.dstRect.right;
			blueBar = AndroidFastRenderView.dstRect.right;
			break;
		case 6:
			redBar = AndroidFastRenderView.dstRect.right;
			greenBar = 0;
			blueBar = AndroidFastRenderView.dstRect.right;
			break;
		case 7:
			redBar = AndroidFastRenderView.dstRect.right;
			greenBar = AndroidFastRenderView.dstRect.right;
			blueBar = 0;
			break;
		case 8:
			redBar = AndroidFastRenderView.dstRect.right;
			greenBar = AndroidFastRenderView.dstRect.right;
			blueBar = AndroidFastRenderView.dstRect.right;
			break;
		}
	}

	@Override
	public void paint() {
		AndroidGraphics g = (AndroidGraphics) game.getGraphics();
		if (firstTimePainting) {
			bufferBitmap = Bitmap.createBitmap(g.canvas.getWidth(),
					g.canvas.getHeight(), Config.RGB_565);
			bufferCanvas = new Canvas(bufferBitmap);
			bufferCanvas.drawRGB(200, 200, 200);
			paint.setColor(Color.rgb(230, 230, 230));
			paint.setAntiAlias(true);
			for (int i = 0; i < 22; i++) {
				bufferCanvas.drawLine((float) (i * 40 + 19), 0f,
						(float) (i * 40 + 19), 854f, paint);
				bufferCanvas.drawLine(0f, (float) (i * 40 + 19), 600f,
						(float) (i * 40 + 19), paint);
			}
			paint.setColor(Color.WHITE);
			for (Wall w : wallArray) {
				if (w.getClass() == WallNormal.class) {
					WallNormal wn = (WallNormal) w;
					bufferCanvas.drawRect(wn.x - 20, wn.y - 20, wn.x + 19,
							wn.y + 19, paint);
				}
			}
			paint.setColor(Color.BLACK);
			for (Switch s : switchArray) {
				bufferCanvas.drawOval(new RectF(s.x - 10, s.y - 10, s.x + 10,
						s.y + 10), paint);
			}
			firstTimePainting = false;
		}
		g.canvas.drawBitmap(bufferBitmap, 0, 0, null);

		for (Wall w : wallArray) {
			w.draw(g);
		}
		for (Goal go : goalArray) {
			go.draw(g);
		}
		for (Portal p : portalArray) {
			p.draw(g);
		}

		for (int i = ballParticles.size() - 1; i >= 0; i--) {
			if (ballParticles.get(i).delete)
				ballParticles.remove(i);
		}
		for (BallParticle bp : ballParticles) {
			bp.draw(g);
		}

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
		reset = true;
	}

	@Override
	public void paintGUI() {
		paint.setTextSize(AndroidFastRenderView.dstRect.bottom / 20);
		paint.setTextAlign(Align.CENTER);
		paint.setColor(Color.rgb(255, 255, 255));
		AndroidFastRenderView.canvas
				.drawRect(
						AndroidFastRenderView.convertToAbsXCoordinate(0),
						AndroidFastRenderView
								.convertToAbsYCoordinate(AndroidFastRenderView.srcRect.top),
						AndroidFastRenderView
								.convertToAbsXCoordinate(AndroidFastRenderView.srcRect.right + 10),
						AndroidFastRenderView
								.convertToAbsYCoordinate(AndroidFastRenderView.srcRect.top)
								+ AndroidFastRenderView.dstRect.bottom / 10,
						paint);
		paint.setColor(Color.rgb(150, 150, 150));
		AndroidFastRenderView.canvas
				.drawLine(
						AndroidFastRenderView.dstRect.right / 2,
						AndroidFastRenderView
								.convertToAbsYCoordinate(AndroidFastRenderView.srcRect.top),
						AndroidFastRenderView.dstRect.right / 2,
						AndroidFastRenderView
								.convertToAbsYCoordinate(AndroidFastRenderView.srcRect.top)
								+ AndroidFastRenderView.dstRect.bottom / 10,
						paint);
		AndroidFastRenderView.canvas
				.drawText(
						"Menu",
						AndroidFastRenderView
								.convertToAbsXCoordinate((AndroidFastRenderView.srcRect.left + 300) / 2),
						AndroidFastRenderView
								.convertToAbsYCoordinate(AndroidFastRenderView.srcRect.top)
								+ AndroidFastRenderView.dstRect.bottom / 15,
						paint);
		AndroidFastRenderView.canvas
				.drawText(
						"Reset",
						AndroidFastRenderView
								.convertToAbsXCoordinate((AndroidFastRenderView.srcRect.right + 300) / 2),
						AndroidFastRenderView
								.convertToAbsYCoordinate(AndroidFastRenderView.srcRect.top)
								+ AndroidFastRenderView.dstRect.bottom / 15,
						paint);

		paint.setColor(Color.rgb(255, 255, 255));
		AndroidFastRenderView.canvas
				.drawRect(
						0,
						(float) (AndroidFastRenderView.dstRect.bottom - scoreScreenHeight),
						AndroidFastRenderView.dstRect.right,
						(float) (AndroidFastRenderView.dstRect.bottom
								- scoreScreenHeight
								+ AndroidFastRenderView.dstRect.bottom / 5 + AndroidFastRenderView.dstRect.bottom / 15),
						paint);

		paint.setColor(Color.rgb(200, 200, 200));
		AndroidFastRenderView.canvas
				.drawLine(
						AndroidFastRenderView.dstRect.right / 2,
						(float) (AndroidFastRenderView.dstRect.bottom - scoreScreenHeight),
						AndroidFastRenderView.dstRect.right / 2,
						(float) (AndroidFastRenderView.dstRect.bottom
								- scoreScreenHeight + AndroidFastRenderView.dstRect.bottom / 5),
						paint);

		AndroidFastRenderView.canvas
				.drawLine(
						0,
						(float) (AndroidFastRenderView.dstRect.bottom
								- scoreScreenHeight + AndroidFastRenderView.dstRect.bottom / 15),
						AndroidFastRenderView.dstRect.right,
						(float) (AndroidFastRenderView.dstRect.bottom
								- scoreScreenHeight + AndroidFastRenderView.dstRect.bottom / 15),
						paint);

		AndroidFastRenderView.canvas
				.drawLine(
						0,
						(float) (AndroidFastRenderView.dstRect.bottom
								- scoreScreenHeight + AndroidFastRenderView.dstRect.bottom / 5),
						AndroidFastRenderView.dstRect.right,
						(float) (AndroidFastRenderView.dstRect.bottom
								- scoreScreenHeight + AndroidFastRenderView.dstRect.bottom / 5),
						paint);

		paint.setTextSize(AndroidFastRenderView.dstRect.bottom / 30);
		paint.setColor(Color.rgb(150, 150, 150));
		AndroidFastRenderView.canvas
				.drawText(
						"Moves: " + GameScreen.moves,
						AndroidFastRenderView.dstRect.right / 4,
						(float) (AndroidFastRenderView.dstRect.bottom
								- scoreScreenHeight + AndroidFastRenderView.dstRect.bottom / 20),
						paint);
		AndroidFastRenderView.canvas
				.drawText(
						"Best: " + GameScreen.best,
						AndroidFastRenderView.dstRect.right * 3 / 4,
						(float) (AndroidFastRenderView.dstRect.bottom
								- scoreScreenHeight + AndroidFastRenderView.dstRect.bottom / 20),
						paint);

		if (state == GameState.ScoreScreen) {
			paint.setTextSize(AndroidFastRenderView.dstRect.bottom / 20);
			AndroidFastRenderView.canvas
					.drawText(
							"Restart",
							AndroidFastRenderView.dstRect.right / 4,
							(float) (AndroidFastRenderView.dstRect.bottom
									- scoreScreenHeight + AndroidFastRenderView.dstRect.bottom * 3 / 20),
							paint);

			AndroidFastRenderView.canvas
					.drawText(
							"Next",
							AndroidFastRenderView.dstRect.right * 3 / 4,
							(float) (AndroidFastRenderView.dstRect.bottom
									- scoreScreenHeight + AndroidFastRenderView.dstRect.bottom * 3 / 20),
							paint);

			int size = AndroidFastRenderView.dstRect.bottom / 20;
			AndroidFastRenderView.canvas
					.drawBitmap(
							((AndroidImage) Assets.starFullDark).bitmap,
							null,
							new Rect(
									AndroidFastRenderView.dstRect.right * 1 / 4
											- size / 2,
									(int) (AndroidFastRenderView.dstRect.bottom
											- scoreScreenHeight
											+ AndroidFastRenderView.dstRect.bottom
											/ 5 + AndroidFastRenderView.dstRect.bottom / 120),
									AndroidFastRenderView.dstRect.right * 1 / 4
											+ size / 2,
									(int) (AndroidFastRenderView.dstRect.bottom
											- scoreScreenHeight
											+ AndroidFastRenderView.dstRect.bottom
											/ 5 + size + AndroidFastRenderView.dstRect.bottom / 120)),
							MainMenuScreen.paint);
			if (moves <= twoStarMoves) {
				AndroidFastRenderView.canvas
						.drawBitmap(
								((AndroidImage) Assets.starFullDark).bitmap,
								null,
								new Rect(
										AndroidFastRenderView.dstRect.right * 2
												/ 4 - size / 2,
										(int) (AndroidFastRenderView.dstRect.bottom
												- scoreScreenHeight
												+ AndroidFastRenderView.dstRect.bottom
												/ 5 + AndroidFastRenderView.dstRect.bottom / 120),
										AndroidFastRenderView.dstRect.right * 2
												/ 4 + size / 2,
										(int) (AndroidFastRenderView.dstRect.bottom
												- scoreScreenHeight
												+ AndroidFastRenderView.dstRect.bottom
												/ 5 + size + AndroidFastRenderView.dstRect.bottom / 120)),
								MainMenuScreen.paint);
			} else {
				AndroidFastRenderView.canvas
						.drawBitmap(
								((AndroidImage) Assets.starEmptyDark).bitmap,
								null,
								new Rect(
										AndroidFastRenderView.dstRect.right * 2
												/ 4 - size / 2,
										(int) (AndroidFastRenderView.dstRect.bottom
												- scoreScreenHeight
												+ AndroidFastRenderView.dstRect.bottom
												/ 5 + AndroidFastRenderView.dstRect.bottom / 120),
										AndroidFastRenderView.dstRect.right * 2
												/ 4 + size / 2,
										(int) (AndroidFastRenderView.dstRect.bottom
												- scoreScreenHeight
												+ AndroidFastRenderView.dstRect.bottom
												/ 5 + size + AndroidFastRenderView.dstRect.bottom / 120)),
								MainMenuScreen.paint);
			}
			if (moves <= threeStarMoves) {
				AndroidFastRenderView.canvas
						.drawBitmap(
								((AndroidImage) Assets.starFullDark).bitmap,
								null,
								new Rect(
										AndroidFastRenderView.dstRect.right * 3
												/ 4 - size / 2,
										(int) (AndroidFastRenderView.dstRect.bottom
												- scoreScreenHeight
												+ AndroidFastRenderView.dstRect.bottom
												/ 5 + AndroidFastRenderView.dstRect.bottom / 120),
										AndroidFastRenderView.dstRect.right * 3
												/ 4 + size / 2,
										(int) (AndroidFastRenderView.dstRect.bottom
												- scoreScreenHeight
												+ AndroidFastRenderView.dstRect.bottom
												/ 5 + size + AndroidFastRenderView.dstRect.bottom / 120)),
								MainMenuScreen.paint);
			} else {
				AndroidFastRenderView.canvas
						.drawBitmap(
								((AndroidImage) Assets.starEmptyDark).bitmap,
								null,
								new Rect(
										AndroidFastRenderView.dstRect.right * 3
												/ 4 - size / 2,
										(int) (AndroidFastRenderView.dstRect.bottom
												- scoreScreenHeight
												+ AndroidFastRenderView.dstRect.bottom
												/ 5 + AndroidFastRenderView.dstRect.bottom / 120),
										AndroidFastRenderView.dstRect.right * 3
												/ 4 + size / 2,
										(int) (AndroidFastRenderView.dstRect.bottom
												- scoreScreenHeight
												+ AndroidFastRenderView.dstRect.bottom
												/ 5 + size + AndroidFastRenderView.dstRect.bottom / 120)),
								MainMenuScreen.paint);
			}
		}

		if (selectedColor == "red") {
			paint.setColor(Color.rgb(255, 0, 0));
			AndroidFastRenderView.canvas.drawRect(redBar,
					AndroidFastRenderView.dstRect.bottom
							- AndroidFastRenderView.dstRect.bottom / 15, redBar
							+ colorBarWidth,
					AndroidFastRenderView.dstRect.bottom, paint);
			paint.setColor(Color.rgb(0, 150, 0));
			AndroidFastRenderView.canvas.drawRect(greenBar,
					AndroidFastRenderView.dstRect.bottom
							- AndroidFastRenderView.dstRect.bottom / 15,
					greenBar + colorBarWidth,
					AndroidFastRenderView.dstRect.bottom, paint);
			paint.setColor(Color.rgb(0, 0, 150));
			AndroidFastRenderView.canvas.drawRect(blueBar,
					AndroidFastRenderView.dstRect.bottom
							- AndroidFastRenderView.dstRect.bottom / 15,
					blueBar + colorBarWidth,
					AndroidFastRenderView.dstRect.bottom, paint);
		} else if (selectedColor == "green") {
			paint.setColor(Color.rgb(150, 0, 0));
			AndroidFastRenderView.canvas.drawRect(redBar,
					AndroidFastRenderView.dstRect.bottom
							- AndroidFastRenderView.dstRect.bottom / 15, redBar
							+ colorBarWidth,
					AndroidFastRenderView.dstRect.bottom, paint);
			paint.setColor(Color.rgb(0, 255, 0));
			AndroidFastRenderView.canvas.drawRect(greenBar,
					AndroidFastRenderView.dstRect.bottom
							- AndroidFastRenderView.dstRect.bottom / 15,
					greenBar + colorBarWidth,
					AndroidFastRenderView.dstRect.bottom, paint);
			paint.setColor(Color.rgb(0, 0, 150));
			AndroidFastRenderView.canvas.drawRect(blueBar,
					AndroidFastRenderView.dstRect.bottom
							- AndroidFastRenderView.dstRect.bottom / 15,
					blueBar + colorBarWidth,
					AndroidFastRenderView.dstRect.bottom, paint);
		} else if (selectedColor == "blue") {
			paint.setColor(Color.rgb(150, 0, 0));
			AndroidFastRenderView.canvas.drawRect(redBar,
					AndroidFastRenderView.dstRect.bottom
							- AndroidFastRenderView.dstRect.bottom / 15, redBar
							+ colorBarWidth,
					AndroidFastRenderView.dstRect.bottom, paint);
			paint.setColor(Color.rgb(0, 150, 0));
			AndroidFastRenderView.canvas.drawRect(greenBar,
					AndroidFastRenderView.dstRect.bottom
							- AndroidFastRenderView.dstRect.bottom / 15,
					greenBar + colorBarWidth,
					AndroidFastRenderView.dstRect.bottom, paint);
			paint.setColor(Color.rgb(0, 0, 255));
			AndroidFastRenderView.canvas.drawRect(blueBar,
					AndroidFastRenderView.dstRect.bottom
							- AndroidFastRenderView.dstRect.bottom / 15,
					blueBar + colorBarWidth,
					AndroidFastRenderView.dstRect.bottom, paint);
		}
		paint.setColor(Color.WHITE);
		paint.setTextSize(AndroidFastRenderView.dstRect.bottom / 30);
		for (int i = 0; i < messages.size(); i++) {
			AndroidFastRenderView.canvas.drawText(messages.get(i),
					AndroidFastRenderView.dstRect.right / 2,
					AndroidFastRenderView.dstRect.bottom * (i + 3) / 20, paint);
		}
	}

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	private boolean inAbsBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.absX > x && event.absX < x + width - 1 && event.absY > y
				&& event.absY < y + height - 1)
			return true;
		else
			return false;
	}

	private static int getColors() {

		if (hasRed && hasGreen && hasBlue) {
			colorBarWidth = AndroidFastRenderView.dstRect.right / 3;
			return 1;
		} else if (hasRed && hasGreen && !hasBlue) {
			colorBarWidth = AndroidFastRenderView.dstRect.right / 2;
			return 2;
		} else if (hasRed && !hasGreen && hasBlue) {
			colorBarWidth = AndroidFastRenderView.dstRect.right / 2;
			return 3;
		} else if (!hasRed && hasGreen && hasBlue) {
			colorBarWidth = AndroidFastRenderView.dstRect.right / 2;
			return 4;
		} else if (hasRed && !hasGreen && !hasBlue) {
			colorBarWidth = AndroidFastRenderView.dstRect.right;
			return 5;
		} else if (!hasRed && hasGreen && !hasBlue) {
			colorBarWidth = AndroidFastRenderView.dstRect.right;
			return 6;
		} else if (!hasRed && !hasGreen && hasBlue) {
			colorBarWidth = AndroidFastRenderView.dstRect.right;
			return 7;
		} else if (!hasRed && !hasGreen && !hasBlue) {
			colorBarWidth = AndroidFastRenderView.dstRect.right;
			return 8;
		}
		return 0;
	}

}
