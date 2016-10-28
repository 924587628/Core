package com.baiyi.core.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.baiyi.core.database.AbstractBaseModel;
import com.baiyi.core.database.bean.Bean;
import com.baiyi.core.database.bean.NormalBean;
import com.baiyi.core.database.op.SimpleTableOperator;
import com.baiyi.core.database.op.TableColums;
import com.baiyi.core.database.op.TableOperator;

public class SimpleSQLiteHelper implements SQLiteHelper {
//	private static final String TAG = SimpleSQLiteHelper.class.getSimpleName();
	private SQLiteOpenHelper sysSqlHelper = null;
	private FileSQLiteOpenHelper fileSqlHelper = null;
	private SQLiteHelper.SQLiteListener sqliteListener = null;
	private String[] beans;
	private TableOperator table = new SimpleTableOperator();

	public SimpleSQLiteHelper(Context context, String name, String[] beans,
			CursorFactory factory, int version) {
		this.beans = beans;
		sysSqlHelper = new SQLiteOpenHelper(context, name, factory, version) {

			@Override
			public void onCreate(SQLiteDatabase db) {
				SimpleSQLiteHelper.this.create(db);
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion,
					int newVersion) {
				SimpleSQLiteHelper.this.upgrade(db, oldVersion, newVersion);
			}

			public void onOpen(SQLiteDatabase db) {
				super.onOpen(db);
				SimpleSQLiteHelper.this.open(db);
			}
		};
	}

	public SimpleSQLiteHelper(Context context, String path, String name,
			String[] beans, CursorFactory factory, int version) {
		this.beans = beans;
		fileSqlHelper = new FileSQLiteOpenHelper(context, path, name, factory,
				version) {
			@Override
			public void onCreate(SQLiteDatabase db) {
				SimpleSQLiteHelper.this.create(db);
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion,
					int newVersion) {
				SimpleSQLiteHelper.this.upgrade(db, oldVersion, newVersion);
			}

			public void onOpen(SQLiteDatabase db) {
				super.onOpen(db);
				SimpleSQLiteHelper.this.open(db);
			}
		};
	}

	public void setSqliteListener(SQLiteHelper.SQLiteListener sqliteListener) {
		this.sqliteListener = sqliteListener;
	}

	@Override
	public SQLiteDatabase getWriteHandler() {
		if (fileSqlHelper != null) {
//			TLog.e(TAG, "write:fileSqlHelper");
			return fileSqlHelper.getWritableDatabase();
		} else if (sysSqlHelper != null) {
//			TLog.e(TAG, "write:sysSqlHelper");
			return sysSqlHelper.getWritableDatabase();
		}
//		TLog.e(TAG, "write:null");
		return null;
	}

	@Override
	public SQLiteDatabase getReadHanlder() {
		if (fileSqlHelper != null) {
			return fileSqlHelper.getReadableDatabase();
		} else if (sysSqlHelper != null) {
			return sysSqlHelper.getReadableDatabase();
		}
		return null;
	}

	@Override
	public synchronized void close() {
		if (fileSqlHelper != null) {
			fileSqlHelper.close();
		} else if (sysSqlHelper != null) {
			sysSqlHelper.close();
		}
	}

	@Override
	public synchronized void delete() {
		close();
		if (fileSqlHelper != null) {
			fileSqlHelper.delete();
		}
	}

	private void create(SQLiteDatabase db) {
		createTables(db);
		if (sqliteListener != null) {
			sqliteListener.onCreate(db);
		}
	}

	private void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		boolean isUpgrade = false;
		if (sqliteListener != null) {
			isUpgrade = sqliteListener.onUpgrade(db, oldVersion, newVersion,
					beans);
		}
		if (!isUpgrade) {
			// deleteTables(db);
			// createTables(db);
			addColumnFavoriteListItem(db);
		}
	}

	private void open(SQLiteDatabase db) {
//		TLog.e(TAG, "onOpen ");
	}

	private void addColumnFavoriteListItem(SQLiteDatabase db) {
		for (String bean : beans) {
			if (bean == null) {
				continue;
			}
			int pos = bean.lastIndexOf(".");
			if (pos == -1) {
				return;
			}
			TableColums[] colums = new TableColums[1];
			colums[0] = new TableColums();
			colums[0].setName("type");
			colums[0].setType("INTEGER");
			table.addColumns(db, bean.substring(pos + 1), colums);
		}
	}

	@SuppressWarnings("unused")
	private void deleteTables(SQLiteDatabase db) {
		for (String bean : beans) {
			table.delete(db, bean);
		}
	}

	@SuppressWarnings("unchecked")
	private void createTables(SQLiteDatabase db) {
		for (String bean : beans) {
			try {
				Class<? extends AbstractBaseModel> clazz = (Class<? extends AbstractBaseModel>) Class
						.forName(bean);
				AbstractBaseModel model = clazz.newInstance();
				Bean b = new NormalBean();
				b.setName(bean);
				table.create(db, b.getSimpleName(), model.toTableColums());
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(
						"database bean name is error,not a AbstractBaseModel");
			} catch (IllegalAccessException e) {
//				TLog.e(TAG, "", e);
			} catch (InstantiationException e) {
//				TLog.e(TAG, "", e);
			}
		}
	}

}
