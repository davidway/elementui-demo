package com.blockchain.service.ethereum.util;

import java.io.File;

import org.web3j.crypto.WalletUtils;
import org.web3j.tx.ChainId;

public class MyFileUtil {
	public static String genereateEthereumFilePath(){
		switch (ChainUtil.getChainId()){
		case ChainId.ROPSTEN:
			return new File(WalletUtils.getMainnetKeyDirectory()).getAbsolutePath() + "\\" ;
		case ChainId.MAINNET:
			return new File(WalletUtils.getTestnetKeyDirectory()).getAbsolutePath() + "\\" ;
		default :
			return "";
		}
		
		
	}
}
