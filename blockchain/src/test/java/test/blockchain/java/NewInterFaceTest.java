package test.blockchain.java;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.joda.time.base.BasePartial;
import org.junit.Test;

import test.blockchain.test.TestUtil;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.DTO.BaseParamDTO;
import com.blockchain.VO.AssetTransferFormVO;
import com.tencent.trustsql.sdk.exception.TrustSDKException;
import com.tencent.trustsql.sdk.util.HttpClientUtil;

public class NewInterFaceTest {

	private static final Logger log = Logger.getLogger(NewInterFaceTest.class);

	@Test
	public void test_asset_rec_sum_query() throws TrustSDKException, Exception {
		String submitString = TestUtil.generateAssetSumParam();
		log.debug(submitString);

		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_rec_sum_query_v1.cgi";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		log.debug(submitResultString);
	}

	@Test
	public void test_dam_asset_rec_detail_query() throws TrustSDKException, Exception {
		String submitString = TestUtil.generateAssetDetailParam();
		log.debug(submitString);

		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_rec_detail_query_v1.cgi";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		log.debug(submitResultString);
	}

	@Test
	public void test_dam_asset_rec_trans_sum_query() throws TrustSDKException, Exception {
		String submitString = TestUtil.generateTransSumlParam();
		log.debug(submitString);
		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_rec_trans_sum_query_v1.cgi";
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		log.debug(submitResultString);
	}

	@Test
	public void test_dam_asset_rec_trans_detail_query_() throws TrustSDKException, Exception {
		String submitString = TestUtil.generateTransDetailsParam();
		log.debug(submitString);
		/*
		 * {"date":"2018-06-01","chain_id":"ch1dcca23b3885045","page_no":1,"mch_id"
		 * :"gb433b4b8de489629","version":"1.0","page_limit":20,"asset_type":1,
		 * "mch_sign":
		 * "MEUCIQC+lIUEho26pdZt9Gfth1rQcHJFA2XiXN4czU8/U52KIQIgJHMAGbAeQQZ5PuCgEazmECpAIlZ1/d3n40tG8nMGkbA="
		 * ,
		 * "state":2,"ledger_id":"ldbcd23db6782e342","sign_type":"ECDSA","node_id"
		 * :"ndqszcawe6aw2abkiv","timestamp":1527846760,"trans_type":4}
		 */
		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_rec_trans_detail_query_v1.cgi";
		/*
		 * {"retcode":"0","retmsg":"OK","assets_list":[],"date":"","mch_id":
		 * "trust_dam","mch_sign":
		 * "MEUCIQD9+O6QDPetWVkpz+nq452vejZX9NhWWBwdkI8KjpnxOQIgRW94J660dfIWmcA4QBczMy/fLRr26gKjiuSmV0RpZLI="
		 * ,"sign_type":"ECDSA","version":"1.0"}
		 */
		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		log.debug(submitResultString);
	}

	@Test
	public void test_dam_asset_transfer_mid_apply_v1_cgi() throws TrustSDKException, Exception {
		AssetTransferFormVO assetTransferFormVO = new AssetTransferFormVO();
		assetTransferFormVO.setAmount(5L);
		assetTransferFormVO.setDstAccount(BaseParamDTO.second_user_account_address);
		assetTransferFormVO.setSrcAccount(BaseParamDTO.user_account_address);
		assetTransferFormVO.setFeeAccount(BaseParamDTO.third_user_account_address);

		assetTransferFormVO.setFeeAmount(5L);
		assetTransferFormVO.setSrcAsset("26aDRaBnj3VqFKoGdYz6i3Qb1PqZRBnPLvEqYkYnPfieUUh");
		
		TransferMidApply transferMidAppy = new TransferMidApply();
		
		assetTransferFormVO.setMultAsset(transferMidAppy);
		String submitString = TestUtil.generateDamAssetTransferMidAppy(assetTransferFormVO);
		log.debug(submitString);

		String submitUrl = "https://baas.trustsql.qq.com/cgi-bin/v1.0/dam_asset_multtransmid_apply_v1.cgi";

		String submitResultString = HttpClientUtil.post(submitUrl, submitString);
		log.debug(submitResultString);
	}

}
