package com.blockchain.dto;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(description="兑换使用实体类",value="兑换返回json")
public class AssetSettleDTO {
	@ApiModelProperty(   value = "交易号", dataType = "String")
	private String transactionId;
	@ApiModelProperty(   value = "兑换后的资产id列表", dataType = "String")
	private String srcAssetId;
	@ApiModelProperty(   value = "区块链交易Fhash值" , dataType = "String")

	private String transHash;
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
	@Override
	public String toString() {
		return "AssetSettle [transactionId=" + transactionId + ", srcAssetId=" + srcAssetId + ", transHash=" + transHash + ", getTransactionId()=" + getTransactionId() + ", getSrcAssetId()="
				+ getSrcAssetId() + ", getTransHash()=" + getTransHash() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
	
}
