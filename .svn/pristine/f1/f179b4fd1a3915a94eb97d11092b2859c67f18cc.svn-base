package com.blockchain.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.blockchain.VO.ConfigPropertiesFormVO;
import com.blockchain.exception.ServiceException;
import com.blockchain.service.ConfigPropertiesService;
import com.blockchain.util.ConfigUtils;
import com.tencent.trustsql.sdk.exception.TrustSDKException;

@Service
public class ConfigPropertiesServiceImpl implements ConfigPropertiesService {

	Logger logger = LoggerFactory.getLogger(ConfigPropertiesServiceImpl.class);

	@Override
	public void add(ConfigPropertiesFormVO configPropertiesFormVO) throws TrustSDKException, ServiceException {
		String chainId = configPropertiesFormVO.getChainId();
		String ledgerId = configPropertiesFormVO.getLedgerId();
		String mchId = configPropertiesFormVO.getMchId();
		String nodeId = configPropertiesFormVO.getNodeId();
		String createUserPrivateKey = configPropertiesFormVO.getCreateUserPrivateKey();
		String createUserPublicKey = configPropertiesFormVO.getCreateUserPublicKey();
		ConfigUtils configUtils = ConfigUtils.getSingleton();

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
	public ConfigPropertiesFormVO get() {
		ConfigPropertiesFormVO configPropertiesFormVO = new ConfigPropertiesFormVO();
		ConfigUtils configUtils = ConfigUtils.getSingleton();
		String chainId = configUtils.getChainId();
		String ledgerId = configUtils.getLedgerId();
		String mchId = configUtils.getMchId();
		String nodeId = configUtils.getNodeId();
		String createUserPrivateKey = configUtils.getCreateUserPrivateKey();
		String createUserPublicKey = configUtils.getCreateUserPublicKey();

		configPropertiesFormVO.setChainId(chainId);

		configPropertiesFormVO.setLedgerId(ledgerId);

		configPropertiesFormVO.setMchId(mchId);

		configPropertiesFormVO.setNodeId(nodeId);

		configPropertiesFormVO.setCreateUserPrivateKey(createUserPrivateKey);

		configPropertiesFormVO.setCreateUserPublicKey(createUserPublicKey);
		return configPropertiesFormVO;

	}

}
