package com.blockchain.dto;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="发行返回json",description="发行提交实体")
public class AssetIssueDTO {
	@ApiModelProperty( value = "交易流水号Id")
	private String transactionId;
	@ApiModelProperty( value = "发行资产后的账户资产id")
	private String assetId;
	@ApiModelProperty( value = "区块链交易Fhash值")
	private String transHash;
	
	
	public String getTransHash() {
		return transHash;
	}
	public void setTransHash(String transHash) {
		this.transHash = transHash;
	}
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
	@Override
	public String toString() {
		return "AssetIssueDTO [transactionId=" + transactionId + ", assetId=" + assetId + ", transHash=" + transHash + "]";
	}
	
	
	
}
