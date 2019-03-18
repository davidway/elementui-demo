package com.blockchain.dto;

import java.util.ArrayList;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;


@ApiModel(value="交易查询表单")
public class AssetTransQueryFormDTO {
	@ApiModelProperty(value="转入账户",required=false)
	private String dstAccount;
	@ApiModelProperty(value="转出账户",required=false)
	private String srcAccount;
	@ApiModelProperty(value="平台唯一标识一次交易的ID",required=false)
	private String transactionId;
	@ApiModelProperty(value="交易hash值",required=false)
	private String transHash;
	
	

	@Max(value=20,message="页码limit最大为20")
	@Min(value=1,message="页码必须大于1")
	@ApiModelProperty(value="页码limit，最小为1，最大为20",required=true)
	private Integer pageLimit;
	@ApiModelProperty(value="页码,最小值不能小于1",required=true)
	@Min(value=1,message="最小值不能小于1")
	private Integer pageNo;
	@ApiModelProperty(value="区块高度范围",required=true)
	
	private ArrayList<Integer> blockHeightRange;
	@ApiModelProperty(value="状态交易状态【当前支持：2，本地已申请，4，已提交；（转让签收场景下：6，已签收；7，已拒签；8，已撤销）】",required=true)
	
	private Integer state;
	
	

	
	public String getSrcAccount() {
		return srcAccount;
	}
	public void setSrcAccount(String srcAccount) {
		this.srcAccount = srcAccount;
	}
	public String getDstAccount() {
		return dstAccount;
	}
	public void setDstAccount(String dstAccount) {
		this.dstAccount = dstAccount;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	public Integer getPageLimit() {
		return pageLimit;
	}
	public void setPageLimit(Integer pageLimit) {
		this.pageLimit = pageLimit;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	
	public String getTransHash() {
		return transHash;
	}
	public void setTransHash(String transHash) {
		this.transHash = transHash;
	}


	
	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public ArrayList<Integer> getBlockHeightRange() {
		return blockHeightRange;
	}
	public void setBlockHeightRange(ArrayList<Integer> blockHeightRange) {
		this.blockHeightRange = blockHeightRange;
	}
	@Override
	public String toString() {
		return "AssetTransQueryFormDTO [dstAccount=" + dstAccount + ", srcAccount=" + srcAccount + ", transactionId=" + transactionId + ", transHash=" + transHash + ", pageLimit=" + pageLimit
				+ ", pageNo=" + pageNo + ", blockHeightRange=" + blockHeightRange + ", state=" + state + "]";
	}
	
	

	
}
