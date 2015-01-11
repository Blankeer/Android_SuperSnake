package com.example.snake;

import com.example.snake.SnakeView.Mode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class GameActivity extends Activity {
	SnakeView sv;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		boolean[] flag = intent.getBooleanArrayExtra("cb");
		sv = new SnakeView(this, flag);
		setContentView(sv);
	}

	protected void onDestroy() {
		super.onDestroy();
		sv.setMode(Mode.LOSE);
	}

}
