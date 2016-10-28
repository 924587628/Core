package com.baiyi.core.database.op;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baiyi.core.util.DataTypeUtils;

public class SimpleSqlOperator {
//	private static final String TAG = SimpleSqlOperator.class.getSimpleName();

	public Cursor execSql(SQLiteDatabase db, String sqlString, String[] bindArgs) {
		Cursor cursor = null;
		if (db == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		if (bindArgs != null) {
			builder.append("(");
			for (String s : bindArgs) {
				String tmp = "";
				if(DataTypeUtils.isEmpty(s))
				{
					tmp = "";
				}
				builder.append(tmp).append(" ");
			}
			builder.append(")");
		}
		if (DataTypeUtils.startsWith(sqlString, "select")) {
			cursor = db.rawQuery(sqlString, bindArgs);
		} else {
			if (bindArgs == null) {
				db.execSQL(sqlString);
			} else {
				db.execSQL(sqlString, bindArgs);
			}
		}
		return cursor;
	}

}
