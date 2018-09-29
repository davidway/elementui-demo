package com.blockchain.service.ethereum;

import java.io.UnsupportedEncodingException;















import com.blockchain.service.ethereum.dto.EthAssetSettleDto;
import com.blockchain.service.ethereum.dto.EthAssetTransferFormDto;
import com.blockchain.service.ethereum.dto.EthereumAssetIssueFormDto;
import com.blockchain.service.ethereum.vo.EthAssetIssueVo;
import com.blockchain.service.ethereum.vo.EthAssetSettleVo;
import com.blockchain.service.ethereum.vo.EthAssetTransferVo;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;

public interface EthAssetService {

	EthAssetIssueVo issueToken(EthereumAssetIssueFormDto assetFormDTO) throws Exception;

	EthAssetTransferVo transferToken(EthAssetTransferFormDto assetTransferFormDto) throws TrustSDKException, Exception;

	EthAssetTransferVo transferEth(EthAssetTransferFormDto assetTransferFormDto) throws TrustSDKException, Exception;

	
	EthAssetSettleVo settleToken(EthAssetSettleDto assetSettleFormDto) throws UnsupportedEncodingException, TrustSDKException, Exception;

}
