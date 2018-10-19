package com.blockchain.service.ethereum.util;

import java.math.BigInteger;

import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.blockchain.service.ethereum.vo.EthAssetBurnVo;

public class EthAssetBurnUtils {
	


	public  EthAssetBurnVo genereateBurnParam(BigInteger nonce, EthSendTransaction transactionReceipt) {
		EthAssetBurnVo assetSettleDto = new EthAssetBurnVo();
		
		assetSettleDto.setTransHash(transactionReceipt.getTransactionHash());
		assetSettleDto.setNonce(nonce.toString());
		return assetSettleDto;
	}

}
