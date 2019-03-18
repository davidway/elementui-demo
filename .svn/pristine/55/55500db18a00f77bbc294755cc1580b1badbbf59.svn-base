package com.blockchain.dto;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;


@ApiModel(value="BlockInfoDto区块交易信息")
public class BlockInfoDto {
	@ApiModelProperty(value="上一个hash码")
	String preHash;
	@ApiModelProperty(value="区块创建时间")
	long createTime;
	@ApiModelProperty(value="区块高度")
	long blockHeight;
	@ApiModelProperty(value="区块交易总数")
	long transTotalNum;
	public String getPreHash() {
		return preHash;
	}
	public void setPreHash(String preHash) {
		this.preHash = preHash;
	}

	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getBlockHeight() {
		return blockHeight;
	}
	public void setBlockHeight(long blockHeight) {
		this.blockHeight = blockHeight;
	}
	public long getTransTotalNum() {
		return transTotalNum;
	}
	public void setTransTotalNum(long transTotalNum) {
		this.transTotalNum = transTotalNum;
	}
	@Override
	public String toString() {
		return "BlockInfoDto [preHash=" + preHash + ", createTime=" + createTime + ", blockHeight=" + blockHeight + ", transTotalNum=" + transTotalNum + "]";
	}
	
	
}
