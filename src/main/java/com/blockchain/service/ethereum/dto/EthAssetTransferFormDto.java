package com.blockchain.service.ethereum.dto;



import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "转账表单")
public class EthAssetTransferFormDto {
	@NotEmpty(message = "金额不能为空")
	@ApiModelProperty(value = "份额", required = true)
	@Min(value=1 ,message="金额必须大于0,而且为整数，最大数字为18个9")
	@Max(value=999999999999999999L,message="金额必须大于0,而且为整数，最大数字为18个9")
	private String amount;
	
	@NotEmpty(message = "资产转出帐户不能为空")
	@ApiModelProperty(value = "资产转出帐户", required = true)
	private String srcAccount;
	@NotEmpty(message = "资产转入帐户不能为空")
	@ApiModelProperty(value = "资产转入帐户", required = true)
	private String dstAccount;
	@NotEmpty(message = "用户资产列表")
	@ApiModelProperty(value = "转出账户持有的资产ID列表,逗号分割", required = true)
	private String srcAsset;

	@ApiModelProperty(value = "手续费转入帐户", required = false)
	private String feeAccount;
	@ApiModelProperty(value = "手续费份额,64位长", required = false)
	@Min(value=1 ,message="金额必须大于0,而且为整数，最大数字为18个9")
	@Max(value=999999999999999999L,message="金额必须大于0,而且为整数，最大数字为18个9")
	private String feeAmount;
	@NotEmpty(message = "转出账户用户私钥")
	@ApiModelProperty(value = "转出账户用户私钥", required = false)
	private String userPrivateKey;

	private String nonce;
	@NotEmpty(message = "金额不能为空")
	@ApiModelProperty(value = "份额", required = true)
	@Min(value=1 ,message="金额必须大于0,而且为整数，最大数字为10")
	
	private String gasPrice;
	@NotEmpty(message = "金额不能为空")
	@ApiModelProperty(value = "份额", required = true)
	@Min(value=1 ,message="金额必须大于0,而且为整数，最大数字为10")
	
	private String gasLimit ;
	
	@ApiModelProperty(value = "离线文件", required = true)
	private String offlineFile;
	@ApiModelProperty(value = "密码", required = true)
	private String password;
	
	
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getSrcAccount() {
		return srcAccount;
	}
	public void setSrcAccount(String srcAccount) {
		this.srcAccount = srcAccount;
	}
	public String getDstAccount() {
		return dstAccount;
	}
	public void setDstAccount(String dstAccount) {
		this.dstAccount = dstAccount;
	}
	public String getSrcAsset() {
		return srcAsset;
	}
	public void setSrcAsset(String srcAsset) {
		this.srcAsset = srcAsset;
	}
	public String getFeeAccount() {
		return feeAccount;
	}
	public void setFeeAccount(String feeAccount) {
		this.feeAccount = feeAccount;
	}
	public String getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(String feeAmount) {
		this.feeAmount = feeAmount;
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
	public String getGasPrice() {
		return gasPrice;
	}
	public void setGasPrice(String gasPrice) {
		this.gasPrice = gasPrice;
	}
	public String getGasLimit() {
		return gasLimit;
	}
	public void setGasLimit(String gasLimit) {
		this.gasLimit = gasLimit;
	}
	public String getOfflineFile() {
		return offlineFile;
	}
	public void setOfflineFile(String offlineFile) {
		this.offlineFile = offlineFile;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	} 

}
