/**
 * 
 */
package com.baiyi.core.imgcache;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;

public class ImageCache {

	private static final int DEFAULT_MEM_CACHE_SIZE = 10240; // 10MB
	
	private int memoryCacheSize = DEFAULT_MEM_CACHE_SIZE;
	
	private LruCache<String, BitmapDrawable> memoryCache = null;
	
	private static ImageCache imageCache= null;
	
	private ImageCache(){
		memoryCache = new LruCache<String, BitmapDrawable>(memoryCacheSize){
			
			@Override
			protected void entryRemoved(boolean evicted, String key,
					BitmapDrawable oldValue, BitmapDrawable newValue) {
                if (RecyclingBitmapDrawable.class.isInstance(oldValue)) {
                    // The removed entry is a recycling drawable, so notify it 
                    // that it has been removed from the memory cache
                    ((RecyclingBitmapDrawable) oldValue).setIsCached(false);
                }
			}
			
			@Override
			protected int sizeOf(String key, BitmapDrawable value) {
                final int bitmapSize = getBitmapSize(value) / 1024;
                return bitmapSize == 0 ? 1 : bitmapSize;
			}
		};
	}
	
	
    @TargetApi(12)
    public static int getBitmapSize(BitmapDrawable value) {
        Bitmap bitmap = value.getBitmap();

        if (hasHoneycombMR1()) {
            return bitmap.getByteCount();
        }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
	
	public void setMemCacheSizePercent(float percent){
        if (percent < 0.05f || percent > 0.8f) {
            throw new IllegalArgumentException("setMemCacheSizePercent - percent must be "
                    + "between 0.05 and 0.8 (inclusive)");
        }
        memoryCacheSize = Math.round(percent * Runtime.getRuntime().maxMemory() / 1024);
	}
	
	public static ImageCache getInstance(){
		if(imageCache == null)
		{
			imageCache = new ImageCache();
		}
		return imageCache;
	}
	
	public  static void setImageCacheInstace(){
		if(imageCache==null){
			imageCache = new ImageCache();
		}
	}
	
	/**
	 *  从内存中获取
	 * @param data
	 * @return
	 */
	public BitmapDrawable getMemoryCache(String data){
		if(memoryCache!=null){
			return memoryCache.get(data);
		}
		return null;
	}
	
	public void addBitmapToCache(String data,BitmapDrawable drawable){
		if(memoryCache==null){
			return;
		}
        if (RecyclingBitmapDrawable.class.isInstance(drawable)) {
            // The removed entry is a recycling drawable, so notify it 
            // that it has been added into the memory cache
            ((RecyclingBitmapDrawable) drawable).setIsCached(true);
        }
		memoryCache.put(data, drawable);
	}
	
    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

}

