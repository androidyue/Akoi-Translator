package com.droidyue.app.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppUtils {
	
	
	private static final String LOGTAG = "AppUtils";

	public static final boolean checkServiceRunning(final Context context, final String serviceName) {
		    final ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		    for (final RunningServiceInfo info : manager.getRunningServices(Integer.MAX_VALUE)) {
		    	if (info.service.getClassName().equals(serviceName) && info.pid > 0) {
		    		return true;
		    	}
		    }
		    return false;
	}
	
	public static final String getTopAppPackageName(final Context context) {
		String packageName = null;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1);
		ComponentName comp = list.get(0).topActivity;
		if (null != comp) {
			packageName = comp.getPackageName();
		}
		return packageName;
	}
	
	
	
	/**
	 * @param charSequence
	 * @return
	 */
	public static final boolean isURI(final String text) {
		boolean isURI = false;
		try {
			new URL(text); 
			isURI = true;
		} catch (MalformedURLException e) {
			/* invalid URL */ 
		}
		return isURI;
	}
	
	/**
	 * @param context
	 * @return
	 */
	public static final boolean isNetworkAvailable(final Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnected();
	}

	public static String getMD5(byte[] source) {
		String s = null;
		char hexDigits[] = {       // 用来将字节转换成 16 进制表示的字符
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',  'e', 'f'};
		try
		{
			java.security.MessageDigest md = java.security.MessageDigest.getInstance( "MD5" );
			md.update( source );
			byte tmp[] = md.digest();          // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2];   // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0;                                // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) {          // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i];                 // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];  // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf];            // 取字节中低 4 位的数字转换
			}
			s = new String(str);                                 // 换后的结果转换为字符串

		}catch( Exception e )
		{
			e.printStackTrace();
		}
		return s;
	}
	
}
