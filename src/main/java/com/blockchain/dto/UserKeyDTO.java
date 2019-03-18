package com.blockchain.dto;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(description="公钥，私钥，trustSql值生成",value="公钥，私钥，trustSql值生成返回值json")
public class UserKeyDTO {
	@ApiModelProperty(dataType="String",value="公钥")
	private String publicKey;
	@ApiModelProperty(dataType="String",value="私钥")
	private String privateKey;
	@ApiModelProperty(dataType="String",value="TrustSql后的值")
	private String afterTrustKey;
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getAfterTrustKey() {
		return afterTrustKey;
	}
	public void setAfterTrustKey(String afterTrustKey) {
		this.afterTrustKey = afterTrustKey;
	}
	@Override
	public String toString() {
		return "UserKey [publicKey=" + publicKey + ", privateKey=" + privateKey + ", afterTrustKey=" + afterTrustKey + "]";
	}
	
	

}
