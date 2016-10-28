package com.baiyi.core.database;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import com.baiyi.core.database.op.TableColums;

public abstract class AbstractBaseModel implements Serializable {

	private static final long serialVersionUID = -4685989463430616599L;
//	private static String TAG = AbstractBaseModel.class.getSimpleName();
	private static Map<String, String> TYPES = new HashMap<String, String>();
	private String id; // p key
	static {
		TYPES.put("date", "TEXT");
		TYPES.put("string", "TEXT");
		TYPES.put("integer", "INTEGER");
		TYPES.put("int", "INTEGER");
		TYPES.put("short", "INTEGER");
		TYPES.put("long", "INTEGER");
		TYPES.put("float", "REAL");
		TYPES.put("double", "REAL");
	}

	public AbstractBaseModel() {

//		TAG = this.getClass().getSimpleName();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<String> toFieldSet() {
		return this.toFieldMap().keySet();
	}

	public Map<String, String> toFieldMap() {
		Map<String, String> fieldMap = new HashMap<String, String>();
		Method[] methods = this.getClass().getMethods();
		try {
			String propertyName;
			String typeString;
			for (Method method : methods) {
				String methodName = method.getName();
				if (!methodName.startsWith("get")
						|| methodName.equalsIgnoreCase("getClass")
						|| methodName.equalsIgnoreCase("get"))
					continue;
				typeString = method.getReturnType().getSimpleName();
				propertyName = methodName.substring(3);

				fieldMap.put(propertyName, typeString);
			}
		} catch (Exception e) {
//			TLog.e(TAG, e.getMessage());
		}
		return fieldMap;
	}

	@SuppressLint("DefaultLocale")
	public TableColums[] toTableColums() {
		ArrayList<TableColums> result = new ArrayList<TableColums>();
		Map<String, String> fieldMap = toFieldMap();
		Iterator<String> it = fieldMap.keySet().iterator();
		while (it.hasNext()) {
			TableColums column = new TableColums();
			String name = it.next();
			column.setName(name.toUpperCase());
			String type = fieldMap.get(name).toLowerCase();
			type = (TYPES.containsKey(type) ? TYPES.get(type) : "NONE");
			column.setType(type);
			result.add(column);
		}
		TableColums[] colums = new TableColums[result.size()];
		for (int i = 0; i < result.size(); i++) {
			colums[i] = result.get(i);
		}
		return colums;
	}

	public String toJSONString() {
		return this.toJSON().toString();
	}

	public JSONObject toJSON() {
		Method[] methods = this.getClass().getMethods();
		JSONObject json = new JSONObject();
		Object propertyValue;
		String propertyName;
		try {
			for (Method method : methods) {
				String methodName = method.getName();
				if (!methodName.startsWith("get")
						|| methodName.equalsIgnoreCase("getClass")
						|| methodName.equalsIgnoreCase("get"))
					continue;
				propertyValue = method.invoke(this, new Object[] {});
				propertyName = methodName.substring(3);
				propertyName = Character.toLowerCase(propertyName.charAt(0))
						+ propertyName.substring(1);
				json.put(propertyName, propertyValue);
			}
		} catch (Exception e) {
//			TLog.e(TAG, e.getMessage());
		}
		return json;
	}

	public JSONObject toUpperNameJSON() {
		Method[] methods = this.getClass().getMethods();
		JSONObject json = new JSONObject();
		Object propertyValue;
		String propertyName;
		try {
			for (Method method : methods) {
				String methodName = method.getName();
				if (!methodName.startsWith("get")
						|| methodName.equalsIgnoreCase("getClass")
						|| methodName.equalsIgnoreCase("get"))
					continue;
				propertyValue = method.invoke(this, new Object[] {});
				propertyName = methodName.substring(3);

				// propertyName = propertyName.toUpperCase();
				json.put(propertyName, propertyValue);
			}
		} catch (Exception e) {
//			TLog.e(TAG, e.getMessage());
		}
		return json;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof AbstractBaseModel)) {
			return false;
		}
		AbstractBaseModel model = ((AbstractBaseModel) o);
		if (model.getId() == null) {
			return false;
		}
		if (this.getId().equals(model.getId())) {
			return true;
		}
		return false;
	}

}