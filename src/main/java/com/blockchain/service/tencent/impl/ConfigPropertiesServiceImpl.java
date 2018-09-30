package com.blockchain.service.tencent.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.blockchain.exception.ServiceException;
import com.blockchain.service.tencent.ConfigPropertiesService;
import com.blockchain.service.tencent.dto.ConfigPropertiesFormDto;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;
import com.blockchain.service.tencent.util.BeanUtils;
import com.blockchain.service.tencent.util.ConfigUtils;
import com.blockchain.util.BlockChainName;

@Service("ConfigPropertiesService")
public class ConfigPropertiesServiceImpl implements ConfigPropertiesService {

	Logger logger = LoggerFactory.getLogger(ConfigPropertiesServiceImpl.class);

	@Override
	public void add(ConfigPropertiesFormDto configPropertiesFormDto) throws TrustSDKException, ServiceException {
		String chainId = configPropertiesFormDto.getChainId();
		String ledgerId = configPropertiesFormDto.getLedgerId();
		String mchId = configPropertiesFormDto.getMchId();
		String nodeId = configPropertiesFormDto.getNodeId();
		String createUserPrivateKey = configPropertiesFormDto.getCreateUserPrivateKey();
		String createUserPublicKey = configPropertiesFormDto.getCreateUserPublicKey();
		Integer chainType = configPropertiesFormDto.getChainType();
		ConfigUtils configUtils = new ConfigUtils();

		if (chainType != null) {
			configUtils.setChainType(chainType);
			switch (chainType) {
			case BlockChainName.TENCENT:
				if (StringUtils.isNotBlank(chainId)) {
					configUtils.setChainId(chainId);
				}
				if (StringUtils.isNotBlank(ledgerId)) {

					configUtils.setLedgerId(ledgerId);
				}
				if (StringUtils.isNotBlank(mchId)) {
					configUtils.setMchId(mchId);
				}
				if (StringUtils.isNotBlank(nodeId)) {
					configUtils.setNodeId(nodeId);
				}
				if (StringUtils.isNotBlank(createUserPrivateKey)) {
					configUtils.setCreateUserPrivateKey(createUserPrivateKey);
				}
				if (StringUtils.isNotBlank(createUserPublicKey)) {
					configUtils.setCreateUserPublicKey(createUserPublicKey);
				}
				if (StringUtils.isNotBlank(createUserPublicKey)) {
					configUtils.setCreateUserPublicKey(createUserPublicKey);
				}
				break;
			case BlockChainName.ETH:
				//以太坊不作其余配置,清空腾讯的配置
				configUtils.setChainId("");
				configUtils.setCoin_privateKey("");
				configUtils.setCreateUserPrivateKey("");
				configUtils.setCreateUserPublicKey("");
				configUtils.setLedgerId("");
				configUtils.setMchId("");
				configUtils.setNodeId("");
				break;
			default:
				break;
			}
		}
		return;
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
