package com.blockchain.util;

import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;

public class ParamUtils {

	public static void checkAssetNum(String srcAsset) throws ServiceException {
		String[] temp = srcAsset.split(",");
		if (temp.length > 20) {
			throw new ServiceException().pos("检查资产id是否超过20个").errorCode(StatusCode.PARAM_ERROR).errorMessage("资产id不能超过20个");
		}
	}

}
