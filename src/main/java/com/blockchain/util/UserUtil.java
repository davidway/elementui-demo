package com.blockchain.util;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.DTO.AccountQueryFormDTO;
import com.blockchain.DTO.AssetTransQueryFormDTO;
import com.blockchain.DTO.UserFormDTO;
import com.blockchain.VO.UserInfoVO;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.bean.AccountCert;
import com.tencent.trustsql.sdk.bean.PairKey;
import com.tencent.trustsql.sdk.bean.RequestData;
import com.tencent.trustsql.sdk.bean.UserCert;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.tencent.trustsql.sdk.util.SignStrUtil;
import com.tencent.trustsql.sdk.util.SignUtil;

public class UserUtil {

	static boolean isTest = false;

	public static String generateUserRequest(UserInfoVO userInfoVO, UserFormDTO userFormDTO) throws Exception {
		String requestString = "";
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		RequestData req = new RequestData();

		String sequenceNumber = RequestUtil.getSequenceNumber();
		String mchId =  configUtils.getMchId();

		String userId = userFormDTO.getId();
		String name = userFormDTO.getName();

		req.setMch_id(mchId);
		req.setProduct_code("productA");
		req.setSeq_no(sequenceNumber);
		req.setType("sign");
		UserCert user = new UserCert();
		user.setUser_id(userId);
		user.setUser_fullName(name);

		PairKey pairKey = TrustSDK.generatePairKey(true);
		String publicKey = pairKey.getPublicKey();
		user.setPublic_key(publicKey);

		req.setReq_data(JSONObject.toJSONString(user));
		req.setTime_stamp(System.currentTimeMillis() / 1000L);
	
		String prvKey = configUtils.getCreateUserPrivateKey();
		String signSrc = SignUtil.genSignSrc(req);
		String sign = TrustSDK.signString(prvKey, signSrc.getBytes("UTF-8"), false);
		req.setSign(sign);

		userInfoVO.setBasePrivateKey(pairKey.getPrivateKey());
		userInfoVO.setBasePublicKey(pairKey.getPublicKey());

		return JSON.toJSONString(req);
	}

	public static String generateRegisterAccountRequest(UserCert userCert, boolean isHost) throws Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		RequestData req = new RequestData();
		String sqeNo = RequestUtil.getSequenceNumber();
		req.setMch_id( configUtils.getMchId());
		req.setProduct_code("productA");
		req.setSeq_no(sqeNo);
		req.setType("sign");
		AccountCert account = new AccountCert();
		account.setUser_id(userCert.getUser_id());
		String publicKey = "";
		if (isHost) {
			PairKey pairKey = TrustSDK.generatePairKey(true);
			publicKey = pairKey.getPublicKey();
			account.setPublic_key(publicKey);
		} else {
			account.setUser_id(userCert.getPublic_key());
		}

		req.setReq_data(JSONObject.toJSONString(account));
		req.setTime_stamp(System.currentTimeMillis() / 1000L);
		String prvKey = configUtils.getCreateUserPrivateKey();
		// String prvKey = "gaxUIUD76vmEaJwxUZEcqoM0LDESKtpc3M4FDSlPSV0";
		String signSrc = SignUtil.genSignSrc(req);
		System.out.println(signSrc);
		String sign = TrustSDK.signString(prvKey, signSrc.getBytes("UTF-8"), false);
		req.setSign(sign);
		System.out.println(sign);
		return JSONObject.toJSONString(req);
	}

	public static String generateuserAccoutForm(UserInfoVO userInfoVO, JSONObject userRegistRetData, boolean isHost) throws Exception {
		String sequenceNumber = RequestUtil.getSequenceNumber();
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		RequestData req = new RequestData();
		req.setMch_id( configUtils.getMchId());
		req.setProduct_code("productA");
		req.setSeq_no(sequenceNumber);
		req.setType("sign");
		AccountCert account = new AccountCert();
		account.setUser_id(userRegistRetData.getString("user_id"));
		String accountPublicKey = userRegistRetData.getString("public_key");
		String accountPrivateKey = "";
		if (isHost) {
			PairKey pairKey = TrustSDK.generatePairKey(true);
			accountPublicKey = pairKey.getPublicKey();
			accountPrivateKey = pairKey.getPrivateKey();
			account.setPublic_key(accountPublicKey);
			userInfoVO.setHostWalletPublicKey(accountPublicKey);
			userInfoVO.setHostWalletPrivateKey(accountPrivateKey);
		} else {
			account.setPublic_key(accountPublicKey);
		}

		req.setReq_data(JSONObject.toJSONString(account));
		req.setTime_stamp(System.currentTimeMillis() / 1000L);
		String prvKey = configUtils.getCreateUserPrivateKey();
		// String prvKey = "gaxUIUD76vmEaJwxUZEcqoM0LDESKtpc3M4FDSlPSV0";
		String signSrc = SignUtil.genSignSrc(req);

		String sign = TrustSDK.signString(prvKey, signSrc.getBytes("UTF-8"), false);
		req.setSign(sign);

		return JSONObject.toJSONString(req);
	}

	public static String generateAccountQueryParam(AccountQueryFormDTO assetForm) throws TrustSDKException, Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId =  configUtils.getMchId();
		String prvKey = configUtils.getCreateUserPrivateKey();

		String nodeId =  configUtils.getNodeId();
		String chainId = configUtils.getChainId();
		String ledgerId =  configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);
		if (StringUtils.isNotBlank(assetForm.getAssetAccount())) {
			paramMap.put("asset_account", assetForm.getAssetAccount());
		}
		if (StringUtils.isNotBlank(assetForm.getOwnerUid())) {
			paramMap.put("owner_uid", assetForm.getOwnerUid());
		}
		if (StringUtils.isNotBlank(assetForm.getAssetId())) {
			paramMap.put("asset_id", assetForm.getAssetId());
		}
		paramMap.put("state", assetForm.getState());
		paramMap.put("page_limit", assetForm.getPageLimit());
		paramMap.put("page_no", assetForm.getPageNo());
		paramMap.put("timestamp", System.currentTimeMillis() / 1000L);
		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject();
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public static String generateTransQueryParam(AssetTransQueryFormDTO assetForm) throws TrustSDKException, Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		JSONArray resultJsonArr = new JSONArray();

		String mchId =  configUtils.getMchId();
		String prvKey = configUtils.getCreateUserPrivateKey();

		String nodeId =  configUtils.getNodeId();
		String chainId = configUtils.getChainId();
		String ledgerId =  configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);
		if (StringUtils.isNotBlank(assetForm.getSrcAccount())) {
			paramMap.put("src_account", assetForm.getSrcAccount());
		}
		if (StringUtils.isNotBlank(assetForm.getDstAccount())) {
			paramMap.put("dst_account", assetForm.getDstAccount());
		}
		if (StringUtils.isNotBlank(assetForm.getTransactionId())) {
			paramMap.put("transaction_id", assetForm.getTransactionId());
		}

		if (StringUtils.isNotBlank(assetForm.getMonth())) {
			paramMap.put("month", assetForm.getMonth());
		}

		paramMap.put("page_limit", assetForm.getPageLimit());
		paramMap.put("page_no", assetForm.getPageNo());
		paramMap.put("timestamp", System.currentTimeMillis() / 1000);
		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject();
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public static String generateuserAccoutFormOnlyHostAccount(UserInfoVO userInfoVO, UserFormDTO userFormDTO) throws Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String sequenceNumber = RequestUtil.getSequenceNumber();
		String accountPublicKey = "";
		String accountPrivateKey = "";
		RequestData req = new RequestData();
		req.setMch_id( configUtils.getMchId());
		req.setProduct_code("productA");
		req.setSeq_no(sequenceNumber);
		req.setType("sign");
		AccountCert account = new AccountCert();
		account.setUser_id(userFormDTO.getId());

		PairKey pairKey = TrustSDK.generatePairKey(true);
		accountPublicKey = pairKey.getPublicKey();
		accountPrivateKey = pairKey.getPrivateKey();
		account.setPublic_key(accountPublicKey);
		userInfoVO.setHostWalletPublicKey(accountPublicKey);
		userInfoVO.setHostWalletPrivateKey(accountPrivateKey);

		req.setReq_data(JSONObject.toJSONString(account));
		req.setTime_stamp(System.currentTimeMillis() / 1000L);
		String prvKey = configUtils.getCreateUserPrivateKey();
		// String prvKey = "gaxUIUD76vmEaJwxUZEcqoM0LDESKtpc3M4FDSlPSV0";
		String signSrc = SignUtil.genSignSrc(req);

		String sign = TrustSDK.signString(prvKey, signSrc.getBytes(), false);
		req.setSign(sign);

		return JSONObject.toJSONString(req);
	}

}
