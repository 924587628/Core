package com.baiyi.core.file;


public interface NormalFileIO extends FileIO{
	public boolean isExist(String dirPath,String fileName);
	public byte[] read(String dirPath,String fileName);
	public void write(String dirPath,String fileName,byte[] data,boolean append);
}
