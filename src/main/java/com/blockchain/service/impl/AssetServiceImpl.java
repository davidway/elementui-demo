package com.blockchain.service.impl;



import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.dto.AssetFormDTO;
import com.blockchain.dto.AssetIssueDTO;
import com.blockchain.dto.AssetSettleDTO;
import com.blockchain.dto.AssetSettleFormDTO;
import com.blockchain.dto.AssetSettleSubmitFormDTO;
import com.blockchain.dto.AssetSubmitFormDTO;
import com.blockchain.dto.AssetTransferDTO;
import com.blockchain.dto.AssetTransferFormDTO;
import com.blockchain.dto.AssetTransferSubmitFormDTO;
import com.blockchain.exception.ServiceException;
import com.blockchain.service.AssetService;
import com.blockchain.util.AssetPrepareUtil;
import com.blockchain.util.AssetUtil;
import com.blockchain.util.ConfigUtils;
import com.blockchain.util.ResultUtil;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.tencent.trustsql.sdk.util.HttpClientUtil;

@Service("AssetService")
public class AssetServiceImpl implements AssetService {
	public static final Logger issueLogger = LoggerFactory.getLogger("issueLogger");
	public static final Logger transferLogger = LoggerFactory.getLogger("transferLogger");
	public static final Logger settleLogger = LoggerFactory.getLogger("settleLogger");

	AssetUtil assetUtil = new AssetUtil();

	AssetPrepareUtil assetPrepareUtil = new AssetPrepareUtil();

	@Override
	public AssetIssueDTO issue(AssetFormDTO assetFormDTO) throws Exception {
		ConfigUtils configUtils = new ConfigUtils();
		issueLogger.info("issue调试");
		String applyUrl = configUtils.getHost() + "/asset_issue_apply";
		String applyString = assetUtil.generateIssueApplyParam(assetFormDTO);
		
		issueLogger.info("请求的链接{}", applyUrl);
		issueLogger.info("调用【发行申请】前的参数:{}", applyString);

		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		issueLogger.info("调用【发行申请】后的参数{}", applyResultString);
		ResultUtil.checkResultIfSuccess("资产申请接口", applyResultString);

		String userPrivateKey = assetFormDTO.getUserPrivateKey();
		AssetSubmitFormDTO assetSubmitFormDTO = assetPrepareUtil.prepareAssetSubmitForm(applyResultString);
		assetSubmitFormDTO.setUserPrivateKey(userPrivateKey);

		String submitString = assetUtil.generateIssueSubmitParam(assetSubmitFormDTO);

		String submitUrl = configUtils.getHost() + "/asset_issue_submit";
		issueLogger.info("请求的链接{}", submitUrl);
		issueLogger.info("调用【发行提交】前的参数{}",  submitString);
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);

		issueLogger.info("调用【发行提交】后的参数" + submitResultString);
		ResultUtil.checkSubmitResultIfSuccess("资产提交接口", JSON.toJSONString(assetSubmitFormDTO), submitResultString);
		issueLogger.debug("issue调试结束");
	
		AssetIssueDTO assetIssueDTO = new AssetIssueDTO();
		assetIssueDTO = assetPrepareUtil.generateAssetIssueDto(assetSubmitFormDTO, submitResultString);
		return assetIssueDTO;
	}

	@Override
	public AssetTransferDTO transfer(AssetTransferFormDTO assetTransferFormDTO) throws TrustSDKException, Exception {

		transferLogger.info("transfer调试开始");
		String applyString = assetUtil.generateTransferApplyParam(assetTransferFormDTO);
		transferLogger.info("调用【转账申请前】的参数" + applyString);
		ConfigUtils configUtils = new ConfigUtils();
		String applyUrl = configUtils.getHost() + "/asset_transfer_apply";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		transferLogger.info("调用【转账申请后】的参数" + applyResultString);

		ResultUtil.checkResultIfSuccess("资产转让申请接口", applyResultString);

		AssetTransferSubmitFormDTO asseTransfertSubmitForm = assetPrepareUtil.perpareTransferSubmitForm(assetTransferFormDTO, applyResultString);
		transferLogger.info("调用【转账申请后】的参数" + asseTransfertSubmitForm);

		String submitString = assetUtil.generateTransferSubmitParam(asseTransfertSubmitForm);
		transferLogger.info("调用【转账提交前】的参数" + submitString);
		String submitUrl = configUtils.getHost() + "/asset_transfer_submit";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		transferLogger.info("调用【转账提交后】的参数" + submitResultString);

		ResultUtil.checkSubmitResultIfSuccess("资产转让提交接口", JSONObject.toJSONString(asseTransfertSubmitForm), submitResultString);

		transferLogger.info("transfer调试结束");

		AssetTransferDTO assetTransferDTO = new AssetTransferDTO();
		assetTransferDTO = assetPrepareUtil.generateAssetTransferDTO(asseTransfertSubmitForm, submitResultString);
		return assetTransferDTO;
	}

	@Override
	public AssetSettleDTO settle(AssetSettleFormDTO assetSettleFormDTO) throws UnsupportedEncodingException, TrustSDKException, Exception {
		settleLogger.info("settle调试开始");
		String applyString = assetUtil.generateSettleApplyParam(assetSettleFormDTO);

		settleLogger.info("调用【兑换申请前】" + applyString);
		ConfigUtils configUtils = new ConfigUtils();
		String applyUrl = configUtils.getHost() + "/asset_settle_apply";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		settleLogger.info("调用【兑换申请后】" + applyResultString);

		ResultUtil.checkResultIfSuccess("资产兑换申请接口", applyResultString);

		AssetSettleSubmitFormDTO assetSettleSubmitFormDTO = assetPrepareUtil.perpareSettleSubmitForm(assetSettleFormDTO, applyResultString);
		String submitString = assetUtil.generateSettleSubmitParam(assetSettleSubmitFormDTO);

		settleLogger.info("【兑换【调用提交前】" + submitString);
		String submitUrl = configUtils.getHost() + "/asset_settle_submit";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		settleLogger.info("【兑换【调用提交】后" + submitResultString);

		ResultUtil.checkSubmitResultIfSuccess("资产提交接口", JSON.toJSONString(assetSettleSubmitFormDTO), submitResultString);
		settleLogger.info("settle调试结束");

		AssetSettleDTO assetSettleDTO = new AssetSettleDTO();
		assetSettleDTO = assetPrepareUtil.generateAssetSettleDTO(assetSettleSubmitFormDTO, submitResultString);
		return assetSettleDTO;
	}

	@Override
	public AssetIssueDTO issueSubmit(AssetSubmitFormDTO assetSubmitFormDTO) throws UnsupportedEncodingException, TrustSDKException, Exception {
		issueLogger.debug("传入的参数{}",  assetSubmitFormDTO);
		String submitString = assetUtil.generateIssueSubmitParam(assetSubmitFormDTO);
		issueLogger.debug("调用【资产发行前{}",  submitString);
		ConfigUtils configUtils = new ConfigUtils();
		String submitUrl = configUtils.getHost() + "/asset_issue_submit";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		ResultUtil.checkResultIfSuccess("资产提交接口", submitResultString);
		issueLogger.debug("调用【资产只发行后】{}",  submitResultString);
		issueLogger.debug("issue调试结束");

		AssetIssueDTO assetIssueDTO = new AssetIssueDTO();
		assetIssueDTO = assetPrepareUtil.generateAssetIssueDto(assetSubmitFormDTO, submitResultString);
		return assetIssueDTO;
	}

	@Override
	public AssetTransferDTO transSubmit(AssetTransferSubmitFormDTO asseTransfertSubmitForm) throws TrustSDKException, Exception {

		String submitString = assetUtil.generateTransferSubmitParam(asseTransfertSubmitForm);

		transferLogger.info("调用【转账只提交前】" + submitString);
		ConfigUtils configUtils = new ConfigUtils();
		String submitUrl = configUtils.getHost() + "/asset_transfer_submit";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		transferLogger.info("调用【转账只提交后】" + submitResultString);

		ResultUtil.checkResultIfSuccess("资产提交接口", submitResultString);
	
		transferLogger.info("issue调试结束");

		AssetTransferDTO assetTransferDTO = new AssetTransferDTO();
		assetTransferDTO = assetPrepareUtil.generateAssetTransferDTO(asseTransfertSubmitForm, submitResultString);
		return assetTransferDTO;
	}

	@Override
	public AssetSettleDTO settleSubmit(AssetSettleSubmitFormDTO assetSettleSubmitFormDTO) throws Exception {

		String submitString = assetUtil.generateSettleSubmitParam(assetSettleSubmitFormDTO);
		settleLogger.info("调用【兑换只提交前】" + submitString);
		ConfigUtils configUtils = new ConfigUtils();
		String submitUrl = configUtils.getHost() + "/asset_settle_submit";

		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		settleLogger.info("调用【兑换只提交后】" + submitResultString);
		ResultUtil.checkResultIfSuccess("资产提交接口", submitResultString);

		AssetSettleDTO assetSettleDTO = new AssetSettleDTO();
		assetSettleDTO = assetPrepareUtil.generateAssetSettleDTO(assetSettleSubmitFormDTO, submitResultString);
		return assetSettleDTO;
	}

	@Override
	public AssetTransferSubmitFormDTO transferApply(AssetTransferFormDTO assetTransferFormDTO) throws ServiceException, TrustSDKException, Exception {

		transferLogger.info("transfer调试开始");
		String applyString = assetUtil.generateTransferApplyParam(assetTransferFormDTO);
		transferLogger.info("调用【转账申请前】的参数" + applyString);
		ConfigUtils configUtils = new ConfigUtils();
		String applyUrl = configUtils.getHost() + "/asset_transfer_apply";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		transferLogger.info("调用【转账申请后】的参数" + applyString);

		ResultUtil.checkResultIfSuccess("资产转让申请接口", applyResultString);

		AssetTransferSubmitFormDTO asseTransfertSubmitForm = assetPrepareUtil.perpareTransferSubmitForm(assetTransferFormDTO, applyResultString);

		transferLogger.info("apply调试结束");

		return asseTransfertSubmitForm;
	}

	@Override
	public AssetSubmitFormDTO issueApply(AssetFormDTO assetFormDTO) throws ServiceException, TrustSDKException, Exception {
		ConfigUtils configUtils = new ConfigUtils();
		issueLogger.info("issue调试");
		String applyUrl = configUtils.getHost() + "/asset_issue_apply";
		String applyString = assetUtil.generateIssueApplyParam(assetFormDTO);

		issueLogger.info("请求的链接{}", applyUrl);
		issueLogger.info("调用【发行申请】前的参数:{}", applyString);

		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		issueLogger.info("调用【发行申请】后的参数{}", applyResultString);
		ResultUtil.checkResultIfSuccess("资产申请接口", applyResultString);

		String userPrivateKey = assetFormDTO.getUserPrivateKey();
		AssetSubmitFormDTO assetSubmitFormDTO = assetPrepareUtil.prepareAssetSubmitForm(applyResultString);
		assetSubmitFormDTO.setUserPrivateKey(userPrivateKey);
		return assetSubmitFormDTO;
	}

	@Override
	public AssetSettleSubmitFormDTO settleApply(AssetSettleFormDTO assetSettleFormDTO) throws ServiceException, TrustSDKException, Exception {
		settleLogger.info("settle调试开始");
		String applyString = assetUtil.generateSettleApplyParam(assetSettleFormDTO);

		settleLogger.info("调用【兑换申请前】" + applyString);
		ConfigUtils configUtils = new ConfigUtils();
		String applyUrl = configUtils.getHost() + "/asset_settle_apply";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		settleLogger.info("调用【兑换申请后】" + applyResultString);

		ResultUtil.checkResultIfSuccess("资产兑换申请接口", applyResultString);

		AssetSettleSubmitFormDTO assetSettleSubmitFormDTO = assetPrepareUtil.perpareSettleSubmitForm(assetSettleFormDTO, applyResultString);

		return assetSettleSubmitFormDTO;
	}

}
