package com.example.core.test;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.baiyi.core.loader.JsonLoader;
import com.baiyi.core.loader.Loader.LoaderListener;
import com.baiyi.core.loader.net.BaseNetLoder;
import com.baiyi.core.util.ContextUtil;
import com.example.core.R;
import com.example.core.test.ChannelDataUtils.ChannelResultCallBack;
import com.example.core.test.listview.PullToRefreshBase;
import com.example.core.test.listview.PullToRefreshListView;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getName();

	private Button btnDelete;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);

//		setListView();
//		loadData();
		JsonLoader loader = new JsonLoader(this);
		loader.setUrl("http://121.201.58.72:8000/api/my/notinterest/20299/");
		loader.setMethod(BaseNetLoder.Method_Post);
		loader.setType(BaseNetLoder.POST_FORM);
		loader.setContentTextList(getNoTinterestPostData("56947f015ff11b2210269a99"));
		loader.setLoaderListener(new LoaderListener() {
			
			@Override
			public void onProgress(Object tag, long curByteNum, long totalByteNum) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(Object tag, int responseCode, String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCompelete(Object tag, Object result) {
				// TODO Auto-generated method stub
				JSONArray a = (JSONArray)result;
				System.out.println(a.toString());
			}
		});
		ChannelDataUtils.getDataStratey().startLoader(loader);
	}

	private ArrayList<String[]> getNoTinterestPostData(String articleId) {
		ArrayList<String[]> dataList = new ArrayList<String[]>();
		dataList.add(new String[] { "article_list", articleId });
		return dataList;
		// ArrayList<String[]> textList = new ArrayList<String[]>();
		// textList.add(new String[] { "article_list", articleId });
		// return textList;
	}
	
	private void loadChannel()
	{
		JsonLoader loader = new JsonLoader(this);
		loader.setUrl("http://121.201.58.72:8000/api/push/channel/article");
		loader.setPostData(Config.getFromAssets(this, "test"));
		loader.setMethod(BaseNetLoder.Method_Post);
		loader.setLoaderListener(new LoaderListener() {
			
			@Override
			public void onProgress(Object arg0, long arg1, long arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(Object arg0, int arg1, String arg2) {
				// TODO Auto-generated method stub
				String error = arg2;
				System.out.println(error);
			}
			
			@Override
			public void onCompelete(Object arg0, Object arg1) {
				// TODO Auto-generated method stub
				String msg = arg1.toString();
				System.out.println(msg);
				
			}
		});
		ChannelDataUtils.getDataStratey().startLoader(loader);
	}
	
//	private void loadData()
//	{
//		JsonLoader loader = new JsonLoader(this);
//		loader.setUrl("http://121.201.58.72:8000/api/push/module/article");
//		loader.setPostData(Config.getFromAssets(this, "test"));
//		loader.setMethod(BaseNetLoder.Method_Post);
//		loader.setLoaderListener(new LoaderListener() {
//			
//			@Override
//			public void onProgress(Object arg0, long arg1, long arg2) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onError(Object arg0, int arg1, String arg2) {
//				// TODO Auto-generated method stub
//				String error = arg2;
//				System.out.println(error);
//			}
//			
//			@Override
//			public void onCompelete(Object arg0, Object arg1) {
//				// TODO Auto-generated method stub
//				String msg = arg1.toString();
//				System.out.println(msg);
//				
//			}
//		});
//		ChannelDataUtils.getDataStratey().startLoader(loader);
//	}
	
	private String[] imgs = {"http://7xlycg.com1.z0.glb.clouddn.com/media/picture/interest/20151119/f8255ebe8e6d11e58985b8975aa53adf/detail_0?imageView/2/w/330"
			,"http://7xlycg.com1.z0.glb.clouddn.com/media/picture/article/20151122/2479f55c90c511e5afa014dda9276cfe/detail_0?imageView/2/w/330"
			,"http://7xlycg.com1.z0.glb.clouddn.com/media/picture/article/20151122/823a448a90ca11e5ae8214dda9276cfe/detail_0?imageView/2/w/330"
			,"http://7xlycg.com1.z0.glb.clouddn.com/media/picture/article/20151122/bd5b927c90e211e586d314dda9276cfe/detail_0?imageView/2/w/330"
			,"http://7xlycg.com1.z0.glb.clouddn.com/media/picture/article/20151122/888a620490d211e5bc2f14dda9276cfe/detail_0?imageView/2/w/330"
			,"http://7xlycg.com1.z0.glb.clouddn.com/media/picture/article/20151122/2479f55c90c511e5afa014dda9276cfe/detail_0?imageView/2/w/330"
			,"http://7xlycg.com1.z0.glb.clouddn.com/media/picture/article/20151122/823a448a90ca11e5ae8214dda9276cfe/detail_0?imageView/2/w/330"
			,"http://7xlycg.com1.z0.glb.clouddn.com/media/picture/article/20151122/bd5b927c90e211e586d314dda9276cfe/detail_0?imageView/2/w/330"
			,"http://7xlycg.com1.z0.glb.clouddn.com/media/picture/article/20151122/888a620490d211e5bc2f14dda9276cfe/detail_0?imageView/2/w/330"
			,"http://7xlycg.com1.z0.glb.clouddn.com/media/picture/article/20151122/2479f55c90c511e5afa014dda9276cfe/detail_0?imageView/2/w/330"
			,"http://7xlycg.com1.z0.glb.clouddn.com/media/picture/article/20151122/823a448a90ca11e5ae8214dda9276cfe/detail_0?imageView/2/w/330"
			,"http://7xlycg.com1.z0.glb.clouddn.com/media/picture/article/20151122/bd5b927c90e211e586d314dda9276cfe/detail_0?imageView/2/w/330"
			,"http://7xlycg.com1.z0.glb.clouddn.com/media/picture/article/20151122/888a620490d211e5bc2f14dda9276cfe/detail_0?imageView/2/w/330"
			};

	private PullToRefreshListView listView = null;

	private void setListView() {
		listView = (PullToRefreshListView) findViewById(R.id.listview);

		listView.setPullLoadEnabled(false); // 上拉加载，屏蔽
		listView.setScrollLoadEnabled(true); // 设置滚动加载可用
		// 设置上拉下拉的监听事件
		listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// //下拉刷新，重新获取数据，填充listview
				// getdata(url, true);//刷新数据
				// String stringDate = CommonUtil.getStringDate();//
				// 下拉刷新时获取当前的刷新时间
				listView.setLastUpdatedLabel("2015-11-27");// 将时间添加到刷新的表头
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
	        	int first = refreshView.getRefreshableView().getFirstVisiblePosition();
	        	//int end = view.getLastVisiblePosition();
	        	int childCount = refreshView.getRefreshableView().getChildCount();
	        	
	        	if(refreshView.getRefreshableView().getFooterViewsCount()!=0){
	        		childCount--;
	        	}
				// xinWenURL.setStratPage(xinWenURL.getStratPage() + 20);
				// //默认选择头条栏目
				// String urlfen =geturl();//分页url;
				// LogUtils.e("toutiao", "url:" + urlfen);
				// // 上拉加载
				// getdata(urlfen, false);//加载数据
			}
		});
		// 点击listview调用的方法
		listView.getRefreshableView().setOnItemClickListener(
				new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						// frament2activity(position);//跳转轮播详细页面
					}
				});
		for (int i = 0; i < imgs.length; i++) {
			data.add(imgs[i]);
		}
		
		listView.setHasMoreData(true);
		MyAdapter adapter = new MyAdapter(listView, this);
		adapter.addData(data);
		listView.getRefreshableView().setAdapter(adapter);
	}

	private List<String> data = new ArrayList<String>();

	private void init() {

		btnDelete = (Button) findViewById(R.id.btn_delete);
		btnDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				JsonLoader loader = new JsonLoader(MainActivity.this);
				loader.setUrl("http://121.201.58.72:8000/api/news/index/?user=113&page=1");
				loader.setMethod(BaseNetLoder.Method_Get);
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
						String data = result.toString();
						System.out.println(data);

					}
				});
				ChannelDataUtils.getDataStratey().startLoader(loader);
			}
		});

		String[] beans = this.getResources().getStringArray(
				R.array.single_db_beans);
		int[] versions = this.getResources().getIntArray(
				R.array.single_db_version);
		ContextUtil.initCoreORM(this, null, getDataBasePath(), beans, versions);

		testSave();
	}

	private ArrayList<String[]> getDelPostData(String collectId) {
		ArrayList<String[]> textList = new ArrayList<String[]>();
		textList.add(new String[] { "pk", collectId });
		return textList;
	}

	private void loadNetCollectCanel() {
		JsonLoader loader = new JsonLoader(this);
		loader.setType(BaseNetLoder.POST_FORM);
		loader.setContentTextList(getDelPostData("561b61507854917b42e2a8c5"));

		loader.setUrl("http://123.56.153.180:8000/api/my/v2/collection/");
		loader.setMethod(BaseNetLoder.Method_Post);
		loader.setLoaderListener(new LoaderListener() {

			public void onProgress(Object arg0, long arg1, long arg2) {
			}

			public void onError(Object arg0, int arg1, String arg2) {

			}

			public void onCompelete(Object arg0, Object arg1) {
				JSONArray array = (JSONArray) arg1;
			}
		});
		ChannelDataUtils.getDataStratey().startLoader(loader);
	}

	public static String getDataBasePath() {
		String path = ContextUtil.getSDPath() + "/jj3651/database/";
		return path;
	}

	private void testSave() {

		ChannelDataUtils.getInstance().loadChannelList(this,
				new ChannelResultCallBack() {

					@Override
					public void onResultCallBack(
							List<ChannelItem> userChannelList,
							List<ChannelItem> otherChannelList) {
						// TODO Auto-generated method stub
					}
				});
	}
}
