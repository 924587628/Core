package com.baiyi.core.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.baiyi.core.file.NormalFileIO;

public abstract class BaseMemTableFileCache implements Cache {
	private int cacheItemMax = 20;
	private String dirPath;
	private NormalFileIO fileIo;
	private KeyMaker keyMaker = null;
	private CacheLinkedHashMap<String, Long> map;

	public BaseMemTableFileCache(String path, Integer cacheItemMax,
			NormalFileIO fileIo, boolean scanFiles) {
		this.dirPath = path;
		this.cacheItemMax = cacheItemMax;
		this.fileIo = fileIo;
		if (cacheItemMax == Integer.MAX_VALUE) {
			map = new CacheLinkedHashMap<String, Long>();
		} else {
			map = new CacheLinkedHashMap<String, Long>(cacheItemMax, 0.75f,
					true);
		}
		if (scanFiles) {
			scanFiles();
		}
	}

	public void setKeyMaker(KeyMaker keyMaker) {
		this.keyMaker = keyMaker;
	}

	protected String getKey(String input) {
		if (keyMaker == null) {
			return input;
		}
		return keyMaker.makeKey(input);
	}

	private void scanFiles() {
		if (fileIo == null) {
			return;
		}
		File[] files = fileIo.getFiles(dirPath);
		if (files == null) {
			return;
		}
		ArrayList<File> sortFiles = new ArrayList<File>();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			long time = f.lastModified();
			int size = sortFiles.size();
			int loc = size;
			for (int j = 0; j < size; j++) {
				File file = sortFiles.get(j);
				if (time > file.lastModified()) {
					loc = j;
					break;
				}
			}
			sortFiles.add(loc, f);
		}
		for (int i = 0; i < files.length; i++) {
			File file = sortFiles.get(i);
			if (i >= this.cacheItemMax) {
				file.delete();
			} else {
				if (!map.containsKey(file.getName())) {
					map.put(file.getName(), file.lastModified());
				}
			}
		}

	}
	
	public int getDirFileSize()
	{
		int totalSize = 0;
		if (fileIo == null) {
			return totalSize;
		}
		File[] files = fileIo.getFiles(dirPath);
		if (files == null) {
			return totalSize;
		}
		ArrayList<File> sortFiles = new ArrayList<File>();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			long time = f.lastModified();
			int size = sortFiles.size();
			int loc = size;
			for (int j = 0; j < size; j++) {
				File file = sortFiles.get(j);
				if (time > file.lastModified()) {
					loc = j;
					break;
				}
			}
			sortFiles.add(loc, f);
		}
		for (int i = 0; i < files.length; i++) {
			File file = sortFiles.get(i);
			totalSize += file.length();
		}
		return totalSize;
	}

	private void deleteFiles() {
		if (fileIo == null) {
			return;
		}
		File[] files = fileIo.getFiles(dirPath);
		if (files == null) {
			return;
		}
		ArrayList<File> sortFiles = new ArrayList<File>();
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			long time = f.lastModified();
			int size = sortFiles.size();
			int loc = size;
			for (int j = 0; j < size; j++) {
				File file = sortFiles.get(j);
				if (time > file.lastModified()) {
					loc = j;
					break;
				}
			}
			sortFiles.add(loc, f);
		}
		for (int i = 0; i < files.length; i++) {
			File file = sortFiles.get(i);
			file.delete();
		}

	}

	protected void putMapValue(String key, Long lastModify) {
		map.put(key, lastModify);
	}

	protected Long getMapValue(String key) {
		return map.get(key);
	}

	protected void removeMapValue(String key) {
		map.remove(key);
	}

	protected void removeAllMapValue() {
		if (map != null && map.size() > 0)
			map.clear();
	}

	protected boolean isExistMapValue(String key) {
		return map.containsKey(key);
	}

	protected NormalFileIO getNormalFileIO() {
		return fileIo;
	}

	protected String getDirPath() {
		return dirPath;
	}

	@Override
	public void notifyDataChanged() {
		removeAllMapValue();
		deleteFiles();

	}

	@SuppressWarnings("serial")
	private class CacheLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
		public CacheLinkedHashMap(int initialCapacity, float loadFactor,
				boolean accessOrder) {
			super(initialCapacity, loadFactor, accessOrder);
		}

		public CacheLinkedHashMap() {
			super();
		}

		protected boolean removeEldestEntry(Entry<K, V> entry) {
			if (size() > BaseMemTableFileCache.this.cacheItemMax) {
				BaseMemTableFileCache.this.fileIo.delete(dirPath, entry
						.getKey().toString());
				return true;
			} else {
				return false;
			}
		}
	}
}
