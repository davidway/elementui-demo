package blockchain;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.util.ConfigUtils;
import com.blockchain.vo.UserInfoVO;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.tencent.trustsql.sdk.util.SignStrUtil;

public class BlockChainUtils {

	public static String generateServiceApplyDto(ServiceApplyFormDto serviceApplyFormDto) throws UnsupportedEncodingException, TrustSDKException, Exception {
		ConfigUtils configUtils = new ConfigUtils();
		String mchId = configUtils.getMchId();
		String nodeId = configUtils.getNodeId();
		
		String prvKey = configUtils.getCreateUserPrivateKey();

		String chainId = configUtils.getChainId();
		String createUserPublicKey = configUtils.getCreateUserPublicKey();

		JSONObject nodeInfo = new JSONObject();
		nodeInfo.put("name", "");
		nodeInfo.put("outer_ip", "");
		nodeInfo.put("Inner_ip", "");
		
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", "1.0");
		paramMap.put("sign_type", "ECDSA");
		paramMap.put("mch_id", mchId);

		paramMap.put("mch_pubkey", createUserPublicKey);

		paramMap.put("chain_id", chainId);
		

		paramMap.put("node_id",nodeId );

		paramMap.put("node_info",nodeInfo );

		
		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject(true);
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	

}
