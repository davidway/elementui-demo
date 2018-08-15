package com.blockchain.util;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.DTO.AssetIssueDTO;
import com.blockchain.DTO.AssetSettleDTO;
import com.blockchain.DTO.AssetTransferDTO;
import com.blockchain.VO.AssetSettleFormVO;
import com.blockchain.VO.AssetSettleSubmitFormVO;
import com.blockchain.VO.AssetSubmitFormVO;
import com.blockchain.VO.AssetTransferFormVO;
import com.blockchain.VO.AssetTransferSubmitFormVO;

@Component
public class AssetPrepareUtil {

	public AssetSubmitFormVO prepareAssetSubmitForm(String applyResultString) {
		JSONObject o = JSON.parseObject(applyResultString);
		AssetSubmitFormVO assetSubmitFormVO = new AssetSubmitFormVO();
		String signStrList = o.getString("sign_str_list");
		assetSubmitFormVO.setSignStr(signStrList);
		String transactionId = o.getString("transaction_id");
		assetSubmitFormVO.setTransactionId(transactionId);
		String assetId = o.getString("asset_id");
		assetSubmitFormVO.setAssetId(assetId);
		return assetSubmitFormVO;
		
	}

	public AssetTransferSubmitFormVO perpareTransferSubmitForm(AssetTransferFormVO assetTransferFormVO, String applyResultString) {
		AssetTransferSubmitFormVO s = new AssetTransferSubmitFormVO();
		s.setUserPrivateKey(assetTransferFormVO.getUserPrivateKey());
		
		JSONObject o = JSON.parseObject(applyResultString);
		String transtionId = o.getString("transaction_id");
		String signList = o.getString("sign_str_list");
		String leftAssetId = o.getString("left_asset_id");
		String dstAsssetId = o.getString("dst_asset_id");
		
		s.setTransactionId(transtionId);
		s.setSrcAssetId(leftAssetId);
		s.setUserPrivateKey(assetTransferFormVO.getUserPrivateKey());
		s.setDstAssetId(dstAsssetId);
		s.setSignList(signList);
		return s;
	}

	public AssetSettleSubmitFormVO perpareTransferSubmitForm(AssetSettleFormVO assetSettleFormVO, String applyResultString) {
		AssetSettleSubmitFormVO s = new AssetSettleSubmitFormVO();
		s.setUserPrivateKey(assetSettleFormVO.getUserPrivateKey());
		
		JSONObject o = JSON.parseObject(applyResultString);
		String transtionId = o.getString("transaction_id");
		String signList = o.getString("sign_str_list");
		
		String userPrivateKey = assetSettleFormVO.getUserPrivateKey();
		s.setUserPrivateKey(userPrivateKey);
		s.setTransactionId(transtionId);
		s.setSignList(signList);
		return s;
	}

	public AssetTransferDTO generateAssetTransferDTO(AssetTransferSubmitFormVO asseTransfertSubmitForm, String submitResultString) {
		AssetTransferDTO assetTransferDTO = new AssetTransferDTO();
		
		JSONObject json = JSONObject.parseObject(submitResultString);
		String dstAssetId = json.getString("dst_asset_id");
		String dstAssetAmount = json.getString("dst_asset_amount	");
		String srcAssetId = json.getString("left_asset_id");
		String feeAssetId = json.getString("fee_asset_id");
		String leftAssetAmount = json.getString("left_asset_amount");
		String transHash = json.getString("trans_hash");
		String feeAssetAmount = json.getString("fee_asset_amount");

		String transactionId = asseTransfertSubmitForm.getTransactionId();
		assetTransferDTO.setTransHash(transHash);
		assetTransferDTO.setFeeAssetAmount(feeAssetAmount);
		assetTransferDTO.setSrcAmount(leftAssetAmount);
		assetTransferDTO.setDstAssetAmount(dstAssetAmount);
		assetTransferDTO.setDstAssetId(dstAssetId);
		assetTransferDTO.setFeeAssetId(feeAssetId);
		assetTransferDTO.setSrcAssetId(srcAssetId);
		assetTransferDTO.setTransactionId(transactionId);
		return assetTransferDTO;
	}

	public AssetSettleDTO generateAssetSettleDTO(AssetSettleSubmitFormVO assetSettleSubmitFormVO, String submitResultString) {
		AssetSettleDTO assetSettleDTO = new AssetSettleDTO();
		
		String transactionId = assetSettleSubmitFormVO.getTransactionId();
		JSONObject json = JSONObject.parseObject(submitResultString);
		String leftAssetId = json.getString("left_asset_id");
		String transHash = json.getString("trans_hash");
		assetSettleDTO.setTransHash(transHash);
		assetSettleDTO.setSrcAssetId(leftAssetId);
		assetSettleDTO.setTransactionId(transactionId);
		 return assetSettleDTO;
	}

	public AssetIssueDTO generateAssetIssueDto(AssetSubmitFormVO assetSubmitFormVO, String submitResultString) {
		AssetIssueDTO assetIssueDTO = new AssetIssueDTO();
		
		String transactionId = assetSubmitFormVO.getTransactionId();
		String assetId = JSON.parseObject(submitResultString).getString("asset_id");
		String transHash = JSON.parseObject(submitResultString).getString("trans_hash");
		
		
		assetIssueDTO.setTransactionId(transactionId);
		assetIssueDTO.setAssetId(assetId);
		assetIssueDTO.setTransHash(transHash);
		
		return assetIssueDTO;
	}

}
