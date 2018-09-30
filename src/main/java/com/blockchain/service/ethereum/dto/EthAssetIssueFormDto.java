package com.blockchain.service.ethereum.dto;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

import com.blockchain.validate.group.EthValidateGroup;
import com.blockchain.validate.group.TencentValidateGroup;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;


@ApiModel("资产发行列表")
public class EthAssetIssueFormDto {
	
	@NotEmpty(message = "金额不能为空",groups=TencentValidateGroup.class)
	@ApiModelProperty(value="金额",required=true)
	
	@Min(value=1 ,message="金额必须大于0,而且为整数，最大数字为18个9")
	@Max(value=999999999999999999L,message="金额必须大于0,而且为整数，最大数字为18个9")
	private String amount;
	
	@ApiModelProperty(value="发行方帐号",required=true)
	@NotEmpty(message="发行方帐号不能为空")
	private String createUserAccountAddress;
	
	@ApiModelProperty(value="资产单位",required=true)
	@NotEmpty(message="单位不能为空")
	private String unit;

	@ApiModelProperty(value="以太坊资产全名",required=true)
	@NotEmpty(message="单位不能为空",groups=EthValidateGroup.class)
	private String fullName;
	
	@Min(value=1 ,message="事务数字必须大于0,而且为整数，最大数字为18个9",groups=EthValidateGroup.class)

	@ApiModelProperty(value="以太坊事务单位",required=true)
	private String nonce;
	@NotEmpty(message = "金额不能为空",groups=EthValidateGroup.class)
	@ApiModelProperty(value = "份额", required = true)
	@Min(value=1 ,message="金额必须大于0,而且为整数，最大数字为10")
	@Max(value=10,message="金额必须大于0,而且为整数，最大数字为10")
	private String gasPrice;
	@NotEmpty(message = "金额不能为空",groups=EthValidateGroup.class)
	@ApiModelProperty(value = "份额", required = true)
	@Min(value=1 ,message="金额必须大于0,而且为整数，最大数字为10")
	@Max(value=10,message="金额必须大于0,而且为整数，最大数字为10")
	private String gasLimit ;
	
	@ApiModelProperty(value = "离线文件", required = true)
	private String offlineFile;
	@ApiModelProperty(value = "密码", required = true)
	private String password;
	
	@ApiModelProperty(value = "以太坊用户私钥", required = true)
	private String privateKey;

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

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	@Override
	public String toString() {
		return "AssetIssueFormDTO [amount=" + amount + ", createUserAccountAddress=" + createUserAccountAddress + ", unit=" + unit + ", fullName=" + fullName + ", nonce=" + nonce + ", gasPrice="
				+ gasPrice + ", gasLimit=" + gasLimit + ", offlineFile=" + offlineFile + ", password=" + password + ", privateKey=" + privateKey + "]";
	}
	
	
	


}
