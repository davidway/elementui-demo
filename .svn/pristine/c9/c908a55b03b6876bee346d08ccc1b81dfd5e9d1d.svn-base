package com.blockchain.util;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.DTO.AssetIssueDTO;
import com.blockchain.DTO.AssetSettleDTO;
import com.blockchain.DTO.AssetSettleFormDTO;
import com.blockchain.DTO.AssetSettleSubmitFormDTO;
import com.blockchain.DTO.AssetSubmitFormDTO;
import com.blockchain.DTO.AssetTransferDTO;
import com.blockchain.DTO.AssetTransferFormDTO;
import com.blockchain.DTO.AssetTransferSubmitFormDTO;

@Component
public class AssetPrepareUtil {

	public AssetSubmitFormDTO prepareAssetSubmitForm(String applyResultString) {
		JSONObject o = JSON.parseObject(applyResultString);
		AssetSubmitFormDTO assetSubmitFormVO = new AssetSubmitFormDTO();
		String signStrList = o.getString("sign_str_list");
		assetSubmitFormVO.setSignStr(signStrList);
		String transactionId = o.getString("transaction_id");
		assetSubmitFormVO.setTransactionId(transactionId);
		String assetId = o.getString("asset_id");
		assetSubmitFormVO.setAssetId(assetId);
		return assetSubmitFormVO;
		
	}

	public AssetTransferSubmitFormDTO perpareTransferSubmitForm(AssetTransferFormDTO assetTransferFormVO, String applyResultString) {
		AssetTransferSubmitFormDTO s = new AssetTransferSubmitFormDTO();
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

	public AssetSettleSubmitFormDTO perpareTransferSubmitForm(AssetSettleFormDTO assetSettleFormVO, String applyResultString) {
		AssetSettleSubmitFormDTO s = new AssetSettleSubmitFormDTO();
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

	public AssetTransferDTO generateAssetTransferDTO(AssetTransferSubmitFormDTO asseTransfertSubmitForm, String submitResultString) {
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

	public AssetSettleDTO generateAssetSettleDTO(AssetSettleSubmitFormDTO assetSettleSubmitFormVO, String submitResultString) {
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

	public AssetIssueDTO generateAssetIssueDto(AssetSubmitFormDTO assetSubmitFormVO, String submitResultString) {
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
