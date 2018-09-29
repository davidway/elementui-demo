package com.blockchain.service.tencent.util;

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
import com.blockchain.service.tencent.AssetService;
import com.blockchain.service.tencent.dto.AssetIssueFormDto;
import com.blockchain.service.tencent.dto.AssetSettleFormDto;
import com.blockchain.service.tencent.dto.AssetSettleSubmitFormDto;
import com.blockchain.service.tencent.dto.AssetIssueSubmitFormDto;
import com.blockchain.service.tencent.dto.AssetTransferFormDto;
import com.blockchain.service.tencent.dto.AssetTransferSubmitFormDto;
import com.blockchain.service.tencent.trustsql.sdk.TrustSDK;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;
import com.blockchain.service.tencent.trustsql.sdk.util.SignStrUtil;

public class AssetUtil {
	
	boolean isTest = false;



	private static Logger log = Logger.getLogger(AssetUtil.class);

	public String generateIssueApplyParam(AssetIssueFormDto assetIssueFormDto) throws TrustSDKException, Exception {
		ConfigUtils configUtils = new ConfigUtils();
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
		paramMap.put("source_id", assetIssueFormDto.getSourceId());
		paramMap.put("owner_account", assetIssueFormDto.getCreateUserAccountAddress());
		paramMap.put("asset_type", "1");
		paramMap.put("amount", assetIssueFormDto.getAmount());
		paramMap.put("unit", assetIssueFormDto.getUnit());
		JSONObject contentJsonObject = JSONObject.parseObject(assetIssueFormDto.getContent());
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

	public String generateIssueSubmitParam(AssetIssueSubmitFormDto assetIssueSubmitFormDto) throws UnsupportedEncodingException, TrustSDKException, Exception {
		ConfigUtils configUtils = new ConfigUtils();
		String mchId =  configUtils.getMchId();
		String prvKey =  configUtils.getCreateUserPrivateKey();
		String coinPrivateKey =  configUtils.getCoin_privateKey();
		String nodeId = configUtils.getNodeId();
		String chainId =  configUtils.getChainId();
		String ledgerId =  configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();

		String transactionId = assetIssueSubmitFormDto.getTransactionId();

		String assetId = assetIssueSubmitFormDto.getAssetId();
		String tempString = assetIssueSubmitFormDto.getSignStr();

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

	public String generateTransferApplyParam(AssetTransferFormDto assetTransferFormDto) throws TrustSDKException, Exception {
		ConfigUtils configUtils = new ConfigUtils();
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
		paramMap.put("src_account", assetTransferFormDto.getSrcAccount());
		paramMap.put("dst_account", assetTransferFormDto.getDstAccount());
		if (StringUtils.isNotBlank(assetTransferFormDto.getFeeAccount())) {
			paramMap.put("fee_account", assetTransferFormDto.getFeeAccount());
		}
		if (StringUtils.isNotBlank(assetTransferFormDto.getFeeAmount())) {
			paramMap.put("fee_amount", assetTransferFormDto.getFeeAmount());
		}
		paramMap.put("src_asset_list", assetTransferFormDto.getSrcAsset());

		paramMap.put("asset_type", "1");
		paramMap.put("amount", assetTransferFormDto.getAmount());
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

	public String generateSettleApplyParam(AssetSettleFormDto assetSettleFormDto) throws TrustSDKException, Exception {
		ConfigUtils configUtils = new ConfigUtils();
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

		paramMap.put("owner_account", assetSettleFormDto.getOwnerAccount());
		paramMap.put("asset_type", 1);
		paramMap.put("amount", assetSettleFormDto.getAmount());
		String assetList = assetSettleFormDto.getSrcAsset();

		paramMap.put("src_asset_list", assetList);

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("申请的信息", "金额为：" + assetSettleFormDto.getAmount());
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

	public String generateTransferSubmitParam(AssetTransferSubmitFormDto assetSubmitForm) throws Exception {
		ConfigUtils configUtils = new ConfigUtils();
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

	public String generateSettleSubmitParam(AssetSettleSubmitFormDto assetSettleSubmitFormDto) throws Exception {
		ConfigUtils configUtils = new ConfigUtils();
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
		JSONArray signList = JSONArray.parseArray(assetSettleSubmitFormDto.getSignList());

		for (int i = 0; i < signList.size(); i++) {
			JSONObject jsonObject = signList.getJSONObject(i);
			String sign = TrustSDK.SignRenString(assetSettleSubmitFormDto.getUserPrivateKey(), Hex.decodeHex(jsonObject.getString("sign_str").toCharArray()));
			jsonObject.put("sign", sign);
		}
		paramMap.put("sign_list", signList);
		paramMap.put("transaction_id", assetSettleSubmitFormDto.getTransactionId());
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
