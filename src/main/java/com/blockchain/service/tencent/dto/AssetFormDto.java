package com.blockchain.service.tencent.dto;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.blockchain.service.tencent.util.ConfigUtils;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;


@ApiModel("资产发行列表")
public class AssetFormDto {
	@ApiModelProperty(value="原资产唯一ID，由业务系统自己维护",required=true)
	@NotEmpty(message="资产id不能为空")
	private String sourceId;
	
	@NotEmpty(message="内容不能为空")
	@ApiModelProperty(value="区块链系统唯一标志内容，json格式",required=true)
	private String content;
	@NotEmpty(message = "金额不能为空")
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

	@Override
	public String toString() {
		return "AssetForm [sourceId=" + sourceId + ", content=" + content + ", amount=" + amount + ", createUserAccountAddress=" + createUserAccountAddress + ", unit=" + unit + "]";
	}
	
	
}
