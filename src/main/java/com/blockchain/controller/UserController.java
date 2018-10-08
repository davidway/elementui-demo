package com.blockchain.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web3j.crypto.CipherException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.blockchain.dto.BlockChainType;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.ethereum.EthUserService;
import com.blockchain.service.ethereum.dto.EthAccountQueryFormDto;
import com.blockchain.service.ethereum.dto.EthUserFormDto;
import com.blockchain.service.ethereum.vo.EthAndTokenAssetVo;
import com.blockchain.service.ethereum.vo.EthereumWalletInfo;
import com.blockchain.service.tencent.TencentUserService;
import com.blockchain.service.tencent.dto.AccountQueryFormDto;
import com.blockchain.service.tencent.dto.AssetDto;
import com.blockchain.service.tencent.dto.AssetTransQueryFormDto;
import com.blockchain.service.tencent.dto.KeyInfoDto;
import com.blockchain.service.tencent.dto.TransInfoDto;
import com.blockchain.service.tencent.dto.UserFormDto;
import com.blockchain.service.tencent.dto.UserKeyDto;
import com.blockchain.service.tencent.trustsql.sdk.TrustSDK;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;
import com.blockchain.service.tencent.util.ConfigUtils;
import com.blockchain.service.tencent.vo.PhpSystemJsonContentVo;
import com.blockchain.service.tencent.vo.UserInfoVo;
import com.blockchain.util.BlockChainName;
import com.blockchain.util.ResponseUtil;
import com.blockchain.util.ValidatorUtil;
import com.blockchain.validate.group.EthValidateGroup;
import com.blockchain.validate.group.TencentValidateGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Controller("UserController")
@RequestMapping(value = "/user")
public class UserController {
	private static Logger logger = Logger.getLogger(UserController.class);
	public static final Logger getReturnLogger = Logger.getLogger("getReturnLogger");
	@Resource
	HttpServletResponse response;

	@Resource
	TencentUserService tencentUserService;
	@Resource
	EthUserService ethUserService;

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
	@RequestMapping(value = { "/generatePariKey" }, method = RequestMethod.POST)
	@ApiOperation(value = "生成用户公私钥", httpMethod = "POST", response = UserKeyDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void generatePariKey() {
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
		String jsonString = "";

		UserKeyDto userKeyModel = new UserKeyDto();
	
		try {

			ConfigUtils.check();
			ConfigUtils configUtils = new ConfigUtils();
			Integer chainType = configUtils.getChainType();
			
			switch (chainType) {
			case BlockChainType.TENCENT:
				userKeyModel = tencentUserService.generatePairKey(userKeyModel);
				break;
			case BlockChainType.ETH:
				phpSystemJsonContentVo = phpSystemJsonContentVo.setNoSupportError();
				jsonString = JSON.toJSONString(phpSystemJsonContentVo);
				ResponseUtil.echo(response, jsonString);
				break;
			}
		}
		catch (Exception e) {
			logger.error("错误信息", e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setSDKError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		phpSystemJsonContentVo.setData(userKeyModel);

		try {
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
		} catch (JSONException e) {
			phpSystemJsonContentVo = phpSystemJsonContentVo.setParseJsonError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		ResponseUtil.echo(response, jsonString);
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/addUserHasBaseAndHostAccount" }, method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "创建用户时有基础和代理账户", httpMethod = "POST", response = UserInfoVo.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = "公私钥匹配失败", response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void addUserHasBaseAndHostAccount(@Valid @RequestBody UserFormDto userFormDTO, BindingResult bindingResult) {
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
		String jsonString = "";

	
		try {

			ConfigUtils.check();
			ConfigUtils configUtils = new ConfigUtils();
			Integer chainType = configUtils.getChainType();
			switch (chainType) {
			case BlockChainType.TENCENT:
				ConfigUtils.check();
				ValidatorUtil.validate(bindingResult);
				break;
			case BlockChainType.ETH:
				phpSystemJsonContentVo = phpSystemJsonContentVo.setNoSupportError();
				jsonString = JSON.toJSONString(phpSystemJsonContentVo);
				ResponseUtil.echo(response, jsonString);
				break;
			}
		} catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			UserInfoVo userInfoVO = tencentUserService.addUserHasBaseAndHostAccount(userFormDTO);
			phpSystemJsonContentVo.setData(userInfoVO);
		} catch (ServiceException e) {
			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);

			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("错误信息", e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setSDKError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			jsonString = JSON.toJSONString(phpSystemJsonContentVo, true);
		} catch (JSONException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setParseJsonError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		ResponseUtil.echo(response, jsonString);
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/accountQuery" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产批量查询", httpMethod = "POST", response = AssetDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void accountQuery(@Valid @RequestBody AccountQueryFormDto assetForm, BindingResult bindingResult) {
		PhpSystemJsonContentVo phpSystemJsonContentVO = new PhpSystemJsonContentVo();
		String jsonString = "";
		ConfigUtils configUtils = new ConfigUtils();
		Integer chainType = configUtils.getChainType();

		try {

			ConfigUtils.check();
			switch (chainType) {
			case BlockChainType.TENCENT:
				new ValidatorUtil().validate(assetForm, TencentValidateGroup.class);
				break;
			case BlockChainType.ETH:
				new ValidatorUtil().validate(assetForm, EthValidateGroup.class);
				break;
			}

		} catch (ServiceException e) {

			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {

			switch (chainType) {
			case BlockChainType.TENCENT:
				List<AssetDto> assetList = tencentUserService.accountQuery(assetForm);
				phpSystemJsonContentVO.setData(assetList);
				jsonString = JSON.toJSONString(phpSystemJsonContentVO);
				ResponseUtil.echo(response, jsonString);
				break;
			case BlockChainType.ETH:
				EthAccountQueryFormDto ethAccountQueryFormDto = new EthAccountQueryFormDto();
				BeanUtils.copyProperties(assetForm, ethAccountQueryFormDto);
				EthAndTokenAssetVo tokenAndEth = ethUserService.ethereumAccountQuery(ethAccountQueryFormDto);
				phpSystemJsonContentVO.setData(tokenAndEth);
				jsonString = JSON.toJSONString(phpSystemJsonContentVO);
				ResponseUtil.echo(response, jsonString);
				break;
			}

		} catch (ServiceException e) {

			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("错误信息", e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/transQuery" }, method = RequestMethod.POST)
	@ApiOperation(value = "交易批量查询", httpMethod = "POST", response = TransInfoDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void transQuery(@Valid @RequestBody AssetTransQueryFormDto assetForm, BindingResult bindingResult) {
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
		String jsonString = "";

	
		try {

			ConfigUtils.check();
			ConfigUtils configUtils = new ConfigUtils();
			Integer chainType = configUtils.getChainType();

			switch (chainType) {
			case BlockChainType.TENCENT:
				ConfigUtils.check();
				ValidatorUtil.validate(bindingResult);
				break;
			case BlockChainType.ETH:
				phpSystemJsonContentVo = phpSystemJsonContentVo.setNoSupportError();
				jsonString = JSON.toJSONString(phpSystemJsonContentVo);
				ResponseUtil.echo(response, jsonString);
				break;
			}

		}

		catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			List<TransInfoDto> assetList = tencentUserService.transQuery(assetForm);
			phpSystemJsonContentVo.setData(assetList);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);

		} catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error(e);
			phpSystemJsonContentVo.setRetmsg(e.getMessage());
			phpSystemJsonContentVo.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/getUserInfo" }, method = RequestMethod.POST)
	@ApiOperation(value = "查询用户信息", httpMethod = "POST", response = UserInfoVo.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void getUserInfo(@Valid @RequestParam("privateKey") @ApiParam(value = "用户私钥", required = false, defaultValue = "{\"privateKey\":\"\"}") String privateKey,
			@RequestParam("password") @ApiParam(value = "用户密码", required = false) String password) {
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();

		if (StringUtils.isBlank(privateKey)) {
			ServiceException e = new ServiceException().errorCode(StatusCode.PARAM_ERROR).errorMessage("用户私钥不能为空");
			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			String jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		ConfigUtils configUtils = new ConfigUtils();
		Integer chainType = configUtils.getChainType();

		String returnString = "";
		try {

			ConfigUtils.check();
			if (StringUtils.isBlank(password) && configUtils.getChainType().equals(BlockChainName.ETH)) {
				ServiceException e = new ServiceException().errorCode(StatusCode.PARAM_ERROR).errorMessage("用户私钥不能为空");
				phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
				returnString = JSON.toJSONString(phpSystemJsonContentVo);
				ResponseUtil.echo(response, returnString);
				return;
			}

			switch (chainType) {
			case BlockChainType.TENCENT:
				UserInfoVo userInfoVO = new UserInfoVo();
				userInfoVO = tencentUserService.getUserInfo(privateKey);
				phpSystemJsonContentVo.setData(userInfoVO);
				returnString = JSON.toJSONString(phpSystemJsonContentVo);
				ResponseUtil.echo(response, returnString);
				break;
			case BlockChainType.ETH:
				EthereumWalletInfo walletInfo = new EthereumWalletInfo();
				walletInfo = ethUserService.getUserInfo(password, privateKey);
				phpSystemJsonContentVo.setData(walletInfo);
				returnString = JSON.toJSONString(phpSystemJsonContentVo);
				ResponseUtil.echo(response, returnString);
				break;
			}

		} catch (TrustSDKException e) {
			logger.error(e);
			phpSystemJsonContentVo.setRetmsg(e.getMessage());
			phpSystemJsonContentVo.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			returnString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, returnString);
			return;
		} catch (ServiceException e) {
			logger.error(e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			returnString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, returnString);
			return;
		} catch (JsonProcessingException e) {
			logger.error(e);
			phpSystemJsonContentVo.setRetmsg(e.getMessage());
			phpSystemJsonContentVo.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			returnString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, returnString);
			return;
		} catch (CipherException e) {
			logger.error(e);
			phpSystemJsonContentVo.setRetmsg(e.getMessage());
			phpSystemJsonContentVo.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			returnString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, returnString);
			return;
		}
	}

	@ResponseBody
	@RequestMapping(value = { "/addUserHasBaseAccount" }, method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "创建用户只有基础钱包账户", httpMethod = "POST", response = UserInfoVo.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void addUserHasBaseAccount(@Valid @RequestBody UserFormDto userFormDTO, BindingResult bindingResult) {
		PhpSystemJsonContentVo phpSystemJsonContentVO = new PhpSystemJsonContentVo();
		String jsonString = "";
		ConfigUtils configUtils = null;
		Integer chainType = null;

		try {
			ConfigUtils.check();
			configUtils = new ConfigUtils();
			chainType = configUtils.getChainType();
			switch (chainType) {
			case BlockChainType.TENCENT:
				new ValidatorUtil().validate(userFormDTO, TencentValidateGroup.class);
				break;

			case BlockChainType.ETH:
				new ValidatorUtil().validate(userFormDTO, EthValidateGroup.class);
				break;
			}

		} catch (ServiceException e) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		try {
			switch (chainType) {
			case BlockChainType.TENCENT:
				UserInfoVo userInfoVO = tencentUserService.addUserHasBaseAccount(userFormDTO);
				phpSystemJsonContentVO.setData(userInfoVO);
				break;

			case BlockChainType.ETH:
				EthUserFormDto ethUserFormDto = new EthUserFormDto();
				String password = userFormDTO.getPassword();
				ethUserFormDto.setPassword(password);
				EthereumWalletInfo ethereumWalletInfo = ethUserService.addUserHasBaseAccount(ethUserFormDto);
				phpSystemJsonContentVO.setData(ethereumWalletInfo);
				break;
			}

		} catch (ServiceException e) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error(e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setSDKError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, true);
		} catch (JSONException e) {
			logger.error(e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParseJsonError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		ResponseUtil.echo(response, jsonString);
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/addUserHostAccount" }, method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "添加用户任意代理钱包账户", httpMethod = "POST", response = UserInfoVo.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void addUserHostAccount(@Valid @RequestBody UserFormDto userFormDTO, BindingResult bindingResult) {
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
		String jsonString = "";

	
		try {

			ConfigUtils.check();
			ConfigUtils configUtils = new ConfigUtils();
			Integer chainType = configUtils.getChainType();
			switch (chainType) {
			case BlockChainType.TENCENT:
				ConfigUtils.check();
				ValidatorUtil.validate(bindingResult);
				break;
			case BlockChainType.ETH:
				phpSystemJsonContentVo = phpSystemJsonContentVo.setNoSupportError();
				jsonString = JSON.toJSONString(phpSystemJsonContentVo);
				ResponseUtil.echo(response, jsonString);
				break;
			}

		}

		catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			UserInfoVo userInfoVO = tencentUserService.addUserHostAccount(userFormDTO);
			phpSystemJsonContentVo.setData(userInfoVO);
		} catch (ServiceException e) {
			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setSDKError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			jsonString = JSON.toJSONString(phpSystemJsonContentVo, true);
		} catch (JSONException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setParseJsonError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		ResponseUtil.echo(response, jsonString);
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/checkPairKey" }, method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "验证用户公私是否匹配", httpMethod = "POST", response = UserInfoVo.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void checkPairKey(@Valid @RequestBody KeyInfoDto keyInfo, BindingResult bindingResult) {
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
		String jsonString = "";

		Integer chainType=null;
		try {

			ConfigUtils.check();
			ConfigUtils configUtils = new ConfigUtils();
			 chainType = configUtils.getChainType();
			switch (chainType) {
			case BlockChainType.TENCENT:
				ValidatorUtil.validate(bindingResult);
				break;
			case BlockChainType.ETH:
				phpSystemJsonContentVo = phpSystemJsonContentVo.setNoSupportError();
				jsonString = JSON.toJSONString(phpSystemJsonContentVo);
				ResponseUtil.echo(response, jsonString);
				break;
			}

		} catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			switch (chainType) {
			case BlockChainType.TENCENT:
				tencentUserService.checkPairKey(keyInfo);
				break;

			case BlockChainType.ETH:
				ethUserService.checkPairKey(keyInfo);
				break;
			}

		} catch (ServiceException e) {
			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error(e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setSDKError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			jsonString = JSON.toJSONString(phpSystemJsonContentVo, true);
		} catch (JSONException e) {
			logger.error(e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setParseJsonError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		ResponseUtil.echo(response, jsonString);
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/test" }, method = RequestMethod.POST, consumes = "application/json")
	public void test(@RequestBody PhpSystemJsonContentVo phpSystemJsonContentVO, BindingResult bindingResult) {
		getReturnLogger.info(JSON.toJSONString(phpSystemJsonContentVO));

		return;
	}
}