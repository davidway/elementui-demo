package com.blockchain.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.blockchain.DTO.AccountQueryFormDTO;
import com.blockchain.DTO.AssetDTO;
import com.blockchain.DTO.AssetTransQueryFormDTO;
import com.blockchain.DTO.KeyInfoDTO;
import com.blockchain.DTO.TransInfoDTO;
import com.blockchain.DTO.UserFormDTO;
import com.blockchain.DTO.UserInfoDTO;
import com.blockchain.DTO.UserKeyDTO;
import com.blockchain.exception.ServiceException;
import com.tencent.trustsql.sdk.exception.TrustSDKException;

/**
 * 操作用户消息的service
 * 
 * @author lupf
 * 
 */
public interface UserService {

	public UserKeyDTO generatePairKey(UserKeyDTO userKeyModel) throws TrustSDKException, UnsupportedEncodingException;

	public UserInfoDTO addUserHasBaseAndHostAccount(UserFormDTO userFormVO) throws UnsupportedEncodingException, TrustSDKException, Exception;

	List<AssetDTO> accountQuery(AccountQueryFormDTO assetForm) throws TrustSDKException, Exception;

	List<TransInfoDTO> transQuery(AssetTransQueryFormDTO assetForm) throws ServiceException, TrustSDKException, Exception;

	String getSrcAssetListBySrcAccount(String srcAccount, String content) throws TrustSDKException, Exception;

	public UserInfoDTO addUserHasBaseAccount(UserFormDTO userFormVO) throws ServiceException, TrustSDKException, UnsupportedEncodingException, Exception;

	public UserInfoDTO addUserHostAccount(UserFormDTO userFormVO)  throws ServiceException, TrustSDKException, UnsupportedEncodingException, Exception;

	public void checkPairKey(KeyInfoDTO keyInfo) throws TrustSDKException, ServiceException;

}