package com.blockchain.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.blockchain.dto.*;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.AssetService;
import com.blockchain.service.IssService;
import com.blockchain.service.impl.AssetServiceImpl;
import com.blockchain.service.impl.IssServiceImpl;
import com.blockchain.util.*;
import com.blockchain.vo.PhpSystemJsonContentVO;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller("IssController")
@RequestMapping(value = "/iss")
public class IssController {
	Logger logger = LoggerFactory.getLogger(IssController.class);
	@Resource
	HttpServletResponse response;

	IssService issService = new IssServiceImpl();

	@ResponseBody
	@RequestMapping(value = { "/add" }, method = RequestMethod.POST)
	@ApiOperation(value = "转账申请且提交", httpMethod = "POST", response = AssetTransferDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
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
	public void add() throws Exception {
		IssDto issDto = new IssDto();
		ConfigDto configDto = new ConfigDto();
		String chainId = "chBiMAet6e3sRmeuws";
		String createUserPublicKey="AjSjvrUOvSpMK5ARrebyXqSFsy0/rDHz7IH2xSDlTmgQ";
		String createUserPrivateKey="w1ecBrij8PmWy55CW3F7ksgyYZHXkAC3WCaj269KXjI=";
		String mchId="gbHHj3f4gv36p5IQJw";
		String nodeId="nd8XbJ8hRtUC5GWP7j";
		String host="http://134.175.220.183:15903";

		configDto.setChainId(chainId);
		configDto.setCreateUserPrivateKey(createUserPrivateKey);
		configDto.setCreateUserPublicKey(createUserPublicKey);
		configDto.setHost(host);
		configDto.setMchId(mchId);
		configDto.setNodeId(nodeId);
		issDto.setConfigDto(configDto);

		String account="18jTUPKSrBq88G1pE6vMS2KCVzZLWZhP2y";
		JSONObject content=new JSONObject();
		JSONObject extraInfo  = new JSONObject();
        content.put("test","davidwsay");

        String privateKey="1s1iEsuirxos7G5b19pt3245JOpnRMuqV/VCSXhLYIc=";
        String publicKey="AjemGPHHe6XHQ3uPD4M2X92+jlwm5LbWllUefu5IPcoC";

        issDto.setUserPrivateKey(privateKey);
        issDto.setUserPublicKey(publicKey);
		issDto.setAccount(account);
		issDto.setContent(content);
		issDto.setExtraInfo(extraInfo);

		String jsonString="";
		issService.add(issDto);

		ResponseUtil.echo(response, jsonString);
	}

	@ResponseBody
	@RequestMapping(value = { "/search" }, method = RequestMethod.POST)
	@ApiOperation(value = "转账申请且提交", httpMethod = "POST", response = AssetTransferDTO.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
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
	public void search() throws Exception {
		IssSearchDto issSearchDto = new IssSearchDto();
		ConfigDto configDto = new ConfigDto();
		String chainId = "chBiMAet6e3sRmeuws";
		String createUserPublicKey="AjSjvrUOvSpMK5ARrebyXqSFsy0/rDHz7IH2xSDlTmgQ";
		String createUserPrivateKey="w1ecBrij8PmWy55CW3F7ksgyYZHXkAC3WCaj269KXjI=";
		String mchId="gbHHj3f4gv36p5IQJw";
		String nodeId="nd8XbJ8hRtUC5GWP7j";
		String host="http://134.175.220.183:15903";

		configDto.setChainId(chainId);
		configDto.setCreateUserPrivateKey(createUserPrivateKey);
		configDto.setCreateUserPublicKey(createUserPublicKey);
		configDto.setHost(host);
		configDto.setMchId(mchId);
		configDto.setNodeId(nodeId);
		issSearchDto.setConfigDto(configDto);

		String account="";
		JSONObject content=new JSONObject();
		JSONObject extraInfo  = new JSONObject();
		String hash= "417dcb611b64102a7db3f4d9c3bbaccaefb3367664d057fdb04e9ebc9df75b0f";
        issSearchDto.settHash(hash);
		issSearchDto.setAccount(account);
		issSearchDto.setContent(content);
		issSearchDto.setExtraInfo(extraInfo);

		String jsonString="";
		issService.search(issSearchDto);

		ResponseUtil.echo(response, jsonString);
	}


}
