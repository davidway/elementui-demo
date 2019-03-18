package com.blockchain.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.blockchain.dto.AssetTransQueryFormDTO;
import com.blockchain.dto.BlockChainInfoDto;
import com.blockchain.dto.BlockDetailsInfo;
import com.blockchain.dto.BlockTransDto;
import com.blockchain.dto.TransDetailsVo;
import com.blockchain.dto.TransHeightDto;
import com.blockchain.dto.TransInfoDto;
import com.blockchain.exception.ServiceException;
import com.tencent.trustsql.sdk.exception.TrustSDKException;

public interface BlockChainBrowserService {


	
	TransDetailsVo getTransInfoDetailsByHash(AssetTransQueryFormDTO transInfoDto) throws ServiceException, TrustSDKException, Exception;

	
	

	List<TransInfoDto> getTransInfoList(AssetTransQueryFormDTO  assetTransQueryFormDTO) throws TrustSDKException, Exception;

	List<BlockTransDto> getBlockInfoList(TransHeightDto transHeightDto) throws UnsupportedEncodingException, TrustSDKException, Exception;


	BlockDetailsInfo getBlockInfoDetails(TransHeightDto transHeightDto) throws UnsupportedEncodingException, TrustSDKException, Exception;

	BlockChainInfoDto getChainInfo() throws UnsupportedEncodingException, TrustSDKException, Exception;


}
