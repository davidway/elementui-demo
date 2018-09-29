package com.blockchain.service.ethereum.vo;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(description="兑换使用实体类",value="兑换返回json")
public class EthAssetSettleVo {
	@ApiModelProperty(   value = "交易号", dataType = "String")
	private String transactionId;
	@ApiModelProperty(   value = "兑换后的资产id列表", dataType = "String")
	private String srcAssetId;
	@ApiModelProperty(   value = "区块链交易Fhash值" , dataType = "String")

	private String smartContractAddress;
	private String gasUsed;
	
	private String tokenBalance;
	private String nonce;

	private String ethBalance;

	
	private String transHash;
	
	private String fileName;
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getSrcAssetId() {
		return srcAssetId;
	}
	public void setSrcAssetId(String srcAssetId) {
		this.srcAssetId = srcAssetId;
	}

	public String getTransHash() {
		return transHash;
	}
	public void setTransHash(String transHash) {
		this.transHash = transHash;
	}
	public String getSmartContractAddress() {
		return smartContractAddress;
	}
	public void setSmartContractAddress(String smartContractAddress) {
		this.smartContractAddress = smartContractAddress;
	}
	public String getGasUsed() {
		return gasUsed;
	}
	public void setGasUsed(String gasUsed) {
		this.gasUsed = gasUsed;
	}
	public String getTokenBalance() {
		return tokenBalance;
	}
	public void setTokenBalance(String tokenBalance) {
		this.tokenBalance = tokenBalance;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getEthBalance() {
		return ethBalance;
	}
	public void setEthBalance(String ethBalance) {
		this.ethBalance = ethBalance;
	}
	@Override
	public String toString() {
		return "AssetSettleDTO [transactionId=" + transactionId + ", srcAssetId=" + srcAssetId + ", smartContractAddress=" + smartContractAddress + ", gasUsed=" + gasUsed + ", tokenBalance="
				+ tokenBalance + ", nonce=" + nonce + ", ethBalance=" + ethBalance + ", transHash=" + transHash + "]";
	}
	
	
	
}
