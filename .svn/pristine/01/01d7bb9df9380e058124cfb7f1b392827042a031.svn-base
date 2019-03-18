package com.blockchain.dto;

import java.util.ArrayList;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="BlockTransChainInfoDto节点信息详情")
public class BlockTransChainInfoDto {
	@ApiModelProperty(value="节点信息")
	ArrayList<NodeTransInfo> nodeInfo;
	@ApiModelProperty(value="节点状态")
	Integer chainState;
	@ApiModelProperty(value="节点总数")
	Integer nodeTotalNum;
	@ApiModelProperty(value="区块高度")
	Long blockHeight;
	@ApiModelProperty(value="区块交易总数")
	Long transTotalNum;

	@ApiModelProperty(value="交易状态【当前支持: 2,本地已申请, 4, 已提交;(转让签收场景下: 6, 已签收; 7, 已拒签;8,已撤销) 】")
	int transStatus;
	@ApiModelProperty(value="交易时间")
	String transTime;
	

	
	public Integer getChainState() {
		return chainState;
	}
	public void setChainState(Integer chainState) {
		this.chainState = chainState;
	}

	public Long getBlockHeight() {
		return blockHeight;
	}
	public void setBlockHeight(Long blockHeight) {
		this.blockHeight = blockHeight;
	}
	public Long getTransTotalNum() {
		return transTotalNum;
	}
	public void setTransTotalNum(Long transTotalNum) {
		this.transTotalNum = transTotalNum;
	}
	public Integer getNodeTotalNum() {
		return nodeTotalNum;
	}
	public void setNodeTotalNum(Integer nodeTotalNum) {
		this.nodeTotalNum = nodeTotalNum;
	}
	public int getTransStatus() {
		return transStatus;
	}
	public void setTransStatus(int transStatus) {
		this.transStatus = transStatus;
	}
	
	

	
	public ArrayList<NodeTransInfo> getNodeInfo() {
		return nodeInfo;
	}
	public void setNodeInfo(ArrayList<NodeTransInfo> nodeInfo) {
		this.nodeInfo = nodeInfo;
	}
	public String getTransTime() {
		return transTime;
	}
	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}
	@Override
	public String toString() {
		return "BlockTransChainInfoDto [nodeInfo=" + nodeInfo + ", chainState=" + chainState + ", nodeTotalNum=" + nodeTotalNum + ", blockHeight=" + blockHeight + ", transTotalNum=" + transTotalNum
				+ ", transStatus=" + transStatus + ", transTime=" + transTime + "]";
	}

	
	
	
}
