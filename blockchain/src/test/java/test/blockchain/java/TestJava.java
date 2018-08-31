package test.blockchain.java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import test.blockchain.test.Utility;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.DTO.CrmServiceDTO;
import com.blockchain.exception.ServiceException;
import com.blockchain.util.ConfigUtils;
import com.blockchain.util.CrmUtils;

// 当然 你可以声明一个事务管理 每个单元测试都进行事务回滚 无论成功与否
public class TestJava {

	@Test
	public void test2() {
		String test=null;
			System.out.println(test.equals("test"));
	}

}
