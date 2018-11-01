package com.blockchain.validator.factory;

import org.apache.commons.lang3.StringUtils;

import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.tencent.trustsql.sdk.TrustSDK;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;
import com.blockchain.service.tencent.util.ConfigUtils;

class TencentConfigValidator implements BlockChainConfigValidator {

	@Override
	public void check() throws ServiceException {
		ConfigUtils configUtils = new ConfigUtils();
		String chainId = configUtils.getChainId();
		String coin_privateKey = configUtils.getCoin_privateKey();
		String createUserPublicKey = configUtils.getCreateUserPublicKey();
		String createUserPrivateKey = configUtils.getCreateUserPrivateKey();
		String ledgerId = configUtils.getLedgerId();
		String mchId = configUtils.getMchId();
	
		StringBuffer lessName = new StringBuffer();
		
		if (StringUtils.isBlank(chainId)) {
			lessName.append("配置文件中的联盟链id不能为空，");
		}
		if (StringUtils.isBlank(coin_privateKey)) {
			lessName.append("配置文件中的账本id尚未被解析，");
		}
		if (StringUtils.isBlank(createUserPrivateKey)) {
			lessName.append("配置文件中的用户私钥不能为空，");
		}
		if (StringUtils.isBlank(createUserPublicKey)) {
			lessName.append("配置文件中的用户公钥不能为空，");
		}
		if (StringUtils.isBlank(ledgerId)) {
			lessName.append("配置文件中的账本id不能为空，");
		}
		if (StringUtils.isBlank(mchId)) {
			lessName.append("配置文件中的机构id不能为空，");
		}
		
		try {
			TrustSDK.checkPairKey(createUserPrivateKey, createUserPublicKey);
		} catch (TrustSDKException e) {
			throw new ServiceException().errorCode(StatusCode.PAIR_KEY_ERROR).errorMessage(StatusCode.PAIR_KEY_ERROR_MESSAGE);
		}

	}

}
