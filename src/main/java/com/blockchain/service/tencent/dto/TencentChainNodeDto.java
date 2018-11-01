package com.blockchain.service.tencent.dto;

public class TencentChainNodeDto {
	String nodeId;
	String mchId;
	long  timestamp;
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp =timestamp;
	}
	
}
