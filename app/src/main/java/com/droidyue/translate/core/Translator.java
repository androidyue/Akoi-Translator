package com.droidyue.translate.core;

import java.lang.ref.SoftReference;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.os.Handler;
import android.os.Message;

import com.droidyue.translate.core.TranslateModels.OnTranslatedListener;
import com.droidyue.translate.core.TranslateModels.TranslateRequest;
import com.droidyue.translate.core.TranslateModels.TranslateResult;

public abstract class Translator {
	private static final int MSG_ON_TRANSLATE_RESULT = 0;
	private final Object mWeakHashMapContent = new Object();
	private  final WeakHashMap<OnTranslatedListener,Object> mListener = new WeakHashMap<OnTranslatedListener,Object>();
	private final ConcurrentHashMap<TranslateRequest, SoftReference<TranslateResult>> mCachedDict 
					= new ConcurrentHashMap<TranslateRequest, SoftReference<TranslateResult>>();
	
	
	private  Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (shouldHandleMessage(msg)) {
				return;
			}
			
			if (MSG_ON_TRANSLATE_RESULT == msg.what) {
				for (OnTranslatedListener listener : mListener.keySet()) {
					if (null != listener) {
						listener.OnTranslateResult((TranslateResult)msg.obj);
					}
				}
			}
		}
		
	};
	
	
	/**
	 * This method does not run on UI Thread
	 */
	protected abstract TranslateResult doTranslateBackground(final TranslateRequest request);
	public abstract boolean isTargetLanguageSupport(String languageCode);
	
	protected boolean shouldHandleMessage(final Message msg) {
		return false;
	}
	
	public final Handler getHandler() {
		return mHandler;
	}
	
	public final void startTranslate(final TranslateRequest request) {
		new Thread() {
			@Override
			public void run() {
				TranslateResult result = getResultFromCache(request);
				if (null == result) {
					result = doTranslateBackground(request);
				}
				mHandler.sendMessage(mHandler.obtainMessage(MSG_ON_TRANSLATE_RESULT, result));
				
			}
		}.start();
	}
	
	public final void registerOnTranslateResultListener(OnTranslatedListener listener) {
		mListener.put(listener, mWeakHashMapContent);
	}
	
	public final void unregisterOnTranslateResultListener(OnTranslatedListener listener) {
		mListener.remove(mListener);
	}
	
	private final TranslateResult getResultFromCache(final TranslateRequest request) { 
		TranslateResult result = null;
		SoftReference<TranslateResult> ref = mCachedDict.get(request);
		if (null != ref) {
			result = ref.get();
		}
		return result;
	}
	
}
