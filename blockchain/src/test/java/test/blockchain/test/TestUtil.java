package test.blockchain.test;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.DTO.AssetTransferFormDTO;
import com.blockchain.DTO.BaseParamDTO;
import com.blockchain.util.ConfigUtils;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.tencent.trustsql.sdk.util.SignStrUtil;


public class TestUtil {

	static boolean isTest = true;

	public static String generateAssetSumParam() throws TrustSDKException, Exception {

		String mchId = isTest ? BaseParamDTO.mchId : ConfigUtils.getMchId();
		String prvKey = isTest ? BaseParamDTO.create_user_privateKey : ConfigUtils.getCreateUserPrivateKey();

		String nodeId = isTest ? BaseParamDTO.nodeId : ConfigUtils.getNodeId();
		String chainId = isTest ? BaseParamDTO.chainId : ConfigUtils.getChainId();
		String ledgerId = isTest ? BaseParamDTO.leadgerId : ConfigUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);
		paramMap.put("date","2018-06-13");
		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);
		paramMap.put("asset_type",1);
		
		JSONObject state = new JSONObject();
		state.put("state", new Integer[]{0});
		paramMap.put("state", state);

		paramMap.put("timestamp", System.currentTimeMillis() / 1000);
		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes(), false));
		// generate post data
		JSONObject postJson = new JSONObject();
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public static String generateAssetDetailParam() throws TrustSDKException, Exception {
		String mchId = isTest ? BaseParamDTO.mchId : ConfigUtils.getMchId();
		String prvKey = isTest ? BaseParamDTO.create_user_privateKey : ConfigUtils.getCreateUserPrivateKey();

		String nodeId = isTest ? BaseParamDTO.nodeId : ConfigUtils.getNodeId();
		String chainId = isTest ? BaseParamDTO.chainId : ConfigUtils.getChainId();
		String ledgerId = isTest ? BaseParamDTO.leadgerId : ConfigUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);
		paramMap.put("date","2018-06-01");
		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);
		paramMap.put("asset_type",1);
		paramMap.put("page_limit",20);
		paramMap.put("page_no",1);
		
		JSONObject state = new JSONObject();
		
		paramMap.put("state", 4);

		paramMap.put("timestamp", System.currentTimeMillis() / 1000);
		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes(), false));
		// generate post data
		JSONObject postJson = new JSONObject();
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}


	public static String generateTransSumlParam( ) throws TrustSDKException, Exception {
		String mchId = isTest ? BaseParamDTO.mchId : ConfigUtils.getMchId();
		String prvKey = isTest ? BaseParamDTO.create_user_privateKey : ConfigUtils.getCreateUserPrivateKey();

		String nodeId = isTest ? BaseParamDTO.nodeId : ConfigUtils.getNodeId();
		String chainId = isTest ? BaseParamDTO.chainId : ConfigUtils.getChainId();
		String ledgerId = isTest ? BaseParamDTO.leadgerId : ConfigUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);
		paramMap.put("date","2018-06-01");
		paramMap.put("node_id", nodeId);
		paramMap.put("timestamp", System.currentTimeMillis()/1000L);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);
		paramMap.put("asset_type",1);
		paramMap.put("page_limit",20);
		paramMap.put("page_no",1);
		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes(), false));
		// generate post data
		JSONObject postJson = new JSONObject();
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public static String generateTransDetailsParam() throws TrustSDKException, Exception {
		String mchId = isTest ? BaseParamDTO.mchId : ConfigUtils.getMchId();
		String prvKey = isTest ? BaseParamDTO.create_user_privateKey : ConfigUtils.getCreateUserPrivateKey();

		String nodeId = isTest ? BaseParamDTO.nodeId : ConfigUtils.getNodeId();
		String chainId = isTest ? BaseParamDTO.chainId : ConfigUtils.getChainId();
		String ledgerId = isTest ? BaseParamDTO.leadgerId : ConfigUtils.getLedgerId();
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);
		paramMap.put("date","2018-06-01");
		paramMap.put("node_id", nodeId);

		paramMap.put("chain_id", chainId);
		paramMap.put("ledger_id", ledgerId);
		paramMap.put("asset_type",4);
		paramMap.put("page_limit",20);
		paramMap.put("page_no",1);
		paramMap.put("trans_type", 4);

		JSONObject state = new JSONObject();
		
		paramMap.put("state",2 );
		

		paramMap.put("timestamp", System.currentTimeMillis() / 1000);
		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes(), false));
		// generate post data
		JSONObject postJson = new JSONObject();
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public static String generateDamAssetTransferMidAppy(AssetTransferFormDTO assetTransferFormDTO) throws TrustSDKException, Exception {

		String mchId = isTest ? BaseParamDTO.mchId : ConfigUtils.getMchId();
		String prvKey = isTest ? BaseParamDTO.create_user_privateKey : ConfigUtils.getCreateUserPrivateKey();

		String nodeId = isTest ? BaseParamDTO.nodeId : ConfigUtils.getNodeId();
		String chainId = isTest ? BaseParamDTO.chainId : ConfigUtils.getChainId();
		String ledgerId = isTest ? BaseParamDTO.leadgerId : ConfigUtils.getLedgerId();
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
		if (null != assetTransferFormDTO.getFeeAmount()) {
			paramMap.put("fee_amount", assetTransferFormDTO.getFeeAmount());
		}
		// TODO 区块链工作人员称暂时不支持多个客户提交

		paramMap.put("src_asset_id", assetTransferFormDTO.getSrcAsset());

		paramMap.put("asset_type", "1");
		paramMap.put("amount",  assetTransferFormDTO.getAmount());
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("测试信息", "呵呵");
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

}
