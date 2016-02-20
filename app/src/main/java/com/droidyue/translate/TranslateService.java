package com.droidyue.translate;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.droidyue.app.utils.AppLog;
import com.droidyue.app.utils.AppUtils;
import com.droidyue.translate.core.BaiduTranslator;
import com.droidyue.translate.core.TranslateModels.OnTranslatedListener;
import com.droidyue.translate.core.TranslateModels.TranslateRequest;
import com.droidyue.translate.core.TranslateModels.TranslateResult;
import com.droidyue.translate.core.Translator;
import com.umeng.analytics.MobclickAgent;

public class TranslateService extends Service implements OnPrimaryClipChangedListener, OnTranslatedListener{
	private static final String LOGTAG = "TranslateService";
	private ClipboardManager mClipManager;
	private Translator mTranslator = new BaiduTranslator();
	private String mLastText;
	
	private final Handler mHandler = new Handler() {
		
	};
	
	@Override
	public void onCreate() {
		super.onCreate();
		AppLog.i(LOGTAG, "onCreate");
		mClipManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		mClipManager.addPrimaryClipChangedListener(this);
		mTranslator.registerOnTranslateResultListener(this);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onPrimaryClipChanged() {
		ClipData data = mClipManager.getPrimaryClip();
		if (null != data) {
			Item item = data.getItemAt(data.getItemCount() - 1);
			CharSequence text = item.getText();
			if (shouldTranslate(text)) {
				TranslateRequest request = new TranslateRequest();
				request.mFromLanguage = "auto";
				request.mToLanguage = AppSettings.getInstance().mDestLanguage;
				mLastText = text.toString();
				request.mQuery = mLastText; 
				mTranslator.startTranslate(request);
				lazyResetLastText();
			}
		}
	}
	
	private boolean shouldTranslate(CharSequence text) {
		boolean shouldTranslate = true;
		if (TextUtils.isEmpty(text)) {
			shouldTranslate = false;
		} else if (text.equals(mLastText)) {
			shouldTranslate = false;
		} else if (AppUtils.isURI(text.toString())) {
			AppLog.i(LOGTAG, "shouldTranslate isURI" + text);
			shouldTranslate = false;
		}
		return shouldTranslate;
	}
	
	private void lazyResetLastText() {	
		mHandler.postDelayed(new Runnable () {

			@Override
			public void run() {
				mLastText = null;
			}
			
		}, 3000);
	}

	@Override
	public void OnTranslateResult(TranslateResult result) {
		if (null == result || TextUtils.isEmpty(result.mDestText)) {
			return;
		}
		MobclickAgent.onEvent(getApplicationContext(), "translated");
		int duration;
		if (AppSettings.getInstance().mLongerResult) {
			duration = Toast.LENGTH_LONG;
		} else {
			duration = Toast.LENGTH_SHORT;
		}
		AppLog.i(LOGTAG, "OnTranslateResult dest=" + result.mDestText + ";duration=" + duration);
		Toast t = Toast.makeText(getApplicationContext(), result.mDestText, duration);
		t.setGravity(Gravity.CENTER_VERTICAL, 0, -20);
		t.show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mClipManager.removePrimaryClipChangedListener(this);
		mTranslator.unregisterOnTranslateResultListener(this);
		AppLog.i(LOGTAG, "onDestory");
	}
	
	

}
