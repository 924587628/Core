package com.baiyi.core.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.baiyi.core.util.ContextUtil;

/**
 * 文件缓存(一般处理Json)
 * @author tangkun
 *
 */
public class SimpleNormalFileIO extends BaseFileIO implements NormalFileIO{
//	private final static String TAG = SimpleNormalFileIO.class.getSimpleName();
	

	@Override
	public boolean isExist(String dirPath, String fileName){
		if(!ContextUtil.isSDCardExists()){
			return false;
		}
		return fileExists(dirPath, fileName);
	}

	@Override
	public byte[] read(String dirPath, String fileName){
		if(!ContextUtil.isSDCardExists()){
			return null;
		}
		File file = new File(dirPath, fileName);
		if(!file.exists()){
			return null;
		}
		FileInputStream fs = null;
		try {
			byte[] data =new byte[(int) file.length()];
			fs = new FileInputStream(file);
			fs.read(data);
			return data;
		} catch (FileNotFoundException e) {
//			TLog.e(TAG, "file not found when read");
		}catch(IOException e){
//			TLog.e(TAG, "file read error");
		}finally{
			if(fs!=null){
				 try {
					fs.close();
				} catch (IOException e) {
//					TLog.e(TAG, "file close error when read");
				}
			}
		}
		return null;
	}

	@Override
	public void write(String dirPath, String fileName, byte[] data,boolean append){
		if(!ContextUtil.isSDCardExists()){
			return;
		}
		if(!fileExists(dirPath, fileName)){
			if(!create(dirPath, fileName)){
//				TLog.e(TAG, "create file error,write run error");
				return;
			}
		}
		File file = new File(dirPath, fileName);
		OutputStream os = null;
		try{
			os = new FileOutputStream(file,append);
			os.write(data);
		}catch(FileNotFoundException e){
//			TLog.e(TAG, "file not found when write");
		}catch(IOException e){
			long space = ContextUtil.getSpace();
			if(space<(data.length)*2){
				return;
			}
//			TLog.e(TAG, "file write error");
		}finally{
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
//					TLog.e(TAG, "file close error when write");
				}
			}
		}
		
	}
}
