package com.blockchain.service.ethereum;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.web3j.crypto.CipherException;

import com.blockchain.exception.ServiceException;
import com.blockchain.service.ethereum.dto.EthAccountQueryFormDto;
import com.blockchain.service.ethereum.dto.EthUserFormDto;
import com.blockchain.service.ethereum.vo.EthAndTokenAssetVo;
import com.blockchain.service.ethereum.vo.EthereumWalletInfo;
import com.blockchain.service.tencent.dto.KeyInfoDto;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;
import com.blockchain.service.tencent.vo.UserInfoVo;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 操作用户消息的service
 * 
 * @author lupf
 * 
 */
public interface EthUserService {

	BigDecimal accountEthQuery(EthAccountQueryFormDto assetForm) throws TrustSDKException, Exception;

	
	
	public EthereumWalletInfo addUserHasBaseAccount(EthUserFormDto userFormDto) throws ServiceException, TrustSDKException, UnsupportedEncodingException, Exception;

	
	
	BigInteger accountTokenQuery(EthAccountQueryFormDto assetFormVO) throws TrustSDKException, Exception;

	
	EthAndTokenAssetVo ethereumAccountQuery(EthAccountQueryFormDto accountQueryFormDto) throws TrustSDKException, Exception;



	void checkPairKey(KeyInfoDto keyInfo) throws ServiceException;








	EthereumWalletInfo getUserInfo(String password, String privateKey) throws CipherException, JsonProcessingException;

}