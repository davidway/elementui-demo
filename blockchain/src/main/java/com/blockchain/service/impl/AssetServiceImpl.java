package com.blockchain.service.impl;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sun.util.logging.resources.logging;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.DTO.AssetDTO;
import com.blockchain.DTO.AssetIssueDTO;
import com.blockchain.DTO.AssetSettleDTO;
import com.blockchain.DTO.AssetTransferDTO;
import com.blockchain.DTO.TransInfoDTO;
import com.blockchain.VO.AccountQueryFormVO;
import com.blockchain.VO.AssetFormVO;
import com.blockchain.VO.AssetSettleFormVO;
import com.blockchain.VO.AssetSettleSubmitFormVO;
import com.blockchain.VO.AssetSubmitFormVO;
import com.blockchain.VO.AssetTransQueryFormVO;
import com.blockchain.VO.AssetTransferFormVO;
import com.blockchain.VO.AssetTransferSubmitFormVO;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.ErrorMessage;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.AssetService;
import com.blockchain.util.AssetPrepareUtil;
import com.blockchain.util.AssetUtil;
import com.blockchain.util.ResultUtil;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.tencent.trustsql.sdk.util.HttpClientUtil;

@Service
public class AssetServiceImpl implements AssetService {
	public static final Logger issueLogger = LoggerFactory.getLogger("issueLogger");
	public static final Logger transferLogger = LoggerFactory.getLogger("transferLogger");
	public static final Logger settleLogger = LoggerFactory.getLogger("settleLogger");
	
	@Resource
	AssetUtil assetUtil;
	@Resource
	AssetPrepareUtil assetPrepareUtil;
	
	@Override
	public AssetIssueDTO issue(AssetFormVO assetFormVO) throws Exception {
		issueLogger.info("issue调试");
		issueLogger.info("传入的参数{}" , assetFormVO);
		String applyString = assetUtil.generateIssueApplyParam(assetFormVO);

		issueLogger.info("调用【发行申请】前的参数:{}" , applyString);
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_issue_apply_v1.cgi";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		issueLogger.info("调用【发行申请】后的参数{}" , applyResultString);
		ResultUtil.checkResultIfSuccess("资产申请接口", applyResultString);

		AssetSubmitFormVO assetSubmitFormVO = assetPrepareUtil.prepareAssetSubmitForm(applyResultString);
		String submitString = assetUtil.generateIssueSubmitParam(assetSubmitFormVO);

		issueLogger.info("调用【发行提交】后的参数" + submitString);
		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_issue_submit_v1.cgi";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);

		ResultUtil.checkSubmitResultIfSuccess("资产提交接口", JSON.toJSONString(assetSubmitFormVO), submitResultString);
		issueLogger.debug("issue调试结束");

		AssetIssueDTO assetIssueDTO = new AssetIssueDTO();
		assetIssueDTO = assetPrepareUtil.generateAssetIssueDto(assetSubmitFormVO, submitResultString);
		return assetIssueDTO;
	}


	@Override
	public AssetTransferDTO transfer(AssetTransferFormVO assetTransferFormVO) throws TrustSDKException, Exception {

		transferLogger.info("transfer调试开始");
		String applyString = assetUtil.generateTransferApplyParam(assetTransferFormVO);
		transferLogger.info("调用【转账申请前】的参数" + applyString);
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_transfer_apply_v1.cgi";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		transferLogger.info("调用【转账申请后】的参数" + applyString);

		ResultUtil.checkResultIfSuccess("资产转让申请接口", applyResultString);

		AssetTransferSubmitFormVO asseTransfertSubmitForm = assetPrepareUtil.perpareTransferSubmitForm(assetTransferFormVO, applyResultString);
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
	public AssetSettleDTO settle(AssetSettleFormVO assetSettleFormVO) throws UnsupportedEncodingException, TrustSDKException, Exception {
		settleLogger.info("settle调试开始");
		String applyString = assetUtil.generateSettleApplyParam(assetSettleFormVO);

		settleLogger.info("调用【兑换申请前】" + applyString);
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_settle_apply_v1.cgi";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		settleLogger.info("调用【兑换申请后】" + applyResultString);

		ResultUtil.checkResultIfSuccess("资产兑换申请接口", applyResultString);

		AssetSettleSubmitFormVO assetSettleSubmitFormVO = assetPrepareUtil.perpareTransferSubmitForm(assetSettleFormVO, applyResultString);
		String submitString = assetUtil.generateSettleSubmitParam(assetSettleSubmitFormVO);

		settleLogger.info("【兑换【调用提交前】" + submitString);
		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_settle_submit_v1.cgi";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		settleLogger.info("【兑换【调用提交】后" + submitResultString);

		ResultUtil.checkSubmitResultIfSuccess("资产提交接口", JSON.toJSONString(assetSettleSubmitFormVO), submitResultString);
		settleLogger.info("settle调试结束");

		AssetSettleDTO assetSettleDTO = new AssetSettleDTO();
		assetSettleDTO = assetPrepareUtil.generateAssetSettleDTO(assetSettleSubmitFormVO, submitResultString);
		return assetSettleDTO;
	}

	





	@Override
	public AssetIssueDTO issueSubmit(AssetSubmitFormVO assetSubmitFormVO) throws UnsupportedEncodingException, TrustSDKException, Exception {
		issueLogger.debug("传入的参数" + assetSubmitFormVO);
		String submitString = assetUtil.generateIssueSubmitParam(assetSubmitFormVO);
		issueLogger.debug("调用【资产发行前】" + submitString);

		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_issue_submit_v1.cgi";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		ResultUtil.checkSubmitResultIfSuccess("资产提交接口", submitString,submitResultString);
		issueLogger.debug("调用【资产只发行后】" + submitResultString);
		issueLogger.debug("issue调试结束");

	
		AssetIssueDTO assetIssueDTO = new AssetIssueDTO();
		assetIssueDTO = assetPrepareUtil.generateAssetIssueDto(assetSubmitFormVO, submitResultString);
		return assetIssueDTO;
	}

	@Override
	public AssetTransferDTO transSubmit(AssetTransferSubmitFormVO asseTransfertSubmitForm) throws TrustSDKException, Exception {

	
		String submitString = assetUtil.generateTransferSubmitParam(asseTransfertSubmitForm);

		transferLogger.info("调用【转账只提交前】" + submitString);
		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_transfer_submit_v1.cgi";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		transferLogger.info("调用【转账只提交后】" + submitResultString);

		ResultUtil.checkSubmitResultIfSuccess("资产转让提交接口", submitString, submitResultString);
		System.out.println(submitResultString);
		transferLogger.info("issue调试结束");

		AssetTransferDTO assetTransferDTO = new AssetTransferDTO();
		assetTransferDTO = assetPrepareUtil.generateAssetTransferDTO(asseTransfertSubmitForm, submitResultString);
		return assetTransferDTO;
	}



	@Override
	public AssetSettleDTO settleSubmit(AssetSettleSubmitFormVO assetSettleSubmitFormVO) throws Exception {

		String submitString = assetUtil.generateSettleSubmitParam(assetSettleSubmitFormVO);
		settleLogger.info("调用【兑换只提交前】" + submitString);
		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_settle_submit_v1.cgi";

		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		settleLogger.info("调用【兑换只提交后】" + submitResultString);
		ResultUtil.checkResultIfSuccess("资产提交接口", submitResultString);

		settleLogger.info("settle调试结束");

		AssetSettleDTO assetSettleDTO = new AssetSettleDTO();
		assetSettleDTO = assetPrepareUtil.generateAssetSettleDTO(assetSettleSubmitFormVO, submitResultString);
		return assetSettleDTO;
	}

}
