package com.blockchain.controller.factory;

import com.blockchain.controller.CoinBaseContext;
import com.blockchain.exception.ServiceException;

public class CoinContextUtil {

	public static CoinBaseContext getCoinContext(Integer chainType) throws ServiceException {
		CoinBaseFactory coinBaseFactory = new CoinBaseFactory();
		CoinBaseContext coinBaseContext = new CoinBaseContext();
		coinBaseFactory.setCoinBaseByType(chainType);
		coinBaseContext.setFacotry(coinBaseFactory);
		return coinBaseContext;
	}
}
