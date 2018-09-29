package com.blockchain.controller;

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
import com.blockchain.dto.BlockChainType;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.ethereum.EthAssetService;
import com.blockchain.service.ethereum.dto.EthAssetSettleDto;
import com.blockchain.service.ethereum.dto.EthAssetTransferFormDto;
import com.blockchain.service.ethereum.dto.EthTransInfoDto;
import com.blockchain.service.ethereum.dto.EthereumAssetIssueFormDto;
import com.blockchain.service.tencent.AssetService;
import com.blockchain.service.tencent.dto.AssetIssueFormDto;
import com.blockchain.service.tencent.dto.AssetIssueDto;
import com.blockchain.service.tencent.dto.AssetSettleDto;
import com.blockchain.service.tencent.dto.AssetSettleFormDto;
import com.blockchain.service.tencent.dto.AssetSettleSubmitFormDto;
import com.blockchain.service.tencent.dto.AssetIssueSubmitFormDto;
import com.blockchain.service.tencent.dto.AssetTransferDto;
import com.blockchain.service.tencent.dto.AssetTransferFormDto;
import com.blockchain.service.tencent.dto.AssetTransferSubmitFormDto;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;
import com.blockchain.service.tencent.util.BeanUtils;
import com.blockchain.service.tencent.util.ConfigUtils;
import com.blockchain.service.tencent.util.CrmUtils;
import com.blockchain.service.tencent.util.ParamUtils;
import com.blockchain.service.tencent.util.TrustSDKUtil;
import com.blockchain.service.tencent.vo.PhpSystemJsonContentVo;
import com.blockchain.util.ResponseUtil;
import com.blockchain.util.ValidatorUtil;
import com.blockchain.validate.group.EthValidateGroup;
import com.blockchain.validate.group.TencentValidateGroup;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Controller("AssetController")
@RequestMapping(value = "/asset")
public class AssetController {
	Logger logger = LoggerFactory.getLogger(AssetController.class);
	@Resource
	HttpServletResponse response;
	@Resource
	AssetService assetService;
	@Resource
	EthAssetService ethAssetService;

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
		Integer chainType = configUtils.getChainType();

		try {
			ConfigUtils.check();
			switch (chainType) {
			case BlockChainType.TENCENT:
				new ValidatorUtil().validate(assetTransferFormDto, TencentValidateGroup.class);
				ParamUtils.checkAssetNum(assetTransferFormDto.getSrcAsset());
				TrustSDKUtil.checkPrivateKeyAccountIsMatch(assetTransferFormDto.getUserPrivateKey(), assetTransferFormDto.getSrcAccount());
				ConfigUtils.check();
				break;

			case BlockChainType.ETH:
				new ValidatorUtil().validate(assetTransferFormDto, EthValidateGroup.class);
				break;
			}

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

		try {

			switch (chainType) {
			case BlockChainType.TENCENT:
				AssetTransferDto assetTransferDto = assetService.transfer(assetTransferFormDto);
				phpSystemJsonContentVo.setData(assetTransferDto);
				jsonString = JSON.toJSONString(phpSystemJsonContentVo);
				ResponseUtil.echo(response, jsonString);

			case BlockChainType.ETH:
				EthAssetTransferFormDto ethAssetTransferFormDto = new EthAssetTransferFormDto();
				BeanUtils.copyProperties(assetTransferFormDto, ethAssetTransferFormDto);
				ethAssetService.transferToken(ethAssetTransferFormDto);
				jsonString = JSON.toJSONString(phpSystemJsonContentVo);
				ResponseUtil.echo(response, jsonString);
				break;
			}

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
			switch (chainType) {
			case BlockChainType.TENCENT:
				new ValidatorUtil().validate(assetSettleFormDto, TencentValidateGroup.class);
				ConfigUtils.check();
				TrustSDKUtil.checkPrivateKeyAccountIsMatch(assetSettleFormDto.getUserPrivateKey(), assetSettleFormDto.getOwnerAccount());
				ParamUtils.checkAssetNum(assetSettleFormDto.getSrcAsset());
				break;

			case BlockChainType.ETH:
				new ValidatorUtil().validate(assetSettleFormDto, EthValidateGroup.class);
				break;
			}

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

		try {
			switch (chainType) {
			case BlockChainType.TENCENT:
				AssetSettleDto assetSettleDto = assetService.settle(assetSettleFormDto);
				phpSystemJsonContentVo.setData(assetSettleDto);
				jsonString = JSON.toJSONString(phpSystemJsonContentVo);
				ResponseUtil.echo(response, jsonString);
				break;

			case BlockChainType.ETH:
				EthAssetSettleDto ethAssetSettleDto = new EthAssetSettleDto();
				BeanUtils.copyProperties(assetSettleFormDto, ethAssetSettleDto);
				ethAssetService.settleToken(ethAssetSettleDto);
				jsonString = JSON.toJSONString(phpSystemJsonContentVo);
				ResponseUtil.echo(response, jsonString);
				break;
			}

		} catch (ServiceException e) {
			logger.error("业务错误", e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {

			phpSystemJsonContentVo.setRetmsg(e.getMessage());
			phpSystemJsonContentVo.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
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
			configUtils = new ConfigUtils();
			chainType = configUtils.getChainType();
			switch (chainType) {
			case BlockChainType.TENCENT:
				new ValidatorUtil().validate(assetIssueFormDto, TencentValidateGroup.class);
				break;
			case BlockChainType.ETH:
				new ValidatorUtil().validate(assetIssueFormDto, EthValidateGroup.class);
				break;
			}
			// 校验结束
		} catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {

			switch (chainType) {
			case BlockChainType.TENCENT:
				AssetIssueDto assetIssueDto = assetService.issue(assetIssueFormDto);
				phpSystemJsonContentVo.setData(assetIssueDto);
				jsonString = JSON.toJSONString(phpSystemJsonContentVo);
				ResponseUtil.echo(response, jsonString);
				break;
			case BlockChainType.ETH:
				EthereumAssetIssueFormDto ethassetFormDTO = new EthereumAssetIssueFormDto();
				BeanUtils.copyProperties(assetIssueFormDto, ethassetFormDTO);
				ethAssetService.issueToken(ethassetFormDTO);
				jsonString = JSON.toJSONString(phpSystemJsonContentVo);
				ResponseUtil.echo(response, jsonString);
				break;
			}

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
			ValidatorUtil.validate(bindingResult);
		} catch (ServiceException e) {
			logger.error("业务错误", e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			AssetIssueDto assetIssueDto = assetService.issueSubmit(assetForm);
			phpSystemJsonContentVo.setData(assetIssueDto);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);

		} catch (ServiceException e) {

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
			ValidatorUtil.validate(bindingResult);
		} catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			AssetTransferDto assetTransferDto = assetService.transSubmit(assetForm);
			phpSystemJsonContentVo.setData(assetTransferDto);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);

		} catch (ServiceException e) {

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
			ValidatorUtil.validate(bindingResult);
			ConfigUtils.check();
		} catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			AssetSettleDto assetSettleDto = assetService.settleSubmit(assetForm);
			phpSystemJsonContentVo.setData(assetSettleDto);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);

		} catch (ServiceException e) {

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
}
