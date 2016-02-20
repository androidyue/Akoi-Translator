package com.droidyue.translate.core;

public class TranslateModels {
	
	public static class TranslateRequest {
		public String mQuery;
		public String mFromLanguage;
		public String mToLanguage;
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((mQuery == null) ? 0 : mQuery.hashCode());
			result = prime * result
					+ ((mToLanguage == null) ? 0 : mToLanguage.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TranslateRequest other = (TranslateRequest) obj;
			if (mQuery == null) {
				if (other.mQuery != null)
					return false;
			} else if (!mQuery.equals(other.mQuery))
				return false;
			if (mToLanguage == null) {
				if (other.mToLanguage != null)
					return false;
			} else if (!mToLanguage.equals(other.mToLanguage))
				return false;
			return true;
		}
		
		
	}
	
	
	public static class TranslateResult {
		public static final int HANDLE_TYPE_CACHE = 0;
		public static final int HANDLE_TYPE_NETWORK = 1;
		public int mErrorCode;
		public String mErrorMessage;
		public String mSourceText;
		public String mDestText;
		public int mHandleType;
	}
	
	
	
	public static interface OnTranslatedListener {
		public void OnTranslateResult(final TranslateResult result);
	}
}


