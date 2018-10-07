package com.blockchain.service.tencent.impl;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.blockchain.service.tencent.trustsql.sdk.util.HttpClientUtil;
import com.blockchain.service.tencent.util.AssetPrepareUtil;
import com.blockchain.service.tencent.util.AssetUtil;
import com.blockchain.service.tencent.util.ResultUtil;

@Service("AssetService")
public class AssetServiceImpl implements AssetService {
	public static final Logger issueLogger = LoggerFactory.getLogger("issueLogger");
	public static final Logger transferLogger = LoggerFactory.getLogger("transferLogger");
	public static final Logger settleLogger = LoggerFactory.getLogger("settleLogger");
	

	AssetUtil assetUtil = new AssetUtil();

	AssetPrepareUtil assetPrepareUtil = new AssetPrepareUtil();
	
	@Override
	public AssetIssueDto issue(AssetIssueFormDto assetIssueFormDto) throws Exception {
		issueLogger.info("issue调试");
		issueLogger.info("传入的参数{}" , assetIssueFormDto);
		String applyString = assetUtil.generateIssueApplyParam(assetIssueFormDto);

		issueLogger.info("调用【发行申请】前的参数:{}" , applyString);
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_issue_apply_v1.cgi";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		issueLogger.info("调用【发行申请】后的参数{}" , applyResultString);
		ResultUtil.checkResultIfSuccess("资产申请接口", applyResultString);

		AssetIssueSubmitFormDto assetIssueSubmitFormDto = assetPrepareUtil.prepareAssetSubmitForm(applyResultString);
		String submitString = assetUtil.generateIssueSubmitParam(assetIssueSubmitFormDto);

		issueLogger.info("调用【发行提交】后的参数" + submitString);
		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_issue_submit_v1.cgi";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);

		ResultUtil.checkSubmitResultIfSuccess("资产提交接口", JSON.toJSONString(assetIssueSubmitFormDto), submitResultString);
		issueLogger.debug("issue调试结束");

		AssetIssueDto assetIssueDto = new AssetIssueDto();
		assetIssueDto = assetPrepareUtil.generateAssetIssueDto(assetIssueSubmitFormDto, submitResultString);
		return assetIssueDto;
	}


	@Override
	public AssetTransferDto transfer(AssetTransferFormDto assetTransferFormDto) throws TrustSDKException, Exception {

		transferLogger.info("transfer调试开始");
		String applyString = assetUtil.generateTransferApplyParam(assetTransferFormDto);
		transferLogger.info("调用【转账申请前】的参数" + applyString);
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_transfer_apply_v1.cgi";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		transferLogger.info("调用【转账申请后】的参数" + applyString);

		ResultUtil.checkResultIfSuccess("资产转让申请接口", applyResultString);

		AssetTransferSubmitFormDto asseTransfertSubmitForm = assetPrepareUtil.perpareTransferSubmitForm(assetTransferFormDto, applyResultString);
		String submitString = assetUtil.generateTransferSubmitParam(asseTransfertSubmitForm);
		transferLogger.info("调用【转账提交前】的参数" + submitString);
		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_transfer_submit_v1.cgi";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		transferLogger.info("调用【转账提交后】的参数" + submitResultString);

		ResultUtil.checkSubmitResultIfSuccess("资产转让提交接口", JSONObject.toJSONString(asseTransfertSubmitForm), submitResultString);

		transferLogger.info("issue调试结束");

		AssetTransferDto assetTransferDto = new AssetTransferDto();
		assetTransferDto = assetPrepareUtil.generateAssetTransferDTO(asseTransfertSubmitForm, submitResultString);
		return assetTransferDto;
	}

	@Override
	public AssetSettleDto settle(AssetSettleFormDto assetSettleFormDto) throws UnsupportedEncodingException, TrustSDKException, Exception {
		settleLogger.info("settle调试开始");
		String applyString = assetUtil.generateSettleApplyParam(assetSettleFormDto);

		settleLogger.info("调用【兑换申请前】" + applyString);
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_settle_apply_v1.cgi";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		settleLogger.info("调用【兑换申请后】" + applyResultString);

		ResultUtil.checkResultIfSuccess("资产兑换申请接口", applyResultString);

		AssetSettleSubmitFormDto assetSettleSubmitFormDto = assetPrepareUtil.perpareTransferSubmitForm(assetSettleFormDto, applyResultString);
		String submitString = assetUtil.generateSettleSubmitParam(assetSettleSubmitFormDto);

		settleLogger.info("【兑换【调用提交前】" + submitString);
		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_settle_submit_v1.cgi";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		settleLogger.info("【兑换【调用提交】后" + submitResultString);

		ResultUtil.checkSubmitResultIfSuccess("资产提交接口", JSON.toJSONString(assetSettleSubmitFormDto), submitResultString);
		settleLogger.info("settle调试结束");

		AssetSettleDto assetSettleDto = new AssetSettleDto();
		assetSettleDto = assetPrepareUtil.generateAssetSettleDTO(assetSettleSubmitFormDto, submitResultString);
		return assetSettleDto;
	}

	





	@Override
	public AssetIssueDto issueSubmit(AssetIssueSubmitFormDto assetIssueSubmitFormDto) throws UnsupportedEncodingException, TrustSDKException, Exception {
		issueLogger.debug("传入的参数" + assetIssueSubmitFormDto);
		String submitString = assetUtil.generateIssueSubmitParam(assetIssueSubmitFormDto);
		issueLogger.debug("调用【资产发行前】" + submitString);

		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_issue_submit_v1.cgi";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		ResultUtil.checkResultIfSuccess("资产提交接口",submitResultString);
		issueLogger.debug("调用【资产只发行后】" + submitResultString);
		issueLogger.debug("issue调试结束");

	
		AssetIssueDto assetIssueDto = new AssetIssueDto();
		assetIssueDto = assetPrepareUtil.generateAssetIssueDto(assetIssueSubmitFormDto, submitResultString);
		return assetIssueDto;
	}

	@Override
	public AssetTransferDto transSubmit(AssetTransferSubmitFormDto asseTransfertSubmitForm) throws TrustSDKException, Exception {

	
		String submitString = assetUtil.generateTransferSubmitParam(asseTransfertSubmitForm);

		transferLogger.info("调用【转账只提交前】" + submitString);
 		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_transfer_submit_v1.cgi";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		transferLogger.info("调用【转账只提交后】" + submitResultString);

		ResultUtil.checkResultIfSuccess("资产提交接口",submitResultString);
		System.out.println(submitResultString);
		transferLogger.info("issue调试结束");

		AssetTransferDto assetTransferDto = new AssetTransferDto();
		assetTransferDto = assetPrepareUtil.generateAssetTransferDTO(asseTransfertSubmitForm, submitResultString);
		return assetTransferDto;
	}



	@Override
	public AssetSettleDto settleSubmit(AssetSettleSubmitFormDto assetSettleSubmitFormDto) throws Exception {

		String submitString = assetUtil.generateSettleSubmitParam(assetSettleSubmitFormDto);
		settleLogger.info("调用【兑换只提交前】" + submitString);
		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_settle_submit_v1.cgi";

		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		settleLogger.info("调用【兑换只提交后】" + submitResultString);
		ResultUtil.checkResultIfSuccess("资产提交接口", submitResultString);

		settleLogger.info("settle调试结束");

		AssetSettleDto assetSettleDto = new AssetSettleDto();
		assetSettleDto = assetPrepareUtil.generateAssetSettleDTO(assetSettleSubmitFormDto, submitResultString);
		return assetSettleDto;
	}


	

}
