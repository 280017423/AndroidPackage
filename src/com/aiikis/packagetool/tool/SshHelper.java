package com.aiikis.packagetool.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.qianjiang.framework.util.PackageUtil;

public class SshHelper {
	private static final String HOSTNAME = PackageUtil.getConfigString("host_name");
	private static final String USERNAME = PackageUtil.getConfigString("user_name");
	private static final String PASSWORD = PackageUtil.getConfigString("password");
	private Connection mConnection;
	private static SshHelper MSSHHELPER;

	private SshHelper() {
	}

	public static SshHelper getInstances() {
		if (null == MSSHHELPER) {
			MSSHHELPER = new SshHelper();
		}
		return MSSHHELPER;
	}

	// public static void main(String[] args) {
	String command = "export JAVA_HOME=/usr/local/jdk1.7.0_55; export CLASSPATH=$CLASSPATH:.:$JAVA_HOME/lib:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar;export PATH=$PATH:$JAVA_HOME/bin;export ANT_HOME=/usr/local/apache-ant-1.9.3; export PATH=$PATH:$ANT_HOME/bin; cd /usr/local/package_tool/; source build.sh jyt qianjiang";

	//
	// try {
	// init();
	// // runSSH(mConnection,
	// // "cd /usr/local/package_tool/android_package_res/ ; git pull");
	// // runSSH(mConnection,
	// //
	// "cd /usr/local/apache-tomcat-8.0.5/webapps/App/ ; mv jyt_2014_12_09_16_21_33.apk jyt_v1.3.1.apk");
	// runSSH(mConnection, command);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// closeConnection();
	// }

	public boolean connect() {
		boolean isSuccess = false;
		try {
			mConnection = getOpenedConnection(HOSTNAME, USERNAME, PASSWORD);
			isSuccess = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}

	/**
	 * 执行ssh命令
	 * 
	 * @param connection
	 *            Connection
	 * @param cmd
	 *            执行的命令
	 * @return exit status
	 * @throws IOException
	 */
	public int exeCmd(String cmd) throws IOException {
		if (null == cmd || "".equals(cmd)) {
			System.out.println("命令不能为空");
		}
		if (null == mConnection) {
			return -1;
		}
		System.out.println("运行ssh命令 [" + cmd + "]");

		Session sess = mConnection.openSession();
		sess.execCommand(cmd);

		InputStream stdout = new StreamGobbler(sess.getStdout());
		InputStream stderr = new StreamGobbler(sess.getStderr());
		BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
		BufferedReader stderrReader = new BufferedReader(new InputStreamReader(stderr));

		while (true) {
			String line = br.readLine();
			if (line == null) {
				break;
			}
			Log.d("aaa", line);
		}
		while (true) {
			String line = stderrReader.readLine();
			if (line == null) {
				break;
			}
			Log.d("aaa", line);
		}
		br.close();
		stderrReader.close();
		sess.close();
		Integer statu = sess.getExitStatus().intValue();
		Log.d("aaa", "status:" + statu);
		return statu;
	}

	/**
	 * return a opened Connection
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @return
	 * @throws IOException
	 */
	private static Connection getOpenedConnection(String host, String username, String password) throws IOException {
		Connection conn = new Connection(host);
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPassword(username, password);
		if (isAuthenticated == false) {
			throw new IOException("连接服务器失败，请检查网络和登录信息");
		}
		return conn;
	}

	public void closeConnection() {
		if (null != mConnection) {
			mConnection.close();
		}
		mConnection = null;
	}
}
