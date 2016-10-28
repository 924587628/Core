package com.baiyi.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;

import android.database.Cursor;

public class DataTypeUtils {
	public static int str2int(String value, int defValue) {
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defValue;
		}
	}

	public static Set<String> toUppercaseSet(Set<String> values) {
		Set<String> result = new HashSet<String>();
		for (String s : values) {
			String v = s.toUpperCase();
			result.add(v);
		}
		return result;
	}

	public static Set<String> toLowercaseSet(Set<String> values) {
		Set<String> result = new HashSet<String>();
		for (String s : values) {
			String v = s.toLowerCase();
			result.add(v);
		}
		return result;
	}

	public static String null2string(String value, String defualt) {
		if (value == null) {
			return defualt;
		}
		return value;
	}

	public static boolean isEmpty(Object o) {
		if (o == null) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String[] value) {
		if (value == null) {
			return true;
		}
		if (value.length == 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String value) {
		if (value == null) {
			return true;
		}
		if (value.trim().equals("")) {
			return true;
		}
		return false;
	}

	public static boolean startsWith(String value, String head) {
		if (isEmpty(value)) {
			return false;
		}
		if (isEmpty(head)) {
			return false;
		}
		String tempValue = value.toLowerCase();
		String tempHead = head.toLowerCase();
		if (tempValue.indexOf(tempHead) == 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(Collection<?> datas) {
		if (datas == null) {
			return true;
		}
		if (datas.size() == 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(List<?> list) {
		if (list == null) {
			return true;
		}
		if (list.size() == 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmptyFavoriteCode() {
		return false;
	}

	public static boolean isEmpty(Object[] datas) {
		if (datas == null) {
			return true;
		}
		if (datas.length == 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(Cursor cursor) {
		if (cursor == null) {
			return true;
		}
		if (cursor.getCount() == 0) {
			return true;
		}
		return false;
	}

	public static String date2String(Date date) {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return time.format(new Date());
	}

	public static Date string2Date(String dateString, long defaultMilliseconds) {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return time.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return new Date(defaultMilliseconds);
		}
	}

	public static int getIntegerValue(Integer value, int defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static String list2String(List<? extends Object> lists, String glue) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < lists.size(); i++) {
			if (i != 0) {
				builder.append(",");
			}
			builder.append(lists.get(i));
		}
		return builder.toString();
	}

	public static String Vector2String(Vector<? extends Object> datas,
			String glue) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < datas.size(); i++) {
			if (i != 0) {
				builder.append(",");
			}
			builder.append(datas.get(i));
		}
		return builder.toString();
	}

	// 将带分隔的字符串，转换为整型list
	public static List<Integer> stringToIntList(String array, String glue) {
		if (DataTypeUtils.isEmpty(array)) {
			return null;
		}
		if (glue == null) {
			return null;
		}
		List<Integer> list = new ArrayList<Integer>();
		String[] str = array.split(Pattern.quote(glue));
		for (String s : str) {
			if (!DataTypeUtils.isEmpty(s)) {
				list.add(Integer.parseInt(s));
			}
		}
		return list;
	}

	// 将带分隔的字符串，转换为整型list
	public static Vector<Integer> stringToIntVector(String array, String glue) {
		if (DataTypeUtils.isEmpty(array)) {
			return null;
		}
		if (glue == null) {
			return null;
		}
		Vector<Integer> list = new Vector<Integer>();
		String[] str = array.split(Pattern.quote(glue));
		for (String s : str) {
			if (!DataTypeUtils.isEmpty(s)) {
				list.add(Integer.parseInt(s));
			}
		}
		return list;
	}

	public static List<String> stringToStringList(String array, String glue) {
		if (array == null) {
			return null;
		}
		if (glue == null) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		String[] str = array.split(Pattern.quote(glue));
		for (String s : str) {
			list.add(s);
		}
		return list;
	}

	public static int getValueIndexInArray(String[] array, String value) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equalsIgnoreCase(value)) {
				return i;
			}
		}
		return -1;
	}

	public static String getTimeHHMM(Long time) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		Date data = new Date(time);
		return timeFormat.format(data);
	}

	public static String getTimeMMDD(Long time) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("MM-dd");
		Date data = new Date(time);
		return timeFormat.format(data);
	}

	public static String getTimeYMDHHMM(Long time) {
		Date date = new Date(time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(date);
	}

	public static String getTimeYMD(Long time) {
		Date date = new Date(time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	public static Long parseHexStringToLong(String value) {
		value = value.toUpperCase();
		if (value.length() < 16) {
			return Long.parseLong(value, 16);
		}
		char c = value.charAt(0);
		String remian = value.substring(1, value.length());
		if (c > '7') {
			if (c <= '9') {
				c -= 8;
			} else {
				c -= 15;
			}
			return Long.parseLong("-" + c + remian, 16);
		} else {
			return Long.parseLong(value, 16);
		}

	}

	public static boolean isSevenDay(long oldTime) {
		long currentTime = System.currentTimeMillis() / 1000;
		long differenceTime = currentTime - oldTime;
		long differenceDay = differenceTime / 86400;
		if (differenceDay > 7) {
			return true;
		}
		return false;
	}
}
