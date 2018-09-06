package com.blockchain.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.dto.CrmResultSet;
import com.blockchain.dto.CrmServiceDTO;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;

public class CrmUtils {
	static Logger logger = Logger.getLogger(CrmUtils.class);

	public static void checkAuth() throws ServiceException {
		CrmServiceDTO crmServiceDto = CrmServiceDTO.getSingleton();
		crmServiceDto.check();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

		String ip = MyHttpUtils.getIp(request);
		String serverId = crmServiceDto.getServerId().trim();
		String serverCode = crmServiceDto.getServerCode().trim();
		String configPath = crmServiceDto.getCrmBaseUrls().trim();
		String[] ipsPath = configPath == null ? null : configPath.split(";");

		CrmResultSet result;
		if (ipsPath != null && ipsPath[0].intern() != "") {
			result = checkData(ip, serverId, serverCode, ipsPath);
			switch (result) {
			case NOT_AUTHORITY:
				throw new ServiceException().pos("配置信息文件鉴权").errorCode(StatusCode.AUTHORITY_ERROR).errorMessage(StatusCode.AUTHORITY_ERROR_MESSAGE);
			case TIME_OUT_ERROR:
				throw new ServiceException().pos("配置信息文件鉴权").errorCode(StatusCode.TIME_OUT).errorMessage(StatusCode.TIME_OUT_MESSAGE);
			case URL_NOT_EXISTS:
				throw new ServiceException().pos("配置信息文件鉴权").errorCode(StatusCode.URL_NOT_EXISTS).errorMessage(StatusCode.URL_NOT_EXISTS_MESSAGE);
			case SUCCESS:
				return;
			default:
				throw new ServiceException().pos("配置信息文件鉴权").errorCode(StatusCode.SYSTEM_UNKOWN_ERROR).errorMessage(StatusCode.SYSTEM_UNKOWN_ERROR_MESSAGE);
			}
		} else {
			throw new ServiceException().errorCode(StatusCode.PARAM_ERROR).errorMessage(StatusCode.PARAM_ERROR_MESSAGE);
		}

	}

	private static CrmResultSet checkData(String ip, String serverId, String serverCode, String[] ipsPath) {
		CrmResultSet crmResultSet = null;

		for (int i = 0; i < ipsPath.length; i++) {
			String ipPath = ipsPath[i];
			String path = ipPath + "/code?type=1&codeid=" + serverId + "&codestr=" + serverCode;

			if (ip != null && !ip.isEmpty()) {
				path = ipPath + "/code?type=2&codeid=" + serverId + "&codestr=" + serverCode + "&ip=" + ip;
			}
			try {
				String jsonData = getJsonString(path);
				JSONObject jobj = JSONObject.parseObject(jsonData);
				int result = jobj.getInteger("result");
				int data = jobj.getInteger("data");
				int SUCCESS_STATUS = 1;

				if (result == SUCCESS_STATUS && data == SUCCESS_STATUS) {
					return CrmResultSet.SUCCESS;
				}else{
					return CrmResultSet.NOT_AUTHORITY;
				}
			} catch (SocketTimeoutException e) {
				crmResultSet = crmResultSet.TIME_OUT_ERROR;
				logger.error(e);
				continue;
			} catch (IOException e) {
				crmResultSet = crmResultSet.URL_NOT_EXISTS;
				continue;
			} catch (Exception e) {
				crmResultSet = crmResultSet.ERROR;
				logger.error(e);
				continue;
			}
		}
		return crmResultSet;
	}

	public static String getJsonString(String urlPath) throws Exception {
		URL url = new URL(urlPath);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// 设置超时时间为3秒，避免程序僵死而不能继续往下执行
		connection.setConnectTimeout(3000); // 连接主机的超时时间
		connection.setReadTimeout(3000); // 从主机读取数据的超时时间
		connection.connect();
		InputStream inputStream;
		inputStream = connection.getInputStream();
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		String str = null;
		StringBuffer sb = new StringBuffer();
		while ((str = bufferedReader.readLine()) != null) {
			sb.append(str);
		}
		reader.close();
		connection.disconnect();
		return sb.toString();

	}

}
