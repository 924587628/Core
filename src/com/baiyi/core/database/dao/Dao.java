package com.baiyi.core.database.dao;

import java.util.List;

import com.baiyi.core.database.AbstractBaseModel;

public interface Dao<T extends AbstractBaseModel> {
	public void delete(String id);

	public void delete(T obj);

	public T get(String words, String id);

	public List<T> getList();

	public long getCount();

	public void insert(T obj);

	public void update(T obj);
	
	public void insert(List<T> obj);

	public void update(List<T> obj);
}
