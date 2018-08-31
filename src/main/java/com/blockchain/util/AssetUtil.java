package com.blockchain.util;

import java.io.UnsupportedEncodingException;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.DTO.AssetFormDTO;
import com.blockchain.DTO.AssetSettleFormDTO;
import com.blockchain.DTO.AssetSettleSubmitFormDTO;
import com.blockchain.DTO.AssetSubmitFormDTO;
import com.blockchain.DTO.AssetTransferFormDTO;
import com.blockchain.DTO.AssetTransferSubmitFormDTO;
import com.blockchain.service.AssetService;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.tencent.trustsql.sdk.util.SignStrUtil;

@Component
public class AssetUtil {

	boolean isTest = false;

	@Resource
	AssetService assetService;

	private static Logger log = Logger.getLogger(AssetUtil.class);

	public String generateIssueApplyParam(AssetFormDTO assetFormDTO) throws TrustSDKException, Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId =  configUtils.getMchId();
		String prvKey =  configUtils.getCreateUserPrivateKey();
		String nodeId = configUtils.getNodeId();
		String chainId =  configUtils.getChainId();
		String ledgerId =  configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);
		paramMap.put("source_id", assetFormDTO.getSourceId());
		paramMap.put("owner_account", assetFormDTO.getCreateUserAccountAddress());
		paramMap.put("asset_type", "1");
		paramMap.put("amount", assetFormDTO.getAmount());
		paramMap.put("unit", assetFormDTO.getUnit());
		JSONObject contentJsonObject = JSONObject.parseObject(assetFormDTO.getContent());
		paramMap.put("content", contentJsonObject);
		paramMap.put("timestamp", System.currentTimeMillis() / 1000L);
		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject(true);
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public String generateIssueSubmitParam(AssetSubmitFormDTO assetSubmitFormDTO) throws UnsupportedEncodingException, TrustSDKException, Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId =  configUtils.getMchId();
		String prvKey =  configUtils.getCreateUserPrivateKey();
		String coinPrivateKey =  configUtils.getCoin_privateKey();
		String nodeId = configUtils.getNodeId();
		String chainId =  configUtils.getChainId();
		String ledgerId =  configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();

		String transactionId = assetSubmitFormDTO.getTransactionId();

		String assetId = assetSubmitFormDTO.getAssetId();
		String tempString = assetSubmitFormDTO.getSignStr();

		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);
		paramMap.put("ledger_id", ledgerId);
		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);

		paramMap.put("asset_type", "1");

		paramMap.put("transaction_id", transactionId);
		paramMap.put("asset_id", assetId);
		paramMap.put("timestamp", System.currentTimeMillis() / 1000);

		JSONArray jsonArray = JSONArray.parseArray(tempString);
		JSONArray newArray = new JSONArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject o = jsonArray.getJSONObject(i);
			String id = o.getString("id");
			String account = o.getString("account");
			String signStr = o.getString("sign_str");
			JSONObject json = new JSONObject();
			json.put("id", id);
			json.put("account", account);
			json.put("sign_str", signStr);
			String sign = TrustSDK.SignRenString(coinPrivateKey, Hex.decodeHex(signStr.toCharArray()));

			json.put("sign", sign);
			newArray.add(json);
		}
		paramMap.put("sign_list", newArray);

		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject(true);
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}

		return postJson.toJSONString();

	}

	public String generateTransferApplyParam(AssetTransferFormDTO assetTransferFormDTO) throws TrustSDKException, Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId =  configUtils.getMchId();
		String prvKey =  configUtils.getCreateUserPrivateKey();

		String nodeId = configUtils.getNodeId();
		String chainId =  configUtils.getChainId();
		String ledgerId =  configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);
		paramMap.put("src_account", assetTransferFormDTO.getSrcAccount());
		paramMap.put("dst_account", assetTransferFormDTO.getDstAccount());
		if (StringUtils.isNotBlank(assetTransferFormDTO.getFeeAccount())) {
			paramMap.put("fee_account", assetTransferFormDTO.getFeeAccount());
		}
		if (StringUtils.isNotBlank(assetTransferFormDTO.getFeeAmount())) {
			paramMap.put("fee_amount", assetTransferFormDTO.getFeeAmount());
		}
		paramMap.put("src_asset_list", assetTransferFormDTO.getSrcAsset());

		paramMap.put("asset_type", "1");
		paramMap.put("amount", assetTransferFormDTO.getAmount());
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("交易时间", System.currentTimeMillis());
		paramMap.put("extra_info", jsonObj);
		paramMap.put("timestamp", System.currentTimeMillis() / 1000L);
		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject();
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public String generateSettleApplyParam(AssetSettleFormDTO assetSettleFormDTO) throws TrustSDKException, Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId =  configUtils.getMchId();
		String prvKey =  configUtils.getCreateUserPrivateKey();

		String nodeId = configUtils.getNodeId();
		String chainId =  configUtils.getChainId();
		String ledgerId =  configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);

		paramMap.put("owner_account", assetSettleFormDTO.getOwnerAccount());
		paramMap.put("asset_type", 1);
		paramMap.put("amount", assetSettleFormDTO.getAmount());
		String assetList = assetSettleFormDTO.getSrcAsset();

		paramMap.put("src_asset_list", assetList);

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("申请的信息", "金额为：" + assetSettleFormDTO.getAmount());
		paramMap.put("extra_info", jsonObj);

		paramMap.put("timestamp", System.currentTimeMillis() / 1000);
		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject();
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public String generateTransferSubmitParam(AssetTransferSubmitFormDTO assetSubmitForm) throws Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId =  configUtils.getMchId();
		String prvKey =  configUtils.getCreateUserPrivateKey();

		String nodeId = configUtils.getNodeId();
		String chainId =  configUtils.getChainId();
		String ledgerId =  configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);

		paramMap.put("transaction_id", assetSubmitForm.getTransactionId());
		paramMap.put("asset_type", 1);

		JSONArray signList = JSONArray.parseArray(assetSubmitForm.getSignList());
		for (int i = 0; i < signList.size(); i++) {
			JSONObject jsonObject = signList.getJSONObject(i);
			String sign = TrustSDK.SignRenString(assetSubmitForm.getUserPrivateKey(), Hex.decodeHex(jsonObject.getString("sign_str").toCharArray()));
			jsonObject.put("sign", sign);

		}
		paramMap.put("sign_list", signList);
		paramMap.put("timestamp", System.currentTimeMillis() / 1000);
		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject();
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public String generateSettleSubmitParam(AssetSettleSubmitFormDTO assetSettleSubmitFormDTO) throws Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId =  configUtils.getMchId();
		String prvKey =  configUtils.getCreateUserPrivateKey();

		String nodeId = configUtils.getNodeId();
		String chainId =  configUtils.getChainId();
		String ledgerId =  configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);

		paramMap.put("asset_type", 1);
		JSONArray signList = JSONArray.parseArray(assetSettleSubmitFormDTO.getSignList());

		for (int i = 0; i < signList.size(); i++) {
			JSONObject jsonObject = signList.getJSONObject(i);
			String sign = TrustSDK.SignRenString(assetSettleSubmitFormDTO.getUserPrivateKey(), Hex.decodeHex(jsonObject.getString("sign_str").toCharArray()));
			jsonObject.put("sign", sign);
		}
		paramMap.put("sign_list", signList);
		paramMap.put("transaction_id", assetSettleSubmitFormDTO.getTransactionId());
		paramMap.put("timestamp", System.currentTimeMillis() / 1000);
		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject();
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	

}
