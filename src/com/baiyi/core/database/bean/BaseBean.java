package com.baiyi.core.database.bean;


public abstract class BaseBean implements Bean{
	private String name = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getSimpleName() {
		if(name==null){
			return null;
		}
		int pos = name.lastIndexOf(".");
		if(pos==-1){
			return name;
		}
		return name.substring(pos+1);
	}
	
}
