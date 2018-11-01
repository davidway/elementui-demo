package com.blockchain.service.tencent.dto;



import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.blockchain.util.ConfigUtils;
import com.blockchain.validate.group.EthValidateGroup;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel("资产发行列表")
public class AssetIssueFormDto {
	@ApiModelProperty(value = "原资产唯一ID，由业务系统自己维护", required = true)
	
	private String sourceId;

	@NotEmpty(message = "内容不能为空")
	@ApiModelProperty(value = "区块链系统唯一标志内容，json格式", required = true)
	private String content;
	@NotEmpty(message = "金额不能为空")
	@ApiModelProperty(value = "金额", required = true)
	@Min(value = 1, message = "金额必须大于0,而且为整数，最大数字为1_0_000_000_000_000")
	//@Max(value = 1_0_000_000_000_000L, message = "金额必须大于0,而且为整数，最大数字为1_0_000_000_000_000")
	private String amount;

	@ApiModelProperty(value = "发行方帐号", required = true)
	@NotEmpty(message = "发行方帐号不能为空")
	private String createUserAccountAddress;

	@ApiModelProperty(value = "资产单位", required = true)
	@NotEmpty(message = "单位不能为空")
	private String unit;
	@ApiModelProperty(value = "用户Id", required = true)
	@NotEmpty(message = "userId不能为空")
	private String userId;
	@ApiModelProperty(value = "用户公钥", required = true)
	@NotEmpty(message = "单位不能为空")
	private String userPublicKey;
	@ApiModelProperty(value = "用户私钥", required = true)
	@NotEmpty(message = "用户私钥不能为空")
	private String userPrivateKey;

	@ApiModelProperty(value="以太坊 用户离线文件",required=true)
	
	private String keyStore;
	
	@ApiModelProperty(value="以太坊 用户密码",required=true)
	
	private String password;
	@ApiModelProperty(value = "用户私钥", required = true)
	@NotEmpty(message = "以太币全名",groups=EthValidateGroup.class)

	private String fullName;
	
	@NotEmpty(message = "gasPrice不能为空",groups=EthValidateGroup.class)
	@ApiModelProperty(value = "份额", required = true)
	@Min(value=1 ,message="gasPrice必须大于0,而且为整数")
	private String gasPrice;
	@NotEmpty(message = "gasLimit不能为空",groups=EthValidateGroup.class)
	@ApiModelProperty(value = "份额", required = true)
	@Min(value=1 ,message="gasLimit必须大于0,而且为整数")
	private String gasLimit ;
	
	
	
	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCreateUserAccountAddress() {
		return createUserAccountAddress;
	}

	public void setCreateUserAccountAddress(String createUserAccountAddress) {
		this.createUserAccountAddress = createUserAccountAddress;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "AssetIssueFormDto [sourceId=" + sourceId + ", content=" + content + ", amount=" + amount + ", createUserAccountAddress=" + createUserAccountAddress + ", unit=" + unit + ", fullName="
				+ fullName + "]";
	}



	public String getUserPrivateKey() {
		return userPrivateKey;
	}

	public void setUserPrivateKey(String userPrivateKey) {
		this.userPrivateKey = userPrivateKey;
	}

	public String getKeyStore() {
		return keyStore;
	}

	public void setKeyStore(String keyStore) {
		this.keyStore = keyStore;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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


	
}
