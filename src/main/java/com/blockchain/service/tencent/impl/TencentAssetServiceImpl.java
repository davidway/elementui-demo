package com.blockchain.service.tencent.impl;


import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.exception.ServiceException;
import com.blockchain.service.tencent.AssetService;
import com.blockchain.service.tencent.dto.AssetIssueDto;
import com.blockchain.service.tencent.dto.AssetIssueFormDto;
import com.blockchain.service.tencent.dto.AssetIssueSubmitFormDto;
import com.blockchain.service.tencent.dto.AssetSettleDto;
import com.blockchain.service.tencent.dto.AssetSettleFormDto;
import com.blockchain.service.tencent.dto.AssetSettleSubmitFormDto;
import com.blockchain.service.tencent.dto.AssetTransferDto;
import com.blockchain.service.tencent.dto.AssetTransferFormDto;
import com.blockchain.service.tencent.dto.AssetTransferSubmitFormDto;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;
import com.blockchain.util.AssetPrepareUtil;
import com.blockchain.util.AssetUtil;
import com.blockchain.util.ConfigUtils;
import com.blockchain.util.ResultUtil;
import com.tencent.trustsql.sdk.util.HttpClientUtil;

@Service("AssetService")
public class TencentAssetServiceImpl implements AssetService {
	public static final Logger issueLogger = LoggerFactory.getLogger("issueLogger");
	public static final Logger transferLogger = LoggerFactory.getLogger("transferLogger");
	public static final Logger settleLogger = LoggerFactory.getLogger("settleLogger");

	AssetUtil assetUtil = new AssetUtil();

	AssetPrepareUtil assetPrepareUtil = new AssetPrepareUtil();

	@Override
	public AssetIssueDto issue(AssetIssueFormDto assetFormDto) throws Exception {
		ConfigUtils configUtils = new ConfigUtils();
		issueLogger.info("issue调试");
		String applyUrl = configUtils.getHost() + "/asset_issue_apply";
		String applyString = assetUtil.generateIssueApplyParam(assetFormDto);

		issueLogger.info("请求的链接{}", applyUrl);
		issueLogger.info("调用【发行申请】前的参数:{}", applyString);

		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		issueLogger.info("调用【发行申请】后的参数{}", applyResultString);
		ResultUtil.checkResultIfSuccess("资产申请接口", applyResultString);

		String userPrivateKey = assetFormDto.getUserPrivateKey();
		AssetIssueSubmitFormDto assetSubmitFormDto = assetPrepareUtil.prepareAssetSubmitForm(applyResultString);
		assetSubmitFormDto.setUserPrivateKey(userPrivateKey);

		String submitString = assetUtil.generateIssueSubmitParam(assetSubmitFormDto);

		String submitUrl = configUtils.getHost() + "/asset_issue_submit";
		issueLogger.info("请求的链接{}", submitUrl);
		issueLogger.info("调用【发行提交】前的参数" + submitString);
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);

		issueLogger.info("调用【发行提交】后的参数" + submitResultString);
		ResultUtil.checkSubmitResultIfSuccess("资产提交接口", JSON.toJSONString(assetSubmitFormDto), submitResultString);
		issueLogger.debug("issue调试结束");

		AssetIssueDto assetIssueDto = new AssetIssueDto();
		assetIssueDto = assetPrepareUtil.generateAssetIssueDto(assetSubmitFormDto, submitResultString);
		return assetIssueDto;
	}

	@Override
	public AssetTransferDto transfer(AssetTransferFormDto assetTransferFormDto) throws TrustSDKException, Exception {

		transferLogger.info("transfer调试开始");
		String applyString = assetUtil.generateTransferApplyParam(assetTransferFormDto);
		transferLogger.info("调用【转账申请前】的参数" + applyString);
		ConfigUtils configUtils = new ConfigUtils();
		String applyUrl = configUtils.getHost() + "/asset_transfer_apply";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		transferLogger.info("调用【转账申请后】的参数" + applyString);

		ResultUtil.checkResultIfSuccess("资产转让申请接口", applyResultString);

		AssetTransferSubmitFormDto asseTransfertSubmitForm = assetPrepareUtil.perpareTransferSubmitForm(assetTransferFormDto, applyResultString);
		transferLogger.info("调用【转账申请后】的参数" + asseTransfertSubmitForm);

		String submitString = assetUtil.generateTransferSubmitParam(asseTransfertSubmitForm);
		transferLogger.info("调用【转账提交前】的参数" + submitString);
		String submitUrl = configUtils.getHost() + "/asset_transfer_submit";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		transferLogger.info("调用【转账提交后】的参数" + submitResultString);

		ResultUtil.checkSubmitResultIfSuccess("资产转让提交接口", JSONObject.toJSONString(asseTransfertSubmitForm), submitResultString);

		transferLogger.info("issue调试结束");

		AssetTransferDto assetTransferDto = new AssetTransferDto();
		assetTransferDto = assetPrepareUtil.generateAssetTransferDto(asseTransfertSubmitForm, submitResultString);
		return assetTransferDto;
	}

	@Override
	public AssetSettleDto settle(AssetSettleFormDto assetSettleFormDto) throws UnsupportedEncodingException, TrustSDKException, Exception {
		settleLogger.info("settle调试开始");
		String applyString = assetUtil.generateSettleApplyParam(assetSettleFormDto);

		settleLogger.info("调用【兑换申请前】" + applyString);
		ConfigUtils configUtils = new ConfigUtils();
		String applyUrl = configUtils.getHost() + "/asset_settle_apply";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		settleLogger.info("调用【兑换申请后】" + applyResultString);

		ResultUtil.checkResultIfSuccess("资产兑换申请接口", applyResultString);

		AssetSettleSubmitFormDto assetSettleSubmitFormDto = assetPrepareUtil.perpareSettleSubmitForm(assetSettleFormDto, applyResultString);
		String submitString = assetUtil.generateSettleSubmitParam(assetSettleSubmitFormDto);

		settleLogger.info("【兑换【调用提交前】" + submitString);
		String submitUrl = configUtils.getHost() + "/asset_settle_submit";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		settleLogger.info("【兑换【调用提交】后" + submitResultString);

		ResultUtil.checkSubmitResultIfSuccess("资产提交接口", JSON.toJSONString(assetSettleSubmitFormDto), submitResultString);
		settleLogger.info("settle调试结束");

		AssetSettleDto assetSettleDto = new AssetSettleDto();
		assetSettleDto = assetPrepareUtil.generateAssetSettleDto(assetSettleSubmitFormDto, submitResultString);
		return assetSettleDto;
	}

	@Override
	public AssetIssueDto issueSubmit(AssetIssueSubmitFormDto assetSubmitFormDto) throws UnsupportedEncodingException, TrustSDKException, Exception {
		issueLogger.debug("传入的参数" + assetSubmitFormDto);
		String submitString = assetUtil.generateIssueSubmitParam(assetSubmitFormDto);
		issueLogger.debug("调用【资产发行前】" + submitString);
		ConfigUtils configUtils = new ConfigUtils();
		String submitUrl = configUtils.getHost() + "/asset_issue_submit";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		ResultUtil.checkResultIfSuccess("资产提交接口", submitResultString);
		issueLogger.debug("调用【资产只发行后】" + submitResultString);
		issueLogger.debug("issue调试结束");

		AssetIssueDto assetIssueDto = new AssetIssueDto();
		assetIssueDto = assetPrepareUtil.generateAssetIssueDto(assetSubmitFormDto, submitResultString);
		return assetIssueDto;
	}

	@Override
	public AssetTransferDto transSubmit(AssetTransferSubmitFormDto asseTransfertSubmitForm) throws TrustSDKException, Exception {

		String submitString = assetUtil.generateTransferSubmitParam(asseTransfertSubmitForm);

		transferLogger.info("调用【转账只提交前】" + submitString);
		ConfigUtils configUtils = new ConfigUtils();
		String submitUrl = configUtils.getHost() + "/asset_transfer_submit";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		transferLogger.info("调用【转账只提交后】" + submitResultString);

		ResultUtil.checkResultIfSuccess("资产提交接口", submitResultString);
		System.out.println(submitResultString);
		transferLogger.info("issue调试结束");

		AssetTransferDto assetTransferDto = new AssetTransferDto();
		assetTransferDto = assetPrepareUtil.generateAssetTransferDto(asseTransfertSubmitForm, submitResultString);
		return assetTransferDto;
	}

	@Override
	public AssetSettleDto settleSubmit(AssetSettleSubmitFormDto assetSettleSubmitFormDto) throws Exception {

		String submitString = assetUtil.generateSettleSubmitParam(assetSettleSubmitFormDto);
		settleLogger.info("调用【兑换只提交前】" + submitString);
		ConfigUtils configUtils = new ConfigUtils();
		String submitUrl = configUtils.getHost() + "/asset_settle_submit";

		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		settleLogger.info("调用【兑换只提交后】" + submitResultString);
		ResultUtil.checkResultIfSuccess("资产提交接口", submitResultString);

		AssetSettleDto assetSettleDto = new AssetSettleDto();
		assetSettleDto = assetPrepareUtil.generateAssetSettleDto(assetSettleSubmitFormDto, submitResultString);
		return assetSettleDto;
	}

	@Override
	public AssetTransferSubmitFormDto transferApply(AssetTransferFormDto assetTransferFormDto) throws ServiceException, TrustSDKException, Exception {

		transferLogger.info("transfer调试开始");
		String applyString = assetUtil.generateTransferApplyParam(assetTransferFormDto);
		transferLogger.info("调用【转账申请前】的参数" + applyString);
		ConfigUtils configUtils = new ConfigUtils();
		String applyUrl = configUtils.getHost() + "/asset_transfer_apply";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		transferLogger.info("调用【转账申请后】的参数" + applyString);

		ResultUtil.checkResultIfSuccess("资产转让申请接口", applyResultString);

		AssetTransferSubmitFormDto asseTransfertSubmitForm = assetPrepareUtil.perpareTransferSubmitForm(assetTransferFormDto, applyResultString);

		transferLogger.info("apply调试结束");

		return asseTransfertSubmitForm;
	}

	@Override
	public AssetIssueSubmitFormDto issueApply(AssetIssueFormDto assetFormDto) throws ServiceException, TrustSDKException, Exception {
		ConfigUtils configUtils = new ConfigUtils();
		issueLogger.info("issue调试");
		String applyUrl = configUtils.getHost() + "/asset_issue_apply";
		String applyString = assetUtil.generateIssueApplyParam(assetFormDto);

		issueLogger.info("请求的链接{}", applyUrl);
		issueLogger.info("调用【发行申请】前的参数:{}", applyString);

		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		issueLogger.info("调用【发行申请】后的参数{}", applyResultString);
		ResultUtil.checkResultIfSuccess("资产申请接口", applyResultString);

		String userPrivateKey = assetFormDto.getUserPrivateKey();
		AssetIssueSubmitFormDto assetSubmitFormDto = assetPrepareUtil.prepareAssetSubmitForm(applyResultString);
		assetSubmitFormDto.setUserPrivateKey(userPrivateKey);
		return assetSubmitFormDto;
	}

	@Override
	public AssetSettleSubmitFormDto settleApply(AssetSettleFormDto assetSettleFormDto) throws ServiceException, TrustSDKException, Exception {
		settleLogger.info("settle调试开始");
		String applyString = assetUtil.generateSettleApplyParam(assetSettleFormDto);

		settleLogger.info("调用【兑换申请前】" + applyString);
		ConfigUtils configUtils = new ConfigUtils();
		String applyUrl = configUtils.getHost() + "/asset_settle_apply";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		settleLogger.info("调用【兑换申请后】" + applyResultString);

		ResultUtil.checkResultIfSuccess("资产兑换申请接口", applyResultString);

		AssetSettleSubmitFormDto assetSettleSubmitFormDto = assetPrepareUtil.perpareSettleSubmitForm(assetSettleFormDto, applyResultString);

		return assetSettleSubmitFormDto;
	}

}
