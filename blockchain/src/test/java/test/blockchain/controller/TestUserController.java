package test.blockchain.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.DTO.BaseParamDTO;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.exception.TrustSDKException;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:src/main/resources/application.xml", "file:src/main/resources/spring-mvc.xml" })
// 当然 你可以声明一个事务管理 每个单元测试都进行事务回滚 无论成功与否

public class TestUserController {
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void testAccountQuery() throws Exception {
		String ownerUid = BaseParamDTO.user_id;
		String pageNo = "1";
		String state = "0";
		String pageLimit = "20";
		
		mockMvc.perform((post("/user/accountQuery.action").param("unit", "wsy1527856929420").param("ownerUid", ownerUid).param("pageNo", pageNo).param("state", state).param("pageLimit", pageLimit)))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testTransQuery() throws Exception {
		String srcAccount = BaseParamDTO.user_account_address;
		String month = "201805";
		String pageNo = "1";

		String pageLimit = "20";
		mockMvc.perform((post("/user/transQuery.action").param("srcAccount", srcAccount).param("month", month).param("pageLimit", pageLimit).param("pageNo", pageNo).contentType(MediaType.APPLICATION_JSON))).andExpect(status().isOk())
				.andDo(print());
	}
	

	@Test
	public void testAddUserHasBaseAndHostAccount() throws Exception {
		String name = "ces2104";
		String id = "ces2109";
		
		JSONObject json = new JSONObject();
		json.put("name", name);
		json.put("id", id);
		mockMvc.perform((post("/user/addUserHasBaseAndHostAccount.action").content(json.toJSONString()).contentType(MediaType.APPLICATION_JSON))).andExpect(status().isOk()).andDo(print());
	}
	@Test
	public void testAddUseHasBaseAccount() throws Exception {
		String name = "ces2106";
		String id = "ces2106";
		
		JSONObject json = new JSONObject();
		json.put("name", name);
		json.put("id", id);
		mockMvc.perform((post("/user/addUserHasBaseAccount.action").content(json.toJSONString()).contentType(MediaType.APPLICATION_JSON))).andExpect(status().isOk()).andDo(print());
	}
	@Test
	public void testAddUserHostAccount() throws Exception {
		
		String id = "ces2106";
		String name = "ces2104";
		JSONObject json = new JSONObject();
		json.put("name", name);
		json.put("id", id);
		mockMvc.perform((post("/user/addUserHostAccount.action").content(json.toJSONString()).contentType(MediaType.APPLICATION_JSON))).andExpect(status().isOk()).andDo(print());
	}

	

	@Test
	public void testGenerateKey() throws Exception {

		mockMvc.perform((post("/user/generatePariKey.action").contentType(MediaType.APPLICATION_JSON))).andExpect(status().isOk()).andDo(print());
	}
	
	@Test
	public void test2() throws UnsupportedEncodingException, TrustSDKException{
		String privateKey = "fIABpgkpbq9Uxwlqm6M48peKJV7+DkP3ETC/1PVRic0=";
		String publicKey = "A/bIbCv3WcHWOYGnKxOxPypPfGUg19PtPFVaIcYDEFn8";
		String afterTrustSql = TrustSDK.signString(privateKey, "Tencent TrustSQL".getBytes("UTF-8"), false);
		System.out.println(afterTrustSql);
	}
}
