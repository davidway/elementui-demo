package com.blockchain.vo;


import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;

public class PhpSystemJsonContentVO {
	Integer retcode;
	String retmsg;
	Object data = new Object();

	
	public PhpSystemJsonContentVO(){
		this.retcode=0;
		this.retmsg="成功";
		this.data=data;
	}
	
	
	public Integer getRetcode() {
		return retcode;
	}


	public void setRetcode(Integer retcode) {
		this.retcode = retcode;
	}


	public String getRetmsg() {
		return retmsg;
	}
	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}


	public PhpSystemJsonContentVO setKnownError(ServiceException e ) {
		this.retcode=e.getErrorCode();
		this.retmsg=e.getErrorMessage();
		this.data = e.getData();
		return this;
	}


	public PhpSystemJsonContentVO setSDKError() {
		this.retcode=StatusCode.SYSTEM_UNKOWN_ERROR;
		this.retmsg="SDK环境异常，请联系管理员";
	
		return this;
		
	}


	public PhpSystemJsonContentVO setParseJsonError() {
		this.retcode=StatusCode.SERVICE_EXCEPTION;;
		this.retmsg="json解析错误";
		
		return this;
	}


	public PhpSystemJsonContentVO setUnkownError(String message) {
		this.retcode=StatusCode.SYSTEM_UNKOWN_ERROR;
		this.retmsg="未知错误";
		
		return this;
	}


	@Override
	public String toString() {
		return "PhpSystemJsonContent [retcode=" + retcode + ", retmsg=" + retmsg + ", data=" + data + "]";
	}


	


	
	
	
}
