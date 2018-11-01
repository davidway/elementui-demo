package com.blockchain.service.ethereum.dto;



import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.blockchain.validate.group.EthValidateGroup;
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
	
	
	@NotEmpty(message = "gasPrice不能为空",groups=EthValidateGroup.class)
	@ApiModelProperty(value = "份额", required = true)
	@Min(value=1 ,message="金额必须大于0,而且为整数，最大数字为10")
	private String gasPrice;
	@NotEmpty(message = "gasLimit不能为空",groups=EthValidateGroup.class)
	@ApiModelProperty(value = "份额", required = true)
	@Min(value=1 ,message="金额必须大于0,而且为整数，最大数字为10")
	private String gasLimit ;
	
	
	@ApiModelProperty(value = "离线文件", required = true)
	private String keyStore;
	@ApiModelProperty(value = "密码", required = true)
	private String password;
	
	@ApiModelProperty(value = "事务数字", required = true)
	private String nonce;


	
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
	public String getKeyStore() {
		return keyStore;
	}
	public void setKeyStore(String keyStore) {
		this.keyStore = keyStore;
	}
	@Override
	public String toString() {
		return "EthAssetSettleDto [amount=" + amount + ", ownerAccount=" + ownerAccount + ", userPrivateKey=" + userPrivateKey + ", gasPrice=" + gasPrice + ", gasLimit=" + gasLimit + ", keyStore="
				+ keyStore + ", password=" + password + ", nonce=" + nonce + "]";
	}
	



}
