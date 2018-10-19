package com.blockchain.controller.factory;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.ethereum.EthAssetService;
import com.blockchain.service.ethereum.EthConfigService;
import com.blockchain.service.ethereum.EthUserService;
import com.blockchain.service.ethereum.dto.EthAccountQueryFormDto;
import com.blockchain.service.ethereum.dto.EthAssetIssueFormDto;
import com.blockchain.service.ethereum.dto.EthAssetSettleDto;
import com.blockchain.service.ethereum.dto.EthAssetTransferFormDto;
import com.blockchain.service.ethereum.dto.EthUserFormDto;
import com.blockchain.service.ethereum.impl.EthAssetServiceImpl;
import com.blockchain.service.ethereum.impl.EthConfigServiceImpl;
import com.blockchain.service.ethereum.impl.EthUserServiceImpl;
import com.blockchain.service.ethereum.vo.EthAndTokenAssetVo;
import com.blockchain.service.ethereum.vo.EthAssetBurnVo;
import com.blockchain.service.ethereum.vo.EthAssetTransferVo;
import com.blockchain.service.ethereum.vo.EthereumWalletInfo;
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
import com.blockchain.service.tencent.util.BeanUtils;
import com.blockchain.util.ValidatorUtil;
import com.blockchain.validate.group.EthValidateGroup;

public class EthCoinBase implements CoinBase {

	EthAssetService ethAssetService = new EthAssetServiceImpl();
	EthConfigService ethConfigService = new EthConfigServiceImpl();
	EthUserService ethUserService = new EthUserServiceImpl();
	
	@Override
	public JSONObject issue(AssetIssueFormDto assetIssueFormDto) throws Exception {

		new ValidatorUtil().validate(assetIssueFormDto, EthValidateGroup.class);
		EthAssetIssueFormDto ethassetFormDTO = new EthAssetIssueFormDto();
		BeanUtils.copyProperties(assetIssueFormDto, ethassetFormDTO);

		return (JSONObject) JSONObject.toJSON(ethAssetService.issueToken(ethassetFormDTO));
	}

	@Override
	public JSONObject transfer(AssetTransferFormDto assetTransferFormDto) throws TrustSDKException, Exception {

		
		new ValidatorUtil().validate(assetTransferFormDto, EthValidateGroup.class);
		EthAssetTransferFormDto ethAssetTransferFormDto = new EthAssetTransferFormDto();
		BeanUtils.copyProperties(assetTransferFormDto, ethAssetTransferFormDto);
		EthAssetTransferVo ethAssetTransferVo = ethAssetService.transferToken(ethAssetTransferFormDto);
		
		return (JSONObject) JSONObject.toJSON(ethAssetTransferVo);
	}

	@Override
	public JSONObject settle(AssetSettleFormDto assetSettleFormDto) throws UnsupportedEncodingException, TrustSDKException, Exception {

		new ValidatorUtil().validate(assetSettleFormDto, EthValidateGroup.class);
		EthAssetSettleDto ethAssetSettleDto = new EthAssetSettleDto();
		BeanUtils.copyProperties(assetSettleFormDto, ethAssetSettleDto);
		 EthAssetBurnVo ethAssetBurnVo = ethAssetService.settleToken(ethAssetSettleDto);

		return  (JSONObject) JSONObject.toJSON(ethAssetBurnVo);
	}

	@Override
	public JSONObject issueSubmit(AssetIssueSubmitFormDto assetForm) throws UnsupportedEncodingException, TrustSDKException, Exception {
		throw new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	@Override
	public JSONObject transSubmit(AssetTransferSubmitFormDto assetForm) throws ServiceException, TrustSDKException, Exception {
		throw new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);	}

	@Override
	public JSONObject settleSubmit(AssetSettleSubmitFormDto assetForm) throws Exception {
		throw new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	@Override
	public JSONObject add(ConfigPropertiesFormDto configPropertiesFormDto) throws TrustSDKException, ServiceException {
		new ValidatorUtil().validate(configPropertiesFormDto, EthValidateGroup.class);
		ethConfigService.add(configPropertiesFormDto);
		ConfigPropertiesFormDto s = ethConfigService.get();
		return (JSONObject) JSON.toJSON(s);
	}

	@Override
	public JSONObject get() {
		ConfigPropertiesFormDto config = ethConfigService.get();
		return (JSONObject) JSON.toJSON(config);
	}


	

	@Override
	public void checkPairKey(KeyInfoDto keyInfo) throws ServiceException {
		ethUserService.checkPairKey(keyInfo);
	
	}

	@Override
	public JSONObject addUserHasBaseAccount(UserFormDto userFormDto) throws ServiceException, TrustSDKException, UnsupportedEncodingException, Exception {
		new ValidatorUtil().validate(userFormDto, EthValidateGroup.class);
		EthUserFormDto ethUserFormDto = new EthUserFormDto();
		String password = userFormDto.getPassword();
		ethUserFormDto.setPassword(password);
		EthereumWalletInfo ethereumWalletInfo = ethUserService.addUserHasBaseAccount(ethUserFormDto);
		return (JSONObject) JSON.toJSON(ethereumWalletInfo);
	}

	@Override
	public JSONObject accountQuery(AccountQueryFormDto assetFormVO) throws TrustSDKException, Exception {
		EthAccountQueryFormDto ethAccountQueryFormDto = new EthAccountQueryFormDto();
		BeanUtils.copyProperties(assetFormVO, ethAccountQueryFormDto);
		EthAndTokenAssetVo tokenAndEth = ethUserService.ethereumAccountQuery(ethAccountQueryFormDto);
		
		return (JSONObject) JSON.toJSON(tokenAndEth);
	}

}
