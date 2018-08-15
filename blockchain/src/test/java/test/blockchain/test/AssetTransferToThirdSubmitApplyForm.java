	package test.blockchain.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import test.blockchain.java.AssetInfo;

public class AssetTransferToThirdSubmitApplyForm {
	private String srcAccount;
	private String dstAccount;
	
	private JSONObject assetInfo;
	private String feeAccount;
	private String assetId;
	
	
	
	public String getFeeAccount() {
		return feeAccount;
	}
	public void setFeeAccount(String feeAccount) {
		this.feeAccount = feeAccount;
	}
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

	public JSONObject getAssetInfo() {
		return assetInfo;
	}
	public void setAssetInfo(JSONObject assetInfo) {
		this.assetInfo = assetInfo;
	}
	@Override
	public String toString() {
		return "AssetTransferToThirdSubmitApplyForm [srcAccount=" + srcAccount + ", dstAccount=" + dstAccount + ", assetInfo=" + assetInfo + ", feeAccount=" + feeAccount + "]";
	}



	
	
	
}
