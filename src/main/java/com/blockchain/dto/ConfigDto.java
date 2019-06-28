package com.blockchain.dto;

import org.hibernate.validator.constraints.NotEmpty;




public class ConfigDto  {
	
	@NotEmpty(message="createUserPrivateKey不能为空")
	private String createUserPrivateKey;
	@NotEmpty(message="createUserPublicKey不能为空")
	private String createUserPublicKey;
	@NotEmpty(message="chainId不能为空")
	private String chainId;
	@NotEmpty(message="mchId不能为空")
	
	private String mchId;
	@NotEmpty(message="host不能为空")
	private String host;
	
	@NotEmpty(message="nodeId不能为空")
	private String nodeId;
	
	
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getCreateUserPrivateKey() {
		return createUserPrivateKey;
	}
	public void setCreateUserPrivateKey(String createUserPrivateKey) {
		this.createUserPrivateKey = createUserPrivateKey;
	}
	public String getCreateUserPublicKey() {
		return createUserPublicKey;
	}
	public void setCreateUserPublicKey(String createUserPublicKey) {
		this.createUserPublicKey = createUserPublicKey;
	}
	public String getChainId() {
		return chainId;
	}
	public void setChainId(String chainId) {
		this.chainId = chainId;
	}
	
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}

	
	
}
