package com.droidyue.translate.core;

import com.droidyue.translate.core.TranslateModels.TranslateRequest;
import com.droidyue.translate.core.TranslateModels.TranslateResult;

public class BingTranslator extends Translator{
	
	@Override
	protected TranslateResult doTranslateBackground(TranslateRequest request) {
		return null;
	}


	@Override
	public boolean isTargetLanguageSupport(String languageCode) {
		return false;
	}

}
