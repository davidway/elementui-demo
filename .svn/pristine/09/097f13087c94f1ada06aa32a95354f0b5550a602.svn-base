package com.blockchain.dto;

import java.util.ArrayList;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="BlockChainInfoDto联盟链节点信息")
public class BlockChainInfoDto {
	@ApiModelProperty(value = "节点信息数组", dataType = "String")
	ArrayList<NodeInfo> nodeInfo;
	@ApiModelProperty(value = "联盟链状态 0：未运行；1：启动中；2：正在运行；3：异常；", dataType = "String")
	Integer chainState;
	@ApiModelProperty(value = "节点总数", dataType = "String")
	Integer nodeTotalNum;
	@ApiModelProperty(value = "交易流水号", dataType = "String")
	Long blockHeight;
	
	
	

	public ArrayList<NodeInfo> getNodeInfo() {
		return nodeInfo;
	}
	public void setNodeInfo(ArrayList<NodeInfo> nodeInfo) {
		this.nodeInfo = nodeInfo;
	}
	public Integer getChainState() {
		return chainState;
	}
	public void setChainState(Integer chainState) {
		this.chainState = chainState;
	}
	public Integer getNodeTotalNum() {
		return nodeTotalNum;
	}
	public void setNodeTotalNum(Integer nodeTotalNum) {
		this.nodeTotalNum = nodeTotalNum;
	}
	public Long getBlockHeight() {
		return blockHeight;
	}
	public void setBlockHeight(Long blockHeight) {
		this.blockHeight = blockHeight;
	}
	@Override
	public String toString() {
		return "BlockChainInfoDto [nodeInfo=" + nodeInfo + ", chainState=" + chainState + ", nodeTotalNum=" + nodeTotalNum + ", blockHeight=" + blockHeight + "]";
	}
	

	
	
	
	
	
	
}
