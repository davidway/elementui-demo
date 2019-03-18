package com.blockchain.util;

import org.apache.commons.lang3.StringUtils;

import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.exception.TrustSDKException;

public class TrustSDKUtil {

	public static void checkPrivateKeyAccountIsMatch(String userPrivateKey, String ownerAccount) throws ServiceException, TrustSDKException {
		userPrivateKey = userPrivateKey.trim();
		ownerAccount = ownerAccount.trim();
		String generateAccountAddress = TrustSDK.generateAddrByPrvkey(userPrivateKey);
		if (ownerAccount.equals(generateAccountAddress) == false) {
			throw new ServiceException().errorCode(StatusCode.PARAM_ERROR).errorMessage("私钥校验地址失败");
		}
	}

	public static void checkPariKeyMatch(String createUserPublicKey, String createUserPrivateKey) throws TrustSDKException, ServiceException {

		if (StringUtils.isNotBlank(createUserPublicKey) && StringUtils.isNotBlank(createUserPrivateKey)) {
			createUserPublicKey = createUserPublicKey.trim();
			createUserPrivateKey = createUserPrivateKey.trim();
			if (TrustSDK.checkPairKey(createUserPrivateKey, createUserPublicKey) == false) {

				throw new ServiceException().errorCode(StatusCode.PARAM_ERROR).errorMessage("公私钥配对失败");
			}
		}

	}

}
