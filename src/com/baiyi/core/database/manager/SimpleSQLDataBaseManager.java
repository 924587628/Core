package com.baiyi.core.database.manager;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.baiyi.core.database.DataBase;
import com.baiyi.core.database.bean.Bean;
import com.baiyi.core.database.bean.NormalBean;
import com.baiyi.core.database.bean.SingleBean;
import com.baiyi.core.database.config.SQLiteConfiguration;
import com.baiyi.core.database.helper.SQLiteHelper;
import com.baiyi.core.database.helper.SimpleSQLiteHelper;

public class SimpleSQLDataBaseManager implements SQLDataBaseManager {
//	private final static String TAG = SimpleSQLDataBaseManager.class
//			.getSimpleName();
	private static SimpleSQLDataBaseManager instance = null;
	private SQLiteConfiguration config = null;
	private Context context = null;
	private boolean isInited = false;
	private ConcurrentHashMap<String, SQLiteHelper> dbDic = null;
	private SQLDataBaseManager.UpdateListener updateListner = null;
	private String dataPath;

	private SimpleSQLDataBaseManager() {

	}

	public static SimpleSQLDataBaseManager getInstance() {
		if (instance == null) {
			instance = new SimpleSQLDataBaseManager();
		}
		return instance;
	}

	public void init(Context context, String dataPath, String[] single_db_beans, int[] single_db_version) {
		try {
			this.context = context;
			this.dataPath = dataPath;
			if (!isInited) {
//        		TLog.e(TAG, "init database");
				config = SQLiteConfiguration.getSQLiteConfiguration();
				config.init(context ,single_db_beans, single_db_version);
				dbDic = new ConcurrentHashMap<String, SQLiteHelper>();
				initDefaultDataBaseHelper();
				initSingleDataBaseHelper();
				isInited = true;
			}
		} catch (Exception e) {
			isInited = false;
		}
	}

	public Boolean isInited() {
		return isInited;
	}

	private void initSingleDataBaseHelper() {
		SingleBean[] beans = config.getSingleDbBeans();
		if (beans.length == 0) {
			return;
		}
		for (SingleBean bean : beans) {
			initSingleBean(bean);
		}
	}

	private void initSingleBean(SingleBean bean) {
//		TLog.e(TAG, "table:"+bean.getName());
		SQLiteHelper helper = get(bean);
		if (helper != null) {
			return;
		}
		DataBase db = bean.getDataBase();
		helper = new SimpleSQLiteHelper(context, dataPath, db.getName(),
				new String[] { bean.getName() }, null, db.getVersion());
		initHelper(helper, db.getName());
	}

	private void initHelper(SQLiteHelper helper, final String key) {
		helper.setSqliteListener(new SQLiteHelper.SQLiteListener() {
			@Override
			public boolean onUpgrade(SQLiteDatabase db, int oldVersion,
					int newVersion, String[] newBeans) {
				if (updateListner != null) {
					return updateListner.onUpgrade(db, key, oldVersion,
							newVersion, newBeans);
				}
				return false;
			}

			@Override
			public void onOpen(SQLiteDatabase db) {

			}

			@Override
			public void onCreate(SQLiteDatabase db) {
				
			}
		});
		helper.getWriteHandler();
		dbDic.put(key, helper);

	}

	private void initDefaultDataBaseHelper() {
		NormalBean[] beans = config.getDefaultDbBeans();
//		if(beans == null || beans.length == 0)
//		{
//			TLog.e(TAG, "DataBase:null");
//		}else
//		{
//			TLog.e(TAG, "DataBase" + beans[0].getName());
//		}
		if (beans.length == 0) {
			return;
		}
		SQLiteHelper helper = get(beans[0]);
		if (helper != null) {
			return;
		}
		String[] beanNames = new String[beans.length];
		for (int i = 0; i < beans.length; i++) {
			beanNames[i] = beans[i].getName();
		}
		DataBase db = config.getDefaultDbConfig();
//		TLog.e(TAG, "DataBase" + db.getName());
		helper = new SimpleSQLiteHelper(context, dataPath, db.getName(), beanNames,
				null, db.getVersion());
		initHelper(helper, db.getName());

	}

	private Bean getSuiteBeanConfig(String name) {
		for (NormalBean b : config.getDefaultDbBeans()) {
			if (b.getName().equalsIgnoreCase(name)) {
				return b;
			}
		}
		for (SingleBean b : config.getSingleDbBeans()) {
			if (b.getName().equalsIgnoreCase(name)) {
				return b;
			}
		}
		return null;
	}

	private DataBase getSuiteDataBase(String name) {
		Bean bean = getSuiteBeanConfig(name);
		if (bean == null) {
			return null;
		}
		return bean.getDataBase();
	}

	private synchronized SQLiteHelper get(Bean bean) {
		DataBase db = getSuiteDataBase(bean.getName());
		if (db == null) {
			return null;
		}
		return dbDic.get(db.getName());
	}

	private synchronized void remove(Bean bean) {
		DataBase db = getSuiteDataBase(bean.getName());
		if (db == null) {
			return;
		}
		dbDic.remove(db.getName());
	}

	@Override
	public void close(Bean bean) {
		SQLiteHelper helper = get(bean);
		if (helper != null) {
			helper.close();
			remove(bean);
		}
	}

	@Override
	public void delte(Bean bean) {
		SQLiteHelper helper = get(bean);
		if (helper != null) {
			helper.delete();
			remove(bean);
		}
	}

	@Override
	public void setUpdateListener(UpdateListener listner) {
		this.updateListner = listner;

	}

	@Override
	public SQLiteDatabase getReadDB(Bean bean) {
		SQLiteHelper helper = get(bean);
		if (helper != null) {
			return helper.getReadHanlder();
		}
		return null;
	}

	@Override
	public SQLiteDatabase getWriteDB(Bean bean) {
		// TODO Auto-generated method stub
		SQLiteHelper helper = get(bean);
		if (helper != null) {
			return helper.getWriteHandler();
		}
		return null;
	}

	@Override
	public void closeAll() {
		isInited = false;
		for (Entry<String, SQLiteHelper> entry : dbDic.entrySet()) {
			SQLiteHelper helper = entry.getValue();
			helper.close();
		}
		dbDic.clear();
	}

	@Override
	public Bean getBeanConfig(String name) {
		return getSuiteBeanConfig(name);
	}

}
