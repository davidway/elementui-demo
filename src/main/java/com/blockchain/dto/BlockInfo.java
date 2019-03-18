package com.blockchain.dto;

public class BlockInfo {
	Integer height;
	Long timeStamp;
	String transHash;
	Integer transTotalNum;
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public Long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getTransHash() {
		return transHash;
	}
	public void setTransHash(String transHash) {
		this.transHash = transHash;
	}
	public Integer getTransTotalNum() {
		return transTotalNum;
	}
	public void setTransTotalNum(Integer transTotalNum) {
		this.transTotalNum = transTotalNum;
	}
	@Override
	public String toString() {
		return "BlockInfo [height=" + height + ", timeStamp=" + timeStamp + ", transHash=" + transHash + ", transTotalNum=" + transTotalNum + "]";
	}
	
}
