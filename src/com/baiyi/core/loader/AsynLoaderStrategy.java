package com.baiyi.core.loader;

import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * 异步访问策略
 * @author tangkun
 *
 */
public class AsynLoaderStrategy implements LoaderStrategy{
	private Integer threadNum = null;
	private Queue<Loader> taskQueue = new LinkedBlockingQueue<Loader>();
	private ConcurrentHashMap<Integer, VistorTask> threadPool 
					= new ConcurrentHashMap<Integer, AsynLoaderStrategy.VistorTask>();
	private String tagName;
	private boolean isDestroyed = false;
	private int taskLevel = 3;
	public AsynLoaderStrategy(Integer threadNum,String tagName,int taskLevel) {
		this.threadNum = threadNum;
		this.tagName = tagName;
		if(this.threadNum!=null){
			if(this.threadNum<1){
				this.threadNum = 1;
			}
		}
		if((this.threadNum!=null)||(!this.threadNum.equals(Integer.MAX_VALUE))){
			for(int i=0;i<threadNum;i++){
				VistorTask task = new VistorTask(i);
				threadPool.put(i, task);
				task.start();
			}
		}
	}
	public boolean isDestroy(){
		return isDestroyed;
	}
	@Override
	public LoaderResult startLoader(Loader loader) {
		if(loader instanceof BaseLoader){
			((BaseLoader)loader).initHandler();
		}
		if((threadNum==null)||(threadNum.equals(Integer.MAX_VALUE))){
			loader.start();
			return null;
		}
		for(Entry<Integer, VistorTask> entry:threadPool.entrySet()){
			VistorTask task = entry.getValue();
			if(task.getVisitor()==null){
				task.setVisitor(loader);
				synchronized(task){
					task.notify();
				}
				return null;
			}
		}
		taskQueue.add(loader);
		return null;
	}

	@Override
	public void clear() {
		taskQueue.clear();
		for(Entry<Integer, VistorTask> entry:threadPool.entrySet()){
			VistorTask task = entry.getValue();
			Loader visitor = task.getVisitor();
			if(visitor!=null){
				visitor.cancel();
			}
		}
	}
	@Override
	public void destroy() {
		clear();
		isDestroyed = true;
		for(Entry<Integer, VistorTask> entry:threadPool.entrySet()){
			VistorTask task = entry.getValue();
//			Visitor visitor = task.getVisitor();
//			if(visitor==null){
				synchronized(task){
					task.notify();
				}
//			}
		}
	}
	private synchronized void taskComplete(int id){
		try{
			if(isDestroyed){
				return;
			}
			Loader visitor = taskQueue.poll();
			if(visitor==null){
				return;
			}
			VistorTask thread =  threadPool.get(id);
			if(thread!=null){
				thread.setVisitor(visitor);
				//应该不需要notify
			}
		}catch (Exception e) {
		}
	}
	private class VistorTask extends Thread{  //线程池
		private Loader visitor = null;
		private int id;
		public VistorTask(int id) {
			this.id = id;
			this.setPriority(taskLevel);//线程优先级
			this.setName(tagName+":VisitorTask"+id+AsynLoaderStrategy.this);
		}
		public Loader getVisitor() {
			return visitor;
		}
		public void setVisitor(Loader visitor) {
			this.visitor = visitor;
		}
		@Override
		public void run() {
			while(!isDestroyed){
				try{
					if(visitor!=null){
						visitor.start();
						visitor = null;
						taskComplete(id);
					}else{
						synchronized(this){
							wait();
						}	
					}
				}catch(Exception e){
					if(visitor!=null){
						visitor.error("执行异常");
						visitor = null;
						taskComplete(id);
					}
				}
			}
		}
		
	}
	@Override
	public void stopLoader(Object tag) {
		Loader stopVisitor = null;
		for(Loader visitor:taskQueue){
			Object t = visitor.getTag();
			if(t==null){
				continue;
			}
			if(visitor.getTag().equals(tag)){
				stopVisitor = visitor;
				break;
			}
		}
		if(stopVisitor!=null){
			taskQueue.remove(stopVisitor);
			stopVisitor.cancel();
			return;
		}
		for(Entry<Integer, VistorTask> entry:threadPool.entrySet()){
			VistorTask task = entry.getValue();
			Loader visitor = task.getVisitor();
			if(visitor!=null){
				Object t = visitor.getTag();
				if(t==null){
					continue;
				}
				if(visitor.getTag().equals(tag)){
					visitor.cancel();
					return;
				}
			}
		}
	}
	@Override
	public boolean isExists(Object tag) {
		for(Loader visitor:taskQueue){
			if(visitor.getTag().equals(tag)){
				return true;
			}
		}
		for(Entry<Integer, VistorTask> entry:threadPool.entrySet()){
			VistorTask task = entry.getValue();
			Loader visitor = task.getVisitor();
			if(visitor!=null){
				if(visitor.getTag().equals(tag)){
					return true;
				}
			}
		}
		return false;
	}
}
