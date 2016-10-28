package com.baiyi.core.database.bean;

import com.baiyi.core.database.DataBase;

public class NormalBean extends BaseBean implements Bean {
	private static String dbName = null;
	private static int dbVersion = 1;

	public static String getDbName() {
		return dbName;
	}

	public static void setDbName(String dbName) {
		NormalBean.dbName = dbName;
	}

	public static int getDbVersion() {
		return dbVersion;
	}

	public static void setDbVersion(int dbVersion) {
		NormalBean.dbVersion = dbVersion;
	}

	@Override
	public DataBase getDataBase() {
		if (dbName == null) {
			throw new IllegalArgumentException("default db name not set");
		}
		DataBase db = new DataBase();
		db.setName(dbName);
		db.setVersion(dbVersion);
		return db;
	}
}
