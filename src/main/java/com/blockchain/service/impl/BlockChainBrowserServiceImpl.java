package com.blockchain.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.constant.TransStatus;
import com.blockchain.dto.AssetTransQueryFormDTO;
import com.blockchain.dto.BlockChainInfoDto;
import com.blockchain.dto.BlockDetailsInfo;
import com.blockchain.dto.BlockTransChainInfoDto;
import com.blockchain.dto.BlockTransDto;
import com.blockchain.dto.ConfigDto;
import com.blockchain.dto.TransDetailsVo;
import com.blockchain.dto.TransHeightDto;
import com.blockchain.dto.TransInfoDto;
import com.blockchain.exception.ServiceException;
import com.blockchain.service.BlockChainBrowserService;
import com.blockchain.service.UserService;
import com.blockchain.util.ConfigUtils;
import com.blockchain.util.ResultUtil;
import com.blockchain.util.TencentChainUtils;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.tencent.trustsql.sdk.util.HttpClientUtil;

@Service("BlockChainBrowserServiceImpl")
public class BlockChainBrowserServiceImpl implements BlockChainBrowserService {

	@Resource
	UserService userService;

	@Override
	public List<BlockTransDto> getBlockInfoList(TransHeightDto transHeightDto) throws UnsupportedEncodingException, TrustSDKException, Exception {
		String applyString = TencentChainUtils.generateTransHeightInfo(transHeightDto);
		ConfigDto configDto = transHeightDto.getConfigDto();
		String applyUrl = configDto.getHost() + "/GetTxinfoByHeight";
		applyUrl = applyUrl.replace("15910", "15909");// 腾讯区块链在这里设置了15909的端口，与原来数字资产的端口不同

		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		ResultUtil.checkResultIfSuccess("获取节点列表接口", applyResultString);
		
		List<BlockTransDto> blockChainInfoDtoList = TencentChainUtils.getTransHeightInfoResult(applyResultString,configDto);

		return blockChainInfoDtoList;
	}

	@Override
	public BlockChainInfoDto getChainInfo(ConfigDto configDto) throws UnsupportedEncodingException, TrustSDKException, Exception {
		String applyString = TencentChainUtils.generateBlockChainInfo(configDto);
		String applyUrl = "https://baas.qq.com/cgi-bin/v1.0/nbaas_getchaininfo.cgi";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		ResultUtil.checkResultIfSuccess("获取联盟链节点接口", applyResultString);

		BlockChainInfoDto blockTransChainInfoDto = TencentChainUtils.genereateChainInfoResult(applyResultString);

		return blockTransChainInfoDto;
	}

	/*
	 * private BlockChainInfoDto getTransTotalSummary(BlockChainInfoDto
	 * blockTransChainInfoDto) throws UnsupportedEncodingException,
	 * TrustSDKException, Exception {
	 * 
	 * String applyString = TencentChainUtils.generateSummaryInfo(date);
	 * ConfigUtils configUtils = new ConfigUtils();
	 * 
	 * String applyUrl = configUtils.getHost() + "/asset_rec_sum_query"; String
	 * applyResultString = HttpClientUtil.post(applyUrl, applyString);
	 * System.out.println("汇总接口返回的"+applyResultString); blockTransChainInfoDto =
	 * TencentChainUtils.getSummaryInfoResult(applyResultString);
	 * 
	 * 
	 * return blockTransChainInfoDto;
	 * 
	 * }
	 */

	@Override
	public TransDetailsVo getTransInfoDetailsByHash(AssetTransQueryFormDTO queryTransInfoDto) throws ServiceException, TrustSDKException, Exception {
		TransDetailsVo transDetailsVo = null;
		
		queryTransInfoDto.setState(new Integer[]{TransStatus.SUBMIT_SUCCESS});
		queryTransInfoDto.setConfigDto(queryTransInfoDto.getConfigDto());
		List<TransInfoDto> transInfoDtoList = TencentChainUtils.transQuery(queryTransInfoDto);
		TransInfoDto transInfoDto = null;
		// 获取单笔交易
		if (transInfoDtoList != null && transInfoDtoList.size() > 0) {
			transInfoDto = transInfoDtoList.get(0);
			transDetailsVo = new TransDetailsVo();
			transDetailsVo.setTransInfo(transInfoDto);
			// 得到blockTrans的信息
			TransHeightDto transHeightDto = new TransHeightDto();
			Long beginHeight = 0L;
			Long endHeight = 0L;
			ConfigDto configDto = queryTransInfoDto.getConfigDto();
			if (transInfoDto != null) {
				beginHeight = transInfoDto.getTransHeight();
				endHeight = transInfoDto.getTransHeight();
				transHeightDto.setEndHeight(endHeight);
				transHeightDto.setBeginHeight(beginHeight);
				transHeightDto.setConfigDto(configDto);
				List<BlockTransDto> blockTransDtoList = getBlockInfoList(transHeightDto);
				BlockTransDto blockTransDto = blockTransDtoList.get(0);
				transDetailsVo.setBlockTransDto(blockTransDto);

				BlockTransChainInfoDto blockTransChainInfoDto = getChainInfoByTransHash(queryTransInfoDto.getTransHash(),configDto);
				transDetailsVo.setBlockChainInfoDto(blockTransChainInfoDto);
				return transDetailsVo;
			}
		}

		return transDetailsVo;

	}

	private BlockTransChainInfoDto getChainInfoByTransHash(String transHash,ConfigDto configDto) throws Exception {
		String applyString = TencentChainUtils.generateBlockChainInfo(configDto);
		String applyUrl = "https://baas.qq.com/cgi-bin/v1.0/nbaas_getchaininfo.cgi";
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);

		BlockTransChainInfoDto blockTransChainInfoDto = TencentChainUtils.genereateChainInfoResultForTrans(applyResultString, transHash,configDto);

		return blockTransChainInfoDto;
	}

	

	@Override
	public BlockDetailsInfo getBlockInfoDetails(TransHeightDto transHeightDto) throws UnsupportedEncodingException, TrustSDKException, Exception {
		BlockDetailsInfo bockDetailsInfo = new BlockDetailsInfo();
		String applyString = TencentChainUtils.generateTransHeightInfo(transHeightDto);
		ConfigDto configDto = transHeightDto.getConfigDto();
		String applyUrl = configDto.getHost() + "/GetTxinfoByHeight";
		applyUrl = applyUrl.replace("15910", "15909");// 腾讯区块链在这里设置了15909的端口，与原来数字资产的端口不同

		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		ResultUtil.checkResultIfSuccess("获取节点详情接口", applyResultString);

		bockDetailsInfo = TencentChainUtils.generateBlockInfoDetails(applyResultString, transHeightDto.getBeginHeight(),transHeightDto.getConfigDto());

		return bockDetailsInfo;
	}

	@Override
	public List<TransInfoDto> getTransInfoList(AssetTransQueryFormDTO assetTransQueryFormDTO) throws TrustSDKException, Exception {
		List<TransInfoDto> transInfoList = new ArrayList<TransInfoDto>();
		assetTransQueryFormDTO.setConfigDto(assetTransQueryFormDTO.getConfigDto());
		assetTransQueryFormDTO.setState(new Integer[]{TransStatus.SUBMIT_SUCCESS});
		transInfoList = TencentChainUtils.transQuery(assetTransQueryFormDTO);
		for (TransInfoDto transInfoDto : transInfoList) {
			String blockHash = getBlockHashByHeight(transInfoDto.getTransHeight(),assetTransQueryFormDTO.getConfigDto());
			transInfoDto.setBlockHash(blockHash);
		}
		return transInfoList;
	}

	private String getBlockHashByHeight(long transHeight, ConfigDto configDto) throws Exception {

		TransHeightDto transHeightDto = new TransHeightDto();
		transHeightDto.setBeginHeight(transHeight);
		transHeightDto.setEndHeight(transHeight);
		transHeightDto.setConfigDto(configDto);
		String applyString = TencentChainUtils.generateTransHeightInfo(transHeightDto);

		String applyUrl = configDto.getHost() + "/GetTxinfoByHeight";
		applyUrl = applyUrl.replace("15910", "15909");// 腾讯区块链在这里设置了15909的端口，与原来数字资产的端口不同
		String applyResultString = HttpClientUtil.post(applyUrl, applyString);
		ResultUtil.checkResultIfSuccess("获取区块hash接口", applyResultString);

		JSONObject resultObject = JSON.parseObject(applyResultString);

		
		if (resultObject.getJSONArray("data") != null) {
			JSONArray resultDataObject = resultObject.getJSONArray("data");

			if (resultDataObject != null) {
				for (int i = 0; i < resultDataObject.size(); i++) {
					JSONObject txsJson = resultDataObject.getJSONObject(i).getJSONObject("txs");
					JSONArray transArray = txsJson.getJSONArray("rpt_txs");
					JSONObject blockObject = resultDataObject.getJSONObject(i).getJSONObject("block");
			
					JSONObject blockJson = blockObject;

					JSONObject blockHeader = blockJson.getJSONObject("header");
					Integer blockHeight = blockJson.getInteger("height");
					String blockHash = blockHeader.getString("hash");
					return blockHash;
				}
			}
		} else {
			return "";
		}
		return "";
	}

}
