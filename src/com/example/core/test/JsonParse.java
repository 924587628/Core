package com.example.core.test;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParse {
	
	
	public static boolean getState(JSONObject jsonObject) {
		int result = getIntNodeValue(jsonObject, "state");
		return result == 1 ? true : false;
	}
	
	public static int getIntState(JSONObject jsonObject) {
		return getIntNodeValue(jsonObject, "state");
	}

	public static JSONArray getMsgArray(JSONObject jsonObject) {
		JSONArray msg = null;
		try {
			msg = jsonObject.getJSONArray("msg");
		} catch (JSONException e) {
		}
		return msg;
	}

	public static JSONArray getResultArray(JSONObject jsonObject) {
		JSONArray msg = null;
		try {
			msg = jsonObject.getJSONArray("data");
		} catch (JSONException e) {
		}
		return msg;
	}

	public static JSONObject getResultObj(JSONObject jsonObject) {
		JSONObject msg = null;
		try {
			msg = jsonObject.getJSONObject("data");
		} catch (JSONException e) {
		}
		return msg;
	}
	
	public static int getIntNodeValue(JSONObject o, String name) {
		boolean isHas = o.has(name) && (!o.isNull(name));
		try {
			return isHas ? o.getInt(name) : 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static String getStringNodeValue(JSONObject o, String name) {
		boolean isHas = o.has(name) && (!o.isNull(name));
		try {
			return isHas ? o.getString(name) : "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	public static double getDoubleNodeValue(JSONObject o, String name) {
		boolean isHas = o.has(name) && (!o.isNull(name));
		try {
			return isHas ? o.getDouble(name) : 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static boolean getBooleanNodeValue(JSONObject o, String name) {
		boolean isHas = o.has(name) && (!o.isNull(name));
		try {
			return isHas ? o.getBoolean(name) : false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isHas(JSONObject o, String name) {
		boolean isHas = o.has(name) && (!o.isNull(name));
		return isHas;
	}
	
	public static List<ChannelItem> getChannelList(String data)
	{
		List<ChannelItem> dataList = new ArrayList<ChannelItem>();
		try {
			JSONArray array = new JSONArray(data);
			for(int i = 0 ; i < array.length(); i++)
			{
				ChannelItem item = new ChannelItem();
				JSONObject o = array.getJSONObject(i);
				item.setCid(getIntNodeValue(o, "cid"));
				item.setName(getStringNodeValue(o, "name"));
				item.setOrderId(getIntNodeValue(o, "orderId"));
				item.setSelected(getIntNodeValue(o, "selected"));
				dataList.add(item);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return dataList;
		
	}

}
