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
import com.alibaba.fastjson.JSONObject;
import com.blockchain.controller.factory.CoinBaseFactory;
import com.blockchain.controller.factory.CoinContextUtil;
import com.blockchain.dto.BlockChainType;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.ethereum.EthUserService;
import com.blockchain.service.ethereum.dto.EthAccountQueryFormDto;
import com.blockchain.service.ethereum.dto.EthUserFormDto;
import com.blockchain.service.ethereum.impl.EthUserServiceImpl;
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
import com.blockchain.service.tencent.impl.TencentUserServiceImpl;
import com.blockchain.service.tencent.trustsql.sdk.TrustSDK;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;
import com.blockchain.service.tencent.util.ConfigUtils;
import com.blockchain.service.tencent.util.CrmUtils;
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

	TencentUserService tencentUserService = new TencentUserServiceImpl();

	EthUserService ethUserService = new EthUserServiceImpl();

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
				phpSystemJsonContentVo.setData(userKeyModel);
				jsonString = JSON.toJSONString(phpSystemJsonContentVo);
				ResponseUtil.echo(response, jsonString);
				break;
			case BlockChainType.ETH:
				phpSystemJsonContentVo = phpSystemJsonContentVo.setNoSupportError();
				jsonString = JSON.toJSONString(phpSystemJsonContentVo);
				ResponseUtil.echo(response, jsonString);
				break;
			}
		} catch (Exception e) {
			logger.error("错误信息", e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setSDKError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		return;
	}

/*	@ResponseBody
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

				jsonString = JSON.toJSONString(phpSystemJsonContentVo, true);
				ResponseUtil.echo(response, jsonString);
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
		} catch (Exception e) {
			logger.error("错误信息", e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setSDKError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		return;
	}*/

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
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
		String jsonString = "";
	

		try {

			ConfigUtils.check();
			CrmUtils.checkAuth();
			ConfigUtils configUtils = new ConfigUtils();
			Integer chainType = configUtils.getChainType();
			CoinBaseContext coinBaseContext = CoinContextUtil.getCoinContext(chainType);
			JSONObject jsonObject = coinBaseContext.accountQuery(assetForm);
			ResponseUtil.successEcho(response, phpSystemJsonContentVo, jsonObject);
		} catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		catch (Exception e) {
			logger.error("错误信息", e);
			phpSystemJsonContentVo.setRetmsg(e.getMessage());
			phpSystemJsonContentVo.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
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
				List<TransInfoDto> assetList = tencentUserService.transQuery(assetForm);
				phpSystemJsonContentVo.setData(assetList);
				jsonString = JSON.toJSONString(phpSystemJsonContentVo);
				ResponseUtil.echo(response, jsonString);
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
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
		String jsonString = "";
		ConfigUtils configUtils = null;
		Integer chainType = null;

		try {
			ConfigUtils.check();
			configUtils = new ConfigUtils();
			chainType = configUtils.getChainType();
			CoinBaseContext coinBaseContext = CoinContextUtil.getCoinContext(chainType);
			JSONObject s = coinBaseContext.addUserHasBaseAccount(userFormDTO);
			ResponseUtil.successEcho(response, phpSystemJsonContentVo, s);
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

		Integer chainType = null;
		try {

			ConfigUtils.check();
			ConfigUtils configUtils = new ConfigUtils();
			ValidatorUtil.validate(bindingResult);
			
			chainType = configUtils.getChainType();
			switch (chainType) {
			case BlockChainType.TENCENT:
			
				 boolean checkResult = tencentUserService.checkPairKey(keyInfo);
				 
				jsonString = JSON.toJSONString(phpSystemJsonContentVo, true);
				ResponseUtil.echo(response, jsonString);
				break;
			case BlockChainType.ETH:
				ethUserService.checkPairKey(keyInfo);
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
			logger.error(e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setSDKError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/test" }, method = RequestMethod.POST, consumes = "application/json")
	public void test(@RequestBody PhpSystemJsonContentVo phpSystemJsonContentVO, BindingResult bindingResult) {
		getReturnLogger.info(JSON.toJSONString(phpSystemJsonContentVO));

		return;
	}
}