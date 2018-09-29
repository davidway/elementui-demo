package com.blockchain.service.ethereum.dto;

import java.math.BigInteger;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="发行返回json",description="发行提交实体")
public class EthAssetIssueDto {
	@ApiModelProperty( value = "交易流水号Id")
	private String transactionId;
	@ApiModelProperty( value = "发行资产后的账户资产id")
	private String assetId;
	@ApiModelProperty( value = "区块链交易Fhash值")
	private String transHash;
	
	private String smartContractAddress;
	private String gasUsed;
	
	private String tokenBalance;
	private String nonce;

	private String ethBalance;
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getTransHash() {
		return transHash;
	}

	public void setTransHash(String transHash) {
		this.transHash = transHash;
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

	public String getEthBalance() {
		return ethBalance;
	}

	public void setEthBalance(String ethBalance) {
		this.ethBalance = ethBalance;
	}

	public String getSmartContractAddress() {
		return smartContractAddress;
	}

	public void setSmartContractAddress(String smartContractAddress) {
		this.smartContractAddress = smartContractAddress;
	}

	

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	@Override
	public String toString() {
		return "AssetIssueDTO [transactionId=" + transactionId + ", assetId=" + assetId + ", transHash=" + transHash + ", smartContractAddress=" + smartContractAddress + ", gasUsed=" + gasUsed
				+ ", tokenBalance=" + tokenBalance + ", nonce=" + nonce + ", ethBalance=" + ethBalance + "]";
	}


	
}
