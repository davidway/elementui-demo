package com.blockchain.controller.factory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.dto.EthTransInfoDto;
import com.blockchain.service.ethereum.dto.GasInfoDto;
import com.blockchain.service.tencent.AssetService;
import com.blockchain.service.tencent.ConfigPropertiesService;
import com.blockchain.service.tencent.TencentUserService;
import com.blockchain.service.tencent.dto.AccountQueryFormDto;
import com.blockchain.service.tencent.dto.AssetDto;
import com.blockchain.service.tencent.dto.AssetIssueDto;
import com.blockchain.service.tencent.dto.AssetIssueFormDto;
import com.blockchain.service.tencent.dto.AssetIssueSubmitFormDto;
import com.blockchain.service.tencent.dto.AssetSettleDto;
import com.blockchain.service.tencent.dto.AssetSettleFormDto;
import com.blockchain.service.tencent.dto.AssetSettleSubmitFormDto;
import com.blockchain.service.tencent.dto.AssetTransQueryFormDto;
import com.blockchain.service.tencent.dto.AssetTransferDto;
import com.blockchain.service.tencent.dto.AssetTransferFormDto;
import com.blockchain.service.tencent.dto.AssetTransferSubmitFormDto;
import com.blockchain.service.tencent.dto.ConfigPropertiesFormDto;
import com.blockchain.service.tencent.dto.KeyInfoDto;
import com.blockchain.service.tencent.dto.TransInfoDto;
import com.blockchain.service.tencent.dto.UserFormDto;
import com.blockchain.service.tencent.impl.ConfigPropertiesServiceImpl;
import com.blockchain.service.tencent.impl.TencentAssetServiceImpl;
import com.blockchain.service.tencent.impl.TencentUserServiceImpl;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;
import com.blockchain.service.tencent.vo.UserInfoVo;
import com.blockchain.util.ParamUtils;
import com.blockchain.util.TrustSDKUtil;
import com.blockchain.util.ValidatorUtil;
import com.blockchain.validate.group.TencentValidateGroup;

public class TencentCoinBase implements CoinBase {
	TencentUserService tencentUserService = new TencentUserServiceImpl();
	AssetService assetService = new TencentAssetServiceImpl();
	ConfigPropertiesService configPropertiesService = new ConfigPropertiesServiceImpl();

	@Override
	public JSONObject issue(AssetIssueFormDto assetIssueFormDto) throws ServiceException, Exception {

		new ValidatorUtil().validate(assetIssueFormDto, TencentValidateGroup.class);
		AssetIssueDto assetIssueDto = assetService.issue(assetIssueFormDto);

		return (JSONObject) JSON.toJSON(assetIssueDto);

	}

	@Override
	public JSONObject transfer(AssetTransferFormDto assetTransferFormDto) throws TrustSDKException, Exception {
		new ValidatorUtil().validate(assetTransferFormDto, TencentValidateGroup.class);
		ParamUtils.checkAssetNum(assetTransferFormDto.getSrcAsset());
		TrustSDKUtil.checkPrivateKeyAccountIsMatch(assetTransferFormDto.getUserPrivateKey(), assetTransferFormDto.getSrcAccount());
		AssetTransferDto assetTransferDto = assetService.transfer(assetTransferFormDto);

		return (JSONObject) JSON.toJSON(assetTransferDto);
	}

	@Override
	public JSONObject settle(AssetSettleFormDto assetSettleFormDto) throws UnsupportedEncodingException, TrustSDKException, Exception {
		new ValidatorUtil().validate(assetSettleFormDto, TencentValidateGroup.class);
		TrustSDKUtil.checkPrivateKeyAccountIsMatch(assetSettleFormDto.getUserPrivateKey(), assetSettleFormDto.getOwnerAccount());
		ParamUtils.checkAssetNum(assetSettleFormDto.getSrcAsset());
		AssetSettleDto assetSettleDto = assetService.settle(assetSettleFormDto);

		return (JSONObject) JSON.toJSON(assetSettleDto);
	}

	@Override
	public JSONObject issueSubmit(AssetIssueSubmitFormDto assetForm) throws UnsupportedEncodingException, TrustSDKException, Exception {
		new ValidatorUtil().validate(assetForm, TencentValidateGroup.class);
		AssetIssueDto assetIssueDto = assetService.issueSubmit(assetForm);
		return (JSONObject) JSON.toJSON(assetIssueDto);
	}

	@Override
	public JSONObject transSubmit(AssetTransferSubmitFormDto assetForm) throws ServiceException, TrustSDKException, Exception {
		new ValidatorUtil().validate(assetForm, TencentValidateGroup.class);
		AssetTransferDto assetTransferDto = assetService.transSubmit(assetForm);
		return (JSONObject) JSON.toJSON(assetTransferDto);
	}

	@Override
	public JSONObject settleSubmit(AssetSettleSubmitFormDto assetForm) throws Exception {
		new ValidatorUtil().validate(assetForm, TencentValidateGroup.class);
		AssetSettleDto assetSettleDto = assetService.settleSubmit(assetForm);
		return (JSONObject) JSON.toJSON(assetSettleDto);
	}

	@Override
	public JSONObject add(ConfigPropertiesFormDto configPropertiesFormDto) throws TrustSDKException, ServiceException {
		new ValidatorUtil().validate(configPropertiesFormDto, TencentValidateGroup.class);
		TrustSDKUtil.checkPariKeyMatch(configPropertiesFormDto.getCreateUserPublicKey(), configPropertiesFormDto.getCreateUserPrivateKey());
		configPropertiesService.add(configPropertiesFormDto);
		configPropertiesFormDto = configPropertiesService.get();
		return (JSONObject) JSON.toJSON(configPropertiesFormDto);
	}

	@Override
	public JSONObject get() {
		ConfigPropertiesFormDto configPropertiesFormDto = new ConfigPropertiesFormDto();
		configPropertiesFormDto = configPropertiesService.get();
		return (JSONObject) JSON.toJSON(configPropertiesFormDto);
	}

	@Override
	public void checkPairKey(KeyInfoDto keyInfo) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONObject addUserHasBaseAccount(UserFormDto userFormDto) throws ServiceException, TrustSDKException, UnsupportedEncodingException, Exception {

		new ValidatorUtil().validate(userFormDto, TencentValidateGroup.class);
		UserInfoVo userInfoVO = tencentUserService.addUserHasBaseAccount(userFormDto);
		return (JSONObject) JSON.toJSON(userInfoVO);
	}

	@Override
	public JSONObject accountQuery(AccountQueryFormDto assetFormVO) throws TrustSDKException, Exception {
		new ValidatorUtil().validate(assetFormVO, TencentValidateGroup.class);
		List<AssetDto> assetList = tencentUserService.accountQuery(assetFormVO);

		return (JSONObject) JSON.toJSON(assetList);
	}

	@Override
	public JSONObject getGasInfo(GasInfoDto gasInfoDto) throws ServiceException, IOException {
		throw new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	@Override
	public JSONObject getTransInfo(EthTransInfoDto ethTransInfo) throws ServiceException {
		throw new ServiceException().errorCode(StatusCode.METHOD_NO_SUPPORT).errorMessage(StatusCode.METHOD_NO_SUPPORT_MESSAGE);
	}

	@Override
	public JSONObject getUserInfo(String privateKey, String password) throws ServiceException, TrustSDKException {
		UserInfoVo userInfoVO = new UserInfoVo();
		userInfoVO = tencentUserService.getUserInfo(privateKey);

		return (JSONObject) JSON.toJSON(userInfoVO);
	}

	@Override
	public JSONObject transQuery(AssetTransQueryFormDto assetForm) throws ServiceException, TrustSDKException, Exception {
		new ValidatorUtil().validate(assetForm, TencentValidateGroup.class);
		List<TransInfoDto> assetList = tencentUserService.transQuery(assetForm);

		return (JSONObject) JSON.toJSON(assetList);
	}

}
