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
import com.blockchain.service.AssetService;
import com.blockchain.util.AssetPrepareUtil;
import com.blockchain.util.AssetUtil;
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
		issueLogger.info("issue调试");
		issueLogger.info("传入的参数{}" , assetFormDTO);
		String applyString = assetUtil.generateIssueApplyParam(assetFormDTO);

		issueLogger.info("调用【发行申请】前的参数:{}" , applyString);
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_issue_apply_v1.cgi";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		issueLogger.info("调用【发行申请】后的参数{}" , applyResultString);
		ResultUtil.checkResultIfSuccess("资产申请接口", applyResultString);

		AssetSubmitFormDTO assetSubmitFormDTO = assetPrepareUtil.prepareAssetSubmitForm(applyResultString);
		String submitString = assetUtil.generateIssueSubmitParam(assetSubmitFormDTO);

		issueLogger.info("调用【发行提交】后的参数" + submitString);
		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_issue_submit_v1.cgi";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);

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
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_transfer_apply_v1.cgi";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		transferLogger.info("调用【转账申请后】的参数" + applyString);

		ResultUtil.checkResultIfSuccess("资产转让申请接口", applyResultString);

		AssetTransferSubmitFormDTO asseTransfertSubmitForm = assetPrepareUtil.perpareTransferSubmitForm(assetTransferFormDTO, applyResultString);
		String submitString = assetUtil.generateTransferSubmitParam(asseTransfertSubmitForm);
		transferLogger.info("调用【转账提交前】的参数" + submitString);
		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_transfer_submit_v1.cgi";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		transferLogger.info("调用【转账提交后】的参数" + submitResultString);

		ResultUtil.checkSubmitResultIfSuccess("资产转让提交接口", JSONObject.toJSONString(asseTransfertSubmitForm), submitResultString);

		transferLogger.info("issue调试结束");

		AssetTransferDTO assetTransferDTO = new AssetTransferDTO();
		assetTransferDTO = assetPrepareUtil.generateAssetTransferDTO(asseTransfertSubmitForm, submitResultString);
		return assetTransferDTO;
	}

	@Override
	public AssetSettleDTO settle(AssetSettleFormDTO assetSettleFormDTO) throws UnsupportedEncodingException, TrustSDKException, Exception {
		settleLogger.info("settle调试开始");
		String applyString = assetUtil.generateSettleApplyParam(assetSettleFormDTO);

		settleLogger.info("调用【兑换申请前】" + applyString);
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_settle_apply_v1.cgi";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		settleLogger.info("调用【兑换申请后】" + applyResultString);

		ResultUtil.checkResultIfSuccess("资产兑换申请接口", applyResultString);

		AssetSettleSubmitFormDTO assetSettleSubmitFormDTO = assetPrepareUtil.perpareTransferSubmitForm(assetSettleFormDTO, applyResultString);
		String submitString = assetUtil.generateSettleSubmitParam(assetSettleSubmitFormDTO);

		settleLogger.info("【兑换【调用提交前】" + submitString);
		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_settle_submit_v1.cgi";
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
		issueLogger.debug("传入的参数" + assetSubmitFormDTO);
		String submitString = assetUtil.generateIssueSubmitParam(assetSubmitFormDTO);
		issueLogger.debug("调用【资产发行前】" + submitString);

		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_issue_submit_v1.cgi";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		ResultUtil.checkResultIfSuccess("资产提交接口",submitResultString);
		issueLogger.debug("调用【资产只发行后】" + submitResultString);
		issueLogger.debug("issue调试结束");

	
		AssetIssueDTO assetIssueDTO = new AssetIssueDTO();
		assetIssueDTO = assetPrepareUtil.generateAssetIssueDto(assetSubmitFormDTO, submitResultString);
		return assetIssueDTO;
	}

	@Override
	public AssetTransferDTO transSubmit(AssetTransferSubmitFormDTO asseTransfertSubmitForm) throws TrustSDKException, Exception {

	
		String submitString = assetUtil.generateTransferSubmitParam(asseTransfertSubmitForm);

		transferLogger.info("调用【转账只提交前】" + submitString);
 		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_transfer_submit_v1.cgi";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		transferLogger.info("调用【转账只提交后】" + submitResultString);

		ResultUtil.checkResultIfSuccess("资产提交接口",submitResultString);
		System.out.println(submitResultString);
		transferLogger.info("issue调试结束");

		AssetTransferDTO assetTransferDTO = new AssetTransferDTO();
		assetTransferDTO = assetPrepareUtil.generateAssetTransferDTO(asseTransfertSubmitForm, submitResultString);
		return assetTransferDTO;
	}



	@Override
	public AssetSettleDTO settleSubmit(AssetSettleSubmitFormDTO assetSettleSubmitFormDTO) throws Exception {

		String submitString = assetUtil.generateSettleSubmitParam(assetSettleSubmitFormDTO);
		settleLogger.info("调用【兑换只提交前】" + submitString);
		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_settle_submit_v1.cgi";

		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		settleLogger.info("调用【兑换只提交后】" + submitResultString);
		ResultUtil.checkResultIfSuccess("资产提交接口", submitResultString);

		settleLogger.info("settle调试结束");

		AssetSettleDTO assetSettleDTO = new AssetSettleDTO();
		assetSettleDTO = assetPrepareUtil.generateAssetSettleDTO(assetSettleSubmitFormDTO, submitResultString);
		return assetSettleDTO;
	}

}
