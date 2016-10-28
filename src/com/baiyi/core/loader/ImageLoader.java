package com.baiyi.core.loader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.baiyi.core.imgcache.ImageCache;
import com.baiyi.core.imgcache.RecyclingBitmapDrawable;
import com.baiyi.core.loader.net.BaseNetLoder;
import com.baiyi.core.util.ContextUtil;

/**
 * 
 * @author tangkun
 * 
 */
public class ImageLoader extends BaseNetLoder {
	
	private ImageView imgView = null;
	private int resId;
	private ImageCache imageCache = null;
	public BitmapFactory.Options options = new BitmapFactory.Options();
	private Context context = null;

	/**
	 * 
	 * @param context
	 * @param img 
	 * @param resDefault 显示默认图片资源
	 */
	public ImageLoader(Context context, ImageView img, int resDefault) {
		super(context);
		init();
		setisCanelLoadData(ContextUtil.isGprs(context));
		if(imageCache == null)
		{
			imageCache = ImageCache.getInstance();
		}
		this.context = context;
		this.imgView = img;
		this.resId = resDefault;
	}

	/**
	 * 
	 * @param context
	 * @param isDisplayImg BYApplication.isWifiPic()
	 */
	public ImageLoader(Context context, boolean isDisplayImg) {
		super(context);
		init();
		if(!isDisplayImg)
		{
			return;
		}
		setisCanelLoadData(ContextUtil.isGprs(context));
	}
	
	private void init()
	{
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;
	}
	
	@Override
	protected LoaderResult onVisitor() {
		// TODO Auto-generated method stub
		BitmapDrawable value = null;
		if(imageCache != null)
		{
			value = imageCache.getMemoryCache(getTag().toString());
		}
		if(value != null)
		{
			Message m = new Message();
			m.obj = value;
			handler.sendMessage(m);
			setResult(null);
			return sendCompleteMsg();
		}else
		{
			if(imgView != null && resId != -1)
			{
				handler.sendEmptyMessage(0);
//				imgView.setImageDrawable(context.getResources().getDrawable(resId));
			}
			return super.onVisitor();
		}
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			if(msg.obj == null)
			{
				imgView.setImageDrawable(context.getResources().getDrawable(resId));
				return;
			}
			BitmapDrawable bd = (BitmapDrawable)msg.obj;
			imgView.setImageBitmap(bd.getBitmap());
		};
	};

	@Override
	protected Object covert(byte[] bytes) {
		if (isCancel()) {
			return null;
		}
		Bitmap bitmap = null;
		bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
		if (isCancel()) {
			bitmap.recycle();
			return null;
		}

		BitmapDrawable drawable = null;
		if(bitmap!=null){
			if (ImageCache.hasHoneycomb()) {
				drawable = new BitmapDrawable(bitmap);
			}else{
				drawable = new RecyclingBitmapDrawable(context.getResources(), bitmap);
			}
			if(imageCache != null){
				if(imageCache.getMemoryCache(getTag().toString()) == null)
				{
					imageCache.addBitmapToCache(getTag().toString(), drawable);
				}
			}
		}
		return drawable;
	}
}
