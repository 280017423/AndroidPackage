package com.aiikis.packagetool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Button mBtnPackage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initVariable();
		initView();
	}

	private void initVariable() {

	}

	private void initView() {
		mBtnPackage = (Button) findViewById(R.id.btn_package);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_package:
				startActivity(new Intent(MainActivity.this, PackageActivity.class));
				break;

			default:
				break;
		}

	}

}
