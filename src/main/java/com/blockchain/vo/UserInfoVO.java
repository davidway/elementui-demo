package com.blockchain.vo;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;


@ApiModel(description="返回信息",value="申请用户返回的申请返回json")
public class UserInfoVO {
	@ApiModelProperty(   value = "用户id", dataType = "String")
	private String Id;
	@ApiModelProperty(   value = "用户名", dataType = "String")
	private String name;
	@ApiModelProperty(   value = "用户的私钥", dataType = "String")
	private String basePrivateKey;
	@ApiModelProperty(   value = "用户的公钥", dataType = "String")
	private String basePublicKey;
	@ApiModelProperty(   value = "用户账户地址", dataType = "String")
	private String baseAccountAddress;
	@ApiModelProperty(   value = "用户代理帐号的账户地址", dataType = "String")
	private String hostWalletAccountAddress;
	@ApiModelProperty(   value = "用户代理帐号的账户公钥", dataType = "String")
	private String hostWalletPublicKey;
	@ApiModelProperty(   value = "用户代理帐号的账户私钥", dataType = "String")
	private String hostWalletPrivateKey;
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBasePrivateKey() {
		return basePrivateKey;
	}
	public void setBasePrivateKey(String basePrivateKey) {
		this.basePrivateKey = basePrivateKey;
	}
	public String getBasePublicKey() {
		return basePublicKey;
	}
	public void setBasePublicKey(String basePublicKey) {
		this.basePublicKey = basePublicKey;
	}
	public String getBaseAccountAddress() {
		return baseAccountAddress;
	}
	public void setBaseAccountAddress(String baseAccountAddress) {
		this.baseAccountAddress = baseAccountAddress;
	}
	public String getHostWalletAccountAddress() {
		return hostWalletAccountAddress;
	}
	public void setHostWalletAccountAddress(String hostWalletAccountAddress) {
		this.hostWalletAccountAddress = hostWalletAccountAddress;
	}
	public String getHostWalletPublicKey() {
		return hostWalletPublicKey;
	}
	public void setHostWalletPublicKey(String hostWalletPublicKey) {
		this.hostWalletPublicKey = hostWalletPublicKey;
	}
	public String getHostWalletPrivateKey() {
		return hostWalletPrivateKey;
	}
	public void setHostWalletPrivateKey(String hostWalletPrivateKey) {
		this.hostWalletPrivateKey = hostWalletPrivateKey;
	}
	@Override
	public String toString() {
		return "UserInfo [Id=" + Id + ", name=" + name + ", basePrivateKey=" + basePrivateKey + ", basePublicKey=" + basePublicKey + ", baseAccountAddress=" + baseAccountAddress
				+ ", hostWalletAccountAddress=" + hostWalletAccountAddress + ", hostWalletPublicKey=" + hostWalletPublicKey + ", hostWalletPrivateKey=" + hostWalletPrivateKey + "]";
	}

	
}
