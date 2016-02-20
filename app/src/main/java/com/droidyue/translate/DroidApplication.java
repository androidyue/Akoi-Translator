package com.droidyue.translate;

import com.droidyue.app.utils.AppLog;

import android.app.Application;

public class DroidApplication extends Application{
	public final static boolean sIsDebugMode = true;
	public static boolean sNetworkIsConnected;
	
	@Override
	public void onCreate() {
		super.onCreate();
		AppLog.setDebuggable(sIsDebugMode);
		AppSettings.getInstance().setup(this);
	}
	
}
