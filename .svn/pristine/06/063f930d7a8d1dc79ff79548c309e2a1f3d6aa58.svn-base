package com.blockchain.dto;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "TransInfoDto交易信息")
public class TransInfoDto {
	@ApiModelProperty(value = "金额", dataType = "String")
	private Long amount;
	@ApiModelProperty(value = "转出账户", dataType = "String")
	private String dstAccount;
	@ApiModelProperty(value = "转出资产id", dataType = "String")
	private String dstAssetId;
	@ApiModelProperty(value = "转入账户", dataType = "String")
	private String srcAccount;
	@ApiModelProperty(value = "转入账户资产id", dataType = "String")
	private String srcAssetId;
	@ApiModelProperty(value = "交易状态【当前支持: 1 申请中 2 已申请 3 提交中 4 已提交 5 拒签申请 6 拒签提交中 7 已拒签) 】", dataType = "String")
	private Integer transState;
	@ApiModelProperty(value = "交易时间，精确到秒", dataType = "String")
	private String transTime;
	@ApiModelProperty(value = "交易类型，1 发行 2 转让 3 兑付", dataType = "String")
	private Integer transType;
	@ApiModelProperty(value = "平台唯一标识一次交易的ID", dataType = "String")
	private String transactionId;
	@ApiModelProperty(value = "待签名串列表", dataType = "String")
	private String signList;

	@ApiModelProperty(value = "返回串hash", dataType = "String")
	private String transHash;
	@ApiModelProperty(value = "区块hash", dataType = "String")
	private String blockHash;

	@ApiModelProperty(value = "hash高度", dataType = "String")
	private long transHeight;
	
	@ApiModelProperty(value = "手续费", dataType = "String")
	private Long feeAmount;




	

	public String getBlockHash() {
		return blockHash;
	}

	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}

	public Long getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(Long feeAmount) {
		this.feeAmount = feeAmount;
	}

	public String getTransHash() {
		return transHash;
	}

	public void setTransHash(String transHash) {
		this.transHash = transHash;
	}


	public long getTransHeight() {
		return transHeight;
	}

	public void setTransHeight(long transHeight) {
		this.transHeight = transHeight;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
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

	

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	@Override
	public String toString() {
		return "TransInfoDto [amount=" + amount + ", dstAccount=" + dstAccount + ", dstAssetId=" + dstAssetId + ", srcAccount=" + srcAccount + ", srcAssetId=" + srcAssetId + ", transState="
				+ transState + ", transTime=" + transTime + ", transType=" + transType + ", transactionId=" + transactionId + ", signList=" + signList + ", transHash=" + transHash + ", blockHash="
				+ blockHash + ", transHeight=" + transHeight + ", feeAmount=" + feeAmount + "]";
	}

	

	



	
	
}
