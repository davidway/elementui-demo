package com.blockchain.dto;

import com.alibaba.fastjson.JSONObject;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(description="资产查询描述",value="资产查询返回json")
public class AssetDTO{
	@ApiModelProperty( value = "金额")
	private Long amount;
	@ApiModelProperty( value = "资产账户地址")
	private String assetAccount;
	@ApiModelProperty( value = "资产id")
	private String assetId;
	@ApiModelProperty( value = "资产类型：0. 商票1. 数字货币2. Q币3. 游戏装备4. 黄金5. 积分6. 卡券7. 股权")
	private Integer assetType;
	@ApiModelProperty( value = "0：正常持有 3：已全额转让 5：已清算")
	private Integer state;
	@ApiModelProperty( value = "资产的唯一标识内容")
	private JSONObject content;

	
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
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
	public Integer getAssetType() {
		return assetType;
	}
	public void setAssetType(Integer assetType) {
		this.assetType = assetType;
	}
	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}

	public JSONObject getContent() {
		return content;
	}
	public void setContent(JSONObject content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "Asset [amount=" + amount + ", assetAccount=" + assetAccount + ", assetId=" + assetId + ", assetType=" + assetType + ", state=" + state + ", content=" + content + "]";
	}

	
	
	
	
	
	
	

	
	
}
