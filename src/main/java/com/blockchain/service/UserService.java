package com.blockchain.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.blockchain.dto.AccountQueryFormDTO;
import com.blockchain.dto.AssetDTO;
import com.blockchain.dto.AssetTransQueryFormDTO;
import com.blockchain.dto.KeyInfoDTO;
import com.blockchain.dto.TransInfoDTO;
import com.blockchain.dto.UserFormDTO;
import com.blockchain.dto.UserKeyDTO;
import com.blockchain.exception.ServiceException;
import com.blockchain.vo.UserInfoVO;
import com.tencent.trustsql.sdk.exception.TrustSDKException;

/**
 * 操作用户消息的service
 * 
 * @author lupf
 * 
 */
public interface UserService {

	public UserKeyDTO generatePairKey(UserKeyDTO userKeyModel) throws TrustSDKException, UnsupportedEncodingException;

	public UserInfoVO addUserHasBaseAndHostAccount(UserFormDTO userFormDTO) throws UnsupportedEncodingException, TrustSDKException, Exception;

	List<AssetDTO> accountQuery(AccountQueryFormDTO assetForm) throws TrustSDKException, Exception;

	List<TransInfoDTO> transQuery(AssetTransQueryFormDTO assetForm) throws ServiceException, TrustSDKException, Exception;



	public UserInfoVO addUserHasBaseAccount(UserFormDTO userFormDTO) throws ServiceException, TrustSDKException, UnsupportedEncodingException, Exception;

	public UserInfoVO addUserHostAccount(UserFormDTO userFormDTO)  throws ServiceException, TrustSDKException, UnsupportedEncodingException, Exception;

	public void checkPairKey(KeyInfoDTO keyInfo) throws TrustSDKException, ServiceException;

}