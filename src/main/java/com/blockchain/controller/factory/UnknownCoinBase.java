package com.blockchain.controller.factory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.controller.factory.CoinBase;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.dto.EthTransInfoDto;
import com.blockchain.service.ethereum.dto.EthAccountQueryFormDto;
import com.blockchain.service.ethereum.dto.EthUserFormDto;
import com.blockchain.service.ethereum.dto.GasInfoDto;
import com.blockchain.service.tencent.dto.AccountQueryFormDto;
import com.blockchain.service.tencent.dto.AssetIssueFormDto;
import com.blockchain.service.tencent.dto.AssetIssueSubmitFormDto;
import com.blockchain.service.tencent.dto.AssetSettleFormDto;
import com.blockchain.service.tencent.dto.AssetSettleSubmitFormDto;
import com.blockchain.service.tencent.dto.AssetTransQueryFormDto;
import com.blockchain.service.tencent.dto.AssetTransferFormDto;
import com.blockchain.service.tencent.dto.AssetTransferSubmitFormDto;
import com.blockchain.service.tencent.dto.ConfigPropertiesFormDto;
import com.blockchain.service.tencent.dto.KeyInfoDto;
import com.blockchain.service.tencent.dto.UserFormDto;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;

public class UnknownCoinBase implements CoinBase {

	@Override
	public JSONObject issue(AssetIssueFormDto assetIssueFormDto) throws ServiceException, Exception {
		throw new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	@Override
	public JSONObject transfer(AssetTransferFormDto assetTransferFormDto) throws TrustSDKException, Exception {
		throw new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	@Override
	public JSONObject settle(AssetSettleFormDto assetSettleFormDto) throws UnsupportedEncodingException, TrustSDKException, Exception {
		throw new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	@Override
	public JSONObject issueSubmit(AssetIssueSubmitFormDto assetForm) throws UnsupportedEncodingException, TrustSDKException, Exception {
		throw new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	@Override
	public JSONObject transSubmit(AssetTransferSubmitFormDto assetForm) throws ServiceException, TrustSDKException, Exception {
		throw new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	@Override
	public JSONObject settleSubmit(AssetSettleSubmitFormDto assetForm) throws Exception {
		throw new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	@Override
	public JSONObject add(ConfigPropertiesFormDto configPropertiesFormDto) throws TrustSDKException, ServiceException {
		throw new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	@Override
	public JSONObject get() throws ServiceException {
		throw new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}



	@Override
	public void checkPairKey(KeyInfoDto keyInfo) throws ServiceException {
		throw new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	@Override
	public JSONObject addUserHasBaseAccount(UserFormDto userFormDto) throws ServiceException, TrustSDKException, UnsupportedEncodingException, Exception {
		throw new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	@Override
	public JSONObject accountQuery(AccountQueryFormDto assetFormVO) throws TrustSDKException, Exception {
		throw new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	@Override
	public JSONObject getGasInfo(GasInfoDto gasInfoDto) throws ServiceException, IOException {
		throw  new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	@Override
	public JSONObject getTransInfo(EthTransInfoDto ethTransInfo) throws ServiceException {
		throw  new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	@Override
	public JSONObject getUserInfo(String privateKey, String password) throws ServiceException {
		throw  new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	@Override
	public JSONObject transQuery(AssetTransQueryFormDto assetForm) throws ServiceException {
		throw  new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	

}
