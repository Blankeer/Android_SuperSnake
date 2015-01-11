package com.example.snake;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;

public class MainActivity extends Activity {
	private CheckBox[] cb = new CheckBox[4];
	private Button bu;
	private boolean[] flag = new boolean[4];

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		bu = (Button) findViewById(R.id.button);
		cb[0] = (CheckBox) findViewById(R.id.cb_chuanqiang);
		cb[1] = (CheckBox) findViewById(R.id.cb_fanshe);
		cb[2] = (CheckBox) findViewById(R.id.cb_wudi);
		cb[3] = (CheckBox) findViewById(R.id.cb_xiezhe);
		bu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				for (int i = 0; i < flag.length; i++) {
					flag[i] = cb[i].isChecked();
				}
				Intent intent = new Intent(MainActivity.this,
						GameActivity.class);
				intent.putExtra("cb", flag);
				startActivity(intent);
			}
		});
	}

}
