/**
 * 
 */
package com.example.core.test;

import android.os.CountDownTimer;

/**
 * 倒计时
 * @author wangf
 *
 */
public class TimerCountCallBack extends CountDownTimer {
	private CallBack callBack;
	
	//参数为总时长和计时的时间间隔，单位为毫秒
	public TimerCountCallBack(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
	}

	public void setCallBack(CallBack callBack){
		if(callBack!=null){
			this.callBack=callBack;
		}
	}
	
	//计时过程显示
	@Override
	public void onTick(long millisUntilFinished) {
		callBack.callBackOnTick(millisUntilFinished);
	}

	//计时完毕时触发
	@Override
	public void onFinish() {
		callBack.callBackOnFinish();
	}
	
	
	public interface CallBack
	{
		public void callBackOnTick(long millisUntilFinished);
		public void callBackOnFinish();
	}

}

