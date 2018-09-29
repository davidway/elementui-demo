package com.blockchain.service.ethereum.vo;


public class EthAssetIssueVo {
	String ethBalance;
	String smartContractAddress;
	String accountAddress;
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
	public String getAccountAddress() {
		return accountAddress;
	}
	public void setAccountAddress(String accountAddress) {
		this.accountAddress = accountAddress;
	}
	@Override
	public String toString() {
		return "AssetIssueVo [ethBalance=" + ethBalance + ", smartContractAddress=" + smartContractAddress + ", accountAddress=" + accountAddress + "]";
	}
	
	
	
	
}
