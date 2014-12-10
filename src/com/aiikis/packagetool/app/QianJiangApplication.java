package com.aiikis.packagetool.app;

import com.qianjiang.framework.app.QJApplicationBase;

/**
 * 全局应用程序
 * 
 * @author cui.yp
 * 
 */
public class QianJiangApplication extends QJApplicationBase {
	// 测试包Key
	// public static final String strKey = "MLoOwYzufBBHwQdqEO2ZnMpl";
	// 正式包Key
	public static String LOCATE_CITYNAME = "";
	public static final int APP_INIT_STATE_SUCCESS = 1;
	public static final int THREAD_POOL_SIZE = 3;
	public static final int MEMORY_CACHE_SIZE = 1500000;
	public static int APP_INIT_STATE;

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		CONTEXT = this;
	}

	@Override
	protected void setAppSign() {
	}

	@Override
	protected void setClientType() {
	}

}
