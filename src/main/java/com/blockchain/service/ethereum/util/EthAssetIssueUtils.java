package com.blockchain.service.ethereum.util;

import org.web3j.crypto.Credentials;

import com.blockchain.service.ethereum.dto.EthereumConfig;
import com.blockchain.service.ethereum.ethjava.TokenERC20;
import com.blockchain.service.ethereum.vo.EthAssetIssueVo;

public class EthAssetIssueUtils {

	public EthAssetIssueVo generateAssetIssueVo(TokenERC20 transactionReceipt, Credentials credentials, EthAssetIssueVo assetIssueVo, EthereumConfig ethereumConfig) {

		String accountAddress = credentials.getAddress();
		assetIssueVo.setAccountAddress(accountAddress);
		assetIssueVo.setSmartContractAddress(transactionReceipt.getContractAddress());
		ethereumConfig.setContractAddress(transactionReceipt.getContractAddress());
		
		return assetIssueVo;
	}

}
