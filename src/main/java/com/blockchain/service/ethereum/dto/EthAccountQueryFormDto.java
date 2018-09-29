package com.blockchain.service.ethereum.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.alibaba.fastjson.JSONObject;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;



@ApiModel(value="资产查询")
public class EthAccountQueryFormDto {


	private String assetAccount;

	public String getAssetAccount() {
		return assetAccount;
	}

	public void setAssetAccount(String assetAccount) {
		this.assetAccount = assetAccount;
	}
	
	
	
}
