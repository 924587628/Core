package com.example.core.test;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;

import com.baiyi.core.cache.Cache;
import com.baiyi.core.cache.MD5KeyMaker;
import com.baiyi.core.cache.NetFileCache;
import com.baiyi.core.file.NormalFileIO;
import com.baiyi.core.file.SimpleNormalFileIO;
import com.baiyi.core.loader.AsynLoaderStrategy;
import com.baiyi.core.loader.Loader.LoaderListener;
import com.baiyi.core.loader.LoaderStrategy;
import com.baiyi.core.util.ContextUtil;
import com.baiyi.core.util.DataTypeUtils;


public class ChannelDataUtils {
	private static LoaderStrategy dataStratey = null;
	private static LoaderStrategy imageStrategy = null;
	private static Integer maxSize = null;
	private static Cache imageCoverCache = null;
	
	private static Cache fileCache = null;

	public static Cache getImageCoverCache() {
		if (imageCoverCache == null) {
			String path = ContextUtil.getSDPath() + Config.IMAGE_CACHE_PATH;
			NormalFileIO fileIo = new SimpleNormalFileIO();
			try {
				imageCoverCache = new NetFileCache(path,
						Config.IMAGE_CACHE_COUNT, fileIo, true);
				((NetFileCache) imageCoverCache).setKeyMaker(new MD5KeyMaker());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return imageCoverCache;
	}

	public static Cache getFileCache() {
		if (fileCache == null) {
			String path = ContextUtil.getSDPath() + Config.File_Cache_Path;
			NormalFileIO fileIo = new SimpleNormalFileIO();
			try {
				fileCache = new NetFileCache(path,
						Config.IMAGE_CACHE_COUNT, fileIo, true);
				((NetFileCache) fileCache).setKeyMaker(new MD5KeyMaker());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return fileCache;
	}

	public static int getMaxSize(Context context) {
		if (maxSize == null) {
			ActivityManager activityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			maxSize = activityManager.getMemoryClass();
		}
		return maxSize;
	}
	
	public static LoaderStrategy getDataStratey() {
		if (dataStratey == null) {
			dataStratey = new AsynLoaderStrategy(3, "ds", Thread.NORM_PRIORITY);
		}
		return dataStratey;
	}

	public static LoaderStrategy getImageStrategy(Context context) {
		if (imageStrategy == null) {
			if (getMaxSize(context) == 16) {
				imageStrategy = new AsynLoaderStrategy(1, "is",
						Thread.NORM_PRIORITY);
			} else {
				imageStrategy = new AsynLoaderStrategy(6, "is",
						Thread.NORM_PRIORITY);
			}
		}
		return imageStrategy;
	}
	//已加频道
	private List<ChannelItem> defaultUserChannels = new ArrayList<ChannelItem>();
	//未加频道
	private List<ChannelItem> defaultOtherChannels = new ArrayList<ChannelItem>();
	
	public static final String Key_Channel_Default = "KeyChannelDefault";
	public static final String Key_Channel_Other = "keyChannelOther";
	
	private static ChannelDataUtils instance = null;
	
	private ChannelDataUtils()
	{
		defaultOtherChannels = new ArrayList<ChannelItem>();
		defaultUserChannels = new ArrayList<ChannelItem>();
	}
	
	public static ChannelDataUtils getInstance()
	{
		if(instance == null)
		{
			instance = new ChannelDataUtils();
		}
		return instance;
	}
	
	/**
	 * 获取当前显示频道
	 * @param context
	 * @param callback
	 */
	public void loadChannelList(final Context context, final ChannelResultCallBack callback)
	{
		ChannelLoader loader = new ChannelLoader();
		loader.setAllChannel();
		loader.setLoaderListener(new LoaderListener() {
			
			@Override
			public void onProgress(Object tag, long curByteNum, long totalByteNum) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(Object tag, int responseCode, String errorMessage) {
				// TODO Auto-generated method stub
				getDefaultChannelList(context);
			}
			
			@Override
			public void onCompelete(Object tag, Object result) {
				// TODO Auto-generated method stub
				List<ChannelItem> allList = (List<ChannelItem>)result;
				if(allList == null || allList.size() == 0)
				{
					defaultUserChannels = getDefaultChannelList(context);
					defaultOtherChannels = getOtherChannelList(context);
				}else
				{
					setChannelList(allList);
				}
				callback.onResultCallBack(defaultUserChannels, defaultOtherChannels);
//				defaultUserChannels = (List<ChannelItem>)result;
//				if(Utils.isStringEmpty(defaultUserChannels))
//				{
//					defaultUserChannels = getDefaultChannelList(context);
//				}
//				callback.onResultCallBack(defaultUserChannels);
			}
		});
		getDataStratey().startLoader(loader);
	}
	
	private void setChannelList(List<ChannelItem> allList)
	{
		defaultUserChannels.clear();
		defaultOtherChannels.clear();
		for(ChannelItem item : allList)
		{
			if(item.getSelected() == 0)
			{
				defaultOtherChannels.add(item);
			}else if(item.getSelected() == 1)
			{
				defaultUserChannels.add(item);
			}
		}
	}
	
	/**
	 * 载入本地当前显示频道
	 * @param context
	 * @return
	 */
	private List<ChannelItem> getDefaultChannelList(Context context)
	{
		if(!DataTypeUtils.isEmpty(defaultUserChannels))
		{
			return defaultUserChannels;
		}
		String data = Config.getFromAssets(context, "chanel_default");
		defaultUserChannels = JsonParse.getChannelList(data);
		saveDefaultDataBase();
		return defaultUserChannels;
	}
	
	/**
	 * 载入未显示频道
	 * @param context
	 * @return
	 */
	private List<ChannelItem> getOtherChannelList(Context context)
	{
		if(!DataTypeUtils.isEmpty(defaultOtherChannels))
		{
			return defaultOtherChannels;
		}
		String data = Config.getFromAssets(context, "channel_other");
		defaultOtherChannels = JsonParse.getChannelList(data);
		saveOtherDataBase();
		return defaultOtherChannels;
	}
	
	private List<ChannelItem> addDefaultList = null;
	private List<ChannelItem> addOtherList = null;
	
	/**
	 * 更换数据后 存入数据库
	 * @param callback
	 */
	public void saveChannel(final ChannelSaveCallBack callback, 
			List<ChannelItem> defaultChannelList, List<ChannelItem> otherChannelList)
	{
		addDefaultList = defaultChannelList;
		addOtherList = otherChannelList;
		ChannelLoader loader = new ChannelLoader();
		loader.setDeleteAll();
		loader.setLoaderListener(new LoaderListener() {
			
			@Override
			public void onProgress(Object tag, long curByteNum, long totalByteNum) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(Object tag, int responseCode, String errorMessage) {
				// TODO Auto-generated method stub
				if(callback != null)
				{
					callback.onSaveCallBack(false, errorMessage);
				}
			}
			
			@Override
			public void onCompelete(Object tag, Object result) {
				// TODO Auto-generated method stub
				savaDataBase(callback);
			}
		});
		getDataStratey().startLoader(loader);
	}
	
	/**
	 * 存入数据库
	 * @param callback
	 */
	private void savaDataBase(final ChannelSaveCallBack callback)
	{
		List<ChannelItem> dataList = new ArrayList<ChannelItem>();
		dataList.addAll(addDefaultList);
		dataList.addAll(addOtherList);
		ChannelLoader insertLoader = new ChannelLoader();
		insertLoader.setInsertList(dataList);
		insertLoader.setLoaderListener(new LoaderListener() {
			
			@Override
			public void onProgress(Object tag, long curByteNum, long totalByteNum) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(Object tag, int responseCode, String errorMessage) {
				// TODO Auto-generated method stub
				if(callback != null)
				{
					callback.onSaveCallBack(false, errorMessage);
				}
			}
			
			@Override
			public void onCompelete(Object tag, Object result) {
				
				
				if(callback != null)
				{
					defaultUserChannels.clear();
					defaultOtherChannels.clear();
					callback.onSaveCallBack(true, null);
				}
			}
		});
		getDataStratey().startLoader(insertLoader);
	}
	
	private void saveDefaultDataBase()
	{
		ChannelLoader insertLoader = new ChannelLoader();
		insertLoader.setInsertList(defaultUserChannels);
		insertLoader.setLoaderListener(new LoaderListener() {
			
			@Override
			public void onProgress(Object arg0, long arg1, long arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(Object arg0, int arg1, String arg2) {
				// TODO Auto-generated method stub
				String e = arg2;
				System.out.println(e);
				
			}
			
			@Override
			public void onCompelete(Object arg0, Object arg1) {
				// TODO Auto-generated method stub
				String e = "sdadsadsa";
				System.out.println(e);
				
			}
		});
		getDataStratey().startLoader(insertLoader);
	}
	
	private void saveOtherDataBase()
	{
		ChannelLoader insertLoader = new ChannelLoader();
		insertLoader.setInsertList(defaultOtherChannels);
		getDataStratey().startLoader(insertLoader);
	}
	
	public interface ChannelResultCallBack
	{
		public void onResultCallBack(List<ChannelItem> userChannelList, List<ChannelItem> otherChannelList);
	}
	
	public interface ChannelSaveCallBack
	{
		public void onSaveCallBack(boolean isComplete, String errorMsg);
	}

}
