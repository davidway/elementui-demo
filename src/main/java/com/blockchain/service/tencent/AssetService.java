package com.blockchain.service.tencent;

import java.io.UnsupportedEncodingException;

import com.blockchain.exception.ServiceException;
import com.blockchain.service.ethereum.dto.GasInfo;
import com.blockchain.service.tencent.dto.AssetIssueDto;
import com.blockchain.service.tencent.dto.AssetIssueFormDto;
import com.blockchain.service.tencent.dto.AssetIssueSubmitFormDto;
import com.blockchain.service.tencent.dto.AssetSettleDto;
import com.blockchain.service.tencent.dto.AssetSettleFormDto;
import com.blockchain.service.tencent.dto.AssetSettleSubmitFormDto;
import com.blockchain.service.tencent.dto.AssetTransferDto;
import com.blockchain.service.tencent.dto.AssetTransferFormDto;
import com.blockchain.service.tencent.dto.AssetTransferSubmitFormDto;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;

public interface AssetService {

	AssetIssueDto issue(AssetIssueFormDto assetIssueFormDto) throws Exception;

	AssetTransferDto transfer(AssetTransferFormDto assetTransferFormDto) throws TrustSDKException, Exception;

	AssetSettleDto settle(AssetSettleFormDto assetSettleFormDto) throws UnsupportedEncodingException, TrustSDKException, Exception;

	
	AssetIssueDto issueSubmit(AssetIssueSubmitFormDto assetForm) throws UnsupportedEncodingException, TrustSDKException, Exception;

	AssetTransferDto transSubmit(AssetTransferSubmitFormDto assetForm) throws ServiceException, TrustSDKException, Exception;

	AssetSettleDto settleSubmit(AssetSettleSubmitFormDto assetForm) throws Exception;


}
