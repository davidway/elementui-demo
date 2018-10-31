package com.blockchain.util;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.dto.BlockChainBowerDto;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.tencent.trustsql.sdk.util.SignStrUtil;

public class TencentChainUtils {

	public static String generateChainByNodeParam() throws UnsupportedEncodingException, TrustSDKException, Exception {

	
		ConfigUtils configUtils = new ConfigUtils();
		String mchId = configUtils.getMchId(); 
	//	String prvKey = configUtils.getCreateUserPrivateKey();
		String nodeId = configUtils.getNodeId();
		String publicKey = configUtils.getCreateUserPublicKey();

		long timeStamp =System.currentTimeMillis() / 1000;
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("node_id", nodeId);
		paramMap.put("mch_id", mchId);
		paramMap.put("timestamp", timeStamp);

		paramMap.put("mch_sign", TrustSDK.signString(publicKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));

		JSONObject postJson = new JSONObject(true);
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public static String generateChainInfo() throws UnsupportedEncodingException, TrustSDKException, Exception {
		ConfigUtils configUtils = new ConfigUtils();

		String mchId = configUtils.getMchId();
		String prvKey = configUtils.getCreateUserPrivateKey();

		String chainId = configUtils.getChainId();

		long timeStamp =System.currentTimeMillis() / 1000;
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("chain_id", chainId);

		paramMap.put("mch_id", mchId);
		paramMap.put("timestamp", timeStamp);

		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject(true);
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public static String generateGetTxByHeight() throws UnsupportedEncodingException, TrustSDKException, Exception {
		ConfigUtils configUtils = new ConfigUtils();

		String mchId = configUtils.getMchId();
		String prvKey = configUtils.getCreateUserPrivateKey();

		String chainId = configUtils.getChainId();
		String nodeId = configUtils.getNodeId();
		long timeStamp = System.currentTimeMillis() / 1000;

		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("chain_id", chainId);
		paramMap.put("node_id", nodeId);
		paramMap.put("begin_height", 0);
		paramMap.put("end_height", 10);

		paramMap.put("mch_id", mchId);
		paramMap.put("timestamp", timeStamp);

		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject(true);
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public static LinkedList<BlockChainBowerDto> genereateChainByNodeResult(String applyResultString) {
		BlockChainBowerDto blockChainBowerDto = new BlockChainBowerDto();
		JSONObject resultJson = JSON.parseObject(applyResultString);
		JSONArray chainList = resultJson.getJSONArray("data");
		LinkedList<BlockChainBowerDto> list = new LinkedList<BlockChainBowerDto>();
		for (int i = 0; i < chainList.size(); i++) {
			JSONObject json = chainList.getJSONObject(i);
			String chainId = json.getString("chain_id");
			String chainName = json.getString("chain_name");
			BlockChainBowerDto temp = new BlockChainBowerDto();
			blockChainBowerDto.setChainId(chainId);
			blockChainBowerDto.setChainName(chainName);
			list.add(blockChainBowerDto);
		}

		return list;
	}

}
