package com.example.core.test;

import com.baiyi.core.database.AbstractBaseModel;

/** 
 * ITEM的对应可序化队列属性
 *  */
public class ChannelItem extends AbstractBaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	/** 
	 * 栏目对应ID
	 *  */
	public Integer cid;
	/** 
	 * 栏目对应NAME
	 *  */
	public String name;
	/** 
	 * 栏目在整体中的排序顺序  rank
	 *  */
	public Integer orderId;
	/** 
	 * 栏目是否选中
	 *  */
	public Integer selected;

	public ChannelItem() {
	}

	public ChannelItem(int cid, String name, int orderId,int selected) {
		this.cid = Integer.valueOf(cid);
		this.name = name;
		this.orderId = Integer.valueOf(orderId);
		this.selected = Integer.valueOf(selected);
	}

	public int getCid() {
		return this.cid.intValue();
	}

	public String getName() {
		return this.name;
	}

	public int getOrderId() {
		return this.orderId.intValue();
	}

	public Integer getSelected() {
		return this.selected;
	}

	public void setCid(int paramInt) {
		this.cid = Integer.valueOf(paramInt);
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setOrderId(int paramInt) {
		this.orderId = Integer.valueOf(paramInt);
	}

	public void setSelected(Integer paramInteger) {
		this.selected = paramInteger;
	}

	public String toString() {
		return "ChannelItem [cid=" + this.cid + ", name=" + this.name
				+ ", selected=" + this.selected + "]";
	}
}