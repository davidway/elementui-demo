package com.blockchain.validator.factory;

public class ValidatorFactory {
	BlockChainConfigValidator blockChainConfigValidator;

	public BlockChainConfigValidator getBlockChainValidator() {
		return blockChainConfigValidator;
	}

	public void setBlockChainValidator(BlockChainConfigValidator blockChainConfigValidator) {
		this.blockChainConfigValidator = blockChainConfigValidator;
	}

	public void setProductByType(Integer chainType) {
		switch (chainType) {
		case 0:
			this.blockChainConfigValidator = new TencentConfigValidator();
			break;
		case 1:
			this.blockChainConfigValidator = new EthConfigValidator();
			break;

		default:
			break;
		}

	}

}
