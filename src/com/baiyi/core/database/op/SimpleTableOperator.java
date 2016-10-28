package com.baiyi.core.database.op;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.baiyi.core.util.DataTypeUtils;

public class SimpleTableOperator implements TableOperator {
//	private static final String TAG = SimpleSqlOperator.class.getSimpleName();
	private SimpleSqlOperator sql = new SimpleSqlOperator();

	@Override
	public void delete(SQLiteDatabase db, String name) {
		String sqlString = "DROP TABLE IF EXISTS ";
		sqlString += name;
		sql.execSql(db, sqlString, null);
	}

	@Override
	public void create(SQLiteDatabase db, String name,
			TableColums[] remainColums) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(name);
		sb.append(" (");
		for (int i = 0; i < remainColums.length; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(remainColums[i].toString());
			if (remainColums[i].getName().equalsIgnoreCase("id")) {
				sb.append(" PRIMARY KEY");
			}
		}
		sb.append(")");
		sql.execSql(db, sb.toString(), null);
	}

	@Override
	public void addColumns(SQLiteDatabase db, String name, TableColums[] colums) {
		String sqlHeader = "alter table " + name + " add ";
		if (DataTypeUtils.isEmpty(colums)) {
			return;
		}
		db.beginTransaction();
		for (int i = 0; i < colums.length; i++) {
			String sqlString = sqlHeader + colums[i].toString();
			sql.execSql(db, sqlString, null);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	@Override
	public void deleteColumns(SQLiteDatabase db, String name,
			TableColums[] remainColums) {
		String tempSqlName = name + "_temp";
		if (DataTypeUtils.isEmpty(remainColums)) {
			return;
		}
		db.beginTransaction();
		create(db, tempSqlName, remainColums);
		ArrayList<String> columnNames = new ArrayList<String>();
		for (TableColums column : remainColums) {
			columnNames.add(column.getName());
		}
		copyTableValue(db, name, tempSqlName, columnNames);
		delete(db, name);
		create(db, name, remainColums);
		copyTableValue(db, tempSqlName, name, columnNames);
		delete(db, tempSqlName);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	private void copyTableValue(SQLiteDatabase db, String sourceName,
			String destName, List<String> columnNames) {
		StringBuilder cb = new StringBuilder();
		for (int i = 0; i < columnNames.size(); i++) {
			if (i != 0) {
				cb.append(",");
			}
			cb.append(columnNames.get(i));
		}
		String sqlString = "INSERT INTO " + destName + "(" + cb.toString()
				+ ") SELECT " + cb.toString() + " FROM " + sourceName;
		db.execSQL(sqlString);
	}
}
