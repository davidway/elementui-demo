package com.blockchain.service.ethereum.impl;

import org.apache.commons.lang3.StringUtils;

import com.blockchain.service.ethereum.EthConfigService;
import com.blockchain.service.tencent.dto.ConfigPropertiesFormDto;
import com.blockchain.service.tencent.util.ConfigUtils;
import com.blockchain.util.BlockChainName;

public class EthConfigServiceImpl implements EthConfigService {

	@Override
	public void add(ConfigPropertiesFormDto configPropertiesFormDto) {
		Integer chainType = configPropertiesFormDto.getChainType();
		ConfigUtils configUtils = new ConfigUtils();

		configUtils.setChainType(chainType);
		if (chainType != null) {
			// 以太坊不作其余配置,清空腾讯的配置
			configUtils.setChainId("");
			configUtils.setCoin_privateKey("");
			configUtils.setCreateUserPrivateKey("");
			configUtils.setCreateUserPublicKey("");
			configUtils.setLedgerId("");
			configUtils.setMchId("");
			configUtils.setNodeId("");
		}

	}

	@Override
	public ConfigPropertiesFormDto get() {
		ConfigPropertiesFormDto configPropertiesFormDto = new ConfigPropertiesFormDto();
		ConfigUtils configUtils = new ConfigUtils();
		String chainId = configUtils.getChainId();
		String ledgerId = configUtils.getLedgerId();
		String mchId = configUtils.getMchId();
		String nodeId = configUtils.getNodeId();
		String createUserPrivateKey = configUtils.getCreateUserPrivateKey();
		String createUserPublicKey = configUtils.getCreateUserPublicKey();
		Integer chainType = configUtils.getChainType();

		configPropertiesFormDto.setChainId(chainId);

		configPropertiesFormDto.setLedgerId(ledgerId);

		configPropertiesFormDto.setMchId(mchId);

		configPropertiesFormDto.setNodeId(nodeId);

		configPropertiesFormDto.setCreateUserPrivateKey(createUserPrivateKey);

		configPropertiesFormDto.setCreateUserPublicKey(createUserPublicKey);

		configPropertiesFormDto.setCreateUserPublicKey(createUserPublicKey);

		configPropertiesFormDto.setChainType(chainType);
		return configPropertiesFormDto;

	}

}
