package com.droidyue.app.utils;

import android.util.Log;


public class AppLog {
	private static boolean sIsDebugMode; 
	private static final String BASE_LOGTAG = "AppLog_";
	
	public static void setDebuggable(boolean debuggable) {
		sIsDebugMode = debuggable;
	}
	
	public static final void i(final String tag, final String msg) {
		if (sIsDebugMode) {
			Log.i(BASE_LOGTAG  + tag, msg);
		}
	}
}
