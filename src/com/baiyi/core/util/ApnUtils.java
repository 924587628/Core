package com.baiyi.core.util;

import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author tangkun
 *
 */
public class ApnUtils {
	public static String getSystemProperties(String prop) {
		String output = "";
		try {
			Class<?> sp = Class.forName("android.os.SystemProperties");
			Method get = sp.getMethod("get", String.class);
			output = (String) get.invoke(null, prop);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	
	public static boolean isCMWAP(Context context){  
		String currentAPN = ""; 
		ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
		NetworkInfo info = conManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
		if(info==null){
			return false;///TODO��modified by lirui 2012-01-29 based xoom error null pointer
		}
		currentAPN = info.getExtraInfo(); 
		if(currentAPN == null || currentAPN == ""){  
			return false;  
		}else{  
			if(currentAPN.contains("cmwap") || currentAPN.equals("uniwap") || currentAPN.equals("3gwap")){  
				return true;  
			}else{  
				return false;  
			} 
		}
	 }
	
	public static boolean isuniWAP(Context context){  
		String currentAPN = ""; 
		ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
		NetworkInfo info = conManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
		if(info==null){
			return false;///TODO��modified by lirui 2012-01-29 based xoom error null pointer
		}
		currentAPN = info.getExtraInfo(); 
		if(currentAPN == null || currentAPN == ""){  
			return false;  
		}else{  
			if(currentAPN.equals("uniwap")){  
				return true;  
			}else{  
				return false;  
			} 
		}
	 }
	
	public static boolean isctWAP(Context context){  
		String currentAPN = ""; 
		ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
		NetworkInfo info = conManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
		if(info==null){
			return false;///TODO��modified by lirui 2012-01-29 based xoom error null pointer
		}
		currentAPN = info.getExtraInfo(); 
		if(currentAPN == null || currentAPN == ""){  
			return false;  
		}else{  
			if(currentAPN.equals("ctwap")){  
				return true;  
			}else{  
				return false;  
			} 
		}
	 }
	/**
	 * �Ƿ�Ophoneƽ̨
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isOPhone(Context c) {
		String op = getSystemProperties("apps.setting.platformversion");
		if (op == null) {
			return false;
		}
		op = op.toLowerCase();
		if (op.indexOf("ophone") > -1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ��ȡ�ֻ�UA
	 * 
	 * @param c
	 * @return
	 */
	public static String get_phone_ua() {
		StringBuffer sb = new StringBuffer();
		// sb.append(getSystemProperties("ro.product.manufacturer")); //����
		// sb.append("|");
		sb.append(getSystemProperties("ro.product.manufacturer"));
		sb.append(":");
		sb.append(getSystemProperties("ro.product.model")); // ����
		// sb.append("|");
		// sb.append(getSystemProperties("ro.build.version.release")); //android
		// �汾
		// sb.append("|");
		// sb.append(getSystemProperties("ro.build.display.id")); //rom�汾
		// sb.append("|");
		// sb.append(getSystemProperties("ro.build.version.sdk")); //sdk level
		return sb.toString();
	}

	public static String get_rom() {
		StringBuffer sb = new StringBuffer();
		sb.append(getSystemProperties("ro.build.display.id")); // rom�汾
		return sb.toString();
	}

}


