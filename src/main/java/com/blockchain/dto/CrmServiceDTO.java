package com.blockchain.dto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;

public class CrmServiceDTO {
	static Logger logger = Logger.getLogger(CrmServiceDTO.class);
	static Properties prop = new Properties();

	private String crmBaseUrl;
	private String serverCode;
	private String serverId;
	
	final static CrmServiceDTO crmServiceDTO = new CrmServiceDTO();

	private CrmServiceDTO() {
	}

	public String getServerCode() {
		this.serverCode = getProperties("server_code");
		return serverCode;
	}

	

	public String getServerId() {
		this.serverId = getProperties("server_id");
		return serverId;
	}

	// 使用单例模式
	public static CrmServiceDTO getSingleton() {
		return crmServiceDTO;
	}

	private void setProperties(String key, String name) {
		OutputStream output = null;
		try {
			String path = new ClassPathResource("/crm_config.properties").getFile().getAbsolutePath();

			output = new FileOutputStream(path);
			prop.setProperty(key, name);
			prop.store(output, "configTest");
		} catch (IOException io) {
			io.printStackTrace();
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String getProperties(String name) {
		try {
			String path = new ClassPathResource("/crm_config.properties").getFile().getAbsolutePath();
			InputStream in = new FileInputStream(path);
			prop.load(in);
			return getProperty(prop, name);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String getProperty(Properties prop, String key) {
		String value = prop.getProperty(key);
		if (value != null) {
			return value = value.trim();
		}
		return value;
	}

	public String getCrmBaseUrls() {
		this.crmBaseUrl = getProperties("crm_url");
		return crmBaseUrl;
	}

	public void check() throws ServiceException {
		if (StringUtils.isBlank(this.getCrmBaseUrls()) ) {
			throw new ServiceException().pos("配置信息是否为空").errorCode(StatusCode.CONFIG_NOT_SET).errorMessage(StatusCode.CONFIG_NOT_SET_MESSAGE);
		}
		if (StringUtils.isBlank(this.getServerCode()) ) {
			throw new ServiceException().pos("配置信息是否为空").errorCode(StatusCode.CONFIG_NOT_SET).errorMessage(StatusCode.CONFIG_NOT_SET_MESSAGE);
		}
		if (StringUtils.isBlank(this.getServerId()) ) {
			throw new ServiceException().pos("配置信息是否为空").errorCode(StatusCode.CONFIG_NOT_SET).errorMessage(StatusCode.CONFIG_NOT_SET_MESSAGE);
		}
	}
}
