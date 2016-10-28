package com.baiyi.core.util;

import android.util.Log;

public class CoreLog {
	
	public static int level = Log.VERBOSE;
	
	public static final String TAG_NAME_TIMETEST = "TimeTest";
	public static final String TAG_NAME_SCERROR = "SCERROR";
	public static final String TAG_NAME_PARSER = "PARSER";
	public static final String TAG_NAME_REQUEST = "REQUEST";
	public static final String TAG_NAME_DEFAULT = "DEFAULT";

	public static int d(String msg){
		return d(TAG_NAME_DEFAULT,msg);
	}
	public static int i(String msg){
		return i(TAG_NAME_DEFAULT,msg);
	}
	public static int e(String msg){
		return e(TAG_NAME_DEFAULT,msg);
	}
	public static int e(String msg,Throwable tr){
		return e(TAG_NAME_DEFAULT,msg,tr);
	}
	public static int error_d(String msg){
		return d(TAG_NAME_SCERROR,msg);
	}
	public static int error_i(String msg){
		return i(TAG_NAME_SCERROR,msg);
	}
	public static int error_e(String msg){
		return e(TAG_NAME_SCERROR,msg);
	}
	public static int error_e(String msg,Throwable tr){
		return e(TAG_NAME_SCERROR,msg,tr);
	}
	public static int parser_d(String msg){
		return d(TAG_NAME_PARSER,msg);
	}
	public static int parser_i(String msg){
		return i(TAG_NAME_PARSER,msg);
	}
	public static int parser_e(String msg){
		return e(TAG_NAME_PARSER,msg);
	}
	public static int parser_e(String msg,Throwable tr){
		return e(TAG_NAME_PARSER,msg,tr);
	}
	public static int request_d(String msg){
		return d(TAG_NAME_REQUEST,msg);
	}
	public static int request_i(String msg){
		return i(TAG_NAME_REQUEST,msg);
	}
	public static int request_e(String msg){
		return e(TAG_NAME_REQUEST,msg);
	}
	public static int request_e(String msg,Throwable tr){
		return e(TAG_NAME_REQUEST,msg,tr);
	}
	public static int time_d(String msg){
		return d(TAG_NAME_TIMETEST,msg);
	}
	public static int time_i(String msg){
		return i(TAG_NAME_TIMETEST,msg);
	}
	public static int time_e(String msg){
		return e(TAG_NAME_TIMETEST,msg);
	}
	public static int time_e(String msg,Throwable tr){
		return e(TAG_NAME_TIMETEST,msg,tr);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public static int v(String tag, String msg) {
		if(level<=Log.VERBOSE){
			return Log.v(tag, msg);
		}else{
			return 0;
		}
    }
	public static int d(String tag, String msg) {
		if(level<=Log.DEBUG){
			return Log.d(tag, msg);
		}else{
			return 0;
		}
    }
	public static int i(String tag, String msg) {
		if(level<=Log.INFO){
			return Log.i(tag, msg);
		}else{
			return 0;
		}
    }
	public static int w(String tag, String msg) {
		if(level<=Log.WARN){
			return Log.w(tag, msg);
		}else{
			return 0;
		}
    }
	public static int e(String tag, String msg) {
		if(level<=Log.ERROR){
			return Log.e(tag, msg);
		}else{
			return 0;
		}
    }
	
	public static int e(String tag, String msg, Throwable tr) {
		if(level<=Log.ERROR){
			return Log.e(tag, msg, tr);
		}else{
			return 0;
		}
    }
}
