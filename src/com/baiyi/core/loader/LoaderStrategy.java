package com.baiyi.core.loader;

/**
 * 
 * @author tangkun
 *
 */
public interface LoaderStrategy {
	public LoaderResult startLoader(Loader loader);
	public void clear();
	public void destroy();
	public void stopLoader(Object tag);
	public boolean isExists(Object tag);
	public boolean isDestroy();
}
