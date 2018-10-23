package com.blockchain.service.tencent.impl;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.tencent.TencentUserService;
import com.blockchain.service.tencent.dto.AccountQueryFormDto;
import com.blockchain.service.tencent.dto.AssetDto;
import com.blockchain.service.tencent.dto.AssetTransQueryFormDto;
import com.blockchain.service.tencent.dto.KeyInfoDto;
import com.blockchain.service.tencent.dto.TransInfoDto;
import com.blockchain.service.tencent.dto.UserFormDto;
import com.blockchain.service.tencent.dto.UserKeyDto;
import com.blockchain.service.tencent.trustsql.sdk.TrustSDK;
import com.blockchain.service.tencent.trustsql.sdk.bean.PairKey;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;
import com.blockchain.service.tencent.trustsql.sdk.util.HttpClientUtil;
import com.blockchain.service.tencent.trustsql.sdk.util.SimpleHttpClient;
import com.blockchain.service.tencent.util.ResultUtil;
import com.blockchain.service.tencent.util.UserUtil;
import com.blockchain.service.tencent.vo.UserInfoVo;


public class TencentUserServiceImpl implements TencentUserService {
	private static Logger logger = Logger.getLogger(TencentUserServiceImpl.class);



	@Override
	public UserKeyDto generatePairKey(UserKeyDto userKeyModel) throws TrustSDKException, UnsupportedEncodingException {

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

	@Override
	public UserInfoVo addUserHasBaseAndHostAccount(UserFormDto userFormDto) throws ServiceException,UnsupportedEncodingException, TrustSDKException, Exception {
		//申请原始帐号
		UserInfoVo userInfoVo = generateUserInfo(userFormDto);
		String userRegistRequestString = UserUtil.generateUserRequest(userInfoVo,userFormDto);
		SimpleHttpClient httpClient = new SimpleHttpClient();
		String userRegistResult = httpClient.post("https://baas.trustsql.qq.com/idm_v1.1/api/user_cert/register", userRegistRequestString);
		ResultUtil.checkResultIfSuccess("申请用户", userRegistResult);
		JSONObject userRegistRetData = JSON.parseObject(userRegistResult).getJSONObject("retdata");
	
		//注册原始帐号的账户
		String userBaseAccoutForm = UserUtil.generateuserAccoutForm(userInfoVo,userRegistRetData, false);
		String userBaseAccountRegistResult = httpClient.post("https://baas.trustsql.qq.com/idm_v1.1/api/account_cert/register", userBaseAccoutForm);
		logger.debug("注册原始帐号的账户返回结果:"+userBaseAccountRegistResult);
		ResultUtil.checkResultIfSuccess("申请账户",userBaseAccountRegistResult);
		JSONObject userAccoutData = JSON.parseObject(userRegistResult).getJSONObject("retdata");
		
		String userBaseAccount = userAccoutData.getString("user_address");
		if ( StringUtils.isBlank(userBaseAccount)){
			userBaseAccount = userAccoutData.getString("account_address");
		}
		userInfoVo.setBaseAccountAddress(userBaseAccount);
		
		//再设置一个新的账号作为代理账号
		 String userHostAccoutForm = UserUtil.generateuserAccoutForm(userInfoVo,userRegistRetData, true);
		String userHostAccountFormResult = httpClient.post("https://baas.trustsql.qq.com/idm_v1.1/api/account_cert/register", userHostAccoutForm);
		ResultUtil.checkResultIfSuccess("申请代理账户",userHostAccountFormResult);
		logger.debug("注册申请代理账户返回结果:"+userHostAccountFormResult);
		JSONObject userHostAccoutData = JSON.parseObject(userHostAccountFormResult).getJSONObject("retdata");
		String userHostAccount = userHostAccoutData.getString("user_address");
	
		if ( StringUtils.isBlank(userHostAccount)){
			userHostAccount = userHostAccoutData.getString("account_address");
		}
		userInfoVo.setHostWalletAccountAddress(userHostAccount);

		return userInfoVo;
	}

	private UserInfoVo generateUserInfo(UserFormDto userFormDto) {
		UserInfoVo userInfoVo = new UserInfoVo();
		userInfoVo.setId(userFormDto.getId());
		userInfoVo.setName(userFormDto.getName());
		return userInfoVo;
	}
	@Override
	public List<AssetDto> accountQuery(AccountQueryFormDto assetFormVO) throws TrustSDKException, Exception {

		String accountQueryString = UserUtil.generateAccountQueryParam(assetFormVO);
		logger.debug("调用【资产查询前】" + accountQueryString);
		String url = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_account_query_v1.cgi";
		String accountQueryResult = HttpClientUtil.post(url, accountQueryString);
		logger.debug("调用【资产查询后】" + accountQueryResult);
		ResultUtil.checkResultIfSuccess("资产查询接口", accountQueryResult);

		JSONObject userRegistRetData = JSON.parseObject(accountQueryResult);
		JSONArray jsonArray = JSON.parseArray(userRegistRetData.getString("assets_list"));
		List<AssetDto> assetList = new LinkedList<AssetDto>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject o = jsonArray.getJSONObject(i);
			AssetDto assetDto = new AssetDto();
			if ( assetFormVO.getContent()!=null){
				JSONObject content = o.getJSONObject("content");
				 if (content .equals(assetFormVO.getContent())){
					 assetDto.setAmount(o.getLong("amount"));
						assetDto.setAssetAccount(o.getString("asset_account"));
						assetDto.setAssetId(o.getString("asset_id"));
						assetDto.setAssetType(o.getInteger("asset_type"));
						assetDto.setState(o.getInteger("state"));
						assetDto.setContent(o.getJSONObject("content"));	
						assetList.add(assetDto);
				 }
			}else{
				 assetDto.setAmount(o.getLong("amount"));
					assetDto.setAssetAccount(o.getString("asset_account"));
					assetDto.setAssetId(o.getString("asset_id"));
					assetDto.setAssetType(o.getInteger("asset_type"));
					assetDto.setState(o.getInteger("state"));
					assetDto.setContent(o.getJSONObject("content"));	
				
				assetList.add(assetDto);
			}
			
			
		
		}
		// 从大金额到小金额排序
		Collections.sort(assetList, new Comparator<AssetDto>() {
			@Override
			public int compare(AssetDto one, AssetDto another) {
				return (int) -(one.getAmount() - another.getAmount());
			}
		});
		return assetList;
	}

	@Override
	public List<TransInfoDto> transQuery(AssetTransQueryFormDto assetForm) throws ServiceException, TrustSDKException, Exception {

		String accountQueryString = UserUtil.generateTransQueryParam(assetForm);
		logger.debug("调用【交易查询前】" + accountQueryString);
		String url = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_trans_query_v1.cgi";
		String accountQueryResult = HttpClientUtil.post(url, accountQueryString);
		ResultUtil.checkResultIfSuccess("交易查询接口", accountQueryResult);
		logger.debug("调用【交易查询后】" + accountQueryResult);
		JSONObject userRegistRetData = JSON.parseObject(accountQueryResult);

		JSONArray jsonArray = JSON.parseArray(userRegistRetData.getString("trans_list"));
		List<TransInfoDto> transInfoList = new LinkedList<TransInfoDto>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject o = jsonArray.getJSONObject(i);
			TransInfoDto transInfoDto = new TransInfoDto();
			transInfoDto.setAmount(o.getLong("amount"));
			transInfoDto.setDstAccount(o.getString("dst_account"));
			transInfoDto.setDstAssetId(o.getString("dst_asset_id"));
			transInfoDto.setSrcAccount(o.getString("src_account"));
			transInfoDto.setSrcAssetId(o.getString("src_asset_id"));
			transInfoDto.setTransactionId(o.getString("transaction_id"));
			transInfoDto.setTransState(o.getInteger("trans_state"));
			transInfoDto.setTransTime(o.getString("trans_time"));
			transInfoDto.setHash(o.getString("hash"));
			transInfoDto.setTransType(o.getInteger("trans_type"));
			transInfoDto.setSignList(o.getString("sign_str_list"));
			transInfoList.add(transInfoDto);
		}
		Collections.sort(transInfoList, new Comparator<TransInfoDto>() {
			@Override
			public int compare(TransInfoDto one, TransInfoDto another) {

				return (int) -(Timestamp.valueOf(one.getTransTime()).getTime() - Timestamp.valueOf(another.getTransTime()).getTime());
			}
		});
		return transInfoList;
	}
	

	@Override
	public UserInfoVo addUserHasBaseAccount(UserFormDto userFormDto) throws ServiceException,UnsupportedEncodingException, TrustSDKException, Exception {

		//申请原始帐号
		UserInfoVo userInfoVo = generateUserInfo(userFormDto);
		String userRegistRequestString = UserUtil.generateUserRequest(userInfoVo,userFormDto);
		SimpleHttpClient httpClient = new SimpleHttpClient();
		String userRegistResult = httpClient.post("https://baas.trustsql.qq.com/idm_v1.1/api/user_cert/register", userRegistRequestString);
		ResultUtil.checkResultIfSuccess("申请用户", userRegistResult);
		JSONObject userRegistRetData = JSON.parseObject(userRegistResult).getJSONObject("retdata");
	
		//注册原始帐号的账户
		String userBaseAccoutForm = UserUtil.generateuserAccoutForm(userInfoVo,userRegistRetData, false);
		String userBaseAccountRegistResult = httpClient.post("https://baas.trustsql.qq.com/idm_v1.1/api/account_cert/register", userBaseAccoutForm);
		logger.debug("注册原始帐号的账户返回结果:"+userBaseAccountRegistResult);
		ResultUtil.checkResultIfSuccess("申请账户",userBaseAccountRegistResult);
		JSONObject userAccoutData = JSON.parseObject(userRegistResult).getJSONObject("retdata");
		
		String userBaseAccount = userAccoutData.getString("user_address");
		if ( StringUtils.isBlank(userBaseAccount)){
			userBaseAccount = userAccoutData.getString("account_address");
		}
		userInfoVo.setBaseAccountAddress(userBaseAccount);
		
		return userInfoVo;
	}

	@Override
	public UserInfoVo addUserHostAccount(UserFormDto userFormDto) throws ServiceException, TrustSDKException, UnsupportedEncodingException, Exception {
		//再设置一个新的账号作为代理账号
		UserInfoVo userInfoVo = new UserInfoVo();
		userInfoVo.setId(userFormDto.getId());
		userInfoVo.setName(userFormDto.getName());
		
		String userHostAccoutForm = UserUtil.generateuserAccoutFormOnlyHostAccount(userInfoVo, userFormDto);
		SimpleHttpClient httpClient = new SimpleHttpClient();
		String userHostAccountFormResult = httpClient.post("https://baas.trustsql.qq.com/idm_v1.1/api/account_cert/register", userHostAccoutForm);
		ResultUtil.checkResultIfSuccess("申请代理账户",userHostAccountFormResult);
		logger.debug("注册申请代理账户返回结果:"+userHostAccountFormResult);
		JSONObject userHostAccoutData = JSON.parseObject(userHostAccountFormResult).getJSONObject("retdata");
		String userHostAccount = userHostAccoutData.getString("user_address");
	
		if ( StringUtils.isBlank(userHostAccount)){
			userHostAccount = userHostAccoutData.getString("account_address");
		}
		userInfoVo.setHostWalletAccountAddress(userHostAccount);

		return userInfoVo;
	}

	@Override
	public void checkPairKey(KeyInfoDto keyInfo) throws TrustSDKException, ServiceException {
		String privateKey = keyInfo.getPrivateKey().trim();
		String publicKey = keyInfo.getPublicKey().trim();
		boolean isPair = TrustSDK.checkPairKey(privateKey, publicKey);
		if ( isPair==false){
			throw new ServiceException().errorCode(StatusCode.PARAM_ERROR).errorMessage("公私钥匹配错误");
		}
	}

	@Override
	public UserInfoVo getUserInfo(String privateKey) throws TrustSDKException {
		UserInfoVo userInfoVo = new UserInfoVo();
		String baseAccoutAddress = TrustSDK.generateAddrByPrvkey(privateKey);
		String publicKey = TrustSDK.generatePubkeyByPrvkey(privateKey, true);
		userInfoVo.setBaseAccountAddress(baseAccoutAddress);
		userInfoVo.setBasePrivateKey(privateKey);
		userInfoVo.setBasePublicKey(publicKey);
		return userInfoVo;
	}
}