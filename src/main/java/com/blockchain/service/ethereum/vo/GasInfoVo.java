package com.blockchain.service.ethereum.vo;

import java.math.BigInteger;

public class GasInfoVo {
	private BigInteger gasPrice;
	
	private BigInteger gasLimit;
	
	private BigInteger ethEstimateGas;
	
	
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
	public BigInteger getEthEstimateGas() {
		return ethEstimateGas;
	}
	public void setEthEstimateGas(BigInteger ethEstimateGas) {
		this.ethEstimateGas = ethEstimateGas;
	}
	
	
}
