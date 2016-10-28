package com.baiyi.core.loader;

import org.json.JSONArray;

import android.content.Context;

import com.baiyi.core.loader.net.BaseNetLoder;
import com.baiyi.core.util.CoreLog;

public class JsonLoader extends BaseNetLoder{
	private static final String TAG = JsonLoader.class.getName();
	public JsonLoader(Context context) {
		super(context);
	}
	private boolean isJsonStart(String value){
		if(value.substring(0, 1).equals("{")){
			return true;
		}
		if(value.substring(0, 1).equals("[")){
			return true;
		}
		return false;
	}

	@Override
	protected Object covert(byte[] bytes) {
		// TODO Auto-generated method stub
		JSONArray jsonArray=null;
		try{
			String data=new String(bytes,"utf-8");
			CoreLog.d(TAG, data);
			if(!isJsonStart(data))
			{
				return data;
			}
			while(!isJsonStart(data)){
				data = data.substring(1);
			}
			if(!data.substring(0, 1).equals("[")){ 
				data="["+data+"]";
			}
			jsonArray = new JSONArray(data);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return jsonArray;
	}

}
