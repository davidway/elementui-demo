package com.blockchain.service.ethereum.util;

import java.math.BigInteger;

import org.web3j.protocol.core.methods.response.EthSendTransaction;

import com.blockchain.service.ethereum.vo.EthAssetSettleVo;

public class EthAssetSettleUtils {
	


	public  EthAssetSettleVo genereateTranferParam(BigInteger nonce, EthSendTransaction transactionReceipt) {
		EthAssetSettleVo assetSettleDto = new EthAssetSettleVo();
		
		assetSettleDto.setTransHash(transactionReceipt.getTransactionHash());
		assetSettleDto.setNonce(nonce.toString());
		return assetSettleDto;
	}

}
