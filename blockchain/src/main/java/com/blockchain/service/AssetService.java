package com.blockchain.service;

import java.io.UnsupportedEncodingException;

import com.blockchain.DTO.AssetIssueDTO;
import com.blockchain.DTO.AssetSettleDTO;
import com.blockchain.DTO.AssetTransferDTO;
import com.blockchain.VO.AssetFormVO;
import com.blockchain.VO.AssetSettleFormVO;
import com.blockchain.VO.AssetSettleSubmitFormVO;
import com.blockchain.VO.AssetSubmitFormVO;
import com.blockchain.VO.AssetTransferFormVO;
import com.blockchain.VO.AssetTransferSubmitFormVO;
import com.blockchain.exception.ServiceException;
import com.tencent.trustsql.sdk.exception.TrustSDKException;

public interface AssetService {

	AssetIssueDTO issue(AssetFormVO assetFormVO) throws Exception;

	AssetTransferDTO transfer(AssetTransferFormVO assetTransferFormVO) throws TrustSDKException, Exception;

	AssetSettleDTO settle(AssetSettleFormVO assetSettleFormVO) throws UnsupportedEncodingException, TrustSDKException, Exception;

	
	AssetIssueDTO issueSubmit(AssetSubmitFormVO assetForm) throws UnsupportedEncodingException, TrustSDKException, Exception;

	AssetTransferDTO transSubmit(AssetTransferSubmitFormVO assetForm) throws ServiceException, TrustSDKException, Exception;

	AssetSettleDTO settleSubmit(AssetSettleSubmitFormVO assetForm) throws Exception;

}
