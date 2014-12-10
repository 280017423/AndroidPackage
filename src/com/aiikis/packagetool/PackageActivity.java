package com.aiikis.packagetool;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.aiikis.packagetool.tool.SshHelper;
import com.qianjiang.framework.widget.LoadingUpView;

public class PackageActivity extends Activity implements OnClickListener {

	private static final int STATUS_CONNECT_SUCCESS = 1;
	private static final int STATUS_CONNECT_FAIL = 2;
	private static final int STATUS_EXE_COMMAND_SUCCESS = 3;
	private static final int STATUS_EXE_COMMAND_FAIL = 4;
	private Button mBtnPackage;
	private LoadingUpView mLoadingUpView;
	private SshHelper mSshHelper;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (null != mLoadingUpView && mLoadingUpView.isShowing()) {
				mLoadingUpView.dismiss();
			}
			switch (msg.what) {
				case STATUS_CONNECT_SUCCESS:
					Toast.makeText(PackageActivity.this, "连接服务器成功", Toast.LENGTH_LONG).show();
					break;
				case STATUS_CONNECT_FAIL:
					Toast.makeText(PackageActivity.this, "连接服务器失败，\r\n请检查网络和登录信息", Toast.LENGTH_LONG).show();
					break;
				case STATUS_EXE_COMMAND_SUCCESS:
					Toast.makeText(PackageActivity.this, "打包失败", Toast.LENGTH_LONG).show();
					break;
				case STATUS_EXE_COMMAND_FAIL:
					Toast.makeText(PackageActivity.this, "打包成功", Toast.LENGTH_LONG).show();
					break;

				default:
					break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_package);
		initVariable();
		initView();
	}

	private void initVariable() {
		mLoadingUpView = new LoadingUpView(this);
		mSshHelper = SshHelper.getInstances();
	}

	private void initView() {
		mBtnPackage = (Button) findViewById(R.id.btn_connect);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_connect:
				connect();
				break;
			case R.id.btn_package:
				doPackage();
				break;

			default:
				break;
		}

	}

	private void doPackage() {
		if (null != mLoadingUpView && !mLoadingUpView.isShowing()) {
			mLoadingUpView.showPopup();
		}
		final String command = "export JAVA_HOME=/usr/local/jdk1.7.0_55; export CLASSPATH=$CLASSPATH:.:$JAVA_HOME/lib:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar;export PATH=$PATH:$JAVA_HOME/bin;export ANT_HOME=/usr/local/apache-ant-1.9.3; export PATH=$PATH:$ANT_HOME/bin; cd /usr/local/package_tool/; source build.sh jyt qianjiang";
		// final String command = "ls -l";
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Integer status = mSshHelper.exeCmd(command);
					if (null != status && 0 == status) {
						sendMessage(STATUS_EXE_COMMAND_SUCCESS);
					} else {
						sendMessage(STATUS_EXE_COMMAND_FAIL);
					}
				} catch (IOException e) {
					sendMessage(STATUS_EXE_COMMAND_FAIL);
					e.printStackTrace();
				}
			}
		}).start();

	}

	private void connect() {
		if (null != mLoadingUpView && !mLoadingUpView.isShowing()) {
			mLoadingUpView.showPopup();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean isSuccess = mSshHelper.connect();
				if (isSuccess) {
					sendMessage(STATUS_CONNECT_SUCCESS);
				} else {
					sendMessage(STATUS_CONNECT_FAIL);
					mSshHelper.closeConnection();
				}
			}
		}).start();
	}

	private void sendMessage(int what) {
		Message msg = mHandler.obtainMessage();
		msg.what = what;
		mHandler.sendMessage(msg);
	}

	@Override
	protected void onDestroy() {
		mSshHelper.closeConnection();
		super.onDestroy();
	}

}
