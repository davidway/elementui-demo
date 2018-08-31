package com.blockchain.DTO;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;


@ApiModel(description="交易信息",value="交易信息返回json")
public class TransInfoDTO {
	@ApiModelProperty(   value = "金额", dataType = "String")
	private long amount;
	@ApiModelProperty(   value = "转出账户", dataType = "String")
	private String dstAccount;
	@ApiModelProperty(   value = "转出资产id", dataType = "String")
	private String dstAssetId;
	@ApiModelProperty(   value = "转入账户", dataType = "String")
	private String srcAccount;
	@ApiModelProperty(   value = "转入账户资产id", dataType = "String")
	private String srcAssetId;
	@ApiModelProperty(   value = "交易状态，1 申请中 2 已申请 3 提交中 4 已提交", dataType = "String")
	private Integer transState;
	@ApiModelProperty(   value = "交易时间，精确到秒", dataType = "String")
	private String transTime;
	@ApiModelProperty(   value = "交易类型，1 发行 2 转让 3 兑付", dataType = "String")
	private Integer transType;
	@ApiModelProperty(   value = "平台唯一标识一次交易的ID", dataType = "String")
	private String transactionId;
	@ApiModelProperty(   value = "待签名串列表", dataType = "String")
	private String signList;

	@ApiModelProperty(   value = "返回串hash", dataType = "String")
	private String hash;


	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getDstAccount() {
		return dstAccount;
	}

	public void setDstAccount(String dstAccount) {
		this.dstAccount = dstAccount;
	}

	public String getDstAssetId() {
		return dstAssetId;
	}

	public void setDstAssetId(String dstAssetId) {
		this.dstAssetId = dstAssetId;
	}

	public String getSrcAccount() {
		return srcAccount;
	}

	public void setSrcAccount(String srcAccount) {
		this.srcAccount = srcAccount;
	}

	public String getSrcAssetId() {
		return srcAssetId;
	}

	public void setSrcAssetId(String srcAssetId) {
		this.srcAssetId = srcAssetId;
	}

	public Integer getTransState() {
		return transState;
	}

	public void setTransState(Integer transState) {
		this.transState = transState;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public Integer getTransType() {
		return transType;
	}

	public void setTransType(Integer transType) {
		this.transType = transType;
	}

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

	@Override
	public String toString() {
		return "TransInfo [amount=" + amount + ", dstAccount=" + dstAccount + ", dstAssetId=" + dstAssetId + ", srcAccount=" + srcAccount + ", srcAssetId=" + srcAssetId + ", transState=" + transState
				+ ", transTime=" + transTime + ", transType=" + transType + ", transactionId=" + transactionId + ", signList=" + signList + "]";
	}





}
