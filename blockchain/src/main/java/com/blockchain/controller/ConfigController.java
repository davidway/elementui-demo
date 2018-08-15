package com.blockchain.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.blockchain.VO.ConfigPropertiesFormVO;
import com.blockchain.VO.PhpSystemJsonContentVO;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.ParameterErrorException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.ConfigPropertiesService;
import com.blockchain.util.ResponseUtil;
import com.blockchain.util.TrustSDKUtil;
import com.blockchain.util.ValidatorUtil;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/configProperties")
public class ConfigController {
	Logger logger = Logger.getLogger(ConfigController.class);
	@Resource
	HttpServletResponse response;
	@Resource
	ConfigPropertiesService configPropertiesService;

	@ExceptionHandler(HttpMessageNotReadableException.class)  
	@ResponseBody  
	public PhpSystemJsonContentVO handleHttpMessageNotReadableException(  
	        HttpMessageNotReadableException ex) {  
		PhpSystemJsonContentVO response = new PhpSystemJsonContentVO();  
		response.setData("");
		response.setRetcode(StatusCode.PARAM_ERROR);
		response.setRetmsg("json格式错误，请检查是否为合法json");
	    return response;  
	}  
	
	
	@ResponseBody
	@RequestMapping(value = { "/add" }, method = RequestMethod.POST)
	@ApiOperation(value = "生成/修改 公共配置信息", httpMethod = "POST", response = ConfigPropertiesFormVO.class, consumes = "application/json",produces=MediaType.APPLICATION_JSON_VALUE)
	public void add(@Valid @RequestBody ConfigPropertiesFormVO configPropertiesFormVO, BindingResult bindingResult) throws TrustSDKException {

		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";

	
		try {
			ValidatorUtil.validate(bindingResult);
			TrustSDKUtil.checkPariKeyMatch(configPropertiesFormVO.getCreateUserPublicKey(), configPropertiesFormVO.getCreateUserPrivateKey());
		} catch (ParameterErrorException e1) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e1.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (ServiceException e) {
			phpSystemJsonContentVO = phpSystemJsonContentVO.setParameterError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}
	
		try {
			configPropertiesService.add(configPropertiesFormVO);
		} catch (Exception e) {
			logger.error(e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO);
			ResponseUtil.echo(response, jsonString);
			return;
		}
		configPropertiesFormVO = configPropertiesService.get();
		phpSystemJsonContentVO.setData(configPropertiesFormVO);
		jsonString = JSON.toJSONString(phpSystemJsonContentVO,SerializerFeature.WriteMapNullValue);
		ResponseUtil.echo(response, jsonString);
		return;
	}

	@ResponseBody
	@RequestMapping(value = { "/get" }, method = RequestMethod.POST)
	@ApiOperation(value = "获取公共配置信息", httpMethod = "POST", response = ConfigPropertiesFormVO.class, consumes = "application/json",produces=MediaType.APPLICATION_JSON_VALUE)
	public void get() {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		ConfigPropertiesFormVO configPropertiesFormVO = configPropertiesService.get();
		phpSystemJsonContentVO.setData(configPropertiesFormVO);
		String jsonString = JSON.toJSONString(phpSystemJsonContentVO,SerializerFeature.WriteMapNullValue);
		ResponseUtil.echo(response, jsonString);
		return;

	}
}
