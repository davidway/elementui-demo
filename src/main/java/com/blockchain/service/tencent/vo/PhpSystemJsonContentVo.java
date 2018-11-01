package com.blockchain.service.tencent.vo;


import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;

public class PhpSystemJsonContentVo {
	Integer retcode;
	String retmsg;
	Object data = new Object();

	
	public PhpSystemJsonContentVo(){
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


	public PhpSystemJsonContentVo setKnownError(ServiceException e ) {
		this.retcode=e.getErrorCode();
		this.retmsg=e.consistsReturnString();
		this.data = e.getData();
		return this;
	}


	public PhpSystemJsonContentVo setSDKError() {
		this.retcode=StatusCode.SYSTEM_UNKOWN_ERROR;
		this.retmsg="SDK环境异常，请联系管理员";
	
		return this;
		
	}


	public PhpSystemJsonContentVo setParseJsonError() {
		this.retcode=StatusCode.SERVICE_EXCEPTION;;
		this.retmsg="json解析错误";
		
		return this;
	}


	public PhpSystemJsonContentVo setUnkownError(String message) {
		this.retcode=StatusCode.SYSTEM_UNKOWN_ERROR;
		this.retmsg=message;
		
		return this;
	}


	@Override
	public String toString() {
		return "PhpSystemJsonContent [retcode=" + retcode + ", retmsg=" + retmsg + ", data=" + data + "]";
	}


	public PhpSystemJsonContentVo setNoSupportError() {
		this.retcode=StatusCode.METHOD_NO_SUPPORT;
		this.retmsg=StatusCode.METHOD_NO_SUPPORT_MESSAGE;
			
		return this;
	}


	


	
	
	
}
