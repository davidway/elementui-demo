package test.blockchain.java;

public class AssetInfo {
	private  String asset_id;
	private String src_amount;
	private String fee_amount;
	public String getAsset_id() {
		return asset_id;
	}
	public void setAsset_id(String asset_id) {
		this.asset_id = asset_id;
	}
	public String getSrc_amount() {
		return src_amount;
	}
	public void setSrc_amount(String src_amount) {
		this.src_amount = src_amount;
	}
	public String getFee_amount() {
		return fee_amount;
	}
	public void setFee_amount(String fee_amount) {
		this.fee_amount = fee_amount;
	}
	@Override
	public String toString() {
		return "AssetInfo [asset_id=" + asset_id + ", src_amount=" + src_amount + ", fee_amount=" + fee_amount + "]";
	}
	
	
}
