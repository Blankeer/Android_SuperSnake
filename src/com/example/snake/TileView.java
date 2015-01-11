package com.example.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;

public class TileView extends View {
	private static int tileSize;
	protected static int xTileCount;
	protected static int yTileCount;
	private static int xOffset;
	private static int yOffset;
	private static int imgSize;
	private Bitmap[] tileArrray;
	protected int[][] tileGrid;
	private final static String TAG = "TileView";
	protected static final int WALL = 0, SNAKE = 1, APPLE = 2;

	public TileView(Context context) {
		super(context);
		tileSize = 20;
		imgSize = tileSize - 1;
		tileArrray = new Bitmap[3];
		tileArrray[0] = BitmapFactory.decodeResource(getResources(),
				R.drawable.img_1);
		tileArrray[1] = BitmapFactory.decodeResource(getResources(),
				R.drawable.img_2);
		tileArrray[2] = BitmapFactory.decodeResource(getResources(),
				R.drawable.img_1);
		for (int i = 0; i < tileArrray.length; i++) {
			Matrix matrix = new Matrix();
			matrix.postScale((float) imgSize / tileArrray[i].getWidth(),
					(float) imgSize / tileArrray[i].getHeight());
			tileArrray[i] = Bitmap.createBitmap(tileArrray[i], 0, 0,
					tileArrray[i].getWidth(), tileArrray[i].getHeight(),
					matrix, true);

		}
	}

	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		for (int i = 0; i < xTileCount; i++) {
			for (int j = 0; j < yTileCount; j++) {
				if (tileGrid[i][j] > -1) {
					canvas.drawBitmap(tileArrray[tileGrid[i][j]], xOffset + i
							* tileSize, yOffset + j * tileSize, null);
				}
			}
		}
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Log.i(TAG, "窗口大小发生变化。");
		xTileCount = w / tileSize;
		yTileCount = h / tileSize;
		xOffset = (w - xTileCount * tileSize) / 2;
		yOffset = (h - yTileCount * tileSize) / 2;
		tileGrid = new int[xTileCount][yTileCount];
		clearTiles();
	}

	protected void clearTiles() {
		for (int i = 0; i < xTileCount; i++) {
			for (int j = 0; j < yTileCount; j++) {
				tileGrid[i][j] = -1;
			}
		}
	}

}
