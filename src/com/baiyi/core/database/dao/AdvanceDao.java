package com.baiyi.core.database.dao;

import java.util.List;

import com.baiyi.core.database.AbstractBaseModel;

public interface AdvanceDao<T extends AbstractBaseModel> {
	public void delete(String whereCause, String[] whereArgs);
	
	public void update(String updateString, String sqlString,String[] sqlArgs);

	public List<T> getList(String selection, String[] selectionArgs);

	public List<T> getList(String selection, String[] selectionArgs,
			String orderBy);

	public List<T> getList(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy);

	public long getCount(String selection, String[] selectionArgs);

	public long getCount(String selection, String[] selectionArgs,
			String groupBy, String having);

}
