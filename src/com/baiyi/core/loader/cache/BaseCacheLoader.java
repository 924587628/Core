package com.baiyi.core.loader.cache;

import com.baiyi.core.cache.Cache;
import com.baiyi.core.loader.BaseLoader;
import com.baiyi.core.loader.Loader;
import com.baiyi.core.loader.LoaderResult;

public abstract class BaseCacheLoader  extends BaseLoader implements Loader{
	private final static String OP_READ = "read";
	private final static String OP_WRITE = "write";
	private final static String OP_DELETE = "delete";
	private final static String OP_DELETE_ALL = "deleteAll";
	private final static String OP_UPDATE	=	"update";
	private final static String OP_NOTIFY	=	"notify";
	private String opType = null;
	private Cache opCache = null;
	private byte[] saveData = null;
	
	protected abstract Object onVisitor(String opType);
	protected abstract Object convert(Object data);
	
	public BaseCacheLoader(Cache cache){
		this.opCache = cache;
	}
	
	public void setRead(String key){
		this.opType = OP_READ;
		setTag(key);
	}
	public void setWrite(String key,byte[] saveData){
		this.saveData = saveData;
		this.opType = OP_WRITE;
		setTag(key);
	}
	public void setUpdate(String key,byte[] saveData){
		this.saveData = saveData;
		this.opType = OP_UPDATE;
		setTag(key);
	}
	public void setDelete(String key){
		setTag(key);
		this.opType = OP_DELETE;
	}
	public void setDeleteAll(){
		this.opType = OP_DELETE_ALL;
	}
	public void setNotifyDataChanged(){
		this.opType = OP_NOTIFY;
	}
	private Object read() {
		return opCache.get(getTag().toString());
	}
	private void write() {
		if(saveData!=null){
			opCache.put(getTag().toString(), saveData);
		}
	}
	private void delete() {
		opCache.remove(getTag().toString());
	}
	private void deleteAll() {
		opCache.clear();
	}
	private void update(){
		if(saveData != null)
		{
			opCache.update(getTag().toString(), saveData);
		}
	}
	private void notifyDataChanged(){
		opCache.notifyDataChanged();
	}
	@Override
	protected LoaderResult onVisitor() {
		if(opType==null){
			return sendErrorMsg(0, "没有设置操作");
		}
		Object data = null;
		if(opType.equals(OP_READ)){
			data = read();
		}else if(opType.equals(OP_WRITE)){
			write();
		}else if(opType.equals(OP_DELETE)){
			delete();
		}else if(opType.equals(OP_DELETE_ALL)){
			deleteAll();
		}else if(opType.equals(OP_UPDATE)){
			update();
		}else if(opType.equals(OP_NOTIFY)){
			notifyDataChanged();
		}else{
			data = onVisitor(opType);
		}
		if(data!=null){
			Object o = convert(data);
			setResult(o);
		}
		return sendCompleteMsg();
	}
}
