package com.baiyi.core.util;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.baiyi.core.database.manager.SQLDataBaseManager;
import com.baiyi.core.database.manager.SimpleSQLDataBaseManager;

public class ContextUtil {
	public static boolean isSDCardExists() {
		return Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public static int getDensityDpi(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		return dm.densityDpi;
	}

	public void save() {

	}

	public static String getU17MetaData(Context context) {
		try {
			ApplicationInfo appInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			String cid = appInfo.metaData.getString("U17_CID");
			return cid;
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	public static boolean IsHasKey(JSONObject jsObject, String name) {
		return jsObject.has(name) && (!jsObject.isNull(name));
	}

	/**
	 * 网络是否连通
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorking(Context context) {
		boolean result;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo != null && netinfo.isConnected()) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	public static boolean getWifiConnectionStat(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		return wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
	}

	// public static void initApp(Context
	// context,SQLDataBaseListener.UpdateListener listner){
	// SimpleSQLDataBaseManager manager =
	// SimpleSQLDataBaseManager.getInstance();
	// manager.setUpdateListener(listner);
	// PartSQLDataBaseManager partManager =
	// PartSQLDataBaseManager.getInstance();
	// partManager.setUpdateListener(listner);
	// partManager.init(context);
	// manager.init(context);
	// }
	public static LayoutInflater getLayoutInflater(Context context) {
		return (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public static void initCoreORM(Context context,
			SQLDataBaseManager.UpdateListener listner, String dataPath,
			String[] single_db_beans, int[] single_db_version) {
		SimpleSQLDataBaseManager manager = SimpleSQLDataBaseManager
				.getInstance();
		manager.setUpdateListener(listner);
		manager.init(context, dataPath, single_db_beans, single_db_version);
	}

	// public static int getSDCardSpace() {
	// String state = Environment.getExternalStorageState();
	// if (Environment.MEDIA_MOUNTED.equals(state)) {
	// File sdcardDir = Environment.getExternalStorageDirectory();
	// StatFs sf = new StatFs(sdcardDir.getPath());
	// long blockSize = sf.getBlockSize();
	// long availCount = sf.getAvailableBlocks();
	// return (int) (availCount * blockSize);
	// }
	// return 0;
	// }

	/**
	 * 
	 * @return 返回以Byte为单位
	 */
	public static long getSpace() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			String sdcard = Environment.getExternalStorageDirectory().getPath();
			StatFs statFs = new StatFs(sdcard);
			long blockSize = statFs.getBlockSize();
			long blocks = statFs.getAvailableBlocks();
			long availableSpare = blocks * blockSize;
			// Logger.error("剩余空间", "availableSpare = " + availableSpare);
			// if (availableSpare > sizeMb) {
			// ishasSpace = true;
			// }
			return availableSpare;
		}
		return 0;
	}

//	public static String getDataBasePath() {
//		String path = getSDPath() + "/zaibang/database/";
//		return path;
//	}

	public static boolean getSDCard1M() {
		if (getSpace() < 1)
			return false;
		return true;
	}

	/**
	 * SD卡根目录
	 * 
	 * @return
	 */
	public static String getSDPath() {
		if (!isSDCardExists()) {
			return null;
		}
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	public static int dip2px(Context context, float dip) {
		int dpi = ContextUtil.getDensityDpi(context);
		return (int) ((dip / 160.0f) * dpi);
	}

	public static String getStringFromAttrs(Context context,
			TypedArray typeArray, int attr) {
		int id = typeArray.getResourceId(attr, 0);
		String text = null;
		if (id == 0) {
			text = typeArray.getString(attr);
		} else {
			text = context.getResources().getString(id);
		}
		return text;
	}

	public static int getViewHeight(View view) {
		view.measure(0, 0); // 计算子项View 的宽高
		return view.getMeasuredHeight(); // 统计所有子项的总高度
	}

	public static int getViewWidth(View view) {
		view.measure(0, 0);
		return view.getWidth();
	}

	public static int getTextViewHeight(Context context, int width,
			String text, float dipSize) {
		TextView view = new TextView(context);
		view.setWidth(width);
		view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dipSize);
		view.setText(text);
		return getViewHeight(view);
	}

	public static float getTextViewHeight(String text, TextView view) {
		CharSequence oldText = view.getText();
		view.setText(text);
		float height = getViewHeight(view);
		view.setText(oldText);
		return height;
	}

	public static void closeInputWindow(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (activity.getCurrentFocus() != null) {
			inputMethodManager.hideSoftInputFromWindow(activity
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public static boolean isGprs(Context context) {
		ConnectivityManager connectManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		}
		int connectType = networkInfo.getType();
		if (connectType == ConnectivityManager.TYPE_WIFI) {
			return false;
		}
		return true;
	}

	public static String getNetWorkTypeSimpleName(Context context) {
		ConnectivityManager connectManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();
		int connectType = networkInfo.getType();
		if (connectType == ConnectivityManager.TYPE_WIFI) {
			return "WIFI";
		} else {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			int mobileNetType = telephonyManager.getNetworkType();
			String mobileNetTypeName;
			switch (mobileNetType) {
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				mobileNetTypeName = "1X";
				break;
			case TelephonyManager.NETWORK_TYPE_CDMA:
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_EDGE:
				mobileNetTypeName = "EDGE";
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_GPRS:
				mobileNetTypeName = "GPRS";
				break;
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_HSPA:
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_UMTS:
				mobileNetTypeName = "3G";
				break;
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				mobileNetTypeName = "UNKOWN";
				break;
			default:
				mobileNetTypeName = "UNKOWN";
				break;
			}
			return mobileNetTypeName;
		}
	}
}
