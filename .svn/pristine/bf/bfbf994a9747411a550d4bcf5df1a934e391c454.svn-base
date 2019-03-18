package com.blockchain.dto;

import java.util.List;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="TransDetailsVo交易信息详情")
public class TransDetailsVo {
	@ApiModelProperty("交易信息")
	TransInfoDto transInfo;
	@ApiModelProperty("区块信息")
	BlockTransDto blockTransDto;
	@ApiModelProperty("节点信息")
	BlockTransChainInfoDto blockTransChainInfoDto;

	
	public TransInfoDto getTransInfo() {
		return transInfo;
	}
	public void setTransInfo(TransInfoDto transInfo) {
		this.transInfo = transInfo;
	}
	
	
	public BlockTransChainInfoDto getBlockChainInfoDto() {
		return blockTransChainInfoDto;
	}
	public void setBlockChainInfoDto(BlockTransChainInfoDto blockTransChainInfoDto) {
		this.blockTransChainInfoDto = blockTransChainInfoDto;
	}
	public BlockTransDto getBlockTransDto() {
		return blockTransDto;
	}
	public void setBlockTransDto(BlockTransDto blockTransDto) {
		this.blockTransDto = blockTransDto;
	}
	@Override
	public String toString() {
		return "TransInfoDtoVo [transInfo=" + transInfo + ", blockTransDto=" + blockTransDto + ", blockChainInfoDto=" + blockTransChainInfoDto + "]";
	}

	
}
