package com.example.core.test;

import java.util.List;

import com.baiyi.core.database.dao.AbstractNormalDao;

public class ChannelDao extends AbstractNormalDao<ChannelItem>{
	
	public List<ChannelItem> getDefaultChannelList()
	{
		String selection = "WHERE SELECTED=?";
		String order = "order by ORDERID asc";
		return getList(selection, new String[]{"1"}, order);
	}
	
	public List<ChannelItem> getOtherChannelList()
	{
		String selection = "WHERE SELECTED=?";
		String order = "order by ORDERID asc";
		return getList(selection, new String[]{"0"}, order);
	}
	
	public List<ChannelItem> getAllChannelList()
	{
		String order = "order by ORDERID asc";
		return getList(null, null, order);
	}
	
	public void deleteToId(String cid)
	{
		delete("WHERE CID=?", new String[] {cid});
	}

}
