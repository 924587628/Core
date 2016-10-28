package com.baiyi.core.database.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baiyi.core.database.AbstractBaseModel;
import com.baiyi.core.database.bean.Bean;
import com.baiyi.core.database.manager.SQLDataBaseManager;
import com.baiyi.core.database.op.SimpleSqlOperator;
import com.baiyi.core.util.DataTypeUtils;

public class DaoUtil<T extends AbstractBaseModel>{
//	private static final String TAG = DaoUtil.class.getSimpleName();
	private static Map<String, Integer> TYPES = new HashMap<String, Integer>();
	public static final int TYPE_STRING = 1;
	public static final int TYPE_INTEGER = 2;
	public static final int TYPE_LONG = 3;
	public static final int TYPE_SHORT = 4;
	public static final int TYPE_FLOAT = 5;
	public static final int TYPE_DOUBLE = 6;
	static {
		TYPES.put("date", TYPE_STRING);
		TYPES.put("string", TYPE_STRING);
		TYPES.put("integer", TYPE_INTEGER);
		TYPES.put("int", TYPE_INTEGER);
		TYPES.put("long", TYPE_LONG);
		TYPES.put("short", TYPE_SHORT);
		TYPES.put("float", TYPE_FLOAT);
		TYPES.put("double", TYPE_DOUBLE);
	}
	private SQLDataBaseManager manager = null;
	private SimpleSqlOperator sql = null;
	private Bean bean = null;
	private String beanName = null;
//	private DaoClassUtil<T> classUtil = null;
	public DaoUtil(SQLDataBaseManager manager,Bean bean){
		this.manager = manager;
		this.bean = bean;
		this.sql = new SimpleSqlOperator();
		beanName = bean.getName();
	}
	public void setBeanParam(Bean bean){
		this.bean = bean;
		this.bean.setName(beanName);
	}
	public Cursor execReadSql(String sqlString,String[] sqlArgs){
		SQLiteDatabase db = manager.getReadDB(bean);
		if(db==null){
			return null;
		}
		return sql.execSql(db, sqlString, sqlArgs);
	}
	public void closeConnect(){
		manager.close(bean);
	}
	public void deleteDB(){
		manager.delte(bean);
	}
	public String getBeanSimpleName(){
		return bean.getSimpleName();
	}
	public String getBeanName(){
		return bean.getName();
	}
	public Cursor execWriteSql(String sqlString,String[] sqlArgs){
		SQLiteDatabase db = manager.getWriteDB(bean);
		if(db==null){
			return null;
		}
		return sql.execSql(db, sqlString, sqlArgs);
	}
	public SQLiteDatabase getWriteDataBase() {
		return manager.getWriteDB(bean);
		
	}
	@SuppressWarnings("unchecked")
	public List<T> cursor2List(Cursor cursor) {
		List<T> rtnlist = null;
		if (DataTypeUtils.isEmpty(cursor) || cursor.getCount() == 0) {
			if (cursor != null) {
				cursor.close();
			}
			return rtnlist;
		}
		rtnlist = new ArrayList<T>();
		T obj = null;
		while (cursor.moveToNext()) {
			try {
				obj = (T) Class.forName(beanName).newInstance();
			} catch (IllegalAccessException e1) {
				
			} catch (InstantiationException e1) {
				
			} catch (ClassNotFoundException e1) {
			}
			Map<String, String> fieldMap = obj.toFieldMap();
			Iterator<String> it = obj.toFieldSet().iterator();
			while (it.hasNext()) {
				String fieldName = (String) it.next();
				try {
					String fieldType = fieldMap.get(fieldName);
					Object objArg = getValue(cursor, fieldName, fieldType);
					Method method = getSetMethod(obj,fieldType,fieldName);
					if (fieldType.equalsIgnoreCase("date")) {
						objArg = DataTypeUtils.string2Date(objArg.toString(), 1);
					}
					if(method!=null){
						method.invoke(obj, objArg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					if(e.getMessage()!=null){
//						TLog.e(TAG, e.getMessage());
					}
					continue;
				}
			}
			rtnlist.add(obj);
		}
		cursor.close();
		return rtnlist;
	}
	private Method getSetMethod(T obj,String fieldType,String fieldName)throws SecurityException,NoSuchMethodException{
		@SuppressWarnings("rawtypes")
		Class[] clazzArray = getGenericClass(fieldType);
		Method method = null;
		try {
			method = obj.getClass().getMethod("set" + fieldName,clazzArray[0]);
		}catch (NoSuchMethodException e) {
			if(clazzArray.length==2){
				method = obj.getClass().getMethod("set" + fieldName,clazzArray[1]);
			}
		}
		return method;
	}
	
	

	@SuppressWarnings("rawtypes")
	private Class[] getGenericClass(String type) {
		Class[] clazzArray = new Class[2];
		if (type.equalsIgnoreCase("string")) {
			clazzArray[0] = String.class;
		} else if (type.equalsIgnoreCase("date")) {
			clazzArray[0] = Date.class;
		} else if (DataTypeUtils.startsWith(type, "int")) {
			clazzArray[0] = int.class;
			clazzArray[1] = Integer.class;
		} else if (DataTypeUtils.startsWith(type,"short")) {
			clazzArray[0] = short.class;
			clazzArray[1] = Short.class;
		} else if (DataTypeUtils.startsWith(type, "long")) {
			clazzArray[0] = long.class;
			clazzArray[1] = Long.class;
		} else if (DataTypeUtils.startsWith(type, "float")) {
			clazzArray[0] = float.class;
			clazzArray[1] = Float.class;
		} else if (DataTypeUtils.startsWith(type, "double")) {
			clazzArray[0] = double.class;
			clazzArray[1] = Double.class;
		}else {
			clazzArray[0] = Object.class;
		}
		return clazzArray;
	}

	@SuppressLint("DefaultLocale")
	private Object getValue(Cursor cursor, String fieldName, String fieldType) {
		int columnIndex = cursor.getColumnIndex(fieldName.toUpperCase());
		Object objArg = null;
		switch (getTypeIndex(fieldType)) {
		case TYPE_DOUBLE:
			objArg = cursor.getDouble(columnIndex);
			break;
		case TYPE_FLOAT:
			objArg = cursor.getFloat(columnIndex);
			break;
		case TYPE_INTEGER:
			objArg = cursor.getInt(columnIndex);
			break;
		case TYPE_LONG:
			objArg = cursor.getLong(columnIndex);
			break;
		case TYPE_SHORT:
			objArg = cursor.getShort(columnIndex);
			break;
		case TYPE_STRING:
		default:
			objArg = cursor.getString(columnIndex);
		}
		return objArg;
	}

	@SuppressLint("DefaultLocale")
	private int getTypeIndex(String typeString) {
		typeString = typeString.toLowerCase();
		return TYPES.containsKey(typeString) ? TYPES.get(typeString) : 0;
	}
}
