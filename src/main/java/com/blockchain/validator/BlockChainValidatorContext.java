package com.blockchain.validator;

import com.blockchain.exception.ServiceException;
import com.blockchain.validator.factory.ValidatorFactory;

public class BlockChainValidatorContext {
	ValidatorFactory validatorFactory;

	public void check() throws ServiceException {
		validatorFactory.getBlockChainValidator().check();

	}

	public void setFactory(ValidatorFactory validatorFactory) {

		this.validatorFactory = validatorFactory;
	}

}
