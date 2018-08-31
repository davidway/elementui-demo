package com.blockchain.service;

import java.io.UnsupportedEncodingException;

import com.blockchain.DTO.AssetFormDTO;
import com.blockchain.DTO.AssetIssueDTO;
import com.blockchain.DTO.AssetSettleDTO;
import com.blockchain.DTO.AssetSettleFormDTO;
import com.blockchain.DTO.AssetSettleSubmitFormDTO;
import com.blockchain.DTO.AssetSubmitFormDTO;
import com.blockchain.DTO.AssetTransferDTO;
import com.blockchain.DTO.AssetTransferFormDTO;
import com.blockchain.DTO.AssetTransferSubmitFormDTO;
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
