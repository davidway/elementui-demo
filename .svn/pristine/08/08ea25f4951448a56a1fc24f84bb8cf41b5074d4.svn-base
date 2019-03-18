package com.blockchain.dto;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author weekend
 *
 */
@ApiModel(value = "NodeInfo区块节点信息")
public class NodeInfo {
	@ApiModelProperty(value = "最新的区块hash")
	String latestTransHash;
	@ApiModelProperty(value = "最新的区块时间")
	long latestBlockTime;
	@ApiModelProperty(value = "最新的区块高度")
	long latestBlockHeight;
	@ApiModelProperty(value = "最新的节点状态0.停止 1.正在启动 2.运行中 3.异常")
	Integer nodeState;
	@ApiModelProperty(value = "节点名称")
	String nodeName;

	public String getLatestTransHash() {
		return latestTransHash;
	}

	public void setLatestTransHash(String latestTransHash) {
		this.latestTransHash = latestTransHash;
	}

	public long getLatestBlockTime() {
		return latestBlockTime;
	}

	public void setLatestBlockTime(long latestBlockTime) {
		this.latestBlockTime = latestBlockTime;
	}

	public Integer getNodeState() {
		return nodeState;
	}

	public void setNodeState(Integer nodeState) {
		this.nodeState = nodeState;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public long getLatestBlockHeight() {
		return latestBlockHeight;
	}

	public void setLatestBlockHeight(long latestBlockHeight) {
		this.latestBlockHeight = latestBlockHeight;
	}

	@Override
	public String toString() {
		return "NodeInfo [latestTransHash=" + latestTransHash + ", latestBlockTime=" + latestBlockTime + ", latestBlockHeight=" + latestBlockHeight + ", nodeState=" + nodeState + ", nodeName="
				+ nodeName + "]";
	}

	public void setTransTime(String transTime) {
		// TODO Auto-generated method stub

	}

	public void setTransStatus(int status) {
		// TODO Auto-generated method stub

	}

}
