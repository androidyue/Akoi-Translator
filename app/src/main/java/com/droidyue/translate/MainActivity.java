package com.droidyue.translate;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.droidyue.app.utils.AppLog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends PreferenceActivity implements OnPreferenceClickListener {
	private static final String LOGTAG = "MainActivity";
	private SharedPreferences mPref;
	private OnSharedPreferenceChangeListener mPrefListener = new OnSharedPreferenceChangeListener() {

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			AppLog.i(LOGTAG, "onSharedPreferenceChanged key=" + key);
			if (getString(R.string.pref_key_language).equals(key)) {
				String value = sharedPreferences.getString(key, getString(R.string.pref_default_language_value));
				AppSettings.getInstance().mDestLanguage = value;
				Preference p = getPreferenceManager().findPreference(key);
				String summary = getDisplayLanguage(value);
				p.setSummary(summary);
			} else if (getString(R.string.pref_key_longer_result).equals(key)) {
				boolean value = sharedPreferences.getBoolean(key, AppSettings.LONGER_RESULT_DEFAULT_VALUE);
				AppSettings.getInstance().mLongerResult = value;
			}
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupUI();
		mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		startService(new Intent(getApplicationContext(), TranslateService.class));
		YoumiAdProxy.getInstance().setup(this, "1b6a6d953afe4d3f", "66b175e0a3c615ee", DroidApplication.sIsDebugMode);
		YoumiAdProxy.getInstance().prepareVideoAd();
		UmengUpdateAgent.update(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		mPref.unregisterOnSharedPreferenceChangeListener(mPrefListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPref.registerOnSharedPreferenceChangeListener(mPrefListener);
		MobclickAgent.onResume(this);
	}
	
	
		
	@Override
	protected void onDestroy() {
		YoumiAdProxy.getInstance().finalizeVideoAd();
		super.onDestroy();
	}

	private void setupUI() {
		setTitle(R.string.app_title);
		addPreferencesFromResource(R.xml.settings);
		bindPreferenceClickListener(R.string.pref_key_feedback);
		bindPreferenceClickListener(R.string.pref_key_about);
//		bindPreferenceClickListener(R.string.pref_key_recommend);
		bindPreferenceClickListener(R.string.pref_key_donate);
		Preference p = getPreferenceManager().findPreference(getString(R.string.pref_key_language));
		p.setSummary(getDisplayLanguage(AppSettings.getInstance().mDestLanguage));
		
		p = getPreferenceManager().findPreference(getString(R.string.pref_key_longer_result));
		p.setDefaultValue(AppSettings.LONGER_RESULT_DEFAULT_VALUE);
	}

	private void bindPreferenceClickListener(int keyResId) {
		PreferenceManager manager = getPreferenceManager();
		Preference p = manager.findPreference(getString(keyResId));
		if (null != p) {
			p.setOnPreferenceClickListener(this);
		}
	}
	
	private final String getDisplayLanguage(String languageValue) {
		String displayLanguage = null;
		Resources res = getResources();
		String[] entries = res.getStringArray(R.array.baidu_support_language_entries);
		String[] entryValues = res.getStringArray(R.array.baidu_support_language_entry_values);
		if (entries.length == entryValues.length) {
			for (int i =0; i < entries.length; i ++ ) {
				if (entryValues[i].equals(languageValue)) {
					displayLanguage = entries[i];
				}
			}
		}
		return displayLanguage;
	}
	
	@Override
	public boolean onPreferenceClick(Preference preference) {
		String clickedKey = preference.getKey();
		MobclickAgent.onEvent(this, clickedKey);
		AppLog.i(LOGTAG, "onPreferenceClick clickedKey=" + clickedKey);
		if (getString(R.string.pref_key_feedback).equals(clickedKey)) {
			FeedbackAgent agent = new FeedbackAgent(this);
			agent.startFeedbackActivity();
		} else if (getString(R.string.pref_key_recommend).equals(clickedKey)) {
			YoumiAdProxy.getInstance().showVideoAd(this);
		} else if (getString(R.string.pref_key_about).equals(clickedKey)) {
			showDialog(0);
		} else if (getString(R.string.pref_key_donate).equals(clickedKey)) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("http://droidyue.com/donate/"));
			startActivity(intent);
		}
		return false;
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.about).setMessage(R.string.about_dialog_content)
		.setPositiveButton(android.R.string.ok, null)
		.setNegativeButton(android.R.string.cancel, null);
		return builder.create();
	}
	
	
	
}
