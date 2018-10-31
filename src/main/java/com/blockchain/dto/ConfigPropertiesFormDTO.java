package com.blockchain.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(description="配置信息",value="配置信息返回json数据")
public class ConfigPropertiesFormDTO {
	
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
	@ApiModelProperty( value = "主机ip")
	@Pattern(regexp = "^(?i)(https?|ftp)://.*$" ,message="host必须是http 或https开头")
	private String host;

	
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
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	@Override
	public String toString() {
		return "ConfigPropertiesFormDTO [createUserPrivateKey=" + createUserPrivateKey + ", createUserPublicKey=" + createUserPublicKey + ", chainId=" + chainId + ", ledgerId=" + ledgerId
				+ ", mchId=" + mchId + ", nodeId=" + nodeId + ", host=" + host + "]";
	}

	
	
	
	
}
