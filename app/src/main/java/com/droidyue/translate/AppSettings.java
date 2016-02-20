package com.droidyue.translate;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.droidyue.app.utils.AppLog;

public class AppSettings {
	private static final String LOGTAG = "AppSettings";
	private static AppSettings sInstance = new AppSettings();
	private Context mAppContext;
	private SharedPreferences mDefaultPref;
	public static final boolean LONGER_RESULT_DEFAULT_VALUE = false;
	
	public String mDestLanguage;
	public boolean mLongerResult;
	
	private AppSettings() {
		
	}
	
	public static AppSettings getInstance() {
		return sInstance;
	}
	
	public final void setup(Context context) {
		mAppContext = context.getApplicationContext();
		mDefaultPref = PreferenceManager.getDefaultSharedPreferences(mAppContext);
		mapValues();
		AppLog.i(LOGTAG, "setup mDestLanguage=" + mDestLanguage);
	}
	
	private void mapValues() {
		mDestLanguage = mDefaultPref.getString(mAppContext.getString(R.string.pref_key_language), mAppContext.getString(R.string.pref_default_language_value));
		mLongerResult = mDefaultPref.getBoolean(mAppContext.getString(R.string.pref_key_longer_result), LONGER_RESULT_DEFAULT_VALUE);
	}
	
	public final  String getResString(int textResId) {
		return mAppContext.getString(textResId);
	}
	
	public final String[] getResStringArray(int textArrayResId) {
		return mAppContext.getResources().getStringArray(textArrayResId);
	}
	
	
}
