package test.blockchain.test;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import test.blockchain.java.AssetInfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.DTO.BaseParamDTO;
import com.blockchain.util.AssetUtil;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.tencent.trustsql.sdk.util.HttpClientUtil;

public class Test {

	@org.junit.Test
	public void testAssetTransferToThirdApply() {
		AssetUtil assetUtil = new AssetUtil();
		AssetTransferToThirdSubmitApplyForm assetTransferSignFormVO = new AssetTransferToThirdSubmitApplyForm();
		assetTransferSignFormVO.setSrcAccount(BaseParamDTO.user_account_address);
		assetTransferSignFormVO.setDstAccount(BaseParamDTO.second_user_account_address);
		assetTransferSignFormVO.setFeeAccount(BaseParamDTO.third_user_account_address);

		/*
		 * AssetInfo assetInfo1 = new AssetInfo();
		 * assetInfo1.setSrc_amount("1"); assetInfo1.setFee_amount("1");
		 * assetInfo1
		 * .setAsset_id("26a8opMYhniqQW5igf8axpksHHCB5XyuVNfvWqJsemWNz6c");
		 */
		/*
		JSONObject assetInfo2 = new JSONObject(true);
		
		 * assetInfo2.put("asset_id",
		 * "26a9o2QVV4yw7XKFjCrbcBRuz1GTWo7gipJ5kHQ1UUHojom");
		 * assetInfo2.put("src_amount",1); assetInfo2.put("fee_amount",1);
		 */

		AssetInfo assetInfo2 = new AssetInfo();
		assetInfo2.setSrc_amount("1");
		assetInfo2.setFee_amount("1");
		assetInfo2.setAsset_id("26a8pFbJMmprPGc9iLqXcu5487cJEYcy5tG5Co5hVAYEgrU");
		JSONArray jsonArray = new JSONArray();
		JSONObject finalAssetInfo = new JSONObject();
		/* JSONObject jsonAssetInfo1 = (JSONObject) JSON.toJSON(assetInfo1); */
		/* JSONObject jsonAssetInfo2 = (JSONObject) JSON.toJSON(assetInfo2); */
		/* jsonArray.add(jsonAssetInfo1); */
		jsonArray.add(assetInfo2);
		finalAssetInfo.put("asset_info", jsonArray);
		assetTransferSignFormVO.setAssetInfo(finalAssetInfo);
		String applyString = null;
		try {
			applyString = assetUtil.generateTransferApplySignParam(assetTransferSignFormVO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(applyString);
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_multtransmid_apply_v1.cgi";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		System.out.println(applyResultString);
	}

	@org.junit.Test
	public void testAssetTransferToThirdSubmit() throws UnsupportedEncodingException, TrustSDKException, Exception {
		AssetUtil assetUtil = new AssetUtil();
		AssetTransferToThirdSubmitForm assetTransferSignFormSubmitVO = new AssetTransferToThirdSubmitForm();

		String applyString = assetUtil.generateTransferSubmitSignParam(assetTransferSignFormSubmitVO);

		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_multtransmid_apply_v1.cgi";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		System.out.println(applyResultString);
	}

	@org.junit.Test
	public void testAssetTransferToSignApply() throws UnsupportedEncodingException, TrustSDKException, Exception {
		AssetUtil assetUtil = new AssetUtil();
		AssetTransferToSignApply assetTransferSignFormSubmitVO = new AssetTransferToSignApply();

		String applyString = assetUtil.generateAssetTransferToSignApply(assetTransferSignFormSubmitVO);

		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_multsignin_apply_v1.cgi";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		System.out.println(applyResultString);
	}

	@org.junit.Test
	public void testAssetTransferToSignSubmit() throws UnsupportedEncodingException, TrustSDKException, Exception {
		AssetUtil assetUtil = new AssetUtil();
		AssetTransferToSignSubmit assetTransferSignFormSubmitVO = new AssetTransferToSignSubmit();
		String applyString = assetUtil.generateAssetTransferToSignSubmit(assetTransferSignFormSubmitVO);
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_signin_submit_v1.cgi";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		System.out.println(applyResultString);
	}

}
