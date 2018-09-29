package com.blockchain.service.tencent;

import com.blockchain.exception.ServiceException;
import com.blockchain.service.tencent.dto.ConfigPropertiesFormDto;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;

public interface ConfigPropertiesService {

	void add(ConfigPropertiesFormDto configPropertiesFormDto) throws TrustSDKException, ServiceException;

	ConfigPropertiesFormDto get();

}
