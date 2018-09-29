package com.blockchain.service.ethereum.dto;

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

public class EthereumConfig {
	static Logger logger = Logger.getLogger(EthereumConfig.class);
	static Properties prop = new Properties();
	private String contractAddress;
	private String serviceSystemUrl;
	private String issueActionName;
	private String transferActionName;
	private String settleActionName;

	public String getContractAddress() {
		this.contractAddress = getProperties("contractAddress");
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		setProperties("contractAddress", contractAddress);
		this.contractAddress = contractAddress;
	}

	private void setProperties(String key, String name) {
		OutputStream output = null;
		try {
			String path = new ClassPathResource("../main-resources/ethereum-config.properties").getFile().getAbsolutePath();

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
			String path = new ClassPathResource("../main-resources/ethereum-config.properties").getFile().getAbsolutePath();
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

	public String getServiceSystemUrl() {
		this.serviceSystemUrl = getProperties("serviceSystemUrl");
		return serviceSystemUrl;
	}

	public String getIssueActionName() {
		this.issueActionName = getProperties("issueActionName");
		return issueActionName;
	}

	public String getTransferActionName() {
		this.transferActionName = getProperties("transferActionName");
		return transferActionName;
	}

	public String getSettleActionName() {
		this.settleActionName = getProperties("settleActionName");
		return settleActionName;
	}

	public String getRpcUrl() {
		return getProperties("rpcUrl").trim();
	}

}
