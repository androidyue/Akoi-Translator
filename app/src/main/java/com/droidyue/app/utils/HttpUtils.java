package com.droidyue.app.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils {
	private static final String LOGTAG = "HttpUtils";

	public static String doGet(String urlString) {
		HttpURLConnection urlConnection = null;
		String result = null;
		try {
			URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			result = inputStream2String(in);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != urlConnection) {
				urlConnection.disconnect();
			}
		}
		return result;
	}
	
	public static String doPost(final String urlString, final String data) {
		URL urlToRequest;
		String result = null;
		HttpURLConnection urlConnection = null;
		try {
			urlToRequest = new URL(urlString);
			urlConnection  = (HttpURLConnection) urlToRequest.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			DataOutputStream dos = new DataOutputStream(urlConnection.getOutputStream());
			dos.write(data.getBytes("UTF-8"));
			dos.flush();
			dos.close();
			result = inputStream2String(urlConnection.getInputStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		AppLog.i(LOGTAG, "doPost result=" + result);
		return result;
	}
	
	public static String inputStream2String(final InputStream inputStream) {
		BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder total = new StringBuilder();
		String line;
		try {
			while ((line = r.readLine()) != null) {
			    total.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return total.toString();
	}
}
