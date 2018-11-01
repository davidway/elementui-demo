package com.blockchain.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.tencent.TencentUserService;
import com.blockchain.service.tencent.dto.AccountQueryFormDto;
import com.blockchain.service.tencent.dto.AssetDto;
import com.blockchain.service.tencent.dto.AssetTransQueryFormDto;
import com.blockchain.service.tencent.dto.KeyInfoDto;
import com.blockchain.service.tencent.dto.TransInfoDto;
import com.blockchain.service.tencent.dto.UserFormDto;
import com.blockchain.service.tencent.dto.UserKeyDto;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;
import com.blockchain.service.tencent.vo.PhpSystemJsonContentVo;
import com.blockchain.service.tencent.vo.UserInfoVo;
import com.blockchain.util.ConfigUtils;
import com.blockchain.util.ResponseUtil;
import com.blockchain.util.ValidatorUtil;
import com.tencent.trustsql.sdk.TrustSDK;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Controller("UserController")
@RequestMapping(value = "/user")
public class UserController {
	private static Logger logger = Logger.getLogger(UserController.class);

	@Resource
	HttpServletResponse response;

	@Resource
	TencentUserService tencentUserService;

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
			userKeyModel = tencentUserService.generatePairKey(userKeyModel);
		} catch (Exception e) {
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
			ValidatorUtil.validate(bindingResult);
			List<AssetDto> assetList = tencentUserService.accountQuery(assetForm);
			phpSystemJsonContentVo.setData(assetList);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
		} catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
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
			ValidatorUtil.validate(bindingResult);
			List<TransInfoDto> assetList = tencentUserService.transQuery(assetForm);
			phpSystemJsonContentVo.setData(assetList);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
		} catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		catch (Exception e) {
			logger.error("出错{}", e);
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
	public void getUserInfo(@RequestParam("privateKey") @ApiParam(value = "用户私钥", defaultValue = "{\"privateKey\":\"\"}") String privateKey) {
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();

		if (StringUtils.isBlank(privateKey)) {
			ServiceException e = new ServiceException().errorCode(StatusCode.PARAM_ERROR).errorMessage("用户私钥不能为空");
			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			String jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		UserInfoVo userInfoVo = new UserInfoVo();
		try {
			String baseAccoutAddress = TrustSDK.generateAddrByPrvkey(privateKey);
			String publicKey = TrustSDK.generatePubkeyByPrvkey(privateKey, true);
			userInfoVo.setBaseAccountAddress(baseAccoutAddress);
			userInfoVo.setBasePrivateKey(privateKey);
			userInfoVo.setBasePublicKey(publicKey);
			phpSystemJsonContentVo.setData(userInfoVo);

			String jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (TrustSDKException e) {
			logger.error(e);
			phpSystemJsonContentVo.setRetmsg(e.getMessage());
			phpSystemJsonContentVo.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			String jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
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
	public void addUserHasBaseAccount(@Valid @RequestBody UserFormDto userFormDto, BindingResult bindingResult) {
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
		String jsonString = "";
		try {
			ConfigUtils.check();
			ValidatorUtil.validate(bindingResult);
			UserInfoVo userInfoVo = tencentUserService.addUserHasBaseAccount(userFormDto);
			phpSystemJsonContentVo.setData(userInfoVo);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo, true);

		} catch (ServiceException e) {
			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (JSONException e) {
			logger.error(e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setParseJsonError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("内容",e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setSDKError();
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
		try {

			ValidatorUtil.validate(bindingResult);
		} catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			tencentUserService.checkPairKey(keyInfo);

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
}