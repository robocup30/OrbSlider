package com.hiddenpixels.orbSlider;

import com.hiddenpixels.framework.Graphics;
import com.hiddenpixels.framework.Input.TouchEvent;

import android.graphics.Rect;

public class Ball {

	int x, y;
	String colorString;
	Rect rect, rectSmall;
	String direction;
	int size = 40;
	public int moveSpeed, vMoveSpeed, hMoveSpeed;
	Wall wallInCollision;
	Ball ballInCollision;
	boolean movedThisStep = false;
	boolean wasInMotion = false;

	// List<BallParticle> ballParticles = new ArrayList<BallParticle>();

	public Ball(int x, int y, String colorString) {
		this.x = x * size;
		this.y = y * size;
		this.colorString = colorString;
		moveSpeed = 10;
		hMoveSpeed = 0;
		vMoveSpeed = 0;
		rect = new Rect(this.x - size / 2, this.y - size / 2,
				this.x + size / 2, this.y + size / 2);
		rectSmall = new Rect(this.x - 2, this.y - 2, this.x + 2, this.y + 2);
		direction = "still";
		wallInCollision = null;
		ballInCollision = null;
	}

	public void createParticle() {

		// Particle p = new Particle(x, y, Assets.circleParticle);
		// p.setSize(1, 1, -.08, 1, 1);
		// p.setLifespan(13, 13);
		// if (colorString == "red") {
		// p.setColor(Color.argb(255, 200, 70, 70));
		// } else if (colorString == "green") {
		// p.setColor(Color.argb(255, 70, 200, 70));
		// } else if (colorString == "blue") {
		// p.setColor(Color.argb(255, 70, 70, 200));
		// }
		// GameScreen.ps.add(p);
	}

	public void updateRect() {
		rect.set(this.x - size / 2, this.y - size / 2, this.x + size / 2,
				this.y + size / 2);
		rectSmall.set(this.x - 2, this.y - 2, this.x + 2, this.y + 2);

	}

	public void move() {
		if (!movedThisStep && direction != "still") {
			if (wallInCollision == null && ballInCollision == null) {
				// empty
				normalMove();
			} else if (wallInCollision == null && ballInCollision != null) {
				// there is ball
				if (ballInCollision.colorString == colorString) {
					ballInCollision.move();
				}
				checkBallCollisionAgain(ballInCollision);
				if (ballInCollision == null) {
					normalMove();
				} else {
					normalStop();
				}
			} else if (ballInCollision == null && wallInCollision != null) {
				// there is wall
				if (wallInCollision.getClass() == WallNormal.class) {
					normalStop();
				} else if (wallInCollision.getClass() == WallOnce.class) {
					WallOnce wo = (WallOnce) wallInCollision;
					if (wo.activated) {
						normalStop();
					} else if (!wo.activated) {
						if (wo.b == null) {
							wo.b = this;
							normalMove();
						} else if (wo.b == this) {
							normalMove();
						} else {
							normalStop();
						}
					}
				} else if (wallInCollision.getClass() == WallGate.class) {
					WallGate wg = (WallGate) wallInCollision;
					if (wg.isOpen) {
						normalMove();
					} else {
						normalStop();
					}
				} else if (wallInCollision.getClass() == WallGlass.class) {
					WallGlass wg = (WallGlass) wallInCollision;
					if (wg.activated) {
						if (wasInMotion) {
							wg.shatter();
						}
						normalStop();
					} else {
						normalMove();
					}
				} else if (wallInCollision.getClass() == WallColored.class) {
					WallColored wc = (WallColored) wallInCollision;
					if (wc.colorString == colorString) {
						normalMove();
					} else {
						normalStop();
					}
				}
			} else {
				// there are both
				if (ballInCollision.colorString != colorString) {
					normalStop();
				} else if (wallInCollision.getClass() == WallOnce.class) {
					normalStop();
				} else if (wallInCollision.getClass() == WallColored.class) {
					ballInCollision.move();
					checkBallCollisionAgain(ballInCollision);
					if (ballInCollision == null) {
						normalMove();
					} else {
						normalStop();
					}
				} else if (wallInCollision.getClass() == WallGlass.class) {
					ballInCollision.move();
					checkBallCollisionAgain(ballInCollision);
					if (ballInCollision == null) {
						normalMove();
					} else {
						normalStop();
					}
				} else if(wallInCollision.getClass() == WallGate.class) {
					ballInCollision.move();
					checkBallCollisionAgain(ballInCollision);
					if (ballInCollision == null) {
						normalMove();
					} else {
						normalStop();
					}
				} else {
					normalMove();
				}
			}
			movedThisStep = true;
		}
		if (x < -40 || x > 640 || y < -40 || y > 894) {
			GameScreen.reset = true;
		}
		GameScreen.ballParticles.add(new BallParticle(x, y, colorString));
	}

	public void normalMove() {
		setHVMoveSpeed();
		x += hMoveSpeed;
		y += vMoveSpeed;
		updateRect();
		wasInMotion = true;
	}

	public void normalStop() {
		wasInMotion = false;
		direction = "still";
		updateRect();
	}

	public void checkWallCollision(Wall w) {
		if (wallInCollision == null) {
			setHVMoveSpeed();
			setRectSmall();
			if (Rect.intersects(rectSmall, w.rect)) {
				wallInCollision = w;
			} else {
				wallInCollision = null;
			}
			rectSmall.set(x, y, x, y);
		}
	}

	public void checkBallCollision(Ball b) {
		if (ballInCollision == null) {
			setRectSmall();
			if (Rect.intersects(rectSmall, b.rect) && this != b) {
				ballInCollision = b;
			} else {
				ballInCollision = null;
			}
			rectSmall.set(x, y, x, y);
		}
	}

	public void checkBallCollisionAgain(Ball b) {
		setRectSmall();
		if (Rect.intersects(rectSmall, b.rect) && this != b) {
			ballInCollision = b;
		} else {
			ballInCollision = null;
		}
		rectSmall.set(x, y, x, y);
	}

	public void reset() {
		ballInCollision = null;
		wallInCollision = null;
		movedThisStep = false;
	}

	public void setRectSmall() {
		if (direction == "still") {
			rectSmall.set(x, y, x, y);
		} else if (direction == "left") {
			rectSmall.set(x - size / 2 - 2, y, x - size / 2 - 1, y + 1);
		} else if (direction == "right") {
			rectSmall.set(x + size / 2 + 1, y, x + size / 2 + 2, y + 1);
		} else if (direction == "up") {
			rectSmall.set(x, y - size / 2 - 2, x + 1, y - size / 2 - 1);
		} else if (direction == "down") {
			rectSmall.set(x, y + size / 2 + 1, x + 1, y + size / 2 + 2);
		}
	}

	public void draw(Graphics g) {

	}

	public void setHVMoveSpeed() {
		if (direction == "still") {
			hMoveSpeed = 0;
			vMoveSpeed = 0;
		} else if (direction == "left") {
			hMoveSpeed = -moveSpeed;
			vMoveSpeed = 0;
		} else if (direction == "right") {
			hMoveSpeed = moveSpeed;
			vMoveSpeed = 0;
		} else if (direction == "up") {
			hMoveSpeed = 0;
			vMoveSpeed = -moveSpeed;
		} else if (direction == "down") {
			hMoveSpeed = 0;
			vMoveSpeed = moveSpeed;
		}
	}

	public String getDirection() {
		return direction;
	}

	public String getColorString() {
		return colorString;
	}

	public void setColorString(String colorString) {
		this.colorString = colorString;
	}

	public Rect getRect() {
		return rect;
	}

	public void setRect(Rect rect) {
		this.rect = rect;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public boolean checkTouch(TouchEvent event) {
		if (inBounds(event, x - size / 2, y - size / 2, size, size)) {
			return true;
		} else {
			return false;
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
}
