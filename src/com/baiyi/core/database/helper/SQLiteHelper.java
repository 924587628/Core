package com.baiyi.core.database.helper;

import android.database.sqlite.SQLiteDatabase;

public interface SQLiteHelper {
	public SQLiteDatabase getWriteHandler();

	public SQLiteDatabase getReadHanlder();

	public void close();

	public void delete();

	public void setSqliteListener(SQLiteListener sqliteListener);

	public interface SQLiteListener {
		public void onCreate(SQLiteDatabase db);

		public boolean onUpgrade(SQLiteDatabase db, int oldVersion,
				int newVersion, String[] newBeans);

		public void onOpen(SQLiteDatabase db);
	}
}
