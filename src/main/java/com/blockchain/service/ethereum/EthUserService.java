package com.blockchain.service.ethereum;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.blockchain.exception.ServiceException;
import com.blockchain.service.ethereum.dto.EthAccountQueryFormDto;
import com.blockchain.service.ethereum.dto.EthAndTokenAssetDto;
import com.blockchain.service.ethereum.dto.EthTransInfoDto;
import com.blockchain.service.ethereum.dto.EthUserFormDto;
import com.blockchain.service.ethereum.vo.EthereumWalletInfo;
import com.blockchain.service.tencent.dto.AssetTransQueryFormDto;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;

/**
 * 操作用户消息的service
 * 
 * @author lupf
 * 
 */
public interface EthUserService {

	BigDecimal accountEthQuery(EthAccountQueryFormDto assetForm) throws TrustSDKException, Exception;

	List<EthTransInfoDto> transQuery(AssetTransQueryFormDto assetForm) throws ServiceException, TrustSDKException, Exception;

	
	public EthereumWalletInfo addUserHasBaseAccount(EthUserFormDto userFormDto) throws ServiceException, TrustSDKException, UnsupportedEncodingException, Exception;

	
	
	BigInteger accountTokenQuery(EthAccountQueryFormDto assetFormVO) throws TrustSDKException, Exception;

	
	EthAndTokenAssetDto ethereumAccountQuery(EthAccountQueryFormDto accountQueryFormDto) throws TrustSDKException, Exception;

}