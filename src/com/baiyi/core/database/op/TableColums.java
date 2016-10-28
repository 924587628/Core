package com.baiyi.core.database.op;

public class TableColums {
	private String name = null;
	private String type = null;
	private String value = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name).append(" ").append(type);
		if (value != null) {
			builder.append(" ").append("default ").append(value);
		}
		return builder.toString();
	}
}
