package com.baiyi.core.file;

import java.io.File;

public interface FileIO {
	public boolean createDir(String dirPath);
	public boolean create(String dirPath,String fileName);
	public void delete(String dirPath,String fileName);
	public void deleteAll(String dirPath);
	public File getFile(String dirPath,String fileName);
	public File[] getFiles(String dirPath);
}
