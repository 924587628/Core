/**
 * 
 */
package com.example.core.test.task;

import java.util.ArrayList;
import java.util.List;

import com.baiyi.core.loader.Loader;
import com.baiyi.core.util.CoreLog;
import com.baiyi.core.util.DataTypeUtils;

/**
 * 防止重复切换频道
 * 只处理最后一次切换频道
 * @author tangkun
 *
 */
public class NewsLoaderManager {
	
	public NewsLoaderManager()
	{
		
	}
	
	private List<Task> tasks = new ArrayList<Task>();
	
	public void addTask(Task task)
	{
		cannelTask(task);
		tasks.add(task);
		for(int i = 0; i < tasks.size();i++)
		{
			CoreLog.e("NewsLoaderManager", "add : " + tasks.size() + "== " + tasks.get(i).getTag().toString());
		}
	}
	
	public void cannelTask(Task task)
	{
		if(DataTypeUtils.isEmpty(task))
		{
			return;
		}
		Loader imgLoader = task.getImgLoader();
		if(imgLoader == null)
		{
			return;
		}
		imgLoader.cancel();
		CoreLog.e("NewsLoaderManager", "remove : " + tasks.size() + "== " + task.getTag().toString());
		tasks.remove(0);
	}
	
//	public boolean isHasTask()
//	{
//		if(Utils.isStringEmpty(tasks))
//		{
//			return false;
//		}
//		return true;
//	}
	
	public void clear()
	{
		if(!DataTypeUtils.isEmpty(tasks))
		{
			tasks.clear();
		}
		tasks = null;
	}

}
