package com.droidyue.translate;

import android.app.Activity;
import android.content.Context;

public class YoumiAdProxy {
	protected static final String LOGTAG = "YoumiAdProxy";
	private static YoumiAdProxy sInstance = new YoumiAdProxy();
	private Context mAppContext;
	private boolean mWatchedBefore;
	
	public static YoumiAdProxy getInstance() {
		return sInstance;
	}
	
	public void setup(Context context, String appKey, String appSecret, boolean debugMode) {
		mAppContext = context.getApplicationContext();
	}
	
	public void finalizeVideoAd() {
	}
	
	public final void prepareVideoAd() {
	}
	
	public void showVideoAd(Activity activity) {
		
	}
	
	
}
