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
import com.blockchain.DTO.AssetFormDTO;
import com.blockchain.DTO.AssetIssueDTO;
import com.blockchain.DTO.AssetSettleDTO;
import com.blockchain.DTO.AssetSettleFormDTO;
import com.blockchain.DTO.AssetSettleSubmitFormDTO;
import com.blockchain.DTO.AssetSubmitFormDTO;
import com.blockchain.DTO.AssetTransferDTO;
import com.blockchain.DTO.AssetTransferFormDTO;
import com.blockchain.DTO.AssetTransferSubmitFormDTO;
import com.blockchain.VO.PhpSystemJsonContentVO;
import com.blockchain.exception.ParameterErrorException;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.exception.SubmitException;
import com.blockchain.exception.ThreadException;
import com.blockchain.service.AssetService;
import com.blockchain.util.ConfigUtils;
import com.blockchain.util.ParamUtils;
import com.blockchain.util.ResponseUtil;
import com.blockchain.util.TrustSDKUtil;
import com.blockchain.util.ValidatorUtil;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Controller
@RequestMapping(value = "/asset")
public class AssetController {
	Logger logger = LoggerFactory.getLogger(AssetController.class);
	@Resource
	HttpServletResponse response;
	@Resource
	AssetService assetService;

	@ResponseBody
	@RequestMapping(value = { "/transfer" }, method = RequestMethod.POST)
	@ApiOperation(value = "转账申请且提交", httpMethod = "POST", response = AssetTransferDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.STATUS_SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class) })
	public void transfer(@Valid @RequestBody AssetTransferFormDTO assetTransferFormDTO, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			TrustSDKUtil.checkPrivateKeyAccountIsMatch(assetTransferFormDTO.getUserPrivateKey(), assetTransferFormDTO.getSrcAccount());
			ValidatorUtil.validate(bindingResult);
			ConfigUtils.check();
			ParamUtils.checkAssetNum(assetTransferFormDTO.getSrcAsset());

		} catch (ParameterErrorException e) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (TrustSDKException e) {
			logger.error("trustSql报错", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ServiceException e) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setUnkownError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {

			AssetTransferDTO assetTransferDTO = assetService.transfer(assetTransferFormDTO);
			phpSystemJsonContentVO.setData(assetTransferDTO);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);

		} catch (SubmitException e) {
			logger.error("提交错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setSubmitException(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ServiceException e) {
			logger.error("业务错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ThreadException e) {
			logger.error("并发异常", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setThreadException(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/settle" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产兑付申请且提交", httpMethod = "POST", response = AssetSettleDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.STATUS_SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class) })
	public void settle(@Valid @RequestBody AssetSettleFormDTO assetSettleFormVO, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			ConfigUtils.check();
			TrustSDKUtil.checkPrivateKeyAccountIsMatch(assetSettleFormVO.getUserPrivateKey(), assetSettleFormVO.getOwnerAccount());
			ValidatorUtil.validate(bindingResult);
			ParamUtils.checkAssetNum(assetSettleFormVO.getSrcAsset());
		} catch (ParameterErrorException e1) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e1.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ServiceException e) {
			logger.error("业务错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setUnkownError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			AssetSettleDTO assetSettleDTO = assetService.settle(assetSettleFormVO);
			phpSystemJsonContentVO.setData(assetSettleDTO);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);

		} catch (SubmitException e) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setSubmitException(e);

			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ServiceException e) {
			logger.error("业务错误", e);

			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ThreadException e) {
			logger.error("并发异常", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setThreadException(e);

			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/issue" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产发行申请且提交", httpMethod = "POST", response = AssetIssueDTO.class, consumes = "application/json", produces = "application/json")
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.STATUS_SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class) })
	public void issue(@Valid @RequestBody AssetFormDTO assetFormVO, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			ConfigUtils.check();

			ValidatorUtil.validate(bindingResult);
		} catch (ParameterErrorException e1) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e1.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ServiceException e) {
			logger.error("业务错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			AssetIssueDTO assetIssueDTO = assetService.issue(assetFormVO);
			phpSystemJsonContentVO.setData(assetIssueDTO);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);

		} catch (SubmitException e) {
			logger.error("提交错误", e);
			phpSystemJsonContentVO.setSubmitException(e);

			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ServiceException e) {
			logger.error("业务错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ThreadException e) {
			logger.error("并发异常", e);

			phpSystemJsonContentVO = phpSystemJsonContentVO.setThreadException(e);

			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
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
			@ApiResponse(code = StatusCode.STATUS_SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class) })
	public void issueSubmit(@Valid @RequestBody AssetSubmitFormDTO assetForm, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			ConfigUtils.check();
			ValidatorUtil.validate(bindingResult);
		} catch (ParameterErrorException e1) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e1.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ServiceException e) {
			logger.error("业务错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			AssetIssueDTO assetIssueDTO = assetService.issueSubmit(assetForm);
			phpSystemJsonContentVO.setData(assetIssueDTO);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);

		} catch (ServiceException e) {
			logger.error("业务错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ThreadException e) {
			logger.error("并发异常", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setThreadException(e);

			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
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
			@ApiResponse(code = StatusCode.STATUS_SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class) })
	public void transferSubmit(@Valid @RequestBody AssetTransferSubmitFormDTO assetForm, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			ConfigUtils.check();
			ValidatorUtil.validate(bindingResult);
		} catch (ParameterErrorException e1) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e1.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ServiceException e) {
			logger.error("业务错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			AssetTransferDTO assetTransferDTO = assetService.transSubmit(assetForm);
			phpSystemJsonContentVO.setData(assetTransferDTO);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);

		} catch (ServiceException e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ThreadException e) {
			logger.error("并发异常", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setThreadException(e);

			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/settleSubmit" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产兑付只提交", httpMethod = "POST", response = AssetSettleDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.STATUS_SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class) })
	public void settleSubmit(@Valid @RequestBody AssetSettleSubmitFormDTO assetForm, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			ValidatorUtil.validate(bindingResult);
			ConfigUtils.check();
		} catch (ParameterErrorException e1) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e1.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ServiceException e) {
			logger.error("业务错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			AssetSettleDTO assetSettleDTO = assetService.settleSubmit(assetForm);
			phpSystemJsonContentVO.setData(assetSettleDTO);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);

		} catch (ServiceException e) {
			logger.error("业务错误", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ThreadException e) {
			logger.error("并发异常", e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setThreadException(e);

			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		return;
	}
}
