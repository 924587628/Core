package com.baiyi.core.cache;

import com.baiyi.core.util.MD5;


public class MD5KeyMaker implements KeyMaker {
	@Override
	public String makeKey(String input) {
		return MD5.getMD5(input.trim());
	}

}
