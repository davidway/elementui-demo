package com.blockchain.service.ethereum.vo;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;


@ApiModel(description="返回信息",value="申请用户返回的申请返回json")
public class EthereumWalletInfo {
	
	
	private String basePrivateKey;
	private String keyStore;
	private String assitWords;
	private String address;
	private String password;
	public String getAssitWords() {
		return assitWords;
	}
	public void setAssitWords(String assitWords) {
		this.assitWords = assitWords;
	}
	public String getBasePrivateKey() {
		return basePrivateKey;
	}
	public void setBasePrivateKey(String basePrivateKey) {
		this.basePrivateKey = basePrivateKey;
	}
	
	public String getKeyStore() {
		return keyStore;
	}
	public void setKeyStore(String keyStore) {
		this.keyStore = keyStore;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	

	
	

	
}
