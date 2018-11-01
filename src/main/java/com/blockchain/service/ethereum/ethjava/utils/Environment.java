package com.blockchain.service.ethereum.ethjava.utils;

import com.blockchain.service.ethereum.dto.EthereumConfig;

/**
 * 运行配置项
 */
public class Environment {

	public static String getRpcUrl() {
		EthereumConfig ethereumConfig = new EthereumConfig();
		String url =  ethereumConfig.getRpcUrl();
		return url;
	}

}
