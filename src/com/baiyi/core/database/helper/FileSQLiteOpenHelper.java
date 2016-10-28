package com.baiyi.core.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.os.Environment;

import com.baiyi.core.file.NormalFileIO;
import com.baiyi.core.file.SimpleNormalFileIO;

public abstract class FileSQLiteOpenHelper {
//	 private static final String TAG = FileSQLiteOpenHelper.class.getSimpleName();

	    private final Context mContext;
	    private final String mName;
	    private final String mPath;
	    private final CursorFactory mFactory;
	    private final int mNewVersion;
	    
	    private NormalFileIO fileIo = new SimpleNormalFileIO();
	    private SQLiteDatabase mDatabase = null;
	    private boolean mIsInitializing = false;
	    /***
	     * Create a helper object to create, open, and/or manage a database.
	     * The database is not actually created or opened until one of
	     * {@link #getWritableDatabase} or {@link #getReadableDatabase} is called.
	     *
	     * @param context to use to open or create the database
	     * @param name of the database file, or null for an in-memory database
	     * @param factory to use for creating cursor objects, or null for the default
	     * @param version number of the database (starting at 1); if the database is older,
	     *     {@link #onUpgrade} will be used to upgrade the database
	     */
	    public FileSQLiteOpenHelper(Context context,String path,String name, CursorFactory factory, int version) {
	        if (version < 1) throw new IllegalArgumentException("Version must be >= 1, was " + version);

	        mContext = context;
	        mName = name;
	        mFactory = factory;
	        mNewVersion = version;
	        mPath = path;
	    }
	    
	    public synchronized SQLiteDatabase getWritableDatabase(){
	        if (mDatabase != null && mDatabase.isOpen() && !mDatabase.isReadOnly()) {
	            return mDatabase;  // The database is already open for business
	        }
	        if (mIsInitializing) {
//	        	TLog.e(TAG, "getWritableDatabase called recursively");
	            throw new IllegalStateException("getWritableDatabase called recursively");
	        }

	        boolean success = false;
	        SQLiteDatabase db = null;
	        try {
	            mIsInitializing = true;
	            try{
	            		String dirPath = mPath;

				        boolean isNewDB = false;
//			        	TLog.e(TAG, "dirPath" + dirPath + " mName" + mName);
				        
				        if(!fileIo.isExist(dirPath, mName)){
				        	if(!fileIo.create(dirPath, mName))
				        	{
//				        		TLog.e(TAG, "创建文件失败");
						        //创建文件失败
				        		return null;
				        	}
				        	isNewDB = true;
				        }
//		        		TLog.e(TAG, "创建文件成功");
				        db = SQLiteDatabase.openOrCreateDatabase(dirPath+mName, mFactory);
				       
				        if(isNewDB){
				        	onCreate(db);
				        }
	            }catch(Exception e){
					e.printStackTrace();
	            }
	            int version = db.getVersion();
	            if (version != mNewVersion) {
	                db.beginTransaction();
	                try {
	                    if (version != 0) {
	                        onUpgrade(db, version, mNewVersion);
	                    }
	                    db.setVersion(mNewVersion);
	                    db.setTransactionSuccessful();
	                } finally {
	                    db.endTransaction();
	                }
	            }

	            onOpen(db);
	            success = true;
	            return db;
	        } finally {
	            mIsInitializing = false;
	            if (success) {
	                if (mDatabase != null) {
	                    try { mDatabase.close(); } catch (Exception e) { }
//	                    mDatabase.unlock();
	                }
	                mDatabase = db;
	            } else {
//	                if (mDatabase != null) mDatabase.unlock();
	                if (db != null) db.close();
	            }
	        }
	    }

	    public synchronized SQLiteDatabase getReadableDatabase(){
	        if (mDatabase != null && mDatabase.isOpen()) {
	            return mDatabase;  // The database is already open for business
	        }

	        if (mIsInitializing) {
	            throw new IllegalStateException("getReadableDatabase called recursively");
	        }

	        try {
	            return getWritableDatabase();
	        } catch (SQLiteException e) {
	            if (mName == null) throw e;  // Can't open a temp database read-only!
	        }

	        SQLiteDatabase db = null;
	        try {
	            mIsInitializing = true;
	            String path = mContext.getDatabasePath(mName).getPath();
	            db = SQLiteDatabase.openDatabase(path, mFactory, SQLiteDatabase.OPEN_READONLY);
	            if (db.getVersion() != mNewVersion) {
	                throw new SQLiteException("Can't upgrade read-only database from version " +
	                        db.getVersion() + " to " + mNewVersion + ": " + path);
	            }

	            onOpen(db);
	            mDatabase = db;
	            return mDatabase;
	        } finally {
	            mIsInitializing = false;
	            if (db != null && db != mDatabase) db.close();
	        }
	    }
	    public synchronized void  delete(){
	    	close();
	    	boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	    	if(sdCardExist){	    		
	    		fileIo.delete(mPath, mName);
	    		fileIo.delete(mPath, mName+"-journal");
	    	}
	    	
	    }
		public synchronized void close() {
	        if (mIsInitializing) throw new IllegalStateException("Closed during initialization");
	        if(mDatabase==null){
	        	return;
	        }
	        if(mDatabase.isDbLockedByOtherThreads()){
	        	return;
	        }
	        if (mDatabase != null && mDatabase.isOpen()) {
	            mDatabase.close();
	            mDatabase = null;
	        }else{
	        }
	    }
	    public abstract void onCreate(SQLiteDatabase db);
	    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	    public void onOpen(SQLiteDatabase db) {}
}
