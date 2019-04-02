package com.blockchain.dto;


import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="TransInfoListDto交易信息联盟对象")
public class TransInfoListDto {
	@NotNull(message="不能为空")
	@Min(value=1,message="最小值不能小于1")
	Integer pageNo;
	@NotNull(message="不能为空")
	@Min(value=1,message="最小值不能小于1")
	Integer pageLimit;
	
	@ApiModelProperty(value = "配置文件信息", required = true)
	@NotNull(message = "配置信息")
	@Valid
	private ConfigDto configDto;
	
	
	
	public ConfigDto getConfigDto() {
		return configDto;
	}
	public void setConfigDto(ConfigDto configDto) {
		this.configDto = configDto;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageLimit() {
		return pageLimit;
	}
	public void setPageLimit(Integer pageLimit) {
		this.pageLimit = pageLimit;
	}
	@Override
	public String toString() {
		return "TransInfoListDto [pageNo=" + pageNo + ", pageLimit=" + pageLimit + "]";
	}

	
	
}
