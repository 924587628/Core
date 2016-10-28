package com.baiyi.core.database.manager;

import android.database.sqlite.SQLiteDatabase;

import com.baiyi.core.database.bean.Bean;

public interface SQLDataBaseManager {
	public SQLiteDatabase getReadDB(Bean bean);

	public SQLiteDatabase getWriteDB(Bean bean);

	public void close(Bean bean);

	public void delte(Bean bean);

	public void closeAll();

	public Bean getBeanConfig(String name);

	public void setUpdateListener(UpdateListener listner);

	public interface UpdateListener {
		public boolean onUpgrade(SQLiteDatabase db, String dbName,
				int oldVersion, int newVersion, String[] newBeans);
	}
}
