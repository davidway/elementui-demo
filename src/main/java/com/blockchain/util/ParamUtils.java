package com.blockchain.util;

import com.blockchain.exception.ParameterErrorException;

public class ParamUtils {

	public static void checkAssetNum(String srcAsset) throws ParameterErrorException {
		String[] temp = srcAsset.split(",");
		if (temp.length > 10) {
			throw new ParameterErrorException("资产id不能超过10个");
		}
	}

}
