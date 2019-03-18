package com.blockchain.dto;

import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel( value="申请兑换表单")
public class AssetSettleSubmitFormDTO {
	@ApiModelProperty(value="平台唯一标识一次交易的ID",required=true)
	@NotEmpty(message="平台唯一标识一次交易的ID不能为空")
	private String transactionId;
	@ApiModelProperty(value="待签名串",required=true)
	@NotEmpty(message="待签名串不能为空")
	private String signList;
	@ApiModelProperty(value="申请人私钥",required=true)
	@NotEmpty(message="申请人私钥不能为空")
	private String userPrivateKey;
	
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getSignList() {
		return signList;
	}
	public void setSignList(String signList) {
		this.signList = signList;
	}
	public String getUserPrivateKey() {
		return userPrivateKey;
	}
	public void setUserPrivateKey(String userPrivateKey) {
		this.userPrivateKey = userPrivateKey;
	}
	@Override
	public String toString() {
		return "AssetSettleSubmitForm [transactionId=" + transactionId + ", signList=" + signList + ", userPrivateKey=" + userPrivateKey + "]";
	}
		
}
