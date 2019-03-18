package com.blockchain.exception;

import com.alibaba.fastjson.JSONObject;

public class ThreadException extends Exception {
	private Object data;
	private Integer errorCode;

	public ThreadException(String s, Integer errorCode,Object object) {
		super(s);
		this.data = object;
		this.errorCode = errorCode;
		
	}
	public ThreadException(String s,Integer errorCode) {
		super(s);
		this.errorCode = errorCode;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}

	
}
