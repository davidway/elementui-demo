package com.blockchain.dto;

import java.util.Arrays;

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
public class AccountQueryFormDTO {

	@ApiModelProperty(value= "用户id")
	private String ownerUid;
	@ApiModelProperty(value= "用户账户")
	private String assetAccount;
	@ApiModelProperty(value= "用户资产id")
	private String assetId;
	@ApiModelProperty(value= "页码limit",required=true)
	@Max(value=20,message="页码limit最大是20")
	private Integer pageLimit;
	@ApiModelProperty(value= "页码",required=true)
	@Min(value=1,message="页码必须大于1")
	private Integer pageNo;
	@ApiModelProperty(value= "状态值,0：正常持有 3：已全额转让 5：已清算",required=true)
	@NotNull(message="状态值不能为空")
	private int[] state;
	@ApiModelProperty(value= "资产内容json串，用来匹配对应的资产使用",required=true)
	private JSONObject content;
	
	
	public String getOwnerUid() {
		return ownerUid;
	}
	public void setOwnerUid(String ownerUid) {
		this.ownerUid = ownerUid;
	}
	public String getAssetAccount() {
		return assetAccount;
	}
	public void setAssetAccount(String assetAccount) {
		this.assetAccount = assetAccount;
	}
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
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
	
	public JSONObject getContent() {
		return content;
	}
	public void setContent(JSONObject content) {
		this.content = content;
	}
	public int[] getState() {
		return state;
	}
	public void setState(int[] state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "AccountQueryFormDTO [ownerUid=" + ownerUid + ", assetAccount=" + assetAccount + ", assetId=" + assetId + ", pageLimit=" + pageLimit + ", pageNo=" + pageNo + ", state="
				+ Arrays.toString(state) + ", content=" + content + "]";
	}
	
	
	
}
