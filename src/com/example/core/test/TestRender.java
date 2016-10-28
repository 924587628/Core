/**
 * 
 */
package com.example.core.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import com.baiyi.core.loader.ImageLoader;
import com.baiyi.core.loader.Loader.LoaderListener;
import com.baiyi.core.loader.net.BaseNetLoder;
import com.baiyi.core.util.ContextUtil;
import com.example.core.R;

/**
 * @author tangkun
 *
 */
public class TestRender implements GdapterTypeRender{
	
	private View convertView = null;
	ImageView img = null;
	
	private Context context = null;
	private MyAdapter adapter = null;
	
	public TestRender(Context context, MyAdapter adapter)
	{
		this.context = context;
		this.adapter = adapter;
		convertView = ContextUtil.getLayoutInflater(context).inflate(R.layout.list_item_img, null);
	}

	@Override
	public View getConvertView() {
		// TODO Auto-generated method stub
		return convertView;
	}

	@Override
	public void setEvents() {
		// TODO Auto-generated method stub
		img = (ImageView) convertView.findViewById(R.id.img);
	}

	@Override
	public void setDatas(int position, boolean isLoading) {
		// TODO Auto-generated method stub
		String data = (String)adapter.getItem(position);
		displayImg(data);
	}

	public void displayImg(String imgPath) {
		ImageLoader loader = new ImageLoader(context, img, R.drawable.ic_launcher);
		loader.setUrl(imgPath);
		loader.setMethod(BaseNetLoder.Method_Get);
		loader.setCache(ChannelDataUtils.getImageCoverCache());
		loader.setLoaderListener(new LoaderListener() {

			@Override
			public void onProgress(Object tag, long curByteNum,
					long totalByteNum) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(Object tag, int responseCode,
					String errorMessage) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCompelete(Object tag, Object result) {
				// TODO Auto-generated method stub
				if (result == null) {
					return;
				}

				BitmapDrawable bm = (BitmapDrawable) result;
				img.setImageDrawable(bm);

			}
		});
		ChannelDataUtils.getImageStrategy(context).startLoader(loader);
	}

}
