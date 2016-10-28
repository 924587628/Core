package com.baiyi.core.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.text.TextUtils;

/**
 * xml文件
 * 
 * @author tangkun
 * 
 */
public class Preference implements PreferenceLister {

//	private static final String TAG = Preference.class.getName();
	private static byte[] HEADER = { 120, 127, 69 };
	private static int VERSION = 2;
	private static final String EN_KEY = "_&^%&*20150602#$%)%^@";
	private ConcurrentHashMap<String, String> values;
	private ConcurrentHashMap<String, String> trans;
	private File cfg;
	private static Preference instance;

	public static void Init(File cfg) {
		if (instance == null) {
			instance = new Preference(cfg);
		}
	}

	public static Preference getInstance() {
		if (instance == null) {
//			TLog.e(TAG, "preference not init please call ");
		}
		return instance;
	}

	private Preference(File cfg) {
		this.values = new ConcurrentHashMap<String, String>(256);
		this.cfg = cfg;
		loadConfig(cfg);
	}

	private Preference(Context context, byte[] data) {
		this.values = new ConcurrentHashMap<String, String>(256);
		loadConfig(data);
	}

	public void loadConfig(File cfg) {
		if (!cfg.exists()) {
			return;
		}
		this.values.clear();
		FileInputStream fis = null;

		byte[] data = new byte[(int) cfg.length()];
		try {
			fis = new FileInputStream(cfg);
			fis.read(data);
			fis.close();
			fis = null;
		} catch (Exception e) {
			return;
		}
		data = encrypt(data, EN_KEY.getBytes());
		if ((data[0] != HEADER[0]) || (data[1] != HEADER[1])
				|| (data[2] != HEADER[2])) {
			return;
		}
		DataInputStream dis = new DataInputStream(
				new ByteArrayInputStream(data));
		try {
			dis.skip(3L);
			int v = dis.readByte();
			if (v != VERSION) {
			}
			int c = dis.readShort();
			for (int i = 0; i < c; i++) {
				String key = readString(dis);
				String val = readString(dis);
				if (((key != null ? 1 : 0) & (val != null ? 1 : 0)) != 0) {
					this.values.put(key, val);
				}
			}
			return;
		} catch (Exception e) {
//			TLog.e(TAG, "load", e);
		} finally {
			try {
				dis.close();
				dis = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void loadConfig(byte[] data) {
		this.values.clear();

		data = encrypt(data, EN_KEY.getBytes());
		if ((data[0] != HEADER[0]) || (data[1] != HEADER[1])
				|| (data[2] != HEADER[2])) {
//			TLog.e(TAG, "配置文件头错误!");
			return;
		}
		DataInputStream dis = new DataInputStream(
				new ByteArrayInputStream(data));
		try {
			dis.skip(3L);
			int v = dis.readByte();
			if (v != VERSION) {
//				TLog.e(TAG, "配置文件版本过旧!");
			}
			int c = dis.readShort();
			for (int i = 0; i < c; i++) {
				String key = readString(dis);
				String val = readString(dis);
				if (((key != null ? 1 : 0) & (val != null ? 1 : 0)) != 0) {
					this.values.put(key, val);
				}
			}
			return;
		} catch (Exception e) {
//			TLog.e("GobalConfig", "load", e);
		} finally {
			try {
				dis.close();
				dis = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public synchronized void saveConfig() {
		File file = this.cfg;
		FileOutputStream fos = null;

		ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.write(HEADER);
			dos.writeByte(VERSION);
			dos.writeShort(this.values.size());
			Iterator<Map.Entry<String, String>> it = this.values.entrySet()
					.iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry<String, String> e = (Map.Entry) it.next();
				writeString(dos, (String) e.getKey());
				writeString(dos, (String) e.getValue());
			}
			byte[] data = encrypt(bos.toByteArray(), EN_KEY.getBytes());
			fos = new FileOutputStream(file, false);
			fos.write(data);
			fos.flush();
			bos.close();
			bos = null;
			return;
		} catch (Exception e) {
//			TLog.e(TAG, "save", e);
		} finally {
			try {
				dos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized String Get(String key, String def) {
		String v = getFromTrans(key);
		if (v != null) {
			return v;
		}
		v = (String) this.values.get(key);
		if (v == null) {
			return def;
		}
		return v;
	}

	public synchronized int getInt(String key, int def) {
		String v = getFromTrans(key);
		if (v == null) {
			v = (String) this.values.get(key);
		}
		if (v == null) {
			return def;
		}
		try {
			return Integer.parseInt(v);
		} catch (Exception e) {
			try {
				return (int) Double.parseDouble(key);
			} catch (Exception ee) {
			}
		}
		return def;
	}

	public synchronized long getLong(String key, long def) {
		String v = getFromTrans(key);
		if (v == null) {
			v = (String) this.values.get(key);
		}
		if (v == null) {
			return def;
		}
		try {
			return Long.parseLong(v);
		} catch (Exception e) {
			try {
				return Long.parseLong(key);
			} catch (Exception ee) {
			}
		}
		return def;
	}

	public synchronized String Get(String key) {
		String v = getFromTrans(key);
		if (v == null) {
			v = (String) this.values.get(key);
		}
		return v;
	}

	public synchronized void Set(String key, String value) {
		if (!setToTrans(key, value)) {
			if (TextUtils.isEmpty(value)) {
				value = "";
			}
			this.values.put(key, value);
		}
	}

	public synchronized void Delete(String key) {
		if (!deleteFromTrans(key)) {
			this.values.remove(key);
		}
	}

	private synchronized String readString(DataInputStream dis)
			throws IOException {
		int l = dis.readShort();
		byte[] a = new byte[l];
		dis.read(a);
		return new String(a);
	}

	private synchronized void writeString(DataOutputStream dos, String str)
			throws IOException {
		byte[] a = str.getBytes();
		dos.writeShort(a.length);
		dos.write(a);
	}

	public void release() {
		this.values.clear();
	}

	public synchronized boolean getBoolean(String key, boolean def) {
		String v = getFromTrans(key);
		if (v == null) {
			v = (String) this.values.get(key);
		}
		if (v == null) {
			return def;
		}
		try {
			return Boolean.parseBoolean(v);
		} catch (Exception e) {
		}
		return def;
	}

	public synchronized void SaveNow() {
		saveConfig();
	}

	public synchronized void Remove(String key) {
		Delete(key);
	}

	private static byte[] encrypt(byte[] src, byte[] pass) {
		byte[] res = new byte[src.length];
		for (int i = 0; i < src.length; i += pass.length) {
			for (int j = 0; (j < pass.length) && (i + j < src.length); j++) {
				res[(i + j)] = ((byte) (src[(i + j)] ^ pass[j]));
			}
		}
		return res;
	}

	private boolean deleteFromTrans(String key) {
		if (this.trans == null) {
			return false;
		}
		this.trans.remove(key);
		return true;
	}

	private boolean setToTrans(String key, String v) {
		if (this.trans == null) {
			return false;
		}
		if (TextUtils.isEmpty(v)) {
			v = "";
		}
		this.trans.put(key, v);
		return true;
	}

	private String getFromTrans(String key) {
		if (this.trans == null) {
			return null;
		}
		return (String) this.trans.get(key);
	}

	public synchronized void beginTrans() {
		this.trans = new ConcurrentHashMap<String, String>(this.values);
	}

	public synchronized void commitTrans() {
		if (this.trans == null) {
			return;
		}
		this.values.clear();
		for (Map.Entry<String, String> e : this.trans.entrySet()) {
			this.values.put(e.getKey(), e.getValue());
		}
		SaveNow();
		this.trans = null;
	}

	public synchronized void rollBackTrans() {
		this.trans.clear();
		this.trans = null;
	}

	public synchronized void clear() {
		if (this.trans != null) {
			this.trans.clear();
		}
		this.values.clear();
		SaveNow();
	}
}
