package com.blockchain.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(description = "transHeightDto", value = "transHeightDto")
public class TransHeightDto {
	@Min(value = -1, message = " beginHeight最小值不能小于0")
	@NotNull(message = "beginHeight不能为空")
	@ApiModelProperty(value = "开始高度", required = true)
	private Long beginHeight;

	@ApiModelProperty(value = "endHeight结束高度", required = true)
	@Min(value = 1, message = " endHeight最小值不能小于1")
	
	@NotNull(message = "endHeight不能为空")
	private Long endHeight;

	



	public Long getBeginHeight() {
		return beginHeight;
	}

	public void setBeginHeight(Long beginHeight) {
		this.beginHeight = beginHeight;
	}

	public Long getEndHeight() {
		return endHeight;
	}

	public void setEndHeight(Long endHeight) {
		this.endHeight = endHeight;
	}

	@Override
	public String toString() {
		return "TransHeightDto [beginHeight=" + beginHeight + ", endHeight=" + endHeight + "]";
	}

	

}
