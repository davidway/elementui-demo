package com.blockchain.dto;

import  com.blockchain.exception.StatusCode;

public enum CrmResultSet {
	SUCCESS(StatusCode.SUCCESS,StatusCode.SUCCESS_MESSAGE),NOT_AUTHORITY(StatusCode.AUTHORITY_ERROR,StatusCode.AUTHORITY_ERROR_MESSAGE),TIME_OUT_ERROR(StatusCode.TIME_OUT,StatusCode.TIME_OUT_MESSAGE),ERROR(StatusCode.SYSTEM_UNKOWN_ERROR,StatusCode.SYSTEM_UNKOWN_ERROR_MESSAGE),URL_NOT_EXISTS(StatusCode.URL_NOT_EXISTS,StatusCode.URL_NOT_EXISTS_MESSAGE);
	Integer status;
	String message;
	
	
	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}




	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	CrmResultSet(Integer status,String message){
			this.message = message;
			this.status = status;
	}
	
}
