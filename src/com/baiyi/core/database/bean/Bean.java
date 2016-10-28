package com.baiyi.core.database.bean;

import com.baiyi.core.database.DataBase;

public interface Bean {
	public DataBase getDataBase();

	public String getName();

	public String getSimpleName();

	public void setName(String name);
}
