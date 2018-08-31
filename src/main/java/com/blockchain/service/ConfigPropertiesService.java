package com.blockchain.service;

import com.blockchain.VO.ConfigPropertiesFormVO;
import com.blockchain.exception.ServiceException;
import com.tencent.trustsql.sdk.exception.TrustSDKException;

public interface ConfigPropertiesService {

	void add(ConfigPropertiesFormVO configPropertiesFormVO) throws TrustSDKException, ServiceException;

	ConfigPropertiesFormVO get();

}
