package com.baiyi.core.file;

import java.io.File;
import java.io.IOException;

import com.baiyi.core.util.ContextUtil;

public abstract class BaseFileIO implements FileIO{
//	private final static String TAG = BaseFileIO.class.getSimpleName();
	@Override
	public boolean createDir(String dirPath){
		if(!ContextUtil.isSDCardExists()){
			return false;
		}
		File dir = new File(dirPath);
		if(!dir.exists()){
			try {
				return dir.mkdirs();
			} catch (Exception e) {
			}
		}
		return true;
	}
	@Override
	public File[] getFiles(String dirPath){
		if(!ContextUtil.isSDCardExists()){
			return null;
		}
		File dir = new File(dirPath);
		if(!dir.exists()){
			return null;
		}
		return dir.listFiles();
	}
	@Override
	public File getFile(String dirPath,String fileName){
		File f = new File(dirPath, fileName);
		return f;
	}
	
	@Override
	public boolean create(String dirPath, String fileName){
		if(!ContextUtil.isSDCardExists()){
			return false;
		}
		File dir = new File(dirPath);
		if(!dir.exists()){
			if(!dir.mkdirs()){
//				TLog.d(TAG, "make dir fail");
				return false;
			}
		}
		File file = new File(dir, fileName);
		if(!file.exists()){
			try {
				return file.createNewFile();
			} catch (IOException e) {
				long space = ContextUtil.getSpace();
				if(space<0){
					return false;
				}
//				TLog.d(TAG, "create file:"+dirPath+fileName+" error");
				return false;
			}
		}
		return true;
	}

	@Override
	public void delete(String dirPath, String fileName){
		if(!ContextUtil.isSDCardExists()){
			return;
		}

		File dir = new File(dirPath);
		if(!dir.exists()){
			return;
		}
		File file = new File(dir, fileName);
		if(!file.exists()){
			return;
		}else{
			if(!file.delete()){
				return;
			}
		}
		File[] files = dir.listFiles();
		if(files.length==0){
			dir.delete();
		}
	}

	@Override
	public void deleteAll(String dirPath){
		if(!ContextUtil.isSDCardExists()){
			return;
		}
		File dir = new File(dirPath);
		if(!dir.exists()){
			return;
		}
		File[] files = dir.listFiles();
		if(files.length>0){
			for(File f:files){
				if(f.isFile()){
					f.delete();
				}else if(f.isDirectory()){
					deleteAll(f.getAbsolutePath());
				}
			}
		}
//		dir.delete();
	}
	protected boolean fileExists(String dirPath,String fileName){
		File dir = new File(dirPath);
		if(!dir.exists()){
			return false;
		}
		File file = new File(dir, fileName);
		if(!file.exists()){
			return false;
		}
		return true;
	}
}
