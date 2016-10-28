package com.baiyi.core.loader;

/**
 * 
 * @author tangkun
 *
 */
public interface Loader {
	public LoaderResult start();
	public void cancel();
	public void error(String msg);
	public boolean isCancel();
	public void setTag(Object tag);
	public Object getTag();
	public void setLoaderListener(LoaderListener listner);
	public interface LoaderListener{
		public void onCompelete(Object tag,Object result);
		public void onProgress(Object tag,long curByteNum,long totalByteNum);
		public void onError(Object tag,int responseCode,String errorMessage);
	}
}
