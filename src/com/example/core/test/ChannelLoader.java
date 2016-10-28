package com.example.core.test;

import java.util.List;

import com.baiyi.core.loader.LoaderResult;
import com.baiyi.core.loader.sql.BaseNormalDaoLoader;

public class ChannelLoader extends BaseNormalDaoLoader<ChannelDao>{

	private static final String OP_Channel_All = "op_channel_all";
	private static final String OP_Channel_Default = "op_channel_default";
	private static final String OP_Channel_Other = "op_channel_Other";
	
	private static final String OP_Channel_delete_id = "op_channel_delete_id";
	
	private String cid = null;

	public void setAllChannel()
	{
		setOpType(OP_Channel_All);
	}
	
	public LoaderResult getAllChannel()
	{
		LoaderResult result = new LoaderResult();
		result.setCode(1);
		result.setTag(getTag());
		result.setResult(getAllChannelList());
		return result;
	}
	
	public void setDeleteToCid(String cid)
	{
		this.cid = cid;
		setOpType(OP_Channel_delete_id);
	}
	
	public void deleteToId()
	{
		dao.deleteToId(cid);
	}
	
	public List<ChannelItem> getAllChannelList()
	{
		return dao.getAllChannelList();
	}

	public void setDefaultChannel()
	{
		setOpType(OP_Channel_Default);
	}
	
	public LoaderResult getDefaultChannelList()
	{
		LoaderResult result = new LoaderResult();
		result.setCode(1);
		result.setTag(getTag());
		result.setResult(dao.getDefaultChannelList());
		return result;
	}

	public void setOtherChannel()
	{
		setOpType(OP_Channel_Other);
	}
	
	public LoaderResult getOtherChannelList()
	{
		LoaderResult result = new LoaderResult();
		result.setCode(1);
		result.setTag(getTag());
		result.setResult(dao.getOtherChannelList());
		return result;
	}
	
	@Override
	protected LoaderResult onVisitor(String opType) {
		// TODO Auto-generated method stub
		LoaderResult result = null;
		if(opType.equals(OP_Channel_Default))
		{
			result = getDefaultChannelList();
		}else if(opType.equals(OP_Channel_Other))
		{
			result = getOtherChannelList();
		}else if(opType.equals(OP_Channel_All))
		{
			result = getAllChannel();
		}else if(opType.equals(OP_Channel_delete_id))
		{
			deleteToId();
		}
		return result;
	}

}
