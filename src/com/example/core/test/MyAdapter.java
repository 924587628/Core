/**
 * 
 */
package com.example.core.test;

import android.content.Context;
import android.view.View;

import com.example.core.R;
import com.example.core.test.listview.PullToRefreshListView;

/**
 * @author tangkun
 * 
 */
public class MyAdapter extends ScrollAdapter<String> {
	
	private Context context = null;

	/**
	 * @param mListView
	 */
	public MyAdapter(PullToRefreshListView mListView, Context context) {
		super(mListView);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public GdapterTypeRender getAdapterTypeRender(int position) {
		// TODO Auto-generated method stub
		GdapterTypeRender rander = new TestRender(context, this);
		return rander;
	}

	@Override
	public void displayScrollStop() {
		setIsImgLoading(true);

		int childCount = 0;
    	if(getListView().getRefreshableView().getFooterViewsCount()!=0){
    		childCount--;
    	}

		int headCount = getListView().getRefreshableView().getHeaderViewsCount();
		int start = getListView().getRefreshableView().getFirstVisiblePosition(); 
		int end = getListView().getRefreshableView().getLastVisiblePosition();
		for (int i = start, j = end; i <= j; i++) 
		{
			int position = i + headCount + childCount;
			String data = (String)getListView().getRefreshableView().getItemAtPosition(position);
			if(data == null)
			{
				continue;
			}
			View view = getListView().getRefreshableView().getChildAt(i - start + headCount + childCount);
			GdapterTypeRender render = (GdapterTypeRender)view.getTag(R.id.id_adapter_item_type_render);
			displayImg(render, data);
		}
	}

	private void displayImg(GdapterTypeRender render, String imgPath) {
		if(render instanceof TestRender)
		{
			((TestRender) render).displayImg(imgPath);
		}
	}

}
