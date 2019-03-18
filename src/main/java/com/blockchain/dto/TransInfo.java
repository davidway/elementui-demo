package com.blockchain.dto;

public class TransInfo {
	Integer transType;
	Integer transStatus;
	Long amount;
	Long feeAmount;
	String srcAccountAddress;
	String dstAccountAddress;
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	public Integer getTransStatus() {
		return transStatus;
	}
	public void setTransStatus(Integer transStatus) {
		this.transStatus = transStatus;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public Long getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(Long feeAmount) {
		this.feeAmount = feeAmount;
	}
	public String getSrcAccountAddress() {
		return srcAccountAddress;
	}
	public void setSrcAccountAddress(String srcAccountAddress) {
		this.srcAccountAddress = srcAccountAddress;
	}
	public String getDstAccountAddress() {
		return dstAccountAddress;
	}
	public void setDstAccountAddress(String dstAccountAddress) {
		this.dstAccountAddress = dstAccountAddress;
	}
	@Override
	public String toString() {
		return "TransInfo [transType=" + transType + ", transStatus=" + transStatus + ", amount=" + amount + ", feeAmount=" + feeAmount + ", srcAccountAddress=" + srcAccountAddress
				+ ", dstAccountAddress=" + dstAccountAddress + "]";
	}
	
}
