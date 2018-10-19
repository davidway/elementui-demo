package com.blockchain.service.tencent;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.blockchain.exception.ServiceException;
import com.blockchain.service.tencent.dto.AccountQueryFormDto;
import com.blockchain.service.tencent.dto.AssetDto;
import com.blockchain.service.tencent.dto.AssetTransQueryFormDto;
import com.blockchain.service.tencent.dto.KeyInfoDto;
import com.blockchain.service.tencent.dto.TransInfoDto;
import com.blockchain.service.tencent.dto.UserFormDto;
import com.blockchain.service.tencent.dto.UserKeyDto;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;
import com.blockchain.service.tencent.vo.UserInfoVo;

/**
 * 操作用户消息的service
 * 
 * @author lupf
 * 
 */
public interface TencentUserService {

	public UserKeyDto generatePairKey(UserKeyDto userKeyModel) throws TrustSDKException, UnsupportedEncodingException;

	public UserInfoVo addUserHasBaseAndHostAccount(UserFormDto userFormDto) throws UnsupportedEncodingException, TrustSDKException, Exception;

	List<AssetDto> accountQuery(AccountQueryFormDto assetForm) throws TrustSDKException, Exception;

	List<TransInfoDto> transQuery(AssetTransQueryFormDto assetForm) throws ServiceException, TrustSDKException, Exception;



	public UserInfoVo addUserHasBaseAccount(UserFormDto userFormDto) throws ServiceException, TrustSDKException, UnsupportedEncodingException, Exception;

	public UserInfoVo addUserHostAccount(UserFormDto userFormDto)  throws ServiceException, TrustSDKException, UnsupportedEncodingException, Exception;

	public boolean checkPairKey(KeyInfoDto keyInfo) throws TrustSDKException, ServiceException;

	public UserInfoVo getUserInfo(String privateKey) throws TrustSDKException;

}