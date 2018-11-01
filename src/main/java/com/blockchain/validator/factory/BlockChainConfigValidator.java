package com.blockchain.validator.factory;

import com.blockchain.exception.ServiceException;

public interface BlockChainConfigValidator {
	public void check() throws ServiceException;
}
