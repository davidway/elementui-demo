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
import com.alibaba.fastjson.JSONObject;
import com.blockchain.controller.factory.CoinBaseFactory;
import com.blockchain.controller.factory.CoinContextUtil;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.tencent.ConfigPropertiesService;
import com.blockchain.service.tencent.dto.BlockChainBowerDto;
import com.blockchain.service.tencent.dto.ConfigPropertiesFormDto;
import com.blockchain.service.tencent.impl.ConfigPropertiesServiceImpl;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;
import com.blockchain.service.tencent.trustsql.sdk.util.HttpClientUtil;
import com.blockchain.service.tencent.util.ConfigUtils;
import com.blockchain.service.tencent.util.CrmUtils;
import com.blockchain.service.tencent.util.TencentChainUtils;
import com.blockchain.service.tencent.vo.PhpSystemJsonContentVo;
import com.blockchain.util.ResponseUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Controller("ConfigController")
@RequestMapping(value = "/configProperties")
public class ConfigController {
	Logger logger = Logger.getLogger(ConfigController.class);
	@Resource
	HttpServletResponse response;

	ConfigPropertiesService configPropertiesService = new ConfigPropertiesServiceImpl();

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
	@RequestMapping(value = { "/add" }, method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.AUTHORITY_ERROR, message = StatusCode.AUTHORITY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.URL_NOT_EXISTS, message = StatusCode.URL_NOT_EXISTS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.TIME_OUT, message = StatusCode.TIME_OUT_MESSAGE, response = StatusCode.class),

			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	@ApiOperation(value = "生成/修改 公共配置信息", httpMethod = "POST", response = ConfigPropertiesFormDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public void add(@Valid @RequestBody ConfigPropertiesFormDto configPropertiesFormDto, BindingResult bindingResult) throws TrustSDKException {

		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
		String jsonString = "";

		try {
			CrmUtils.checkAuth();
			Integer chainType = configPropertiesFormDto.getChainType();
			CoinBaseContext coinBaseContext = CoinContextUtil.getCoinContext(chainType);
			JSONObject jsonObject = coinBaseContext.add(configPropertiesFormDto);
			
			ResponseUtil.successEcho(response,phpSystemJsonContentVo, jsonObject);
		} catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("错误信息", e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setUnkownError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}

		return;
	}

	private void check(Integer chainType) throws ServiceException {

		if (chainType == null) {
			throw new ServiceException().pos("设置chainType").errorCode(StatusCode.CONFIG_NOT_SET).errorMessage(StatusCode.CONFIG_NOT_SET_MESSAGE);
		}

	}

	@ResponseBody
	@RequestMapping(value = { "/get" }, method = RequestMethod.POST)
	@ApiOperation(value = "获取公共配置信息", httpMethod = "POST", response = ConfigPropertiesFormDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public void get() {
		ConfigUtils configUtils = new ConfigUtils();
		PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
		String jsonString;
		try {
			Integer chainType = configUtils.getChainType(); 
			check(chainType);
			CoinBaseContext coinBaseContext = CoinContextUtil.getCoinContext(chainType);
			JSONObject  configPropertiesFormDto= coinBaseContext.get();	
			ResponseUtil.successEcho(response,phpSystemJsonContentVo, configPropertiesFormDto);
			return;
			
		} catch (ServiceException e) {

			phpSystemJsonContentVo = phpSystemJsonContentVo.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("错误信息", e);
			phpSystemJsonContentVo = phpSystemJsonContentVo.setUnkownError(e.getMessage());
			jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
			return;
		}
	

	}

	@ResponseBody
	@RequestMapping(value = { "/getChainInfo" }, method = RequestMethod.POST)
	@ApiOperation(value = "获取公共配置信息", httpMethod = "POST", response = ConfigPropertiesFormDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public void getBlockChainInfo() {
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/trustsql_baas_getchainbynode.cgi";
		try {
			String applyString = TencentChainUtils.generateChainByNodeParam();
			System.out.println(applyString);

			String applyResultString = HttpClientUtil.post(applyUrl, applyString);
			// JSONUtils.prettyPrint(applyResultString);
			LinkedList<BlockChainBowerDto> blockChainBowerDto = TencentChainUtils.genereateChainByNodeResult(applyResultString);

			PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
			phpSystemJsonContentVo.setData(blockChainBowerDto);
			String jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@ResponseBody
	@RequestMapping(value = { "/getNodeInfo" }, method = RequestMethod.POST)
	@ApiOperation(value = "获取节点信息", httpMethod = "POST", response = ConfigPropertiesFormDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public void getNodeInfo() {
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/trustsql_baas_getchaininfo.cgi";
		try {
			String applyString = TencentChainUtils.generateChainInfo();

			String applyResultString = HttpClientUtil.post(applyUrl, applyString);

			PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
			phpSystemJsonContentVo.setData(applyResultString);
			String jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@ResponseBody
	@RequestMapping(value = { "/getTransHeightInfo" }, method = RequestMethod.POST)
	@ApiOperation(value = "获取交易高度信息", httpMethod = "POST", response = ConfigPropertiesFormDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public void getTransHeightInfo() {
		String applyUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/trustsql_baas_gettxbyheight.cgi";
		try {
			String applyString = TencentChainUtils.generateGetTxByHeight();

			String applyResultString = HttpClientUtil.post(applyUrl, applyString);
			PhpSystemJsonContentVo phpSystemJsonContentVo = new PhpSystemJsonContentVo();
			phpSystemJsonContentVo.setData(applyResultString);
			String jsonString = JSON.toJSONString(phpSystemJsonContentVo);
			ResponseUtil.echo(response, jsonString);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
