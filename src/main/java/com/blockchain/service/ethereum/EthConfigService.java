package com.blockchain.service.ethereum;

import com.blockchain.service.tencent.dto.ConfigPropertiesFormDto;

public interface EthConfigService {

	void add(ConfigPropertiesFormDto configPropertiesFormDto);

	ConfigPropertiesFormDto get();

	
}
