package com.blockchain.exception;

import com.alibaba.fastjson.JSON;

public class ErrorMessage {
	private Integer retCd; // 异常对应的返回码
	private String retPos;//异常的位置
	private String msgDes; // 异常对应的描述信息
	private String data;
	
	public ErrorMessage() {
		super();
	}
	public ErrorMessage(Integer retCd, String retPos, String msgDes) {
		super();
		this.retCd = retCd;
		this.retPos = retPos;
		this.msgDes = msgDes;
	}
	public Integer getRetCd() {
		return retCd;
	}
	public void setRetCd(Integer retCd) {
		this.retCd = retCd;
	}
	public String getRetPos() {
		return retPos;
	}
	public void setRetPos(String retPos) {
		this.retPos = retPos;
	}
	public String getMsgDes() {
		return msgDes;
	}
	public void setMsgDes(String msgDes) {
		this.msgDes = msgDes;
	}
	@Override
	public String toString() {
		return "ErrorMessage [retCd=" + retCd + ", retPos=" + retPos + ", msgDes=" + msgDes + "]";
	}
	public String toJsonString(){
		return "错误位置："+retPos+"，错误代码："+retCd+"，错误信息："+msgDes;
	}
	
}
