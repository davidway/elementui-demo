package test.blockchain.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:src/main/resources/application.xml", "file:src/main/resources/spring-mvc.xml" })
// 当然 你可以声明一个事务管理 每个单元测试都进行事务回滚 无论成功与否
public class TestConfigController {
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = webAppContextSetup(this.wac).build();
	}



	@Test
	public void testConfigAdd() throws Exception {
		/*
		 * mchId=asd++&createUserPublicKey=A17kNxaSaGWZeXlQNf4n3bjSfZhYVu0+
		 * PtJiLUzedhZs
		 * &createUserPrivateKey=/Do7BTvGPGgW8hUCQMXPU1ZtQRHO9EMit4zWOI8BKpo
		 * =&chainId
		 * =ch1dcca23b3885045&ledgerId=ldbcd23db6782e342&nodeId=ndqszcawe6aw2abkiv
		 */
		String mchId = "gb433b4b8de489629";
		String createUserPrivateKey = "/7LrlyQsavUUqYnIjP7BY74PjzuWTKUuOI7NKvSFLl8=";
		String createUserPublicKey = "AgZJ/40XPd44GKrHvSolLgUhXlfSmA8Lgg/anCuiG3LB";
		String chainId = "ch1dcca23b3885045";
		String ledgerId = "ldbcd23db6782e342";
		String nodeId = "ndqszcawe6aw2abkiv";
		mockMvc.perform(
				(post("/configProperties/add.action").param("createUserPrivateKey", createUserPrivateKey).param("mchId", mchId).param("createUserPublicKey", createUserPublicKey).param("chainId",
						chainId).param("ledgerId", ledgerId).param("nodeId", nodeId).contentType(MediaType.APPLICATION_JSON))).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testConfigGet() throws Exception {

		mockMvc.perform((post("/configProperties/get.action"))).andExpect(status().isOk()).andDo(print());
	}

}
