/**
 * 
 */
package com.example.core.test;
/**
 * 
 */

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;

import com.example.core.R;
import com.example.core.test.listview.PullToRefreshListView;

/**
 * ª¨∂ØÕ£÷πº”‘ÿ
 * @author tangkun
 * 
 */
public abstract class ScrollAdapter<T> extends BaseAdapter {

	private PullToRefreshListView mListView = null;
	private List<T> data = new ArrayList<T>();
	private boolean isLoading = true;

	public ScrollAdapter(PullToRefreshListView mListView) {
		this.mListView = mListView;
		setListViewScroll();
	}

	public void setListViewScroll() {
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				// scroll stop
				case OnScrollListener.SCROLL_STATE_IDLE:
					setIsImgLoading(true);
					displayScrollStop();
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					// Ω˚÷πº”‘ÿÕº∆¨
					setIsImgLoading(false);
					break;
				case OnScrollListener.SCROLL_STATE_FLING:
					// Ω˚÷πº”‘ÿÕº∆¨
					setIsImgLoading(false);
					break;
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
	}

	public void setIsImgLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}
	
	public PullToRefreshListView getListView()
	{
		return mListView;
	}
	
	public List<T> getData()
	{
		return data;
	}
	public void addDataHead(List<T> list) {
		this.data.addAll(0, list);
		notifyDataSetChanged();
	}
	
	public void addData(List<T> data)
	{
		this.data.addAll(data);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GdapterTypeRender typeRender;
		if (null == convertView) {
			typeRender = getAdapterTypeRender(position);
			convertView = typeRender.getConvertView();
			convertView.setTag(R.id.id_adapter_item_type_render, typeRender);
			typeRender.setEvents();
		} else {
			typeRender = (GdapterTypeRender) convertView.getTag(R.id.id_adapter_item_type_render);
		}
		convertView.setTag(R.id.id_adapter_item_position, position);

		if (null != typeRender) {
			typeRender.setDatas(position, isLoading);
		}
		return convertView;
	}
	
	public abstract GdapterTypeRender getAdapterTypeRender(int position);
	public abstract void displayScrollStop();

}

