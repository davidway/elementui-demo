package com.blockchain.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.controller.factory.CoinContextUtil;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.dto.EthTransInfoDto;
import com.blockchain.service.ethereum.EthAssetService;
import com.blockchain.service.ethereum.dto.GasInfoDto;
import com.blockchain.service.ethereum.impl.EthAssetServiceImpl;
import com.blockchain.service.ethereum.vo.EthTransInfoVo;
import com.blockchain.service.ethereum.vo.GasInfoVo;
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
import com.blockchain.service.tencent.impl.TencentAssetServiceImpl;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;
import com.blockchain.service.tencent.vo.PhpSystemJsonContentVo;
import com.blockchain.util.ConfigUtils;
import com.blockchain.util.CrmUtils;
import com.blockchain.util.ParamUtils;
import com.blockchain.util.ResponseUtil;
import com.blockchain.util.TrustSDKUtil;
import com.blockchain.util.ValidatorUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Controller("AssetController")
@RequestMapping(value = "/asset")
public class AssetController {
	Logger logger = LoggerFactory.getLogger(AssetController.class);
	@Resource
	HttpServletResponse response;

	AssetService assetService = new TencentAssetServiceImpl();

	EthAssetService ethAssetService = new EthAssetServiceImpl();

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	public PhpSystemJsonContentVo handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		PhpSystemJsonContentVo response = new PhpSystemJsonContentVo();
		response.setData("");
		response.setRetcode(StatusCode.PARAM_ERROR);
		response.setRetmsg("json格式错误，请检查是否为合法json");
		return response;
	}

	@ResponseBody
	@RequestMapping(value = { "/transfer" }, method = RequestMethod.POST)
	@ApiOperation(value = "转账申请且提交", httpMethod = "POST", response = AssetTransferDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class), })
	public void transfer(@Valid @RequestBody AssetTransferFormDto assetTransferFormDto, BindingResult bindingResult) {
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
		String jsonString = "";
		ConfigUtils configUtils = new ConfigUtils();
		Integer chainType = null;

		try {
			ConfigUtils.check();

			configUtils = new ConfigUtils();
			chainType = configUtils.getChainType();
			CoinBaseContext coinBaseContext = CoinContextUtil.getCoinContext(chainType);
			JSONObject jsonObject = coinBaseContext.transfer(assetTransferFormDto);
			ResponseUtil.successEcho(response, phpSystemJsonContentVo, jsonObject);

		} catch (TrustSDKException e) {
			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(new ServiceException().errorCode(StatusCode.SYSTEM_UNKOWN_ERROR).errorMessage(StatusCode.SYSTEM_UNKOWN_ERROR_MESSAGE));
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ServiceException e) {
			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setUnkownError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/settle" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产兑付申请且提交", httpMethod = "POST", response = AssetSettleDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
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
	public void settle(@Valid @RequestBody AssetSettleFormDto assetSettleFormDto, BindingResult bindingResult) {
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
		String jsonString = "";
		ConfigUtils configUtils = new ConfigUtils();
		Integer chainType = configUtils.getChainType();
		try {
			ConfigUtils.check();

			configUtils = new ConfigUtils();
			chainType = configUtils.getChainType();
			CoinBaseContext coinBaseContext = CoinContextUtil.getCoinContext(chainType);
			JSONObject jsonObject = coinBaseContext.settle(assetSettleFormDto);
			ResponseUtil.successEcho(response, phpSystemJsonContentVo, jsonObject);

		} catch (ServiceException e) {
			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(new ServiceException().errorCode(StatusCode.SYSTEM_UNKOWN_ERROR).errorMessage(StatusCode.SYSTEM_UNKOWN_ERROR_MESSAGE));
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/issue" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产发行申请且提交", httpMethod = "POST", response = AssetIssueDto.class, consumes = "application/json", produces = "application/json")
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
	public void issue(@Valid @RequestBody AssetIssueFormDto assetIssueFormDto, BindingResult bindingResult) {
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
		String jsonString = "";
		ConfigUtils configUtils = new ConfigUtils();
		Integer chainType;
		try {
			// 校验开始
			ConfigUtils.check();
			CrmUtils.checkAuth();
			configUtils = new ConfigUtils();
			chainType = configUtils.getChainType();
			CoinBaseContext coinBaseContext = CoinContextUtil.getCoinContext(chainType);
			JSONObject jsonObject = coinBaseContext.issue(assetIssueFormDto);

			ResponseUtil.successEcho(response, phpSystemJsonContentVo, jsonObject);

		} catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVo.setRetmsg(e.getMessage());
			phpSystemJsonContentVo.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/issueSubmit" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产发行只提交", httpMethod = "POST", response = AssetIssueDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
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
	public void issueSubmit(@Valid @RequestBody AssetIssueSubmitFormDto assetForm, BindingResult bindingResult) {
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
		String jsonString = "";
		try {

			ConfigUtils.check();
			CrmUtils.checkAuth();
			ConfigUtils configUtils = new ConfigUtils();
			Integer chainType = configUtils.getChainType();
			CoinBaseContext coinBaseContext = CoinContextUtil.getCoinContext(chainType);
			JSONObject jsonObject = coinBaseContext.issueSubmit(assetForm);

			ResponseUtil.successEcho(response, phpSystemJsonContentVo, jsonObject);
		} catch (ServiceException e) {
			logger.error("业务错误", e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (UnsupportedEncodingException e) {
		} catch (Exception e) {
			logger.error("未知错误{}", e);
			phpSystemJsonContentVo.setRetmsg(e.getMessage());
			phpSystemJsonContentVo.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/transferSubmit" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产转让只提交", httpMethod = "POST", response = AssetTransferDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void transferSubmit(@Valid @RequestBody AssetTransferSubmitFormDto assetForm, BindingResult bindingResult) {
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
		String jsonString = "";

		try {

			ConfigUtils.check();
			CrmUtils.checkAuth();
			ConfigUtils configUtils = new ConfigUtils();
			Integer chainType = configUtils.getChainType();
			CoinBaseContext coinBaseContext = CoinContextUtil.getCoinContext(chainType);
			JSONObject jsonObject = coinBaseContext.transSubmit(assetForm);

			ResponseUtil.successEcho(response, phpSystemJsonContentVo, jsonObject);
		}

		catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误{}", e);
			phpSystemJsonContentVo.setRetmsg(e.getMessage());
			phpSystemJsonContentVo.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/transferApply" }, method = RequestMethod.POST)
	@ApiOperation(value = "转账只申请", httpMethod = "POST", response = AssetTransferDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
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
	public void transferApply(@Valid @RequestBody AssetTransferFormDto assetTransferFormDto, BindingResult bindingResult) {
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
		String jsonString = "";
		try {
			ValidatorUtil.validate(bindingResult);
			ConfigUtils.check();
			ParamUtils.checkAssetNum(assetTransferFormDto.getSrcAsset());
			ParamUtils.checkSumAmount(assetTransferFormDto);
			TrustSDKUtil.checkPrivateKeyAccountIsMatch(assetTransferFormDto.getUserPrivateKey(), assetTransferFormDto.getSrcAccount());
			AssetTransferSubmitFormDto assetTransferSubmitFormDto = assetService.transferApply(assetTransferFormDto);
			phpSystemJsonContentVo.setData(assetTransferSubmitFormDto);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);

		} catch (TrustSDKException e) {
			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(new ServiceException().errorCode(StatusCode.SYSTEM_UNKOWN_ERROR).errorMessage(StatusCode.SYSTEM_UNKOWN_ERROR_MESSAGE));
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ServiceException e) {
			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("未知错误", e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setUnkownError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

	}

	
	
	@ResponseBody
	@RequestMapping(value = { "/settleSubmit" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产兑付只提交", httpMethod = "POST", response = AssetSettleDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void settleSubmit(@Valid @RequestBody AssetSettleSubmitFormDto assetForm, BindingResult bindingResult) {
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
		String jsonString = "";

		try {

			ConfigUtils.check();
			CrmUtils.checkAuth();
			ConfigUtils configUtils = new ConfigUtils();
			Integer chainType = configUtils.getChainType();
			CoinBaseContext coinBaseContext = CoinContextUtil.getCoinContext(chainType);
			JSONObject jsonObject = coinBaseContext.settleSubmit(assetForm);

			ResponseUtil.successEcho(response, phpSystemJsonContentVo, jsonObject);

		} catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		catch (Exception e) {
			logger.error("未知错误{}", e);
			phpSystemJsonContentVo.setRetmsg(e.getMessage());
			phpSystemJsonContentVo.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/getGasInfo" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产兑付只提交", httpMethod = "POST", response = AssetSettleDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void getGasInfo(@Valid @RequestBody GasInfoDto gasInfoDto, BindingResult bindingResult) {
		String jsonString = "";
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();

		GasInfoVo gasInfoVo = null;
		try {

			ConfigUtils.check();
			ConfigUtils configUtils = new ConfigUtils();
			Integer chainType = configUtils.getChainType();
			CoinBaseContext coinBaseContext = CoinContextUtil.getCoinContext(chainType);
			JSONObject jsonObject = coinBaseContext.getGasInfo(gasInfoDto);
			ResponseUtil.successEcho(response, phpSystemJsonContentVo, jsonObject);
		} catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (IOException e) {
			phpSystemJsonContentVo = phpSystemJsonContentVo.setUnkownError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		phpSystemJsonContentVo.setData(gasInfoVo);
		jsonString = JSON.toJSONString(phpSystemJsonContentVo);
		ResponseUtil.echo(response, jsonString);
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/getTransInfo" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产兑付只提交", httpMethod = "POST", response = AssetSettleDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.APPLY_THREAD_ERROR, message = StatusCode.APPLY_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_THREAD_ERROR, message = StatusCode.SUBMIT_THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void getTransInfo(@Valid @RequestBody EthTransInfoDto ethTransInfo, BindingResult bindingResult) {
		String jsonString = "";
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();

		EthTransInfoVo ethTransInfoVo = null;
		try {

			ConfigUtils.check();
			ConfigUtils configUtils = new ConfigUtils();
			Integer chainType = configUtils.getChainType();
			CoinBaseContext coinBaseContext = CoinContextUtil.getCoinContext(chainType);
			JSONObject jsonObject = coinBaseContext.getTransInfo(ethTransInfo);
			ResponseUtil.successEcho(response, phpSystemJsonContentVo, jsonObject);

		} catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		phpSystemJsonContentVo.setData(ethTransInfoVo);
		jsonString = JSON.toJSONString(phpSystemJsonContentVo);
		ResponseUtil.echo(response, jsonString);
		return;
	}
	
}
