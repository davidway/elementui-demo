package com.blockchain.controller;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.controller.factory.CoinBase;
import com.blockchain.controller.factory.CoinBaseFactory;
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

public class CoinBaseContext implements CoinBase {

	CoinBaseFactory coinBaseFacotry;

	public void setFacotry(CoinBaseFactory coinBaseFactory) {
		this.coinBaseFacotry = coinBaseFactory;

	}

	@Override
	public JSONObject issue(AssetIssueFormDto assetIssueFormDto) throws ServiceException, Exception {
		return coinBaseFacotry.getCoinBase().issue(assetIssueFormDto);
	}

	@Override
	public JSONObject transfer(AssetTransferFormDto assetTransferFormDto) throws TrustSDKException, Exception {
		return coinBaseFacotry.getCoinBase().transfer(assetTransferFormDto);
	}

	@Override
	public JSONObject settle(AssetSettleFormDto assetSettleFormDto) throws UnsupportedEncodingException, TrustSDKException, Exception {
		return coinBaseFacotry.getCoinBase().settle(assetSettleFormDto);
	}

	@Override
	public JSONObject issueSubmit(AssetIssueSubmitFormDto assetForm) throws UnsupportedEncodingException, TrustSDKException, Exception {
		return coinBaseFacotry.getCoinBase().issueSubmit(assetForm);

	}

	@Override
	public JSONObject transSubmit(AssetTransferSubmitFormDto assetForm) throws ServiceException, TrustSDKException, Exception {
		return coinBaseFacotry.getCoinBase().transSubmit(assetForm);
	}

	@Override
	public JSONObject settleSubmit(AssetSettleSubmitFormDto assetForm) throws Exception {
		return coinBaseFacotry.getCoinBase().settleSubmit(assetForm);
	}

	@Override
	public JSONObject addUserHasBaseAccount(UserFormDto userFormDto) throws ServiceException, TrustSDKException, UnsupportedEncodingException, Exception {
		return coinBaseFacotry.getCoinBase().addUserHasBaseAccount(userFormDto);
	}

	@Override
	public JSONObject accountQuery(AccountQueryFormDto assetFormVO) throws TrustSDKException, Exception {
		return coinBaseFacotry.getCoinBase().accountQuery(assetFormVO);
	}

	@Override
	public void checkPairKey(KeyInfoDto keyInfo) throws ServiceException {
		coinBaseFacotry.getCoinBase().checkPairKey(keyInfo);

	}

	@Override
	public JSONObject add(ConfigPropertiesFormDto configPropertiesFormDto) throws TrustSDKException, ServiceException {
		return this.coinBaseFacotry.getCoinBase().add(configPropertiesFormDto);
	}

	@Override
	public JSONObject get() throws ServiceException {
		return this.coinBaseFacotry.getCoinBase().get();
	}

}
