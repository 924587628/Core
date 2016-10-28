package com.baiyi.core.loader;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
/**
 * 
 * @author tangkun
 *
 */
public abstract class BaseLoader implements Loader{
//	private static final String TAGL = BaseLoader.class.getSimpleName();
	private final static int MSG_LOAD_COMPLETE = 1;
	private final static int MSG_LOAD_PROCESS = 2;
	private final static int MSG_LOAD_ERROR = 3;
	public final static int RESULT_USER_ERROR_CODE 	=      -10000;
	
	public final static int Result_Code_Cannel = -10;
	public final static int Reuslt_Code_ReadTimeOut = -11;
	public final static int Result_Code_NetTimeOut = -12;
	public final static int Result_Code_NotNet = -13;
	
	private Object tag = null;
	private Object result = null;
	private boolean isError = false;
	private boolean isCancel = false;
	private Loader.LoaderListener mLoaderListener = null;
	private Handler handler = null;
	public BaseLoader() {
		try{
			handler = new LoaderHandler();
		}catch (Exception e) {
			handler = null;
		}
	}
	void initHandler(){
		if(handler==null){
			handler = new LoaderHandler();
		}
	}
	@Override
	public LoaderResult start() {
		reset();
		return load();
	}
	private void reset(){
		isError=false;
		isCancel=false;
	}
	protected void setResult(Object result){
		this.result = result;
	}
	/**
	 * 访问地址
	 */
	public Object getTag(){
		return tag;
	}
	/**
	 * 服务器相应 结果
	 * @return
	 */
	protected  LoaderResult load(){
		try{
			LoaderResult result =  onVisitor();
			if(result.getCode()<0){
				return sendErrorMsg(-1, result.getMessage());
			}
			return result;
		}catch (Exception e) {
			e.printStackTrace();
//			ExceptionMessageTrans trans = new ExceptionMessageTrans();
//			String message =  trans.Exception2String(e);
//			if(message!=null){
//				return sendErrorMsg(0, message);
//			}else{
				return sendErrorMsg(0, e.getMessage());
//			}
		}
	}
	/**
	 * 返回网络包
	 * @return
	 * @throws NoSDCardException
	 * @throws NoSDSpaceException
	 * @throws NoNetConnectionException
	 */
	protected abstract LoaderResult onVisitor();
	@Override
	public void cancel() {
		isCancel = true;
		destroy();
	}

	@Override
	public void error(String msg) {
		sendErrorMsg(2, msg);
	}

	@Override
	public boolean isCancel() {
		return isCancel;
	}

	@Override
	public void setTag(Object tag) {
		this.tag = tag;
	}
	
	@Override
	public void setLoaderListener(LoaderListener listner) {
		this.mLoaderListener = listner;
		
	}
	protected final LoaderResult sendCompleteMsg(){
    	if(isError) return null;
    	LoaderResult visitResult = new LoaderResult();
    	visitResult.setCode(1);
    	visitResult.setResult(result);
    	visitResult.setTag(tag);
    	if(mLoaderListener==null) return visitResult;
    	if(handler!=null){
    		Message msg = Message.obtain();
    		msg.what=MSG_LOAD_COMPLETE;
    		handler.sendMessage(msg);
    	}else{
    		onLoadComplete();
    	}
    	return visitResult;
    }
    protected final void sendProcessMsg(long curByteNum, long totalByteNum){
    	if(isError) return;
    	if(mLoaderListener==null) return;
    	if(handler!=null){
	    Message msg = Message.obtain();
			msg.what=MSG_LOAD_PROCESS;
			Bundle bundle = new Bundle();
			bundle.putLong("curByteNum", curByteNum);
			bundle.putLong("totalByteNum", totalByteNum);
			msg.setData(bundle);
			handler.sendMessage(msg);
    	}else{
    		onLoadProcess(curByteNum, totalByteNum);
    	}
    }
    protected final LoaderResult sendErrorMsg(int code, String errorMessage){
    	isError=true;
    	LoaderResult result = new LoaderResult();
    	result.setCode(code);
    	result.setMessage(errorMessage);
    	result.setTag(tag);
    	if(mLoaderListener==null){
    		return result;
    	}
    	if(handler!=null){
    		Message msg = Message.obtain();
    		msg.what=MSG_LOAD_ERROR;
    		Bundle bundle = new Bundle();
    		bundle.putInt("code", code);
    		bundle.putString("errorMessage", errorMessage);
    		msg.setData(bundle);
    		handler.sendMessage(msg);
    	}else{
    		onLoadError(code, errorMessage);
    	}
    	return result;
    }
	
	
	private void destroy(){
		mLoaderListener = null;
		tag = null;
		result = null;
	}
	private void onLoadComplete(){
		if (mLoaderListener != null) {
			mLoaderListener.onCompelete(tag, result);
		}

		destroy();
	}
	private void onLoadProcess(long curByteNum,long totalByteNum){
		if(mLoaderListener != null) {	
			mLoaderListener.onProgress(tag, curByteNum, totalByteNum);
		}
	}
	private void onLoadError(int code,String msg){
		if (mLoaderListener != null) {
			mLoaderListener.onError(tag, code, msg);
		}
		destroy();
	}
	@SuppressLint("HandlerLeak")
	private class LoaderHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			switch (msg.what) {
			case MSG_LOAD_COMPLETE:
				onLoadComplete();
				break;
			case MSG_LOAD_PROCESS:
				long curByteNum = bundle.getLong("curByteNum");
				long totalByteNum = bundle.getLong("totalByteNum");
				onLoadProcess(curByteNum,totalByteNum);
				break;
			case MSG_LOAD_ERROR:
				int code = bundle.getInt("code");
				String errorMessage = bundle.getString("errorMessage");
				onLoadError(code, errorMessage);
				break;
			}
		}
	}
}
