package com.blockchain.service.ethereum.dto;



import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;


@ApiModel(value="兑换表单")
public class EthAssetSettleDto {
	@ApiModelProperty(value="转账金额",required=true)
	@NotNull(message="金额不能为空")
	@Min(value=1,message="金额必须为正整数")
	private String amount;
	@ApiModelProperty(value="资产持有方帐户",required=true)
	@NotEmpty(message="转出方帐号不能为空")
	private String ownerAccount;
	@ApiModelProperty(value="转出方用户私钥",required=true)
	@NotEmpty(message="私钥不能为空")
	private String userPrivateKey;
	
	
	private String gasLimit;
	private String gasPrice;
	private String nonce;
	private String password;
	private String offlineFile;

	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getOwnerAccount() {
		return ownerAccount;
	}
	public void setOwnerAccount(String ownerAccount) {
		this.ownerAccount = ownerAccount;
	}
	public String getUserPrivateKey() {
		return userPrivateKey;
	}
	public void setUserPrivateKey(String userPrivateKey) {
		this.userPrivateKey = userPrivateKey;
	}
	
	
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getGasLimit() {
		return gasLimit;
	}
	public void setGasLimit(String gasLimit) {
		this.gasLimit = gasLimit;
	}
	public String getGasPrice() {
		return gasPrice;
	}
	public void setGasPrice(String gasPrice) {
		this.gasPrice = gasPrice;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOfflineFile() {
		return offlineFile;
	}
	public void setOfflineFile(String offlineFile) {
		this.offlineFile = offlineFile;
	}



}
