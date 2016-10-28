package com.baiyi.core.database.bean;

import com.baiyi.core.database.DataBase;

public class SingleBean extends BaseBean implements Bean {
	private int version = 1;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public DataBase getDataBase() {
		if (getSimpleName() == null) {
			throw new IllegalArgumentException("single db name not set");
		}
		DataBase db = new DataBase();
		db.setName(getSimpleName());
		db.setVersion(version);
		return db;
	}
}
