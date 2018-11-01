package com.blockchain.service.ethereum;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.blockchain.service.dto.EthTransInfoDto;
import com.blockchain.service.ethereum.dto.EthAssetIssueFormDto;
import com.blockchain.service.ethereum.dto.EthAssetSettleDto;
import com.blockchain.service.ethereum.dto.EthAssetTransferFormDto;
import com.blockchain.service.ethereum.dto.GasInfoDto;
import com.blockchain.service.ethereum.vo.EthAssetIssueVo;
import com.blockchain.service.ethereum.vo.EthAssetBurnVo;
import com.blockchain.service.ethereum.vo.EthAssetTransferVo;
import com.blockchain.service.ethereum.vo.EthTransInfoVo;
import com.blockchain.service.ethereum.vo.GasInfoVo;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;

public interface EthAssetService {

	EthAssetIssueVo issueToken(EthAssetIssueFormDto assetFormDTO) throws Exception;

	EthAssetTransferVo transferToken(EthAssetTransferFormDto assetTransferFormDto) throws TrustSDKException, Exception;

	EthAssetTransferVo transferEth(EthAssetTransferFormDto assetTransferFormDto) throws TrustSDKException, Exception;

	
	EthAssetBurnVo settleToken(EthAssetSettleDto assetSettleFormDto) throws UnsupportedEncodingException, TrustSDKException, Exception;
	

	GasInfoVo getGasInfo(GasInfoDto gasInfoDto) throws IOException;

	EthTransInfoVo getTransInfo(EthTransInfoDto ethTransInfo);

}
