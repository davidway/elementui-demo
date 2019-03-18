package com.blockchain.dto;

import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="BlockDetailsInfo区块信息详情")
public class BlockDetailsInfo {
	@ApiModelProperty(value="区块信息详情")
	BlockInfoDto blockInfoDto;
	@ApiModelProperty(value="区块交易信息详情")
	
	List<TransInfoDto> transInfoDto ;
	
	
	public BlockInfoDto getBlockInfoDto() {
		return blockInfoDto;
	}


	public void setBlockInfoDto(BlockInfoDto blockInfoDto) {
		this.blockInfoDto = blockInfoDto;
	}


	public List<TransInfoDto> getTransInfoDto() {
		return transInfoDto;
	}


	public void setTransInfoDto(List<TransInfoDto> transInfoDto) {
		this.transInfoDto = transInfoDto;
	}


	@Override
	public String toString() {
		return "BlockDetailsInfo [blockInfoDto=" + blockInfoDto + ", transInfoDto=" + transInfoDto + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}
	
}
