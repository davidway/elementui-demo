package com.blockchain.service.ethereum.vo;

import java.math.BigInteger;

public class GasInfoVo {
	private BigInteger gasPrice;
	
	private BigInteger gasLimit;
	
	public BigInteger getGasPrice() {
		return gasPrice;
	}
	public void setGasPrice(BigInteger gasPrice) {
		this.gasPrice = gasPrice;
	}
	public BigInteger getGasLimit() {
		return gasLimit;
	}
	public void setGasLimit(BigInteger gasLimit) {
		this.gasLimit = gasLimit;
	}
	
	
}
