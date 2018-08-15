package test.blockchain.service;

import java.io.FileNotFoundException;

import javax.annotation.Resource;






import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Log4jConfigurer;

import com.blockchain.DTO.BaseParamDTO;
import com.blockchain.VO.AccountQueryFormVO;
import com.blockchain.VO.AssetFormVO;
import com.blockchain.VO.AssetTransferFormVO;
import com.blockchain.controller.AssetController;
import com.blockchain.exception.ServiceException;
import com.blockchain.service.AssetService;
import com.blockchain.util.AssetUtil;
import com.blockchain.util.BeanUtils;
import com.tencent.trustsql.sdk.exception.TrustSDKException;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:src/main/resources/application.xml", "file:src/main/resources/spring-mvc.xml" })
public class ServiceTest {

	@Resource
	AssetService assetService;

	@Test
	public void testIssue() throws Exception {
		AssetUtil assetUtil = new AssetUtil();
		AssetFormVO assetFormVO = new AssetFormVO();
		// amount=3000&unit=wsy&sourceId=23123&createUserAccountAddress=1LbJjxYAK2ceZksgKxepzCA9skyHuZ5BvE
		assetFormVO.setAmount(9999);
		assetFormVO.setUnit("wsy");
		assetFormVO.setSourceId("wsy1234");
		assetFormVO.setContent("{test:1004}");
		assetFormVO.setCreateUserAccountAddress(BaseParamDTO.user_account_address);

		assetService.issue(assetFormVO);

	}
	
	@Test
	public void testTansferToMyself() throws ServiceException, TrustSDKException, Exception{
		AssetTransferFormVO myselfTransFerForm = new AssetTransferFormVO();
		myselfTransFerForm.setAmount("1");
	
		myselfTransFerForm.setSrcAccount(BaseParamDTO.user_account_address);
		myselfTransFerForm.setDstAccount(BaseParamDTO.user_account_address);
		myselfTransFerForm.setUserPrivateKey(BaseParamDTO.user_private_key);
		
	}
	
	@Test
	public void testSeachAmount() throws ServiceException, TrustSDKException, Exception{
		AssetTransferFormVO assetTransferFormVO = new AssetTransferFormVO();
		assetTransferFormVO.setSrcAccount(BaseParamDTO.user_account_address);
	

		
	}
	
	 final static  Logger logger  =  LoggerFactory.getLogger("issueLogger");
	 
	@Test
	public void test_dam_asset_transfer_mid_apply_v1() throws FileNotFoundException{
		 Log4jConfigurer.initLogging("classpath:log4j.properties");
		Integer id=3;
		Object symbol=5;
		logger.info("Processing trade with id: {} and symbol : {} ",id,symbol);
	}


}