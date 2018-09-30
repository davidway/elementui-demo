package com.blockchain.service.tencent.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.blockchain.validate.group.EthValidateGroup;
import com.blockchain.validate.group.TencentValidateGroup;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;


@ApiModel(value="兑换表单")
public class AssetSettleFormDto {
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
	@ApiModelProperty(value="转出方资产id，暂时不支持多个",required=true)
	@NotEmpty(message="当前剩余资产id",groups=TencentValidateGroup.class)
	private String srcAsset;
	
	@ApiModelProperty(value = "以太坊事务数字")
	private String nonce;
	
	@ApiModelProperty(value = "份额")
	@Min(value=1 ,message="金额必须大于0,而且为整数，最大数字为10")
	private String gasPrice;

	@ApiModelProperty(value = "份额", required = true)
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
	
	public String getSrcAsset() {
		return srcAsset;
	}
	public void setSrcAsset(String srcAsset) {
		this.srcAsset = srcAsset;
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
	@Override
	public String toString() {
		return "AssetSettleFormDto [amount=" + amount + ", ownerAccount=" + ownerAccount + ", userPrivateKey=" + userPrivateKey + ", srcAsset=" + srcAsset + ", nonce=" + nonce + ", gasPrice="
				+ gasPrice + ", gasLimit=" + gasLimit + ", offlineFile=" + offlineFile + ", password=" + password + "]";
	}
	
	
}
