package com.baiyi.core.database.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.Cursor;

import com.baiyi.core.database.AbstractBaseModel;
import com.baiyi.core.database.bean.Bean;
import com.baiyi.core.database.manager.SimpleSQLDataBaseManager;
import com.baiyi.core.util.DataTypeUtils;

public abstract class AbstractNormalDao<T extends AbstractBaseModel> implements
		Dao<T>, AdvanceDao<T> {
//	private static final String TAG = AbstractNormalDao.class.getSimpleName();
	protected DaoUtil<T> util = null;
	private boolean isInit = false;

	public AbstractNormalDao() {
		init();
	}

	private boolean init() {
		SimpleSQLDataBaseManager manager = SimpleSQLDataBaseManager
				.getInstance();
		if (!manager.isInited()) {
			return false;
		}
		String name = getEntityClass().getName();
		Bean bean = manager.getBeanConfig(name);
		if (bean == null) {
			return false;
		}
		bean.setName(name);
		util = new DaoUtil<T>(manager, bean);
		isInit = true;
		return true;
	}

	@Override
	public void delete(String whereCause, String[] whereArgs) {
		if (!isInit) {
			if (!init()) {
				return;
			}
		}
		String sqlString = "DELETE FROM " + util.getBeanSimpleName() + " "
				+ whereCause;
		util.execWriteSql(sqlString, whereArgs);
	}

	@Override
	public void update(String updateString, String whereCause, String[] whereArgs)
	{
		if (!isInit) {
			if (!init()) {
				return;
			}
		}
		String sqlString = "UPDATE " + util.getBeanSimpleName() + " SET "
				+ updateString + " " + whereCause;
		util.execWriteSql(sqlString, whereArgs);
	}

	@Override
	public List<T> getList(String selection, String[] selectionArgs) {
		if (!isInit) {
			if (!init()) {
				return null;
			}
		}
		return getList(selection, selectionArgs, null, null, null);
	}

	@Override
	public List<T> getList(String selection, String[] selectionArgs,
			String orderBy) {
		if (!isInit) {
			if (!init()) {
				return null;
			}
		}
		return getList(selection, selectionArgs, null, null, orderBy);
	}

	@Override
	public List<T> getList(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		if (!isInit) {
			if (!init()) {
				return null;
			}
		}
		Cursor cursor = getCursor(selection, selectionArgs, groupBy, having,
				orderBy);
		List<T> list = util.cursor2List(cursor);
		return list;
	}

	private Cursor getCursor(String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		String sqlString = "SELECT * FROM " + util.getBeanSimpleName();
		if (!DataTypeUtils.isEmpty(selection)) {
			sqlString += " " + selection;
		}
		if (!DataTypeUtils.isEmpty(groupBy)) {
			sqlString += " " + groupBy;
			if (!DataTypeUtils.isEmpty(having)) {
				sqlString += having;
			}
		}
		if (!DataTypeUtils.isEmpty(orderBy)) {
			sqlString += " " + orderBy;
		}
		Cursor cursor = null;
		cursor = util.execReadSql(sqlString, selectionArgs);
		return cursor;
	}

	@Override
	public long getCount(String selection, String[] selectionArgs){
		if (!isInit) {
			if (!init()) {
				return 0;
			}
		}
		return getCount(selection, selectionArgs, null, null);
	}

	@Override
	public long getCount(String selection, String[] selectionArgs,
			String groupBy, String having) {
		if (!isInit) {
			if (!init()) {
				return 0;
			}
		}
		String sqlString = "SELECT COUNT(*) AS NUM FROM "
				+ util.getBeanSimpleName();
		if (!DataTypeUtils.isEmpty(selection)) {
			sqlString += " " + selection;
		}
		if (!DataTypeUtils.isEmpty(groupBy)) {
			sqlString += " " + groupBy;
			if (!DataTypeUtils.isEmpty(having)) {
				sqlString += having;
			}
		}
		Cursor cursor = null;
		cursor = util.execReadSql(sqlString, selectionArgs);
		cursor.moveToFirst();
		long data = cursor.getLong(0);
		cursor.close();
		return data;
	}

	public int getMaxId() {
		if (!isInit) {
			if (!init()) {
				return 0;
			}
		}
		String sqlString = "select max(id) from " + util.getBeanSimpleName();
		Cursor cursor = null;
		cursor = util.execReadSql(sqlString, null);
		cursor.moveToFirst();
		long data = cursor.getLong(0);
		cursor.close();
		return (int) data;
	}

	@Override
	public void delete(String id) {
		if (!isInit) {
			if (!init()) {
				return;
			}
		}
		if (DataTypeUtils.isEmpty(id))
			return;
		String sqlString = "DELETE FROM " + util.getBeanSimpleName()
				+ " WHERE id=?";
		util.execWriteSql(sqlString, new String[] { id });
	}

	public void deleteList(List<T> obj) {
		if (!isInit) {
			if (!init()) {
				return;
			}
		}
		util.getWriteDataBase().beginTransaction();
		for (T item : obj) {
			delete(item);
		}
		util.getWriteDataBase().setTransactionSuccessful();
		util.getWriteDataBase().endTransaction();
	}

	public void deleteAll() {
		if (!isInit) {
			if (!init()) {
				return;
			}
		}
		String sqlString = "DELETE FROM " + util.getBeanSimpleName();
		util.execWriteSql(sqlString, null);
	}

	@Override
	public void delete(T obj) {
		if (!isInit) {
			if (!init()) {
				return;
			}
		}
		if (obj == null)
			return;
		if (DataTypeUtils.isEmpty(obj.getId()))
			return;
		delete(obj.getId());
	}

	@Override
	public T get(String words, String id) {
		if (!isInit) {
			if (!init()) {
				return null;
			}
		}
		if (DataTypeUtils.isEmpty(id))
			return null;
		String sqlString = "SELECT * FROM " + util.getBeanSimpleName()
				+ " WHERE " + words + "=?";
		Cursor cursor = null;
		cursor = util.execReadSql(sqlString, new String[] { id });
		List<T> list = util.cursor2List(cursor);
		if (DataTypeUtils.isEmpty(list))
			return null;
		return list.get(0);
	}

	@Override
	public List<T> getList(){
		if (!isInit) {
			if (!init()) {
				return null;
			}
		}
		return getList(null, null, null, null, null);
	}

	@Override
	public long getCount() {
		if (!isInit) {
			if (!init()) {
				return 0;
			}
		}
		return getCount(null, null, null, null);
	}

	@Override
	public void insert(T obj) {
		if (!isInit) {
			if (!init()) {
				return;
			}
		}
		if (!isInit) {
			if (!init()) {
				return;
			}
		}
		saveOrUpdate(true, obj);
	}

	@Override
	public void update(T obj) {
		if (!isInit) {
			if (!init()) {
				return;
			}
		}
		saveOrUpdate(false, obj);
	}

	@Override
	public void insert(List<T> obj) {
		if (!isInit) {
			if (!init()) {
				return;
			}
		}
		util.getWriteDataBase().beginTransaction();
		for (T item : obj) {
			insert(item);
		}
		util.getWriteDataBase().setTransactionSuccessful();
		util.getWriteDataBase().endTransaction();
	}

	@Override
	public void update(List<T> obj) {
		if (!isInit) {
			if (!init()) {
				return;
			}
		}
		util.getWriteDataBase().beginTransaction();
		for (T item : obj) {
			update(item);
		}
		util.getWriteDataBase().setTransactionSuccessful();
		util.getWriteDataBase().endTransaction();

	}

	private void saveOrUpdate(boolean isInsert, T obj){
		String sqlString;
		List<String> objList = new LinkedList<String>();
		String tmp = "";
		String tmp2 = "";
		int i = 0;
		JSONObject json = obj.toJSON();
		Iterator<?> it = json.keys();
		while (it.hasNext()) {
			String field = (String) it.next();
			if (!isInsert && field.equals("id"))
				continue;
			if (i++ != 0) {
				tmp += ",";
				if (isInsert)
					tmp2 += ",";
			}
			tmp += isInsert ? field : field + "=?";
			if (isInsert)
				tmp2 += "?";
			try {
				Object value = json.get(field);
				if (value instanceof Date) {
					value = DataTypeUtils.date2String((Date) value);
				}
				objList.add(value.toString());
			} catch (JSONException e) {
				// e.printStackTrace();
//				TLog.e(TAG, e.getMessage());
			}
		}
		if (isInsert) {
			sqlString = "INSERT INTO " + obj.getClass().getSimpleName() + "("
					+ tmp + ") VALUES(" + tmp2 + ")";
		} else {
			objList.add(obj.getId());
			sqlString = "UPDATE " + obj.getClass().getSimpleName() + " SET "
					+ tmp + " WHERE id=?";
		}
		String[] stringList = new String[objList.size()];

		stringList = objList.toArray(stringList);
		util.execWriteSql(sqlString, stringList);
	}

	@SuppressWarnings("unchecked")
	protected Class<? extends AbstractBaseModel> getEntityClass() {
		return (Class<? extends AbstractBaseModel>) getGenericClass(
				this.getClass(), 0);
	}

	protected Class<?> getGenericClass(Class<?> clazz, int index) {
		Type genType = clazz.getGenericSuperclass();
		if (genType instanceof ParameterizedType) {
			Type[] params = ((ParameterizedType) genType)
					.getActualTypeArguments();
			if ((params != null) && (params.length >= (index - 1))) {
				return (Class<?>) params[index];
			}
		}// end if.
		return null;
	}

	protected Object getEntityObject() {
		Class<?> clazz = this.getEntityClass();
		Object obj = null;
		try {
			obj = clazz.newInstance();
		} catch (Exception e) {
//			TLog.e(TAG, e.getMessage());
		}
		return obj;
	}

}
