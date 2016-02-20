package com.droidyue.translate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.droidyue.app.utils.AppLog;
import com.droidyue.app.utils.AppUtils;

public class DroidReceiver extends BroadcastReceiver {
	private static final String LOGTAG = "DroidReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		AppLog.i(LOGTAG, "intent=" + intent);
		final String action = intent.getAction();
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
			DroidApplication.sNetworkIsConnected = AppUtils.isNetworkAvailable(context);
			AppLog.i(LOGTAG, "sNetworkIsConnected = " + DroidApplication.sNetworkIsConnected);
			Intent serviceIntent = new Intent(context, TranslateService.class);
			if (DroidApplication.sNetworkIsConnected) {
				context.startService(serviceIntent);
			} else {
				context.stopService(serviceIntent);
			}
		}
	}

}
