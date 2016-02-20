package com.droidyue.translate.core;

import com.droidyue.app.utils.AppLog;
import com.droidyue.app.utils.AppUtils;
import com.droidyue.app.utils.HttpUtils;
import com.droidyue.translate.AppSettings;
import com.droidyue.translate.R;
import com.droidyue.translate.core.TranslateModels.TranslateRequest;
import com.droidyue.translate.core.TranslateModels.TranslateResult;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;

import java.util.HashMap;

public class BaiduTranslator extends Translator{
	private static final String API_KEY = "20160220000012732";
	private static final String API_SECRET = "4E8sObv8T8uvmdi9HG11";
	private static final String SALT = "baidu_salty";

	private static final String REQUEST_URL =
			"http://api.fanyi.baidu.com/api/trans/vip/translate?q=%query%&from=%from%&to=%to%&appid=%api_key%&salt=%salt%&sign=%sign%";

	private static final String LOGTAG = "BaiduTranslator";
	
	private final HashMap<String, String> mSpecialLanguageMap = new HashMap<String, String>();
	/**
	 * 
		中文	zh	英语	en
		日语	jp	韩语	kor
		西班牙语	spa	法语	fra
		泰语	th	阿拉伯语	ara
		俄罗斯语	ru	葡萄牙语	pt
		粤语	yue	文言文	wyw
		白话文	zh	自动检测	auto
		德语	de	意大利语	it
	 */
	{
		putLanguageMap(R.string.pref_value_language_korean, "kor");
		putLanguageMap(R.string.pref_value_language_spanish, "spa");
		putLanguageMap(R.string.pref_value_language_french, "fra");
		putLanguageMap(R.string.pref_value_language_arabic, "ara");
		
	}
	
	private final TranslateResult mErrorResult = new TranslateResult();
	{
		mErrorResult.mErrorCode = -1;
	}

	private void putLanguageMap(int textResId, String requestLanguage) {
		mSpecialLanguageMap.put(AppSettings.getInstance().getResString(textResId), requestLanguage);
	}
	
	
	@Override
	protected TranslateResult doTranslateBackground(final TranslateRequest request) {
		String query = Uri.encode(request.mQuery);
		String fromLan = obtainFromLanguage(request);
		String toLan = obtainToLanguage(request);
		String encryptedSign = API_KEY + request.mQuery + SALT + API_SECRET;
		String sign = AppUtils.getMD5(encryptedSign.getBytes());
		String url = REQUEST_URL.replace("%api_key%", API_KEY).replace("%query%", query)
				.replace("%from%", fromLan).replace("%to%", toLan)
				.replace("%salt%", SALT).replace("%sign%", sign);
		String rawResponse = HttpUtils.doGet(url);
		AppLog.i(LOGTAG, "rawResponse=" + rawResponse + "; url=" + url);
		return parseTranslateResult(rawResponse); 
	}

	private TranslateResult parseTranslateResult(String rawResponse) {
		TranslateResult result;
		if (null != rawResponse) {
			try {
				JSONObject jsonObj = new JSONObject(rawResponse);
				TranslateResult temp = new TranslateResult();
				temp.mErrorCode = 0;
				temp.mDestText = jsonObj.getJSONArray("trans_result").getJSONObject(0).getString("dst");
				result = temp;
			} catch (JSONException e) {
				e.printStackTrace();
				result = mErrorResult;
			}
		} else {
			result = mErrorResult;
		}
		return result;
	}
	
	
	
	@Override
	public boolean isTargetLanguageSupport(String languageCode) {
		return false;
	}
	
	private String obtainFromLanguage(final TranslateRequest request) {
		return "auto";
	}
	
	private String obtainToLanguage(final TranslateRequest request) {
		String destLanguage = mSpecialLanguageMap.get(request.mToLanguage);
		if (null == destLanguage) {
			destLanguage = request.mToLanguage;
		}
		return destLanguage;
	}

}
