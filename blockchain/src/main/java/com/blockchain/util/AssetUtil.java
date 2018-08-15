package com.blockchain.util;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import test.blockchain.test.AssetTransferSingleSignFormVO;
import test.blockchain.test.AssetTransferToSignApply;
import test.blockchain.test.AssetTransferToSignSubmit;
import test.blockchain.test.AssetTransferToThirdSingleSubmitForm;
import test.blockchain.test.AssetTransferToThirdSubmitApplyForm;
import test.blockchain.test.AssetTransferToThirdSubmitForm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.DTO.BaseParamDTO;
import com.blockchain.VO.AccountQueryFormVO;
import com.blockchain.VO.AssetFormVO;
import com.blockchain.VO.AssetSettleFormVO;
import com.blockchain.VO.AssetSettleSubmitFormVO;
import com.blockchain.VO.AssetSubmitFormVO;
import com.blockchain.VO.AssetTransQueryFormVO;
import com.blockchain.VO.AssetTransferFormVO;
import com.blockchain.VO.AssetTransferSubmitFormVO;
import com.blockchain.service.AssetService;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.tencent.trustsql.sdk.util.SignStrUtil;

@Component
public class AssetUtil {

	boolean isTest = true;

	@Resource
	AssetService assetService;

	private static Logger log = Logger.getLogger(AssetUtil.class);

	public String generateIssueApplyParam(AssetFormVO assetFormVO) throws TrustSDKException, Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId = isTest ? BaseParamDTO.mchId : configUtils.getMchId();
		String prvKey = isTest ? BaseParamDTO.create_user_privateKey : configUtils.getCreateUserPrivateKey();
		String nodeId = isTest ? BaseParamDTO.nodeId : configUtils.getNodeId();
		String chainId = isTest ? BaseParamDTO.chainId : configUtils.getChainId();
		String ledgerId = isTest ? BaseParamDTO.leadgerId : configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);
		paramMap.put("source_id", assetFormVO.getSourceId());
		paramMap.put("owner_account", assetFormVO.getCreateUserAccountAddress());
		paramMap.put("asset_type", "1");
		paramMap.put("amount", assetFormVO.getAmount());
		paramMap.put("unit", assetFormVO.getUnit());
		JSONObject contentJsonObject = JSONObject.parseObject(assetFormVO.getContent());
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

	public String generateIssueSubmitParam(AssetSubmitFormVO assetSubmitFormVO) throws UnsupportedEncodingException, TrustSDKException, Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId = isTest ? BaseParamDTO.mchId : configUtils.getMchId();
		String prvKey = isTest ? BaseParamDTO.create_user_privateKey : configUtils.getCreateUserPrivateKey();
		String coinPrivateKey = isTest ? BaseParamDTO.coin_privateKey : configUtils.getCoin_privateKey();
		String nodeId = isTest ? BaseParamDTO.nodeId : configUtils.getNodeId();
		String chainId = isTest ? BaseParamDTO.chainId : configUtils.getChainId();
		String ledgerId = isTest ? BaseParamDTO.leadgerId : configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();

		String transactionId = assetSubmitFormVO.getTransactionId();

		String assetId = assetSubmitFormVO.getAssetId();
		String tempString = assetSubmitFormVO.getSignStr();

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

	public String generateTransferApplyParam(AssetTransferFormVO assetTransferFormVO) throws TrustSDKException, Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId = isTest ? BaseParamDTO.mchId : configUtils.getMchId();
		String prvKey = isTest ? BaseParamDTO.create_user_privateKey : configUtils.getCreateUserPrivateKey();

		String nodeId = isTest ? BaseParamDTO.nodeId : configUtils.getNodeId();
		String chainId = isTest ? BaseParamDTO.chainId : configUtils.getChainId();
		String ledgerId = isTest ? BaseParamDTO.leadgerId : configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);
		paramMap.put("src_account", assetTransferFormVO.getSrcAccount());
		paramMap.put("dst_account", assetTransferFormVO.getDstAccount());
		if (StringUtils.isNotBlank(assetTransferFormVO.getFeeAccount())) {
			paramMap.put("fee_account", assetTransferFormVO.getFeeAccount());
		}
		if (StringUtils.isNotBlank(assetTransferFormVO.getFeeAmount())) {
			paramMap.put("fee_amount", assetTransferFormVO.getFeeAmount());
		}
		paramMap.put("src_asset_list", assetTransferFormVO.getSrcAsset());

		paramMap.put("asset_type", "1");
		paramMap.put("amount", assetTransferFormVO.getAmount());
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

	public String generateSettleApplyParam(AssetSettleFormVO assetSettleFormVO) throws TrustSDKException, Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId = isTest ? BaseParamDTO.mchId : configUtils.getMchId();
		String prvKey = isTest ? BaseParamDTO.create_user_privateKey : configUtils.getCreateUserPrivateKey();

		String nodeId = isTest ? BaseParamDTO.nodeId : configUtils.getNodeId();
		String chainId = isTest ? BaseParamDTO.chainId : configUtils.getChainId();
		String ledgerId = isTest ? BaseParamDTO.leadgerId : configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);

		paramMap.put("owner_account", assetSettleFormVO.getOwnerAccount());
		paramMap.put("asset_type", 1);
		paramMap.put("amount", assetSettleFormVO.getAmount());
		String assetList = assetSettleFormVO.getSrcAsset();

		paramMap.put("src_asset_list", assetList);

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("申请的信息", "金额为：" + assetSettleFormVO.getAmount());
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

	public String generateTransferSubmitParam(AssetTransferSubmitFormVO assetSubmitForm) throws Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId = isTest ? BaseParamDTO.mchId : configUtils.getMchId();
		String prvKey = isTest ? BaseParamDTO.create_user_privateKey : configUtils.getCreateUserPrivateKey();

		String nodeId = isTest ? BaseParamDTO.nodeId : configUtils.getNodeId();
		String chainId = isTest ? BaseParamDTO.chainId : configUtils.getChainId();
		String ledgerId = isTest ? BaseParamDTO.leadgerId : configUtils.getLedgerId();
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

	public String generateSettleSubmitParam(AssetSettleSubmitFormVO assetSettleSubmitFormVO) throws Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId = isTest ? BaseParamDTO.mchId : configUtils.getMchId();
		String prvKey = isTest ? BaseParamDTO.create_user_privateKey : configUtils.getCreateUserPrivateKey();

		String nodeId = isTest ? BaseParamDTO.nodeId : configUtils.getNodeId();
		String chainId = isTest ? BaseParamDTO.chainId : configUtils.getChainId();
		String ledgerId = isTest ? BaseParamDTO.leadgerId : configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);

		paramMap.put("asset_type", 1);
		JSONArray signList = JSONArray.parseArray(assetSettleSubmitFormVO.getSignList());

		for (int i = 0; i < signList.size(); i++) {
			JSONObject jsonObject = signList.getJSONObject(i);
			String sign = TrustSDK.SignRenString(assetSettleSubmitFormVO.getUserPrivateKey(), Hex.decodeHex(jsonObject.getString("sign_str").toCharArray()));
			jsonObject.put("sign", sign);
		}
		paramMap.put("sign_list", signList);
		paramMap.put("transaction_id", assetSettleSubmitFormVO.getTransactionId());
		paramMap.put("timestamp", System.currentTimeMillis() / 1000);
		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject();
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public String generateTransferApplySignParam(AssetTransferToThirdSubmitApplyForm assetTransferSignFormVO) throws UnsupportedEncodingException, TrustSDKException, Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId = isTest ? BaseParamDTO.mchId : configUtils.getMchId();
		String prvKey = isTest ? BaseParamDTO.create_user_privateKey : configUtils.getCreateUserPrivateKey();

		String nodeId = isTest ? BaseParamDTO.nodeId : configUtils.getNodeId();
		String chainId = isTest ? BaseParamDTO.chainId : configUtils.getChainId();
		String ledgerId = isTest ? BaseParamDTO.leadgerId : configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);
		paramMap.put("src_account", assetTransferSignFormVO.getSrcAccount());
		paramMap.put("dst_account", assetTransferSignFormVO.getDstAccount());
		if (StringUtils.isNotBlank(assetTransferSignFormVO.getFeeAccount())) {
			paramMap.put("fee_account", assetTransferSignFormVO.getFeeAccount());
		}

		paramMap.put("mult_asset", assetTransferSignFormVO.getAssetInfo());

		paramMap.put("asset_type", "1");

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

	public String generateTransferSubmitSignParam(AssetTransferToThirdSubmitForm assetTransferToThirdSubmitSubmitForm) throws UnsupportedEncodingException, Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId = isTest ? BaseParamDTO.mchId : configUtils.getMchId();
		String prvKey = isTest ? BaseParamDTO.create_user_privateKey : configUtils.getCreateUserPrivateKey();

		String nodeId = isTest ? BaseParamDTO.nodeId : configUtils.getNodeId();
		String chainId = isTest ? BaseParamDTO.chainId : configUtils.getChainId();
		String ledgerId = isTest ? BaseParamDTO.leadgerId : configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);

		paramMap.put("transaction_id", assetTransferToThirdSubmitSubmitForm.getTransactionId());
		paramMap.put("asset_type", 1);

		JSONArray signList = JSONArray.parseArray(assetTransferToThirdSubmitSubmitForm.getSignList());
		for (int i = 0; i < signList.size(); i++) {
			JSONObject jsonObject = signList.getJSONObject(i);
			String sign = TrustSDK.SignRenString(assetTransferToThirdSubmitSubmitForm.getUserPrivateKey(), Hex.decodeHex(jsonObject.getString("sign_str").toCharArray()));
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

	public String generateAssetTransferToSignApply(AssetTransferToSignApply assetTransferSignFormSubmitVO) throws UnsupportedEncodingException, TrustSDKException, Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId = isTest ? BaseParamDTO.mchId : configUtils.getMchId();
		String prvKey = isTest ? BaseParamDTO.create_user_privateKey : configUtils.getCreateUserPrivateKey();

		String nodeId = isTest ? BaseParamDTO.nodeId : configUtils.getNodeId();
		String chainId = isTest ? BaseParamDTO.chainId : configUtils.getChainId();
		String ledgerId = isTest ? BaseParamDTO.leadgerId : configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);
		paramMap.put("src_account", assetTransferSignFormSubmitVO.getSrcAccount());
		paramMap.put("dst_account", assetTransferSignFormSubmitVO.getDstAccount());

		paramMap.put("fee_account", assetTransferSignFormSubmitVO.getFeeAccount());

		paramMap.put("total_src_amount", assetTransferSignFormSubmitVO.getTotalSrcAmount());
		paramMap.put("total_fee_amount", assetTransferSignFormSubmitVO.getTotalFeeAmount());

		paramMap.put("asset_type", "1");

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

	public String generateAssetTransferToSignSubmit(AssetTransferToSignSubmit assetTransferSignFormSubmitVO) throws UnsupportedEncodingException, Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId = isTest ? BaseParamDTO.mchId : configUtils.getMchId();
		String prvKey = isTest ? BaseParamDTO.create_user_privateKey : configUtils.getCreateUserPrivateKey();

		String nodeId = isTest ? BaseParamDTO.nodeId : configUtils.getNodeId();
		String chainId = isTest ? BaseParamDTO.chainId : configUtils.getChainId();
		String ledgerId = isTest ? BaseParamDTO.leadgerId : configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);

		paramMap.put("transaction_id", assetTransferSignFormSubmitVO.getTransactionId());
		paramMap.put("asset_type", 1);

		JSONArray signList = JSONArray.parseArray(assetTransferSignFormSubmitVO.getSignList());
		for (int i = 0; i < signList.size(); i++) {
			JSONObject jsonObject = signList.getJSONObject(i);
			String sign = TrustSDK.SignRenString(assetTransferSignFormSubmitVO.getUserPrivateKey(), Hex.decodeHex(jsonObject.getString("sign_str").toCharArray()));
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

	public String generateTransferSingleApplySignParam(AssetTransferSingleSignFormVO assetTransferSingleSignFormVO) throws UnsupportedEncodingException, TrustSDKException, Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId = isTest ? BaseParamDTO.mchId : configUtils.getMchId();
		String prvKey = isTest ? BaseParamDTO.create_user_privateKey : configUtils.getCreateUserPrivateKey();

		String nodeId = isTest ? BaseParamDTO.nodeId : configUtils.getNodeId();
		String chainId = isTest ? BaseParamDTO.chainId : configUtils.getChainId();
		String ledgerId = isTest ? BaseParamDTO.leadgerId : configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);
		paramMap.put("src_account", assetTransferSingleSignFormVO.getSrcAccount());
		paramMap.put("dst_account", assetTransferSingleSignFormVO.getDstAccount());
		paramMap.put("amount", assetTransferSingleSignFormVO.getAmount());

		paramMap.put("fee_account", assetTransferSingleSignFormVO.getFeeAccount());

		paramMap.put("src_asset_id", assetTransferSingleSignFormVO.getSrcAssetId());
		paramMap.put("fee_amount", assetTransferSingleSignFormVO.getFeeAmount());
		paramMap.put("sign_in_date", assetTransferSingleSignFormVO.getSignInDate());

		paramMap.put("asset_type", "1");

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

	public String generateTransferSubmitSingleSignParam(AssetTransferToThirdSingleSubmitForm assetTransferSignFormSubmitVO) throws UnsupportedEncodingException, Exception {
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String mchId = isTest ? BaseParamDTO.mchId :  configUtils.getMchId();
		String prvKey = isTest ? BaseParamDTO.create_user_privateKey :  configUtils.getCreateUserPrivateKey();

		String nodeId = isTest ? BaseParamDTO.nodeId :  configUtils.getNodeId();
		String chainId = isTest ? BaseParamDTO.chainId :   configUtils.getChainId();
		String ledgerId = isTest ? BaseParamDTO.leadgerId :  configUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);

		paramMap.put("transaction_id", assetTransferSignFormSubmitVO.getTransactionId());
		paramMap.put("asset_type", 1);

		JSONArray signList = JSONArray.parseArray(assetTransferSignFormSubmitVO.getSignList());
		for (int i = 0; i < signList.size(); i++) {
			JSONObject jsonObject = signList.getJSONObject(i);
			String s = jsonObject.getString("sign_str");
			String sign = TrustSDK.SignRenString(assetTransferSignFormSubmitVO.getUserPrivateKey(), Hex.decodeHex(s.toCharArray()));
			jsonObject.put("sign", sign);

		}
		
		String tempString = assetTransferSignFormSubmitVO.getSignList();
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
			String sign = TrustSDK.SignRenString(BaseParamDTO.coin_privateKey, Hex.decodeHex(signStr.toCharArray()));

			json.put("sign", sign);
			newArray.add(json);
		}
		paramMap.put("sign_list", newArray);
		
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
