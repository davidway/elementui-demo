package com.blockchain.controller.factory;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.exception.ServiceException;
import com.blockchain.service.ethereum.dto.EthAccountQueryFormDto;
import com.blockchain.service.ethereum.dto.EthUserFormDto;
import com.blockchain.service.tencent.dto.AccountQueryFormDto;
import com.blockchain.service.tencent.dto.AssetIssueFormDto;
import com.blockchain.service.tencent.dto.AssetIssueSubmitFormDto;
import com.blockchain.service.tencent.dto.AssetSettleFormDto;
import com.blockchain.service.tencent.dto.AssetSettleSubmitFormDto;
import com.blockchain.service.tencent.dto.AssetTransferFormDto;
import com.blockchain.service.tencent.dto.AssetTransferSubmitFormDto;
import com.blockchain.service.tencent.dto.ConfigPropertiesFormDto;
import com.blockchain.service.tencent.dto.KeyInfoDto;
import com.blockchain.service.tencent.dto.UserFormDto;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;

public interface CoinBase {

	public JSONObject issue(AssetIssueFormDto assetIssueFormDto) throws ServiceException, Exception;

	public JSONObject transfer(AssetTransferFormDto assetTransferFormDto) throws TrustSDKException, Exception;

	public JSONObject settle(AssetSettleFormDto assetSettleFormDto) throws UnsupportedEncodingException, TrustSDKException, Exception;

	JSONObject issueSubmit(AssetIssueSubmitFormDto assetForm) throws UnsupportedEncodingException, TrustSDKException, Exception;

	JSONObject transSubmit(AssetTransferSubmitFormDto assetForm) throws ServiceException, TrustSDKException, Exception;

	JSONObject settleSubmit(AssetSettleSubmitFormDto assetForm) throws Exception;

	JSONObject add(ConfigPropertiesFormDto configPropertiesFormDto) throws TrustSDKException, ServiceException;

	JSONObject get() throws ServiceException ;
	
	public JSONObject addUserHasBaseAccount(UserFormDto userFormDto) throws ServiceException, TrustSDKException, UnsupportedEncodingException, Exception;
	
	JSONObject accountQuery(AccountQueryFormDto assetFormVO) throws TrustSDKException, Exception;

	void checkPairKey(KeyInfoDto keyInfo) throws ServiceException;
	
	
}
