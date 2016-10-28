package com.baiyi.core.database.config;

import android.content.Context;

import com.baiyi.core.database.DataBase;
import com.baiyi.core.database.bean.NormalBean;
import com.baiyi.core.database.bean.SingleBean;
import com.baiyi.core.util.DataTypeUtils;

public class SQLiteConfiguration {
//	private static final String TAG = SQLiteConfiguration.class.getName();
	private static SQLiteConfiguration instance = null;
	// private String sqlPath = null;
	private DataBase defaultDb = null;
	private NormalBean[] defaultDbBeans = null;
	private SingleBean[] singleDbBeans = null;
	// private PartBean[] partDbBeans = null;
	private Boolean isInited = false;

	private SQLiteConfiguration() {
	}

	public static SQLiteConfiguration getSQLiteConfiguration() {
		if (instance == null) {
			instance = new SQLiteConfiguration();
		}
		return instance;
	}

	public void setInited(boolean isInited) {
		this.isInited = isInited;
	}

	public boolean isInited() {
		return isInited;
	}

	public DataBase getDefaultDbConfig() {
		return defaultDb;
	}

	// public String getDbPathConfig(){
	// return sqlPath;
	// }
	public NormalBean[] getDefaultDbBeans() {
		return defaultDbBeans;
	}

	public SingleBean[] getSingleDbBeans() {
		return singleDbBeans;
	}

	public void init(Context context, String[] single_db_beans, int[] single_db_version) {
//		this.context = context;
		if (!isInited) {

			// readSQLPath();
//    		TLog.e(TAG, "init SQLiteConfiguration");
			readDefaultDBConfiguration();
			readSingleDBConfiguration(single_db_beans, single_db_version);
			isInited = true;
		}
	}

	// private void readSQLPath(){
	// sqlPath =
	// DataTypeUtils.null2string(context.getResources().getString(Res.string.path),"/u17/phone/data");
	// sqlPath = ContextUtil.getSDPath()+sqlPath;
	// }
//    <!-- 数据库存放路径 -->
//    <string name="path">/zaibang/database/</string>
//
//    <!-- 缺省数据库版本 -->
//    <string name="default_db_version">1</string>
//    <!-- 缺省数据库名称 -->
//    <string name="default_db_name">default.db</string>
//    <!-- 缺省数据表对应实例 -->
//    <string-array name="default_db_beans"></string-array>
	private void readDefaultDBConfiguration() {
		int defaultDbVersion = DataTypeUtils.str2int("1", 1);
		String deafultDbName = DataTypeUtils.null2string("default.db", "default.db");
		defaultDb = new DataBase();
		defaultDb.setName(deafultDbName);
		defaultDb.setVersion(defaultDbVersion);
		String[] beans = new String[0];
		defaultDbBeans = new NormalBean[beans.length];
		NormalBean.setDbName(deafultDbName);
		NormalBean.setDbVersion(defaultDbVersion);
		for (int i = 0; i < beans.length; i++) {
			NormalBean bean = new NormalBean();
			bean.setName(beans[i]);
			defaultDbBeans[i] = bean;
		}
	}

	private void readSingleDBConfiguration(String[] single_db_beans, int[] single_db_version) {
		String[] beans = single_db_beans;
		int[] versions = single_db_version;
		if (beans.length != versions.length) {
			throw new IllegalArgumentException(
					"single_db_beans lenght is not equal single_db_version");
		}
		int length = beans.length;
		singleDbBeans = new SingleBean[length];
		for (int i = 0; i < length; i++) {
			SingleBean bean = new SingleBean();
			bean.setName(beans[i]);
			bean.setVersion(versions[i]);
			singleDbBeans[i] = bean;
		}

	}
}
