/**
 * 
 */
package com.example.core.test.task;

import com.baiyi.core.loader.Loader;
import com.baiyi.core.loader.LoaderStrategy;

/**
 * @author tangkun
 *
 */
public class Task {
	private String tag;
	private Loader imgLoader;
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public Loader getImgLoader() {
		return imgLoader;
	}
	public void setImgLoader(Loader imgLoader) {
		this.imgLoader = imgLoader;
	}

}
