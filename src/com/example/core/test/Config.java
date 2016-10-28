package com.example.core.test;

import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Display;
import android.view.WindowManager;

import com.baiyi.core.util.DataTypeUtils;

public class Config {

	private Integer screenWidth;
	private Integer screenHeight;

	private static Config instance = null;

	public static final int DB_VERSION = 1;

	private int versionCode;
	private String versionName;
	
	public static final String IMAGE_CACHE_PATH = "/jujiao/files/coverimgs/";
	public static final String File_Cache_Path = "/jujiao/files/file/";
	public static final String CACHE_FILE_CHANNEL_PATH = "/jujiao/files/channel/";
	public static final int IMAGE_CACHE_COUNT = 100;
	public static final int LIST_ITEM_COUNT = 20;
	
	public static final String XmlFileName = "Cms365";

	private Config() {
	}

	public static Config getInstance() {
		if (instance == null)
			instance = new Config();
		return instance;
	}

	public void initScreenParams(Context context) {
		// 初始化屏幕参数
		Display display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
	}

	private void initVersionParams(Context context) {
		// 初始化版本变量
		try {
			PackageInfo pinfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(),
							PackageManager.GET_CONFIGURATIONS);
			versionCode = pinfo.versionCode;
			versionName = pinfo.versionName;
		} catch (NameNotFoundException e) {

		}

	}

	// 版本号
	public int getVersionCode(Context context) {
		if (versionCode == 0) {
			initVersionParams(context);
		}
		return versionCode;
	}

	public String getVersionName(Context context) {
		if (DataTypeUtils.isEmpty(versionName)) {
			initVersionParams(context);
		}
		return versionName;
	}

	public int getScreenWidth(Context context) {
		if (screenWidth == null) {
			initScreenParams(context);
			if (screenWidth == null) {
				return 0;
			}
		}
		return screenWidth;
	}

	public int getScreenHeight(Context context) {
		if (screenHeight == null) {
			initScreenParams(context);
			if (screenHeight == null) {
				return 0;
			}
		}
		return screenHeight;
	}
	
	public int getSideTextSize(Context context)
	{
		float ratioWidth = (float)getScreenWidth(context) / 720;  
		float ratioHeight = (float)getScreenHeight(context) / 1280;  
		          
		float RATIO = Math.min(ratioWidth, ratioHeight);  
		return Math.round(30 * RATIO);
	}

	public static String getFromAssets(Context context, String fileName) {
		String result = "";
		try {
			InputStream in = context.getResources().getAssets().open(fileName);
			// 获取文件的字节数
			int lenght = in.available();
			// 创建byte数组
			byte[] buffer = new byte[lenght];
			// 将文件中的数据读到byte数组中
			in.read(buffer);
			result = EncodingUtils.getString(buffer, "GBK");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
