package com.baiyi.core.loader.cache;

import com.baiyi.core.cache.Cache;
import com.baiyi.core.loader.Loader;

public class SimpleCacheLoader extends BaseCacheLoader implements Loader{

	public SimpleCacheLoader(Cache cache) {
		super(cache);
	}

	@Override
	protected Object onVisitor(String opType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object convert(Object data) {
		// TODO Auto-generated method stub
		return data;
	}

}
