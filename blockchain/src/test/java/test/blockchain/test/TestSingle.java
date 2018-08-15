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

public class TestSingle {

	@org.junit.Test
	public void testAssetTransferToThirdApply() {
		AssetUtil assetUtil = new AssetUtil();
		AssetTransferSingleSignFormVO assetTransferSingleSignFormVO = new AssetTransferSingleSignFormVO();
		String applyString = "";
		assetTransferSingleSignFormVO.setSrcAccount(BaseParamDTO.user_account_address);
		assetTransferSingleSignFormVO.setDstAccount(BaseParamDTO.second_user_account_address);
		assetTransferSingleSignFormVO.setFeeAccount(BaseParamDTO.third_user_account_address);
		String srcAssetId = "26a92Kx4NBz9zTVjC7tQPUDJJfhgLVYiQF9pZHAfK15LnKA";
		assetTransferSingleSignFormVO.setSrcAssetId(srcAssetId);
		assetTransferSingleSignFormVO.setFeeAmount("1");
		assetTransferSingleSignFormVO.setSignInDate("2019-01-01 12:53:00");
		assetTransferSingleSignFormVO.setAmount("5");
		try {
			applyString = assetUtil.generateTransferSingleApplySignParam(assetTransferSingleSignFormVO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(applyString);
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_transfer_mid_apply_v1.cgi";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		System.out.println(applyResultString);
	}

	@org.junit.Test
	public void testAssetTransferToThirdSubmit() throws UnsupportedEncodingException, TrustSDKException, Exception {
		AssetUtil assetUtil = new AssetUtil();
		AssetTransferToThirdSingleSubmitForm assetTransferSignSingleFormSubmitVO = new AssetTransferToThirdSingleSubmitForm();
		
		JSONArray jsonArray = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("account", "1PuSJUg9cwQxikAQbkuCkKGwPPaPtNENMs");
		json.put("id", "1");
		json.put("sign_str", "ad513a1c61ea5fbfaa11a9a7b187448ff0e96185f7270cf6372e336fee83bbb2");
		jsonArray.add(json);
		assetTransferSignSingleFormSubmitVO.setSignList(JSON.toJSONString(jsonArray));
		String transactionId="201807161460094988";
		assetTransferSignSingleFormSubmitVO.setTransactionId(transactionId);
		assetTransferSignSingleFormSubmitVO.setUserPrivateKey(BaseParamDTO.user_private_key);
		
		
		String applyString = assetUtil.generateTransferSubmitSingleSignParam(assetTransferSignSingleFormSubmitVO);

		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_transfer_mid_submit_v1.cgi";
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
