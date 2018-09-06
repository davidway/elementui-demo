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
import com.blockchain.dto.AccountQueryFormDTO;
import com.blockchain.dto.AssetDTO;
import com.blockchain.dto.AssetTransQueryFormDTO;
import com.blockchain.dto.KeyInfoDTO;
import com.blockchain.dto.TransInfoDTO;
import com.blockchain.dto.UserFormDTO;
import com.blockchain.dto.UserKeyDTO;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.UserService;
import com.blockchain.util.ConfigUtils;
import com.blockchain.util.ResponseUtil;
import com.blockchain.util.ValidatorUtil;
import com.blockchain.vo.PhpSystemJsonContentVO;
import com.blockchain.vo.UserInfoVO;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
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
	UserService userService;

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	public PhpSystemJsonContentVO handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		PhpSystemJsonContentVO response = new PhpSystemJsonContentVO();
		response.setData("");
		response.setRetcode(StatusCode.PARAM_ERROR);
		response.setRetmsg("json格式错误，请检查是否为合法json");
		return response;
	}

	@ResponseBody
	@RequestMapping(value = { "/generatePariKey" }, method = RequestMethod.POST)
	@ApiOperation(value = "生成用户公私钥", httpMethod = "POST", response = UserKeyDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void generatePariKey() {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";

		UserKeyDTO userKeyModel = new UserKeyDTO();
		try {
			userKeyModel = userService.generatePairKey(userKeyModel);
		} catch (Exception e) {
			logger.error("错误信息",e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setSDKError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		phpSystemJsonContentVO.setData(userKeyModel);

		try {
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
		} catch (JSONException e) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParseJsonError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		ResponseUtil.echo(response, jsonString);
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/addUserHasBaseAndHostAccount" }, method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "创建用户时有基础和代理账户", httpMethod = "POST", response = UserInfoVO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = "公私钥匹配失败", response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void addUserHasBaseAndHostAccount(@Valid @RequestBody UserFormDTO userFormDTO, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			ConfigUtils.check();
			ValidatorUtil.validate(bindingResult);
		} catch (ServiceException e) {
		
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			UserInfoVO userInfoVO = userService.addUserHasBaseAndHostAccount(userFormDTO);
			phpSystemJsonContentVO.setData(userInfoVO);
		} catch (ServiceException e) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);

			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("错误信息",e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setSDKError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, true);
		} catch (JSONException e) {
		
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParseJsonError();
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		ResponseUtil.echo(response, jsonString);
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/accountQuery" }, method = RequestMethod.POST)
	@ApiOperation(value = "资产批量查询", httpMethod = "POST", response = AssetDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void accountQuery(@Valid @RequestBody AccountQueryFormDTO assetForm, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			ConfigUtils.check();
			ValidatorUtil.validate(bindingResult);
		} catch (ServiceException e) {
			
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			List<AssetDTO> assetList = userService.accountQuery(assetForm);
			phpSystemJsonContentVO.setData(assetList);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);

		} catch (ServiceException e) {
			
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("错误信息",e);
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
	@ApiOperation(value = "交易批量查询", httpMethod = "POST", response = TransInfoDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void transQuery(@Valid @RequestBody AssetTransQueryFormDTO assetForm, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			ConfigUtils.check();
			ValidatorUtil.validate(bindingResult);
		} catch (ServiceException e) {
			logger.error(e);
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			List<TransInfoDTO> assetList = userService.transQuery(assetForm);
			phpSystemJsonContentVO.setData(assetList);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);

		} catch (ServiceException e) {

			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error(e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/getUserInfo" }, method = RequestMethod.POST)
	@ApiOperation(value = "查询用户信息", httpMethod = "POST", response = UserInfoVO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void getUserInfo(@RequestParam("privateKey") @ApiParam(value = "用户私钥", defaultValue = "{\"privateKey\":\"\"}") String privateKey) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();

		if (StringUtils.isBlank(privateKey)) {
			ServiceException e = new ServiceException().errorCode(StatusCode.PARAM_ERROR).errorMessage("用户私钥不能为空");
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			String jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		UserInfoVO userInfoVO = new UserInfoVO();
		try {
			String baseAccoutAddress = TrustSDK.generateAddrByPrvkey(privateKey);
			String publicKey = TrustSDK.generatePubkeyByPrvkey(privateKey, true);
			userInfoVO.setBaseAccountAddress(baseAccoutAddress);
			userInfoVO.setBasePrivateKey(privateKey);
			userInfoVO.setBasePublicKey(publicKey);
			phpSystemJsonContentVO.setData(userInfoVO);

			String jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (TrustSDKException e) {
			logger.error(e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			String jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}
	}

	@ResponseBody
	@RequestMapping(value = { "/addUserHasBaseAccount" }, method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "创建用户只有基础钱包账户", httpMethod = "POST", response = UserInfoVO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void addUserHasBaseAccount(@Valid @RequestBody UserFormDTO userFormDTO, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			ConfigUtils.check();
			ValidatorUtil.validate(bindingResult);
		} catch (ServiceException e) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			UserInfoVO userInfoVO = userService.addUserHasBaseAccount(userFormDTO);
			phpSystemJsonContentVO.setData(userInfoVO);
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
	@ApiOperation(value = "添加用户任意代理钱包账户", httpMethod = "POST", response = UserInfoVO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void addUserHostAccount(@Valid @RequestBody UserFormDTO userFormDTO, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			ConfigUtils.check();
			ValidatorUtil.validate(bindingResult);
		} catch (ServiceException e) {

			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			UserInfoVO userInfoVO = userService.addUserHostAccount(userFormDTO);
			phpSystemJsonContentVO.setData(userInfoVO);
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
	@RequestMapping(value = { "/checkPairKey" }, method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "验证用户公私是否匹配", httpMethod = "POST", response = UserInfoVO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PARAM_ERROR, message = StatusCode.PARAM_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUCCESS, message = StatusCode.SUCCESS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.PAIR_KEY_ERROR, message = StatusCode.PAIR_KEY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SERVICE_EXCEPTION, message = StatusCode.SERVICE_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.SUBMIT_ERROR, message = StatusCode.SUBMIT_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void checkPairKey(@Valid @RequestBody KeyInfoDTO keyInfo, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {

			ValidatorUtil.validate(bindingResult);
		} catch (ServiceException e) {

			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		try {
			userService.checkPairKey(keyInfo);

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
}