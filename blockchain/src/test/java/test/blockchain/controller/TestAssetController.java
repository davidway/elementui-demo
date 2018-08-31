package test.blockchain.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.DTO.AssetFormDTO;
import com.blockchain.DTO.BaseParamDTO;
import com.blockchain.util.AssetUtil;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.exception.TrustSDKException;

/**
 * @author zhzh 2015-4-7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:src/main/resources/application.xml", "file:src/main/resources/spring-mvc.xml" })
// 当然 你可以声明一个事务管理 每个单元测试都进行事务回滚 无论成功与否
public class TestAssetController {
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
	
	private HttpSession session;
	
	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(this.wac).build();
		 session = (MockHttpSession) mockMvc.perform(post("/login")
	                .param("username", "15812345678")
	                .param("password","e10adc3949ba59abbe56e057f20f883e"))
	                .andReturn().getRequest().getSession();;
	}

	@Test
	public void testIssue() throws Exception {
		String amount = "4000";
		String unit = "wsy";
		JSONObject content = JSONObject.parseObject("{\"wsy2\":\"test\"}");
		String sourceId = "test123";
		String createUserAccountAddress = "1LbJjxYAK2ceZksgKxepzCA9skyHuZ5BvE";
		String test="1234";
		JSONObject json = new JSONObject();
		json.put("amount", amount);
		json.put("unit", unit);
		json.put("sourceId", sourceId);
		json.put("content", content.toJSONString());
		json.put("createUserAccountAddress", createUserAccountAddress);
		System.out.println(json.toJSONString());
		mockMvc.perform((post("/asset/issue.action").content(json.toJSONString()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testSettle() throws Exception {
		String amount = "90000";
		String ownerAccountString = BaseParamDTO.user_account_address;
		String userPrivateKey = BaseParamDTO.user_private_key;

		mockMvc.perform((post("/asset/settle.action").param("amount", amount).param("ownerAccount", ownerAccountString).param("userPrivateKey", userPrivateKey))).andExpect(status().isOk()).andDo(
				print());
	}

	@Test
	public void testTransfer() throws Exception {
		String amount = "400";
		String srcAccount = BaseParamDTO.user_account_address;
		String userPrivateKey = BaseParamDTO.user_private_key;
		JSONObject content = JSON.parseObject("{a:1}");
		String dstAccount = BaseParamDTO.second_user_account_address;
		String sourceId = "1234567890123456";

		mockMvc.perform(
				(post("/asset/transfer.action").param("content", content.toJSONString()).param("sourceId", sourceId).param("amount", amount).param("srcAccount", srcAccount).param("dstAccount",
						dstAccount).param("userPrivateKey", userPrivateKey))).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testIssueSubmit() throws Exception {
		String signStr = "";
		String transactionId = "";
		String assetId = "";

		signStr = "[{\"id\":\"1\",\"account\":\"12gRQNAefaADjQeEfw9gHhpqXJG47F9y1C\",\"sign_str\":\"58dc80a605a812ed507f5bf857f8793a94cdf1fe7225cbf3ec2711254be40a2a\"}]";
		transactionId = "201806071460074599";
		assetId = "26aBYUgtTCYbhCCMj1TMLeGXoZUwao9hXDJxPpAe8dgm9Dr";

		JSONObject json = new JSONObject();
		json.put("assetId", assetId);
		json.put("signStr", signStr);
		json.put("transactionId", transactionId);

		mockMvc.perform((post("/asset/issueSubmit.action").content(json.toJSONString()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))).andExpect(status().isOk()).andDo(
				MockMvcResultHandlers.print());
		/*
		 * mockMvc.perform((post("/asset/issueSubmit.action").param("assetId",
		 * assetId).param("signStr", signStr).param("transactionId",
		 * transactionId))) .andExpect(status().isOk()).andDo(print());
		 */
	}

	@Test
	public void testTransferSubmit() throws Exception {
		/*
		 * paramMap.put("transaction_id",
		 * applyResultJsonObject.getString("transaction_id"));
		 * 
		 * 
		 * JSONArray signList = applyResultJsonObject.getJSONArray("sign_list");
		 */
		String transactionId = "201805301460058467";
		String signStr = "[{\"id\":\"1\",\"sign_str\":\"3203bf3f91b599c230d43a4170d0ad47167ff97ab79dbfd6c8d879ee8a1a9c9b\",\"account\":\"1LbJjxYAK2ceZksgKxepzCA9skyHuZ5BvE\"}]";
		String assetId = "26aJkCvhHgxPmNdMWcqXLhwNAvFKzG3Xw9Jo7QPayjbCgDF";
		String userFly1PrivateKey = "t3rLRIAGF85q/RMCz5HY6F5dOUq7rc+BZ69QEiLUVWw=";
		mockMvc.perform((post("/asset/transferSubmit.action").param("transactionId", transactionId).param("signList", signStr).param("assetId", assetId).param("userPrivateKey", userFly1PrivateKey)))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testSettleSubmit() throws Exception {
		/*
		 * JSONArray signList =
		 * applyResultJsonObject.getJSONArray("sign_str_list");
		 * paramMap.put("transaction_id",
		 * applyResultJsonObject.getString("transaction_id"));
		 */
		String amount = "1";
		String ownerAccount = BaseParamDTO.user_account_address;
		String userPrivateKey = BaseParamDTO.user_private_key;

		mockMvc.perform((post("/asset/settleSubmit.action").param("amount", amount).param("ownerAccount", ownerAccount).param("srcPrivateKey", userPrivateKey))).andExpect(status().isOk()).andDo(
				print());
	}

	@Test
	public void test() throws TrustSDKException {

		String createUserPrivateKey = "fIABpgkpbq9Uxwlqm6M48peKJV7+DkP3ETC/1PVRic0=";
		String createUserPublicKey = "A/bIbCv3WcHWOYGnKxOxPypPfGUg19PtPFVaIcYDEFn8";
		boolean isTest = TrustSDK.checkPairKey(createUserPrivateKey, createUserPublicKey);
		System.out.println(isTest);
	}

}
