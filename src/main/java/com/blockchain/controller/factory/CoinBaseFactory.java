package com.blockchain.controller.factory;

import com.blockchain.dto.BlockChainType;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.tencent.dto.AssetIssueFormDto;
import com.blockchain.service.tencent.dto.AssetSettleFormDto;
import com.blockchain.service.tencent.dto.AssetTransferFormDto;
import com.blockchain.service.tencent.util.ConfigUtils;

public class CoinBaseFactory {

	CoinBase coinBase;

	public void setCoinBaseByType(Integer chainType) throws ServiceException {
		ConfigUtils configUtils = new ConfigUtils();
		if (chainType == null) {
			chainType = configUtils.getChainType();
			if ( chainType==null){
				throw new ServiceException().errorCode(StatusCode.PARAM_ERROR).errorMessage(StatusCode.PARAM_ERROR_MESSAGE);
			}
		}
		switch (chainType) {
		case BlockChainType.TENCENT:
			this.coinBase = new TencentCoinBase();
			break;
		case BlockChainType.ETH:
			this.coinBase = new EthCoinBase();
			break;
		default:
			this.coinBase = new UnknownCoinBase();
			break;
		}

	}

	public CoinBase getCoinBase() {
		return this.coinBase;
	}

}
