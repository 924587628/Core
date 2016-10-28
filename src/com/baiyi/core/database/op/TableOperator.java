package com.baiyi.core.database.op;

import android.database.sqlite.SQLiteDatabase;

public interface TableOperator {
	public void delete(SQLiteDatabase db, String name);

	public void create(SQLiteDatabase db, String name,
			TableColums[] remainColums);

	public void addColumns(SQLiteDatabase db, String name, TableColums[] colums);

	public void deleteColumns(SQLiteDatabase db, String name,
			TableColums[] remainColums);
}
