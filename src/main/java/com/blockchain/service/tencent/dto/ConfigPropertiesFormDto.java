package com.blockchain.service.tencent.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.validation.annotation.Validated;

import com.blockchain.validate.group.EthValidateGroup;
import com.blockchain.validate.group.TencentValidateGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(description="配置信息",value="配置信息返回json数据")
public class ConfigPropertiesFormDto {
	
	@ApiModelProperty( value = "发行方私钥")
	
	private String createUserPrivateKey;
	
	@ApiModelProperty( value = "发行方公钥")
	private String createUserPublicKey;
	@ApiModelProperty( value = "发行方联盟链id")
	private String chainId;
	@ApiModelProperty( value = "发行方账本id")
	private String ledgerId;
	@ApiModelProperty( value = "发行方机构id")
	private String mchId;
	@ApiModelProperty( value = "发行方节点id")
	private String nodeId;
	
	@ApiModelProperty( value = "类型选择器,0为腾讯，1为以太坊")
	@Min(value=0,message="chainType范围值在0~1")
	@Max(value=1,message="chainType范围值在0~1")
	
	private Integer chainType;
	

	public String getChainId() {
		return chainId;
	}
	public void setChainId(String chainId) {
		this.chainId = chainId;
	}
	
	
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getCreateUserPrivateKey() {
		return createUserPrivateKey;
	}
	public void setCreateUserPrivateKey(String createUserPrivateKey) {
		this.createUserPrivateKey = createUserPrivateKey;
	}
	public String getCreateUserPublicKey() {
		return createUserPublicKey;
	}
	public void setCreateUserPublicKey(String createUserPublicKey) {
		this.createUserPublicKey = createUserPublicKey;
	}
	@Override
	public String toString() {
		return "ConfigPropertiesForm [createUserPrivateKey=" + createUserPrivateKey + ", createUserPublicKey=" + createUserPublicKey + ", chainId=" + chainId + ", ledgerId=" + ledgerId + ", mchId="
				+ mchId + ", nodeId=" + nodeId + "]";
	}
	public Integer getChainType() {
		return chainType;
	}
	public void setChainType(Integer chainType) {
		this.chainType = chainType;
	}

	
	
	
	
}
