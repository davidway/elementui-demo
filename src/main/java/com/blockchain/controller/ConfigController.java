package com.blockchain.controller;

import java.util.LinkedList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
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
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.blockchain.dto.BlockChainBowerDto;
import com.blockchain.dto.ConfigPropertiesFormDTO;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.ConfigPropertiesService;
import com.blockchain.util.ConfigUtils;
import com.blockchain.util.CrmUtils;
import com.blockchain.util.ResponseUtil;
import com.blockchain.util.ResultUtil;
import com.blockchain.util.TencentChainUtils;
import com.blockchain.util.TrustSDKUtil;
import com.blockchain.util.ValidatorUtil;
import com.blockchain.vo.PhpSystemJsonContentVO;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.tencent.trustsql.sdk.util.HttpClientUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Controller("ConfigController")
@RequestMapping(value = "/configProperties")
public class ConfigController {
	Logger logger = Logger.getLogger(ConfigController.class);
	@Resource
	HttpServletResponse response;
	@Resource
	ConfigPropertiesService configPropertiesService;

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
	@RequestMapping(value = { "/add" }, method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.AUTHORITY_ERROR, message = StatusCode.AUTHORITY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.URL_NOT_EXISTS, message = StatusCode.URL_NOT_EXISTS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.TIME_OUT, message = StatusCode.TIME_OUT_MESSAGE, response = StatusCode.class),

			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	@ApiOperation(value = "生成/修改 公共配置信息", httpMethod = "POST", response = ConfigPropertiesFormDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public void add(@Valid @RequestBody ConfigPropertiesFormDTO configPropertiesFormDTO, BindingResult bindingResult) throws TrustSDKException {

		PhpSystemJsonContentVO PhpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";

		try {
			ValidatorUtil.validate(bindingResult);
			CrmUtils.checkAuth();
			TrustSDKUtil.checkPariKeyMatch(configPropertiesFormDTO.getCreateUserPublicKey(), configPropertiesFormDTO.getCreateUserPrivateKey());
			configPropertiesService.add(configPropertiesFormDTO);
		} catch (ServiceException e) {
			logger.error("错误信息", e);
			PhpSystemJsonContentVO = PhpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(PhpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("错误信息", e);
			PhpSystemJsonContentVO.setRetmsg(e.getMessage());
			PhpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(PhpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		configPropertiesFormDTO = configPropertiesService.get();
		PhpSystemJsonContentVO.setData(configPropertiesFormDTO);
		jsonString = JSON.toJSONString(PhpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
		ResponseUtil.echo(response, jsonString);
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/get" }, method = RequestMethod.POST)
	@ApiOperation(value = "获取公共配置信息", httpMethod = "POST", response = ConfigPropertiesFormDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public void get() {
		PhpSystemJsonContentVO PhpSystemJsonContentVO = new PhpSystemJsonContentVO();
		ConfigPropertiesFormDTO configPropertiesFormDTO = configPropertiesService.get();
		PhpSystemJsonContentVO.setData(configPropertiesFormDTO);
		String jsonString = JSON.toJSONString(PhpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
		ResponseUtil.echo(response, jsonString);
		return;

	}

	@ResponseBody
	@RequestMapping(value = { "/getChainInfo" }, method = RequestMethod.POST)
	@ApiOperation(value = "获取公共配置信息", httpMethod = "POST", response = ConfigPropertiesFormDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public void getBlockChainInfo() {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/trustsql_baas_getchainbynode.cgi";
		try {
			ConfigUtils.check();
			String applyString = TencentChainUtils.generateChainByNodeParam();

			String applyResultString = HttpClientUtil.post(applyUrl, applyString);
			ResultUtil.checkResultIfSuccess("获取连信息接口", applyResultString);
			// JSONUtils.prettyPrint(applyResultString);
			LinkedList<BlockChainBowerDto> blockChainBowerDto = TencentChainUtils.genereateChainByNodeResult(applyResultString);

			phpSystemJsonContentVO.setData(blockChainBowerDto);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
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

	}

	@ResponseBody
	@RequestMapping(value = { "/getNodeInfo" }, method = RequestMethod.POST)
	@ApiOperation(value = "获取节点信息", httpMethod = "POST", response = ConfigPropertiesFormDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public void getNodeInfo() {
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/nbaas_getchaininfo.cgi";
		try {
			String applyString = TencentChainUtils.generateChainInfo();

			String applyResultString = HttpClientUtil.post(applyUrl, applyString);

			PhpSystemJsonContentVO PhpSystemJsonContentVO = new PhpSystemJsonContentVO();
			PhpSystemJsonContentVO.setData(applyResultString);
			String jsonString = JSON.toJSONString(PhpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@ResponseBody
	@RequestMapping(value = { "/getTransHeightInfo" }, method = RequestMethod.POST)
	@ApiOperation(value = "获取交易高度信息", httpMethod = "POST", response = ConfigPropertiesFormDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public void getTransHeightInfo() {
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/nbaas_gettxbyheight.cgi";
		try {
			String applyString = TencentChainUtils.generateGetTxByHeight();

			String applyResultString = HttpClientUtil.post(applyUrl, applyString);
			PhpSystemJsonContentVO PhpSystemJsonContentVO = new PhpSystemJsonContentVO();
			PhpSystemJsonContentVO.setData(applyResultString);
			String jsonString = JSON.toJSONString(PhpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
