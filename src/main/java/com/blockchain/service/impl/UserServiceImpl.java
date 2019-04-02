package com.blockchain.service.impl;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.dto.AccountQueryFormDTO;
import com.blockchain.dto.AssetDTO;
import com.blockchain.dto.AssetTransQueryFormDTO;
import com.blockchain.dto.ConfigDto;
import com.blockchain.dto.KeyInfoDTO;
import com.blockchain.dto.TransInfoDto;
import com.blockchain.dto.UserFormDTO;
import com.blockchain.dto.UserKeyDTO;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.UserService;
import com.blockchain.util.ConfigUtils;
import com.blockchain.util.ResultUtil;
import com.blockchain.util.UserUtil;
import com.blockchain.vo.UserInfoVO;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.bean.PairKey;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.tencent.trustsql.sdk.util.HttpClientUtil;
import com.tencent.trustsql.sdk.util.SimpleHttpClient;

@Service("UserService")
public class UserServiceImpl implements UserService {
	public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public UserKeyDTO generatePairKey(UserKeyDTO userKeyModel) throws TrustSDKException, UnsupportedEncodingException {

		String privateKey = "";
		String publicKey = "";
		String afterTrustSql = "";
		PairKey pairKey = null;

		pairKey = TrustSDK.generatePairKey(true);
		privateKey = pairKey.getPrivateKey();
		publicKey = pairKey.getPublicKey();
		afterTrustSql = TrustSDK.signString(privateKey, "Tencent TrustSQL".getBytes("UTF-8"), false);

		userKeyModel.setPrivateKey(privateKey);
		userKeyModel.setPublicKey(publicKey);
		userKeyModel.setAfterTrustKey(afterTrustSql);

		return userKeyModel;
	}

	private UserInfoVO generateUserInfo(UserFormDTO userFormDTO) {
		UserInfoVO userInfoVO = new UserInfoVO();
		userInfoVO.setId(userFormDTO.getId());
		userInfoVO.setName(userFormDTO.getName());
		return userInfoVO;
	}

	@Override
	public List<AssetDTO> accountQuery(AccountQueryFormDTO assetFormVO) throws TrustSDKException, Exception {

		String accountQueryString = UserUtil.generateAccountQueryParam(assetFormVO);
		logger.debug("调用【资产查询前{}", accountQueryString);
		ConfigDto configDto = assetFormVO.getConfigDto();
		String url = configDto.getHost() + "/asset_account_query";
		String accountQueryResult = HttpClientUtil.post(url, accountQueryString);
		logger.debug("调用的IP接口是{}",url);
		logger.debug("调用【资产查询后】{}" , accountQueryResult);
		ResultUtil.checkResultIfSuccess("资产查询接口", accountQueryResult);

		JSONObject userRegistRetData = JSON.parseObject(accountQueryResult);
		JSONArray jsonArray = JSON.parseArray(userRegistRetData.getString("asset_list"));
		List<AssetDTO> assetList = new LinkedList<AssetDTO>();
		if (jsonArray != null) {

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject o = jsonArray.getJSONObject(i);
				AssetDTO assetDTO = new AssetDTO();
				assetDTO.setAmount(o.getLong("amount"));
				assetDTO.setAssetAccount(assetFormVO.getAssetAccount());
				assetDTO.setAssetId(o.getString("asset_id"));
				assetDTO.setAssetType(o.getInteger("asset_type"));
				assetDTO.setState(o.getInteger("state"));
				assetDTO.setContent(o.getJSONObject("content"));
				assetList.add(assetDTO);

			}
			// 从大金额到小金额排序
			Collections.sort(assetList, new Comparator<AssetDTO>() {
				@Override
				public int compare(AssetDTO one, AssetDTO another) {
					return (int) -(one.getAmount() - another.getAmount());
				}
			});
		}

		return assetList;
	}

	

	@Override
	public UserInfoVO addUserHasBaseAccount(UserFormDTO userFormDTO) throws ServiceException, UnsupportedEncodingException, TrustSDKException, Exception {
		SimpleHttpClient httpClient = new SimpleHttpClient();
		// 申请原始帐号
		UserInfoVO userInfoVO = generateUserInfo(userFormDTO);
		String userRegistRequestString = UserUtil.generateUserRequest(userInfoVO, userFormDTO);
		logger.info("创建账号的 信息为" + userRegistRequestString);

		String userRegistResult = httpClient.post("https://baas.qq.com/tpki/tpki.TpkiSrv.UserApply", userRegistRequestString);
		ResultUtil.checkResultIfSuccess("申请用户", userRegistResult);
		JSONObject userRegistRetData = JSON.parseObject(userRegistResult);

		String userBaseAccount = userRegistRetData.getString("user_address");

		userInfoVO.setBaseAccountAddress(userBaseAccount);

		return userInfoVO;
	}

	@Override
	public void checkPairKey(KeyInfoDTO keyInfo) throws TrustSDKException, ServiceException {
		String privateKey = keyInfo.getPrivateKey().trim();
		String publicKey = keyInfo.getPublicKey().trim();
		boolean isPair = TrustSDK.checkPairKey(privateKey, publicKey);
		if (isPair == false) {
			throw new ServiceException().errorCode(StatusCode.PARAM_ERROR).errorMessage("公私钥匹配错误");
		}
	}
}