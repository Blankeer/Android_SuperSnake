package com.example.snake;

import java.util.ArrayList;
import java.util.List;

import com.example.snake.SnakeView.Point;

public class Snake {
	private List<Point> snakePath;// 路径
	private int orientation;
	public static final int OR_UP = 1, OR_DOWN = -1, OR_LEFT = 2,
			OR_RIGHT = -2;
	public static final int OR_LEFT_UP = 3;
	public static final int OR_LEFT_DOWN = -4;
	public static final int OR_RIGHT_UP = 4;
	public static final int OR_RIGHT_DOWN = -3;

	public Snake() {
		snakePath = new ArrayList<SnakeView.Point>();
		orientation = OR_UP;
	}

	public int getFanSheOr(boolean x, boolean y) {
		if (x && y) {
			return orientation;
		}
		if (!x && !y) {
			return -1 * orientation;
		}
		if (!x) {// x变为负数
			switch (orientation) {
			case OR_LEFT_UP:
				return OR_RIGHT_UP;
			case OR_LEFT_DOWN:
				return OR_RIGHT_DOWN;
			case OR_RIGHT_UP:
				return OR_LEFT_UP;
			case OR_RIGHT_DOWN:
				return OR_LEFT_DOWN;
			}
		}
		if (!y) {
			switch (orientation) {
			case OR_LEFT_UP:
				return OR_LEFT_DOWN;
			case OR_LEFT_DOWN:
				return OR_LEFT_UP;
			case OR_RIGHT_UP:
				return OR_RIGHT_DOWN;
			case OR_RIGHT_DOWN:
				return OR_RIGHT_UP;
			}
		}
		return -1 * orientation;
	}

	public void setOrientation(int a) {
		this.orientation = a;
	}

	public int getOrientation() {
		return orientation;
	}

	public int getLength() {
		return snakePath.size();
	}

	public void clear() {
		snakePath.clear();
	}

	public void addPotint(Point p) {
		snakePath.add(p);
	}

	public void addPoint(Point p, int index) {
		snakePath.add(index, p);
	}

	public void removeEnd() {
		snakePath.remove(snakePath.size() - 1);
	}

	public List<Point> getSnakePath() {
		return this.snakePath;
	}

}
