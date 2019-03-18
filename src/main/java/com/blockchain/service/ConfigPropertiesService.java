package com.blockchain.service;

import com.blockchain.dto.ConfigPropertiesFormDTO;
import com.blockchain.dto.CrmConfigDto;
import com.blockchain.exception.ServiceException;
import com.tencent.trustsql.sdk.exception.TrustSDKException;

public interface ConfigPropertiesService {

	void add(ConfigPropertiesFormDTO configPropertiesFormDTO) throws TrustSDKException, ServiceException;

	ConfigPropertiesFormDTO get();

	CrmConfigDto getCrmConfig();

}
