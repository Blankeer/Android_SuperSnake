package com.example.snake;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

public class SnakeView extends TileView implements Runnable {

	class Mode {
		public final static int READY = 1, PAUSE = 0, RUNNING = 2, LOSE = 3;
	}

	class GameMode {
		private boolean CHUANQIANG = false, FANSHE = false, WUDI = false,
				XIEZHE = false;

		public GameMode(boolean cHUANQIANG, boolean fANSHE, boolean wUDI,
				boolean xIEZHE) {
			CHUANQIANG = cHUANQIANG;
			FANSHE = fANSHE;
			WUDI = wUDI;
			XIEZHE = xIEZHE;
		}

	}

	public void run() {
		while (nowMode == Mode.RUNNING) {
			handler.sendEmptyMessage(0);
			try {
				Thread.sleep(v);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			update();
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			invalidate();
		}

	};
	private boolean[] flag;
	private static final String TAG = "SnakeView";
	private static int nowMode;
	private static Snake snake;
	private static int h, w;
	private static int v = 1000;
	private static Point applePoint;
	// private static int gameMode = 3;// 1����ģʽ��2���ϰ��ɴ�ǽ��3��2�Ļ����ϼ����б����
	public static GameMode gameMode;
	private Context context;

	public SnakeView(Context context) {
		super(context);
		nowMode = Mode.READY;
		snake = new Snake();
	}

	public SnakeView(Context context, boolean flag[]) {
		super(context);
		this.context = context;
		nowMode = Mode.READY;
		snake = new Snake();
		this.flag = flag;
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.h = h;
		this.w = w;
		initGame();// /////////////
	}

	private void initGame() {
		gameMode = new GameMode(flag[0], flag[1], flag[2], flag[3]);
		v = 1000;
		snake.clear();
		clearTiles();
		updateWalls();
		Point p = new Point(xTileCount / 2, yTileCount / 2), p1 = new Point(
				xTileCount / 2, yTileCount / 2 + 1);
		snake.addPotint(p);
		snake.addPotint(p1);// �ߵĳ�ʼ����������
		updataApple();// ����һ��ƻ��
		this.setMode(Mode.RUNNING);
		new Thread(this).start();
	}

	private void update() {
		updateSnake();
	}

	private void updateSnake() {// ��
		List<Point> list = snake.getSnakePath();
		Point head = list.get(0), newHead = new Point(0, 0);
		switch (snake.getOrientation()) {
		case Snake.OR_DOWN:
			newHead.setXY(head.getX(), head.getY() + 1);
			break;
		case Snake.OR_UP:
			newHead.setXY(head.getX(), head.getY() - 1);
			break;
		case Snake.OR_LEFT:
			newHead.setXY(head.getX() - 1, head.getY());
			break;
		case Snake.OR_RIGHT:
			newHead.setXY(head.getX() + 1, head.getY());
			break;
		}
		if (this.gameMode.XIEZHE) {// ��б����
			switch (snake.getOrientation()) {
			case Snake.OR_LEFT_UP:
				newHead.setXY(head.getX() - 1, head.getY() - 1);
				break;
			case Snake.OR_LEFT_DOWN:
				newHead.setXY(head.getX() - 1, head.getY() + 1);
				break;
			case Snake.OR_RIGHT_UP:
				newHead.setXY(head.getX() + 1, head.getY() - 1);
				break;
			case Snake.OR_RIGHT_DOWN:
				newHead.setXY(head.getX() + 1, head.getY() + 1);
				break;
			}
		}
		if (isGameOver(newHead)) {
			setMode(Mode.LOSE);
			// Toast.makeText(context, "Game Over!", 0).show();
			Log.i(TAG, "��Ϸ����");
			return;
		}
		nextPoint(newHead);
		list.add(0, newHead);
		if (newHead.equals(applePoint)) {// �Ե�һ��ƻ��
			if (v > 30) {
				v = (int) (v * 0.7);
			}
			Log.i(TAG, "�Ե�һ��ƻ��");
			updataApple();
		} else {
			Point p2 = list.get(list.size() - 1);
			tileGrid[p2.getX()][p2.getY()] = -1;// �������һ��β��
			snake.removeEnd();
		}
		for (Point p : list) {
			tileGrid[p.getX()][p.getY()] = SNAKE;
		}
	}

	private void updataApple() {
		applePoint = getApplePoint();
		tileGrid[applePoint.getX()][applePoint.getY()] = APPLE;
		Log.i(TAG, "����һ��ƻ����" + applePoint);
	}

	private Point getApplePoint() {// ����ƻ��
		Point p = new Point(0, 0);
		int randX = 0, randY = 0;
		Random r = new Random();
		while (true) {
			if (this.gameMode.CHUANQIANG) {
				randX = r.nextInt(xTileCount);
				randY = r.nextInt(yTileCount);
			} else {
				randX = r.nextInt(xTileCount - 1) + 1;
				randY = r.nextInt(yTileCount - 1) + 1;
			}
			p.setXY(randX, randY);
			int flag = 0;
			for (Point t : snake.getSnakePath()) {
				if (t.equals(p)) {
					flag = 1;
					break;
				}
			}
			if (flag == 0) {
				break;
			}
		}
		return p;
	}

	private void nextPoint(Point head) {// ��һ�ڵ�ı仯
		int x = head.getX(), y = head.getY(), xx = x, yy = y;
		if (this.gameMode.CHUANQIANG) {// ���ϰ����ɴ�ǽ
			if (x < 0) {
				x += xTileCount;
			} else if (x >= xTileCount) {
				x -= xTileCount;
			}
			if (y < 0) {
				y += yTileCount;
			} else if (y >= yTileCount) {
				y -= yTileCount;
			}
			head.setXY(x, y);
			return;
		}
		if (this.gameMode.FANSHE) {// ����
			if (x < 0) {
				x = 1;
			} else if (x >= xTileCount) {
				x = xTileCount - 2;
			}
			if (y < 0) {
				y = 1;
			} else if (y >= yTileCount) {
				y = yTileCount - 2;
			}
			snake.setOrientation(snake.getFanSheOr(x == xx, y == yy));
			head.setXY(x, y);
			return;
		}
	}

	private boolean isGameOver(Point head) {// �ж���Ϸ�Ƿ����
		if (this.gameMode.CHUANQIANG == false && this.gameMode.FANSHE == false) {// ���ܴ�ǽ�ͼ��߽�
			int x = head.getX(), y = head.getY();
			if (x < 1 || x >= xTileCount - 1) {// �߽�
				return true;
			}
			if (y < 1 || y >= yTileCount - 1) {
				return true;
			}
		}
		if (this.gameMode.WUDI) {// �޵�
			for (Point p : snake.getSnakePath()) {// �ܲ��ܳ����Լ�
				if (p.equals(head)) {
					return true;
				}
			}
		}
		return false;
	}

	public void setMode(int mode) {
		this.nowMode = mode;
	}

	private void updateWalls() {
		if (this.gameMode.CHUANQIANG || this.gameMode.FANSHE) {// ���ϰ����ɴ�ǽ
			return;
		}
		for (int i = 0; i < xTileCount; i++) {
			tileGrid[i][0] = WALL;
			tileGrid[i][yTileCount - 1] = WALL;
		}
		for (int i = 0; i < yTileCount; i++) {
			tileGrid[0][i] = WALL;
			tileGrid[xTileCount - 1][i] = WALL;
		}
	}

	public boolean onTouchEvent(MotionEvent event) {// �������,�������ң�б
		if (nowMode != Mode.RUNNING) {
			return true;
		}
		float x = event.getX(), y = event.getY();
		// System.out.println("x:" + x + ",y:" + y);
		int or = -1;
		float tx = w / 3, ty = h / 3, kx = x / tx, ky = y / ty;
		if (kx >= 1 && kx <= 2) {
			if (ky <= 1) {
				or = Snake.OR_UP;
			} else if (ky >= 2) {
				or = Snake.OR_DOWN;
			}
		}
		if (ky >= 1 && ky <= 2 && or == -1) {
			if (kx <= 1) {
				or = Snake.OR_LEFT;
			} else if (kx >= 2) {
				or = Snake.OR_RIGHT;
			}
		}
		if (this.gameMode.XIEZHE) {// ��б����
			if (kx <= 1 && or == -1) {
				if (ky <= 1) {
					or = Snake.OR_LEFT_UP;
				} else if (ky >= 2) {
					or = Snake.OR_LEFT_DOWN;
				}
			}
			if (kx >= 2 && or == -1) {
				if (ky <= 1) {
					or = Snake.OR_RIGHT_UP;
				} else if (ky >= 2) {
					or = Snake.OR_RIGHT_DOWN;
				}
			}
		}
		if (or + snake.getOrientation() != 0) {
			snake.setOrientation(or);
			Log.i(TAG, "̰���߷���ı�Ϊ:" + or);
		}
		return true;
	}

	class Point {
		int x, y;

		public String toString() {
			return "Point [x=" + x + ", y=" + y + "]";
		}

		public void setXY(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public boolean equals(Point p) {
			if (this == p)
				return true;
			if (p == null)
				return false;
			if (p.x == this.x && p.y == this.y) {
				return true;
			}
			return false;
		}

		public Point(int x, int y) {
			setXY(x, y);
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

	}

}
