package com.baiyi.core.cache;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.baiyi.core.file.NormalFileIO;

/**
 * ÎÄ¼þ»º´æ
 * @author tangkun
 *
 */
public class NetFileCache extends BaseMemTableFileCache implements Cache {
	private final Lock lock = new ReentrantLock();

	public NetFileCache(String path, int cacheItemMax, NormalFileIO fileIo,
			boolean scanFiles) {
		super(path, cacheItemMax, fileIo, scanFiles);
	}

	@Override
	public Object get(String key) {
		byte[] data = null;
		try {
			lock.lock();
			String realKey = getKey(key);
			if (!isExistMapValue(realKey)) {
				return null;
			}
			data = getNormalFileIO().read(getDirPath(), realKey);
		} finally {
			lock.unlock();
		}
		return data;
	}

	@Override
	public void put(String key, Object value) {
		try {
			lock.lock();
			String realKey = getKey(key);
			if (isExistMapValue(realKey)) {
				return;
			}
			getNormalFileIO().write(getDirPath(), realKey, (byte[]) value,
					false);
			putMapValue(realKey, System.currentTimeMillis());
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void remove(String key) {
		try {
			lock.lock();
			String realKey = getKey(key);
			getNormalFileIO().delete(getDirPath(), realKey);
			removeMapValue(realKey);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void clear() {
		try {
			lock.lock();
			getNormalFileIO().deleteAll(getDirPath());
			removeAllMapValue();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean isExist(String key) {
		String realKey = getKey(key);
		return isExistMapValue(realKey);
	}

	@Override
	public void update(String key, Object value) {
		try {
			lock.lock();
			String realKey = getKey(key);
			if (!isExistMapValue(realKey)) {
				put(key, value);
			} else {
				removeMapValue(realKey);
				put(key, value);
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public int getFileSize() {
		return getDirFileSize();
	}
}
