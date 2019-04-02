package com.blockchain.util;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.constant.TransStatus;
import com.blockchain.dto.AssetTransQueryFormDTO;
import com.blockchain.dto.BlockChainBowerDto;
import com.blockchain.dto.BlockChainInfoDto;
import com.blockchain.dto.BlockDetailsInfo;
import com.blockchain.dto.BlockInfoDto;
import com.blockchain.dto.BlockTransChainInfoDto;
import com.blockchain.dto.BlockTransDto;
import com.blockchain.dto.ConfigDto;
import com.blockchain.dto.NodeInfo;
import com.blockchain.dto.NodeTransInfo;
import com.blockchain.dto.TransHeightDto;
import com.blockchain.dto.TransInfoDto;
import com.blockchain.exception.ServiceException;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.tencent.trustsql.sdk.util.HttpClientUtil;
import com.tencent.trustsql.sdk.util.SignStrUtil;


public class TencentChainUtils {
	
	static Logger logger = LoggerFactory.getLogger(TencentChainUtils.class);
	
	public static String generateChainByNodeParam() throws UnsupportedEncodingException, TrustSDKException, Exception {

		ConfigUtils configUtils = new ConfigUtils();
		String mchId = configUtils.getMchId();

		String nodeId = configUtils.getNodeId();
		String publicKey = configUtils.getCreateUserPublicKey();

		long timeStamp = System.currentTimeMillis() / 1000;
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("node_id", nodeId);
		paramMap.put("mch_id", mchId);
		paramMap.put("timestamp", timeStamp);

		paramMap.put("mch_sign", TrustSDK.signString(publicKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));

		JSONObject postJson = new JSONObject(true);
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public static String generateChainInfo() throws UnsupportedEncodingException, TrustSDKException, Exception {
		ConfigUtils configUtils = new ConfigUtils();

		String mchId = configUtils.getMchId();
		String prvKey = configUtils.getCreateUserPrivateKey();

		String chainId = configUtils.getChainId();

		long timeStamp = System.currentTimeMillis() / 1000;
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("chain_id", chainId);

		paramMap.put("mch_id", mchId);
		paramMap.put("timestamp", timeStamp);

		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject(true);
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public static String generateGetTxByHeight() throws UnsupportedEncodingException, TrustSDKException, Exception {
		ConfigUtils configUtils = new ConfigUtils();

		String mchId = configUtils.getMchId();
		String prvKey = configUtils.getCreateUserPrivateKey();

		String chainId = configUtils.getChainId();
		String nodeId = configUtils.getNodeId();
		long timeStamp = System.currentTimeMillis() / 1000;

		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("chain_id", chainId);
		paramMap.put("node_id", nodeId);
		paramMap.put("begin_height", 0);
		paramMap.put("end_height", 10);
		paramMap.put("mch_pubkey", configUtils.getCreateUserPublicKey());

		paramMap.put("mch_id", mchId);
		paramMap.put("timestamp", timeStamp);

		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject(true);
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public static LinkedList<BlockChainBowerDto> genereateChainByNodeResult(String applyResultString) {

		BlockChainBowerDto blockChainBowerDto = new BlockChainBowerDto();
		JSONObject resultJson = JSON.parseObject(applyResultString);
		JSONArray chainList = resultJson.getJSONArray("data");
		LinkedList<BlockChainBowerDto> list = new LinkedList<BlockChainBowerDto>();
		for (int i = 0; i < chainList.size(); i++) {
			JSONObject json = chainList.getJSONObject(i);
			String chainId = json.getString("chain_id");
			String chainName = json.getString("chain_name");
			BlockChainBowerDto temp = new BlockChainBowerDto();
			blockChainBowerDto.setChainId(chainId);
			blockChainBowerDto.setChainName(chainName);
			list.add(blockChainBowerDto);
		}

		return list;
	}

	public static String generateBlockChainInfo(ConfigDto configDto) throws UnsupportedEncodingException, TrustSDKException, Exception {
		

		String mchId = configDto.getMchId();
		String prvKey = configDto.getCreateUserPrivateKey();

		String chainId = configDto.getChainId();

		long timeStamp = System.currentTimeMillis() / 1000;

		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("chain_id", chainId);
		paramMap.put("mch_id", mchId);

		paramMap.put("timestamp", timeStamp);

		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject(true);
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public static BlockChainInfoDto genereateChainInfoResult(String applyResultString) {
		JSONObject resultObject = JSON.parseObject(applyResultString);
		BlockChainInfoDto blockChainInfoDto = new BlockChainInfoDto();
		if (resultObject.getJSONObject("data") != null) {
			JSONObject resultDataObject = resultObject.getJSONObject("data");
			JSONArray jsonArray = resultDataObject.getJSONArray("rpt_node");
			ArrayList<NodeInfo> nodeInfoArray = new ArrayList<NodeInfo>();
			Long firstLatestBlockHeight = null;
			boolean first = true;
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.size(); i++) {
					NodeInfo nodeInfo = new NodeInfo();
					JSONObject json = jsonArray.getJSONObject(i);
					String nodeName = json.getString("name");
					Integer nodeState = json.getInteger("state");
					String latestTransHash = json.getString("hash");
					Long latestBlockHeight = json.getLong("height");
					if (first) {
						if (nodeState == 2) {
							firstLatestBlockHeight = latestBlockHeight;
							first = false;
						}

					}
					Long latestBlockTime = json.getLong("block_time");

					nodeInfo.setLatestBlockHeight(latestBlockHeight);
					nodeInfo.setLatestBlockTime(latestBlockTime);
					nodeInfo.setLatestTransHash(latestTransHash);
					nodeInfo.setNodeName(nodeName);
					nodeInfo.setNodeState(nodeState);
					nodeInfoArray.add(nodeInfo);
				}
			}
			blockChainInfoDto.setNodeInfo(nodeInfoArray);
			Integer nodeTotalNum = resultDataObject.getInteger("node_count");
			blockChainInfoDto.setNodeTotalNum(nodeTotalNum);

			Integer chainState = resultDataObject.getInteger("chain_state");
			blockChainInfoDto.setChainState(chainState);

			blockChainInfoDto.setBlockHeight(firstLatestBlockHeight);
		}

		return blockChainInfoDto;
	}

	public static String generateTransHeightInfo(TransHeightDto transHeightDto) throws UnsupportedEncodingException, TrustSDKException, Exception {
		ConfigDto configDto = transHeightDto.getConfigDto();
		String nodeId = configDto.getNodeId();
		String mchId = configDto.getMchId();
		String prvKey = configDto.getCreateUserPrivateKey();
		String publicKey = configDto.getCreateUserPublicKey();
		Long beginHeight = transHeightDto.getBeginHeight();
		Long endHeight = transHeightDto.getEndHeight();

		String chainId = configDto.getChainId();

		long timeStamp = System.currentTimeMillis() / 1000;

		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("node_id", nodeId);
		paramMap.put("chain_id", chainId);
		paramMap.put("mch_id", mchId);
		paramMap.put("end_height", endHeight);
		paramMap.put("begin_height", beginHeight);
		paramMap.put("mch_pubkey", publicKey);

		paramMap.put("timestamp", timeStamp);

		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject(true);
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public static List<BlockTransDto> getTransHeightInfoResult(String applyResultString, ConfigDto configDto) throws ServiceException, TrustSDKException, Exception {
		JSONObject resultObject = JSON.parseObject(applyResultString);

		LinkedList<BlockTransDto> blockTransDtoList = new LinkedList<BlockTransDto>();
		if (resultObject.getJSONArray("data") != null) {
			JSONArray resultDataObject = resultObject.getJSONArray("data");

			if (resultDataObject != null) {
				for (int i = 0; i < resultDataObject.size(); i++) {
					JSONObject txsJson = resultDataObject.getJSONObject(i).getJSONObject("txs");
					JSONArray transArray = txsJson.getJSONArray("rpt_txs");
					JSONObject blockObject = resultDataObject.getJSONObject(i).getJSONObject("block");
					BlockTransDto blockTransDto = new BlockTransDto();
					JSONObject blockJson = blockObject;

					JSONObject blockHeader = blockJson.getJSONObject("header");
					Integer blockHeight = blockJson.getInteger("height");
					String blockHash = blockHeader.getString("hash");
					long timeStamp = blockJson.getLong("time");
					blockTransDto.setBlockHeight(blockHeight);
					blockTransDto.setBlockHash(blockHash);
					blockTransDto.setCreateTime(timeStamp);
				
					for (int j = 0; j < transArray.size(); j++) {
						// TODO:
						JSONObject transJson = transArray.getJSONObject(j);
						AssetTransQueryFormDTO assetForm = new AssetTransQueryFormDTO();
						String transHash = transJson.getString("hash");
						assetForm.setTransHash(transHash);
						
						assetForm.setConfigDto(configDto);
						assetForm.setState(new Integer[]{TransStatus.SUBMIT_SUCCESS});
						List<TransInfoDto> assetList = TencentChainUtils.transQuery(assetForm);
						long sum = 0;
						for (TransInfoDto transInfoDto : assetList) {
							sum += transInfoDto.getAmount() + transInfoDto.getFeeAmount();
						}
						blockTransDto.setAmount(sum);
						blockTransDto.setTransTotalNum(assetList.size());
						blockTransDtoList.add(blockTransDto);
					}

				}

			}

		}
		Collections.sort(blockTransDtoList, new Comparator<BlockTransDto>() {
			@Override
			public int compare(BlockTransDto one, BlockTransDto another) {

				return (int) -(one.getBlockHeight() - another.getBlockHeight() );
			}
		});
		return blockTransDtoList;
	}

	public static List<TransInfoDto> transQuery(AssetTransQueryFormDTO assetForm) throws TrustSDKException, Exception {
		String accountQueryString = UserUtil.generateTransQueryParam(assetForm);
		 logger.debug("调用【交易查询前】{}",  accountQueryString);
		ConfigDto configDto = assetForm.getConfigDto();
		String url = configDto.getHost() + "/trans_batch_query";
		 logger.info("调用的接口名称是{}",  url);
		String accountQueryResult = HttpClientUtil.post(url, accountQueryString);
		ResultUtil.checkResultIfSuccess("交易查询接口", accountQueryResult);
		 logger.debug("调用【交易查询后】{}" , accountQueryResult);
		
		 JSONObject userRegistRetData = JSON.parseObject(accountQueryResult);

		JSONArray jsonArray = JSON.parseArray(userRegistRetData.getString("trans_list"));
		List<TransInfoDto> transInfoList = new LinkedList<TransInfoDto>();
		if (jsonArray != null) {
			try {
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject o = jsonArray.getJSONObject(i);
					TransInfoDto transInfoDto = new TransInfoDto();
					transInfoDto.setAmount(o.getLong("dst_amount"));
					transInfoDto.setFeeAmount(o.getLong("fee_amount"));
					transInfoDto.setDstAccount(o.getString("dst_account"));
					transInfoDto.setDstAssetId(o.getString("dst_asset_id"));
					transInfoDto.setTransHash(o.getString("trans_hash"));
					
					transInfoDto.setSrcAccount(o.getString("src_account"));
					transInfoDto.setSrcAssetId(o.getString("src_asset_list"));
					transInfoDto.setTransactionId(o.getString("transaction_id"));
					transInfoDto.setTransState(o.getInteger("trans_state"));
					transInfoDto.setTransTime(o.getString("extra_info"));
					if (StringUtils.isNotBlank(o.getString("b_time"))){
						long timeStamp =(Timestamp.valueOf(o.getString("b_time")).getTime()/1000);
						transInfoDto.setTransTime(o.getString("b_time"));
						transInfoDto.setTransHeight(o.getLong("b_height"));
						
					}

					transInfoDto.setTransType(o.getInteger("trans_type"));
					transInfoDto.setSignList(o.getString("sign_str_list"));
					transInfoList.add(transInfoDto);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			
		return transInfoList;
	}

	public static String generateSummaryInfo(String date) throws UnsupportedEncodingException, TrustSDKException, Exception {
		ConfigUtils configUtils = new ConfigUtils();
		String nodeId = configUtils.getNodeId();
		String mchId = configUtils.getMchId();
		String prvKey = configUtils.getCreateUserPrivateKey();
		String publicKey = configUtils.getCreateUserPublicKey();

		String chainId = configUtils.getChainId();

		long timeStamp = System.currentTimeMillis() / 1000;

		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("node_id", nodeId);
		paramMap.put("chain_id", chainId);
		paramMap.put("mch_id", mchId);
		paramMap.put("sign_type", "ECDSA");
		List<Integer> assetType = new ArrayList<Integer>();
		assetType.add(1);
		paramMap.put("asset_type", assetType);
		paramMap.put("version", "2.0");
		List<Integer> state = new ArrayList<Integer>();
		state.add(0);
		paramMap.put("state",state);

		paramMap.put("date", date);

		paramMap.put("mch_pubkey", publicKey);

		paramMap.put("timestamp", String.valueOf(timeStamp));

		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject(true);
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public static BlockChainInfoDto getSummaryInfoResult(String applyResultString) {
		BlockChainInfoDto assetSummaryInfoVo = new BlockChainInfoDto();
		JSONObject resultObject = JSON.parseObject(applyResultString);
		JSONArray assetList = resultObject.getJSONArray("asset_list");
			return assetSummaryInfoVo;
	}

	public static String generateTransTotalNum(String date) throws UnsupportedEncodingException, TrustSDKException, Exception {
		/*
		 * ConfigUtils configUtils = new ConfigUtils(); String nodeId =
		 * configUtils.getNodeId(); String mchId = configUtils.getMchId();
		 * String prvKey = configUtils.getCreateUserPrivateKey(); String
		 * publicKey = configUtils.getCreateUserPublicKey();
		 * 
		 * String chainId = configUtils.getChainId();
		 */
		// ConfigUtils configUtils = new ConfigUtils();
		String nodeId = "ndPWSa7bJf4ASBznSI";
		String mchId = "gbHHj3f4gv36p5IQJw";
		String prvKey = "w1ecBrij8PmWy55CW3F7ksgyYZHXkAC3WCaj269KXjI=";
		String publicKey = "AjSjvrUOvSpMK5ARrebyXqSFsy0/rDHz7IH2xSDlTmgQ";

		String chainId = "chBiMAet6e3sRmeuws";

		long timeStamp = System.currentTimeMillis() / 1000;

		Map<String, Object> paramMap = new TreeMap<String, Object>();
		
		paramMap.put("chain_id", chainId);
		paramMap.put("mch_id", mchId);
		paramMap.put("sign_type", "ECDSA");
		List<Integer> assetTypeList = new ArrayList<Integer>();
		assetTypeList.add(1);
		paramMap.put("asset_type", assetTypeList);
		paramMap.put("version", "2.0");

		List<Integer> state = new ArrayList<Integer>();
		state.add(0);
		paramMap.put("state", state);
		paramMap.put("date", date);

		paramMap.put("mch_pubkey", publicKey);

		paramMap.put("timestamp", String.valueOf(timeStamp) );

		paramMap.put("mch_sign", TrustSDK.signString(prvKey, SignStrUtil.mapToKeyValueStr(paramMap).getBytes("UTF-8"), false));
		// generate post data
		JSONObject postJson = new JSONObject(true);
		for (String key : paramMap.keySet()) {
			postJson.put(key, paramMap.get(key));
		}
		return postJson.toJSONString();
	}

	public static BlockTransChainInfoDto genereateChainInfoResultForTrans(String applyResultString, String transHash,ConfigDto configDto) throws TrustSDKException, Exception {

		JSONObject resultObject = JSON.parseObject(applyResultString);
		BlockTransChainInfoDto blockTransChainInfoDto = new BlockTransChainInfoDto();
		if (resultObject.getJSONObject("data") != null) {
			JSONObject resultDataObject = resultObject.getJSONObject("data");
			JSONArray jsonArray = resultDataObject.getJSONArray("rpt_node");
			ArrayList<NodeTransInfo> nodeInfoArray = new ArrayList<NodeTransInfo>();
			Long firstLatestBlockHeight = null;
			boolean first = true;

			AssetTransQueryFormDTO assetForm = new AssetTransQueryFormDTO();
			assetForm.setTransHash(transHash);
			assetForm.setState(new Integer[]{TransStatus.SUBMIT_SUCCESS});
			assetForm.setConfigDto(configDto);
			List<TransInfoDto> assetList = TencentChainUtils.transQuery(assetForm);
			if (assetList != null && assetList.size() > 0) {
				TransInfoDto transInfoDto = assetList.get(0);
				int status = transInfoDto.getTransState();
				String dateTimeString = transInfoDto.getTransTime();
				String timeString = date2TimeStamp(dateTimeString,"yyyy-MM-dd HH:mm:ss");
				long transTime =  Long.valueOf(timeString);

				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.size(); i++) {

						JSONObject json = jsonArray.getJSONObject(i);
						String nodeName = json.getString("name");

						NodeTransInfo nodeTransInfo = new NodeTransInfo();
						nodeTransInfo.setName(nodeName);
						nodeTransInfo.setStatus(status);
						nodeTransInfo.setCreateTime(transTime);
						nodeInfoArray.add(nodeTransInfo);
					}
				}

			}

			Integer nodeTotalNum = resultDataObject.getInteger("node_count");
			blockTransChainInfoDto.setNodeTotalNum(nodeTotalNum);

			Integer chainState = resultDataObject.getInteger("chain_state");
			blockTransChainInfoDto.setChainState(chainState);

			blockTransChainInfoDto.setBlockHeight(firstLatestBlockHeight);
			blockTransChainInfoDto.setNodeInfo(nodeInfoArray);
		}

		return blockTransChainInfoDto;

	}
	public static String date2TimeStamp(String date_str,String format){  
		 try {  
	            SimpleDateFormat sdf = new SimpleDateFormat(format);  
	            return String.valueOf(sdf.parse(date_str).getTime());  
	        } catch (Exception e) {  
	        	logger.error("转换时间出错{}",e);
	            e.printStackTrace();  
	        }  
	        return "";  
	}

	public static BlockDetailsInfo generateBlockInfoDetails(String applyResultString, Long beginHeight, ConfigDto configDto) throws TrustSDKException, Exception {
		BlockDetailsInfo blockDetailsInfo =null;
		JSONObject resultObject = JSON.parseObject(applyResultString);
		
		
		List<TransInfoDto> transInfoDtoList =null;

		if (resultObject.getJSONArray("data") != null) {
			
			JSONArray resultDataObject = resultObject.getJSONArray("data");
		
			if (resultDataObject != null&&resultDataObject.size()>0) {
				blockDetailsInfo= new BlockDetailsInfo();
				BlockInfoDto blockInfoDto = new BlockInfoDto();
				transInfoDtoList = new ArrayList<TransInfoDto>();
				for (int i = 0; i < resultDataObject.size(); i++) {
					JSONObject txsJson = resultDataObject.getJSONObject(i).getJSONObject("txs");
					JSONArray transArray = txsJson.getJSONArray("rpt_txs");
					JSONObject blockObject = resultDataObject.getJSONObject(i).getJSONObject("block");

					JSONObject blockJson = blockObject;

					JSONObject blockHeader = blockJson.getJSONObject("header");
					Integer blockHeight = blockJson.getInteger("height");
					String blockHash = blockHeader.getString("hash");
					String preHash = blockHeader.getString("pre_hash");
					long timeStamp = blockJson.getLong("time");
					long transTotalNum = transArray.size();

					blockInfoDto.setBlockHeight(blockHeight);
					blockInfoDto.setPreHash(preHash);
					blockInfoDto.setCreateTime(timeStamp);
					blockInfoDto.setTransTotalNum(transTotalNum);
					
					for (int j = 0; j < transArray.size(); j++) {
						// TODO:
						JSONObject transJson = transArray.getJSONObject(j);
						AssetTransQueryFormDTO assetForm = new AssetTransQueryFormDTO();
						String transHash = transJson.getString("hash");
						assetForm.setTransHash(transHash);
						assetForm.setConfigDto(configDto );
						assetForm.setState(new Integer[]{TransStatus.SUBMIT_SUCCESS});
						assetForm.setConfigDto(configDto);
						List<TransInfoDto> assetList = TencentChainUtils.transQuery(assetForm);
						if (assetList != null&&assetList.size()>0) {
							TransInfoDto transInfoDto = assetList.get(0);
							transInfoDto.setBlockHash(blockHash);
							transInfoDtoList.add(transInfoDto);
						}

					}
					blockDetailsInfo.setTransInfoDto(transInfoDtoList);
					blockDetailsInfo.setBlockInfoDto(blockInfoDto);
				}

			}

		}

		return blockDetailsInfo;

	}

}
