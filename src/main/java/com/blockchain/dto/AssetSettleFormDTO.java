package com.blockchain.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;


@ApiModel(value="兑换表单")
public class AssetSettleFormDTO {
	@ApiModelProperty(value="转账金额",required=true)
	@NotNull(message="金额不能为空")
	@Min(value=1,message="金额必须为正整数")
	//@Max(value = 10_000_000_000_000L, message = "金额必须大于0,而且为整数，最大数字为10_000_000_000_000L")
	private String amount;
	@ApiModelProperty(value="资产持有方帐户",required=true)
	@NotEmpty(message="转出方帐号不能为空")
	private String ownerAccount;
	@ApiModelProperty(value="转出方用户私钥",required=true)
	@NotEmpty(message="私钥不能为空")
	private String userPrivateKey;
	@ApiModelProperty(value="转出方资产id，暂时不支持多个",required=true)
	@NotEmpty(message="当前剩余资产id")
	private String srcAsset;
	@ApiModelProperty(value="转出公钥",required=true)
	@NotEmpty(message="转出公钥不能为空")
	private String ownerPublickey;
	@ApiModelProperty(value="转出方id",required=true)
	@NotEmpty(message="转出方id不能为空")
	private String ownerId;
	


	
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
	public String getOwnerPublickey() {
		return ownerPublickey;
	}
	public void setOwnerPublickey(String ownerPublickey) {
		this.ownerPublickey = ownerPublickey;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	@Override
	public String toString() {
		return "AssetSettleFormDTO [amount=" + amount + ", ownerAccount=" + ownerAccount + ", userPrivateKey=" + userPrivateKey + ", srcAsset=" + srcAsset + ", ownerPublickey=" + ownerPublickey
				+ ", ownerId=" + ownerId + "]";
	}


	
}
