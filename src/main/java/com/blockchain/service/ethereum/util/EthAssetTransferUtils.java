package com.blockchain.service.ethereum.util;

import java.math.BigInteger;

import org.web3j.protocol.core.methods.response.EthSendTransaction;

import com.blockchain.service.ethereum.dto.EthAssetTransferFormDto;
import com.blockchain.service.ethereum.vo.EthAssetTransferVo;

public class EthAssetTransferUtils {

	public EthAssetTransferVo genereateTranferParam(BigInteger nonce, EthSendTransaction transactionReceipt) {

		EthAssetTransferVo assetIssueDTO = new EthAssetTransferVo();
		assetIssueDTO.setTransHash(transactionReceipt.getTransactionHash());
		assetIssueDTO.setNonce(nonce.toString());
		return assetIssueDTO;

	}

}
