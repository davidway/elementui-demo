package com.blockchain.controller;

import java.util.List;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.blockchain.dto.AssetTransQueryFormDTO;
import com.blockchain.dto.BlockChainInfoDto;
import com.blockchain.dto.BlockDetailsInfo;
import com.blockchain.dto.BlockTransDto;
import com.blockchain.dto.ConfigDto;
import com.blockchain.dto.TransDetailsVo;
import com.blockchain.dto.TransHeightDto;
import com.blockchain.dto.TransInfoDetailsDto;
import com.blockchain.dto.TransInfoDto;
import com.blockchain.dto.TransInfoListDto;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.BlockChainBrowserService;
import com.blockchain.util.ConfigUtils;
import com.blockchain.util.ParamUtils;
import com.blockchain.util.ResponseUtil;
import com.blockchain.util.ValidatorUtil;
import com.blockchain.vo.PhpSystemJsonContentVO;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Controller("BlockChainBrowserController")
@RequestMapping(value = "/browser")
public class BlockChainBrowserController {

	Logger logger = Logger.getLogger(BlockChainBrowserController.class);
	@Resource
	HttpServletResponse response;
	@Resource
	BlockChainBrowserService blockChainBrowserService;

	@ResponseBody
	@RequestMapping(value = { "/getChainInfo" }, method = RequestMethod.POST)
	@ApiOperation(value = "获取联盟链信息", httpMethod = "POST", response = BlockChainInfoDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.AUTHORITY_ERROR, message = StatusCode.AUTHORITY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.URL_NOT_EXISTS, message = StatusCode.URL_NOT_EXISTS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.TIME_OUT, message = StatusCode.TIME_OUT_MESSAGE, response = StatusCode.class),

			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void getChainInfo(@Valid @RequestBody ConfigDto configDto, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";

		try {
			//ConfigUtils.check();
			ValidatorUtil.validate(bindingResult);
			BlockChainInfoDto blockTransChainInfoDto = blockChainBrowserService.getChainInfo(configDto);
			phpSystemJsonContentVO.setData(blockTransChainInfoDto);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
		} catch (ServiceException e) {

			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("错误信息", e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		}

	}

	@ResponseBody
	@RequestMapping(value = { "/getBlockInfoList" }, method = RequestMethod.POST)
	@ApiOperation(value = "通过区块高度获取区块信息列表List", httpMethod = "POST", response = BlockTransDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.AUTHORITY_ERROR, message = StatusCode.AUTHORITY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.URL_NOT_EXISTS, message = StatusCode.URL_NOT_EXISTS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.TIME_OUT, message = StatusCode.TIME_OUT_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
	public void getBlockInfoList(@Valid @RequestBody TransHeightDto transHeightDto, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			//ConfigUtils.check();
			ValidatorUtil.validate(bindingResult);
			ParamUtils.checkNum(transHeightDto.getBeginHeight(), transHeightDto.getEndHeight());
			List<BlockTransDto> blockChainInfoDtoList = blockChainBrowserService.getBlockInfoList(transHeightDto);
			phpSystemJsonContentVO.setData(blockChainInfoDtoList);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
		} catch (ServiceException e) {

			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("错误信息", e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		}
	}

	@ResponseBody
	@RequestMapping(value = { "/getTransInfoList" }, method = RequestMethod.POST)
	@ApiOperation(value = "通过交易查询获取交易信息列表List", httpMethod = "POST", response = TransInfoDto.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.AUTHORITY_ERROR, message = StatusCode.AUTHORITY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.URL_NOT_EXISTS, message = StatusCode.URL_NOT_EXISTS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.TIME_OUT, message = StatusCode.TIME_OUT_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
public void getTransInfoList(@Valid @RequestBody TransInfoListDto transInfoListDto, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			//ConfigUtils.check();
			ValidatorUtil.validate(bindingResult);

			AssetTransQueryFormDTO assetTransQueryFormDTO = new AssetTransQueryFormDTO();
			Integer pageNo = transInfoListDto.getPageNo();
			Integer pageLimit = transInfoListDto.getPageLimit();
			assetTransQueryFormDTO.setPageNo(pageNo);
			assetTransQueryFormDTO.setPageLimit(pageLimit);
			ConfigDto configDto = transInfoListDto.getConfigDto();
			assetTransQueryFormDTO.setConfigDto(configDto);
			List<TransInfoDto> blockChainInfoDtoList = blockChainBrowserService.getTransInfoList(assetTransQueryFormDTO);
			phpSystemJsonContentVO.setData(blockChainInfoDtoList);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
		} catch (ServiceException e) {

			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("错误信息", e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		}
	}

	@ResponseBody
	@RequestMapping(value = { "/getTransInfoDetails" }, method = RequestMethod.POST)
	@ApiOperation(value = "通过交易hash获取交易信息详情Details", httpMethod = "POST", response = TransDetailsVo.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.AUTHORITY_ERROR, message = StatusCode.AUTHORITY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.URL_NOT_EXISTS, message = StatusCode.URL_NOT_EXISTS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.TIME_OUT, message = StatusCode.TIME_OUT_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
public void getTransInfoDetails(@Valid @RequestBody TransInfoDetailsDto transInfoDetailsDto, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			
			ValidatorUtil.validate(bindingResult);
			AssetTransQueryFormDTO assetTransQueryFormDTO = new AssetTransQueryFormDTO();
			assetTransQueryFormDTO.setTransHash(transInfoDetailsDto.getTransHash());
			assetTransQueryFormDTO.setConfigDto(transInfoDetailsDto.getConfigDto());
			TransDetailsVo transDetailsVo = blockChainBrowserService.getTransInfoDetailsByHash(assetTransQueryFormDTO);
			phpSystemJsonContentVO.setData(transDetailsVo);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
		} catch (ServiceException e) {

			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("错误信息", e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		}
	}

	@ResponseBody
	@RequestMapping(value = { "/getBlockInfoDetails" }, method = RequestMethod.POST)
	@ApiOperation(value = "通过区块高度获取区块详情Details", httpMethod = "POST", response = BlockDetailsInfo.class, consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = StatusCode.THREAD_ERROR, message = StatusCode.THREAD_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.AUTHORITY_ERROR, message = StatusCode.AUTHORITY_ERROR_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.URL_NOT_EXISTS, message = StatusCode.URL_NOT_EXISTS_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.TIME_OUT, message = StatusCode.TIME_OUT_MESSAGE, response = StatusCode.class),
			@ApiResponse(code = StatusCode.CONFIG_NOT_SET, message = StatusCode.CONFIG_NOT_SET_MESSAGE, response = StatusCode.class) })
public void getBlockInfoDetails(@Valid @RequestBody TransHeightDto transHeightDto, BindingResult bindingResult) {
		PhpSystemJsonContentVO phpSystemJsonContentVO = new PhpSystemJsonContentVO();
		String jsonString = "";
		try {
			//ConfigUtils.check();
			ValidatorUtil.validate(bindingResult);
			ParamUtils.checkNumForDetails(transHeightDto.getBeginHeight(), transHeightDto.getEndHeight());
			BlockDetailsInfo blockDetail = blockChainBrowserService.getBlockInfoDetails(transHeightDto);
			phpSystemJsonContentVO.setData(blockDetail);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
		} catch (ServiceException e) {

			phpSystemJsonContentVO = phpSystemJsonContentVO.setKnownError(e);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		} catch (Exception e) {
			logger.error("错误信息", e);
			phpSystemJsonContentVO.setRetmsg(e.getMessage());
			phpSystemJsonContentVO.setRetcode(StatusCode.SYSTEM_UNKOWN_ERROR);
			jsonString = JSON.toJSONString(phpSystemJsonContentVO, SerializerFeature.WriteMapNullValue);
			ResponseUtil.echo(response, jsonString);
			return;
		}
	}
}
