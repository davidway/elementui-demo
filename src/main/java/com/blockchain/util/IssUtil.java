package com.blockchain.util;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.dto.AssetSubmitFormDTO;
import com.blockchain.dto.ConfigDto;
import com.blockchain.dto.IssDto;
import com.blockchain.dto.IssSearchDto;
import com.blockchain.vo.IssVo;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.util.SignStrUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.TreeMap;

public class IssUtil {

    private static final Integer APPLY_TYPE = 1;
    private static final Integer SUBMIT_TYPE = 2;
    public static String generateApplyParam(IssDto issDto) throws Exception {
        ConfigDto configDto = issDto.getConfigDto();
        String mchId = configDto.getMchId();
        String createUserPrivateKey = configDto.getCreateUserPrivateKey();

        String chainId = configDto.getChainId();
        String createUserPublicKey = configDto.getCreateUserPublicKey();

        Map<String, Object> paramMap = new TreeMap<String, Object>();
        paramMap.put("version", "2.0");
        paramMap.put("sign_type", "ECDSA");
        paramMap.put("mch_id", mchId);

        paramMap.put("mch_pubkey", createUserPublicKey);

        paramMap.put("chain_id", chainId);
        JSONObject content = issDto.getContent();
        paramMap.put("content",content );
        JSONObject extraInfo = issDto.getExtraInfo();
        paramMap.put("extra_info", issDto.getExtraInfo());
        String account = issDto.getAccount();
        paramMap.put("account", account);
        String pulicKey = issDto.getUserPublicKey();
        paramMap.put("public_key", pulicKey);
        String sign = "";

        paramMap.put("sign", sign);


        paramMap.put("timestamp", System.currentTimeMillis() / 1000);
        paramMap.put("mch_sign", TrustSDK.signString(createUserPrivateKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
        // generate post data
        JSONObject postJson = new JSONObject(true);
        for (String key : paramMap.keySet()) {
            postJson.put(key, paramMap.get(key));
        }
        return postJson.toJSONString();
    }



    public static IssVo generateIssVo(IssVo issVo, String submitResultString) {
        return new IssVo();
    }

    public static String generateSubmitParam(IssDto issDto,JSONObject jsonObject) throws Exception {
        ConfigDto configDto = issDto.getConfigDto();
        String mchId = configDto.getMchId();
        String createUserPrivateKey = configDto.getCreateUserPrivateKey();

        String chainId = configDto.getChainId();
        String createUserPublicKey = configDto.getCreateUserPublicKey();

        Map<String, Object> paramMap = new TreeMap<String, Object>();
        paramMap.put("version", "2.0");
        paramMap.put("sign_type", "ECDSA");
        paramMap.put("mch_id", mchId);

        paramMap.put("mch_pubkey", createUserPublicKey);

        paramMap.put("chain_id", chainId);
        JSONObject content = issDto.getContent();
        paramMap.put("content",content );
        JSONObject extraInfo = issDto.getExtraInfo();
        paramMap.put("extra_info", issDto.getExtraInfo());
        String account = issDto.getAccount();
        paramMap.put("account", account);
        String pulicKey = issDto.getUserPublicKey();
        paramMap.put("public_key", pulicKey);

        String signStr = jsonObject.getString("sign_str");
        String sign = TrustSDK.SignRenString(issDto.getUserPrivateKey(), Hex.decodeHex(signStr.toCharArray()));
        paramMap.put("sign", sign);


        paramMap.put("timestamp", System.currentTimeMillis() / 1000);
        paramMap.put("mch_sign", TrustSDK.signString(createUserPrivateKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
        // generate post data
        JSONObject postJson = new JSONObject(true);
        for (String key : paramMap.keySet()) {
            postJson.put(key, paramMap.get(key));
        }
        return postJson.toJSONString();
    }

    public static String generateSearchParam(IssSearchDto issDto) throws Exception {
        ConfigDto configDto = issDto.getConfigDto();
        String mchId = configDto.getMchId();
        String createUserPrivateKey = configDto.getCreateUserPrivateKey();

        String chainId = configDto.getChainId();
        String createUserPublicKey = configDto.getCreateUserPublicKey();

        Map<String, Object> paramMap = new TreeMap<String, Object>();
        paramMap.put("version", "2.0");
        paramMap.put("sign_type", "ECDSA");
        paramMap.put("mch_id", mchId);

        paramMap.put("mch_pubkey", createUserPublicKey);

        paramMap.put("chain_id", chainId);


        JSONObject content = issDto.getContent();
        paramMap.put("content",content );
        JSONObject extraInfo = issDto.getExtraInfo();
        paramMap.put("extra_info", extraInfo);
        String account = issDto.getAccount();
        String tHash = issDto.gettHash();
        String[] bHeight = issDto.getbHeight();
        String[] bTime = issDto.getbTime();
        if (StringUtils.isNotBlank(account) ){
            paramMap.put("account", account);
        }if (StringUtils.isNotBlank(tHash)){
            paramMap.put("t_hash", tHash);
        }if (bTime!=null &&bTime.length>0){
            paramMap.put("b_height", bHeight);
        }if (bTime!=null &&bTime.length>0){
            paramMap.put("b_time", bTime);
        }
        String pulicKey = issDto.getUserPublicKey();
        paramMap.put("public_key", pulicKey);



        paramMap.put("timestamp", System.currentTimeMillis() / 1000);
        paramMap.put("mch_sign", TrustSDK.signString(createUserPrivateKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
        // generate post data
        JSONObject postJson = new JSONObject(true);
        for (String key : paramMap.keySet()) {
            postJson.put(key, paramMap.get(key));
        }
        return postJson.toJSONString();
    }
}
