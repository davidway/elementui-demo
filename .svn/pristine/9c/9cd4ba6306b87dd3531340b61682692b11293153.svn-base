package com.blockchain.exception;

public class ServiceException extends Exception {

	private Integer errorCode;
	private String errorMessage;
	private Object data;
	private String pos;

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public ServiceException errorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		return this;
	}

	public ServiceException data(Object data) {
		this.data = data;
		return this;
	}

	public ServiceException errorCode(int errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public ServiceException pos(String pos) {
		this.pos = pos;
		return this;
	}

	public String consistsReturnString() {
		StringBuffer sb = new StringBuffer();
		sb.append("发生位置：");
		sb.append(this.pos);
		sb.append(",错误信息：");
		sb.append(this.errorMessage);
		return sb.toString();
	}

}
