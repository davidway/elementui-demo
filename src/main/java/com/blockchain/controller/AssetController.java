package com.blockchain.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
import com.blockchain.exception.StatusCode;
import com.blockchain.service.AssetService;
import com.blockchain.service.impl.AssetServiceImpl;
import com.blockchain.util.ConfigUtils;
import com.blockchain.util.CrmUtils;
import com.blockchain.util.ParamUtils;
import com.blockchain.util.ResponseUtil;
import com.blockchain.util.TrustSDKUtil;
import com.blockchain.util.ValidatorUtil;
import com.blockchain.vo.PhpSystemJsonContentVO;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Controller("AssetController")
@RequestMapping(value = "/asset")
public class AssetController {
	Logger logger = LoggerFactory.getLogger(AssetController.class);
	@Resource
	HttpServletResponse response;

	AssetService assetService = new AssetServiceImpl();

	@ResponseBody
	@RequestMapping(value = { "/transfer" }, method = RequestMethod.POST)
	@ApiOperation(value = "转账申请且提交", httpMethod = "POST", response = AssetTransferDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class),

	})
	public void transfer(@Valid @RequestBody AssetTransferFormDTO assetTransferFormDTO, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			ValidatorUtil.validate(bindingResult);
			//ConfigUtils.check();
			ParamUtils.checkAssetNum(assetTransferFormDTO.getSrcAsset());
			ParamUtils.checkSumAmount(assetTransferFormDTO);
			TrustSDKUtil.checkPrivateKeyAccountIsMatch(assetTransferFormDTO.getUserPrivateKey(), assetTransferFormDTO.getSrcAccount());
			AssetTransferDTO assetTransferDTO = assetService.transfer(assetTransferFormDTO);
			phpSystemJsonContentVO.setData(assetTransferDTO);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);

		} catch (TrustSDKException e) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(new ServiceException().errorCode(StatusCode.SYSTEM_UNKOWN_ERROR).errorMessage(StatusCode.SYSTEM_UNKOWN_ERROR_MESSAGE));
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ServiceException e) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setUnkownError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		}

	}

	@ResponseBody
	@RequestMapping(value = { "/settle" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产兑付申请且提交", httpMethod = "POST", response = AssetSettleDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void settle(@Valid @RequestBody AssetSettleFormDTO assetSettleFormDTO, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			//ConfigUtils.check();
			TrustSDKUtil.checkPrivateKeyAccountIsMatch(assetSettleFormDTO.getUserPrivateKey(), assetSettleFormDTO.getOwnerAccount());
			ValidatorUtil.validate(bindingResult);
			ParamUtils.checkAssetNum(assetSettleFormDTO.getSrcAsset());
			AssetSettleDTO assetSettleDTO = assetService.settle(assetSettleFormDTO);
			phpSystemJsonContentVO.setData(assetSettleDTO);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
		} catch (ServiceException e) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(new ServiceException().errorCode(StatusCode.SYSTEM_UNKOWN_ERROR).errorMessage(StatusCode.SYSTEM_UNKOWN_ERROR_MESSAGE));
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		}

	}

	@ResponseBody
	@RequestMapping(value = { "/issue" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产发行申请且提交", httpMethod = "POST", response = AssetIssueDTO.class, consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.AUTHORITY_ERROR, message = StatusCode.AUTHORITY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.AUTHORITY_ERROR, message = StatusCode.AUTHORITY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.URL_NOT_EXISTS, message = StatusCode.AUTHORITY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.TIME_OUT, message = StatusCode.TIME_OUT_MESSAGE, response = StatusCode.class),

			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void issue(@Valid @RequestBody AssetFormDTO assetFormDTO, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {

			//ConfigUtils.check();
			//CrmUtils.checkAuth();
			ValidatorUtil.validate(bindingResult);
			AssetIssueDTO assetIssueDTO = assetService.issue(assetFormDTO);
			phpSystemJsonContentVO.setData(assetIssueDTO);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);

		} catch (ServiceException e) {

			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/issueApply" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产发行申请", httpMethod = "POST", response = AssetIssueDTO.class, consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.AUTHORITY_ERROR, message = StatusCode.AUTHORITY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.AUTHORITY_ERROR, message = StatusCode.AUTHORITY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.URL_NOT_EXISTS, message = StatusCode.AUTHORITY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.TIME_OUT, message = StatusCode.TIME_OUT_MESSAGE, response = StatusCode.class),

			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void issueApply(@Valid @RequestBody AssetFormDTO assetFormDTO, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {

			//ConfigUtils.check();
			CrmUtils.checkAuth();
			ValidatorUtil.validate(bindingResult);
			AssetSubmitFormDTO assetSubmitFormDTO = assetService.issueApply(assetFormDTO);
			phpSystemJsonContentVO.setData(assetSubmitFormDTO);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);

		} catch (ServiceException e) {

			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/issueSubmit" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产发行只提交", httpMethod = "POST", response = AssetIssueDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),

			@ApiResponse(code = StatusCode.AUTHORITY_ERROR, message = StatusCode.AUTHORITY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.URL_NOT_EXISTS, message = StatusCode.URL_NOT_EXISTS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.TIME_OUT, message = StatusCode.TIME_OUT_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void issueSubmit(@Valid @RequestBody AssetSubmitFormDTO assetForm, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			//ConfigUtils.check();
			//CrmUtils.checkAuth();
			ValidatorUtil.validate(bindingResult);
			AssetIssueDTO assetIssueDTO = assetService.issueSubmit(assetForm);
			phpSystemJsonContentVO.setData(assetIssueDTO);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);

		} catch (ServiceException e) {
			logger.error("业务错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误{}", e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/transferSubmit" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产转让只提交", httpMethod = "POST", response = AssetTransferDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void transferSubmit(@Valid @RequestBody AssetTransferSubmitFormDTO assetForm, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			//ConfigUtils.check();
			ValidatorUtil.validate(bindingResult);
			AssetTransferDTO assetTransferDTO = assetService.transSubmit(assetForm);
			phpSystemJsonContentVO.setData(assetTransferDTO);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
		} catch (ServiceException e) {

			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误{}", e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/transferApply" }, method = RequestMethod.POST)
	@ApiOperation(value = "转账只申请", httpMethod = "POST", response = AssetTransferDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class),

	})
	public void transferApply(@Valid @RequestBody AssetTransferFormDTO assetTransferFormDTO, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			ValidatorUtil.validate(bindingResult);
			//ConfigUtils.check();
			ParamUtils.checkAssetNum(assetTransferFormDTO.getSrcAsset());
			ParamUtils.checkSumAmount(assetTransferFormDTO);
			TrustSDKUtil.checkPrivateKeyAccountIsMatch(assetTransferFormDTO.getUserPrivateKey(), assetTransferFormDTO.getSrcAccount());
			AssetTransferSubmitFormDTO assetTransferSubmitFormDTO = assetService.transferApply(assetTransferFormDTO);
			phpSystemJsonContentVO.setData(assetTransferSubmitFormDTO);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);

		} catch (TrustSDKException e) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(new ServiceException().errorCode(StatusCode.SYSTEM_UNKOWN_ERROR).errorMessage(StatusCode.SYSTEM_UNKOWN_ERROR_MESSAGE));
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ServiceException e) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setUnkownError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		}

	}

	@ResponseBody
	@RequestMapping(value = { "/settleSubmit" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产兑付只提交", httpMethod = "POST", response = AssetSettleDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void settleSubmit(@Valid @RequestBody AssetSettleSubmitFormDTO assetForm, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			ValidatorUtil.validate(bindingResult);
			//ConfigUtils.check();
			AssetSettleDTO assetSettleDTO = assetService.settleSubmit(assetForm);
			phpSystemJsonContentVO.setData(assetSettleDTO);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);

		} catch (ServiceException e) {

			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误{}", e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/settleApply" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产兑付申请", httpMethod = "POST", response = AssetSettleDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void settleApply(@Valid @RequestBody AssetSettleFormDTO assetSettleFormDTO, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";

		try {
			//ConfigUtils.check();
			TrustSDKUtil.checkPrivateKeyAccountIsMatch(assetSettleFormDTO.getUserPrivateKey(), assetSettleFormDTO.getOwnerAccount());
			ValidatorUtil.validate(bindingResult);
			ParamUtils.checkAssetNum(assetSettleFormDTO.getSrcAsset());
			AssetSettleSubmitFormDTO assetSettleSubmitFormDTO = assetService.settleApply(assetSettleFormDTO);
			phpSystemJsonContentVO.setData(assetSettleSubmitFormDTO);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
		} catch (ServiceException e) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(new ServiceException().errorCode(StatusCode.SYSTEM_UNKOWN_ERROR).errorMessage(StatusCode.SYSTEM_UNKOWN_ERROR_MESSAGE));
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		}

	}

}
