package com.blockchain.util;

import org.apache.commons.lang3.StringUtils;

import com.blockchain.dto.AssetTransferFormDTO;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;

public class ParamUtils {

	public static void checkAssetNum(String srcAsset) throws ServiceException {
		String[] temp = srcAsset.split(",");
		if (temp.length > 20) {
			throw new ServiceException().pos("检查资产id是否超过20个").errorCode(StatusCode.PARAM_ERROR).errorMessage("资产id不能超过20个");
		}
	}

	public static void checkSumAmount(AssetTransferFormDTO assetTransferFormDTO) throws ServiceException {
		long sum =0;
		if (StringUtils.isNotBlank(assetTransferFormDTO.getFeeAmount() )){
			sum = Long.valueOf(assetTransferFormDTO.getAmount())+Long.valueOf(assetTransferFormDTO.getFeeAmount());
		}else{
			sum =Long.valueOf(assetTransferFormDTO.getAmount());
		}
	
		if (sum>10_000_000_000_000L) {
			throw new ServiceException().pos("转账金额检查").errorCode(StatusCode.PARAM_ERROR).errorMessage("金额不能超过10_000_000_000_000");
		}
		
	}

}
