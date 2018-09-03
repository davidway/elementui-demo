package com.blockchain.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.blockchain.DTO.ConfigPropertiesFormDTO;
import com.blockchain.exception.ServiceException;
import com.blockchain.service.ConfigPropertiesService;
import com.blockchain.util.ConfigUtils;
import com.tencent.trustsql.sdk.exception.TrustSDKException;

@Service("ConfigPropertiesService")
public class ConfigPropertiesServiceImpl implements ConfigPropertiesService {

	Logger logger = LoggerFactory.getLogger(ConfigPropertiesServiceImpl.class);
	@Override
	public void add(ConfigPropertiesFormDTO configPropertiesFormDTO) throws TrustSDKException, ServiceException {
		String chainId = configPropertiesFormDTO.getChainId();
		String ledgerId = configPropertiesFormDTO.getLedgerId();
		String mchId = configPropertiesFormDTO.getMchId();
		String nodeId = configPropertiesFormDTO.getNodeId();
		String createUserPrivateKey = configPropertiesFormDTO.getCreateUserPrivateKey();
		String createUserPublicKey = configPropertiesFormDTO.getCreateUserPublicKey();
		ConfigUtils configUtils  = ConfigUtils.getSingleton();
	
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

		return;
	}

	@Override
	public ConfigPropertiesFormDTO get() {
		ConfigPropertiesFormDTO configPropertiesFormDTO = new ConfigPropertiesFormDTO();
		ConfigUtils configUtils  = ConfigUtils.getSingleton();
		String chainId = configUtils.getChainId();
		String ledgerId = configUtils.getLedgerId();
		String mchId = configUtils.getMchId();
		String nodeId = configUtils.getNodeId();
		String createUserPrivateKey = configUtils.getCreateUserPrivateKey();
		String createUserPublicKey = configUtils.getCreateUserPublicKey();
		
		configPropertiesFormDTO.setChainId(chainId);

		configPropertiesFormDTO.setLedgerId(ledgerId);

		configPropertiesFormDTO.setMchId(mchId);

		configPropertiesFormDTO.setNodeId(nodeId);

		configPropertiesFormDTO.setCreateUserPrivateKey(createUserPrivateKey);

		configPropertiesFormDTO.setCreateUserPublicKey(createUserPublicKey);
		return configPropertiesFormDTO;
		
	}

}
