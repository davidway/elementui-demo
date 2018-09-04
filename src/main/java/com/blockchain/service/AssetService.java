package com.blockchain.service;

import java.io.UnsupportedEncodingException;

import com.blockchain.dto.AssetFormDTO;
import com.blockchain.dto.AssetIssueDTO;
import com.blockchain.dto.AssetSettleDTO;
import com.blockchain.dto.AssetSettleFormDTO;
import com.blockchain.dto.AssetSettleSubmitFormDTO;
import com.blockchain.dto.AssetSubmitFormDTO;
import com.blockchain.dto.AssetTransferDTO;
import com.blockchain.dto.AssetTransferFormDTO;
import com.blockchain.dto.AssetTransferSubmitFormDTO;
import com.blockchain.exception.ServiceException;
import com.tencent.trustsql.sdk.exception.TrustSDKException;

public interface AssetService {

	AssetIssueDTO issue(AssetFormDTO assetFormDTO) throws Exception;

	AssetTransferDTO transfer(AssetTransferFormDTO assetTransferFormDTO) throws TrustSDKException, Exception;

	AssetSettleDTO settle(AssetSettleFormDTO assetSettleFormDTO) throws UnsupportedEncodingException, TrustSDKException, Exception;

	
	AssetIssueDTO issueSubmit(AssetSubmitFormDTO assetForm) throws UnsupportedEncodingException, TrustSDKException, Exception;

	AssetTransferDTO transSubmit(AssetTransferSubmitFormDTO assetForm) throws ServiceException, TrustSDKException, Exception;

	AssetSettleDTO settleSubmit(AssetSettleSubmitFormDTO assetForm) throws Exception;

}
