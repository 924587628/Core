package com.baiyi.core.cache;

public interface Cache {
	public int getFileSize();
	
	public Object get(String key);

	public void put(String key, Object value);

	public void remove(String key);

	public void clear();

	public void update(String key, Object value);

	public boolean isExist(String key);

	public void notifyDataChanged();
}
