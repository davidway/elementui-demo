package com.blockchain.util;

import com.blockchain.exception.ErrorMessage;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.exception.TrustSDKException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

public class ConfigUtils {
	static Logger logger = Logger.getLogger(ConfigUtils.class);
	static Properties prop = new Properties();
	private String createUserPrivateKey;
	private String createUserPublicKey;
	private String chainId;
	private String nodeId;
	private String ledgerId;
	private String mchId;
	private String coin_privateKey;
	private static final ConfigUtils config = new ConfigUtils();

	public static ConfigUtils getSingleton() {
		return config;
	}

	public String getCreateUserPrivateKey() {
		this.createUserPrivateKey = getProperties("createUserPrivateKey");

		return this.createUserPrivateKey;
	}

	public void setCreateUserPrivateKey(String createUserPrivateKey) {
		config.createUserPrivateKey = createUserPrivateKey;

		setProperties("createUserPrivateKey", createUserPrivateKey);
	}

	public String getCreateUserPublicKey() {
		this.createUserPublicKey = getProperties("createUserPublicKey");

		return this.createUserPublicKey;
	}

	public void setCreateUserPublicKey(String createUserPublicKey) {
		config.createUserPrivateKey = this.createUserPrivateKey;

		setProperties("createUserPublicKey", createUserPublicKey);
	}

	public String getChainId() {
		config.chainId = getProperties("chainId");

		return this.chainId;
	}

	public void setChainId(String chainId) {
		config.chainId = chainId;

		setProperties("chainId", chainId);
	}

	public String getNodeId() {
		config.nodeId = getProperties("nodeId");

		return this.nodeId;
	}

	public void setNodeId(String nodeId) {
		config.nodeId = nodeId;

		setProperties("nodeId", nodeId);
	}

	public String getLedgerId() {
		config.ledgerId = getProperties("ledgerId");

		return config.ledgerId;
	}

	public void setLedgerId(String ledgerId) {
		config.ledgerId = ledgerId;
		setCoin_privateKey(config.ledgerId);
		setProperties("ledgerId", ledgerId);
	}

	public String getMchId() {
		config.mchId = getProperties("mchId");

		return config.mchId;
	}

	public void setMchId(String mchId) {
		config.mchId = mchId;

		setProperties("mchId", mchId);
	}

	public String getCoin_privateKey() {
		config.coin_privateKey = getProperties("coin_privateKey");

		return config.coin_privateKey;
	}

	public void setCoin_privateKey(String ledgerId) {
		config.coin_privateKey = Base64.encodeBase64String(DigestUtils.sha256(ledgerId));

		setProperties("coin_privateKey", this.coin_privateKey);
	}

	public static void check() throws ServiceException {
		StringBuffer lessName = new StringBuffer("");
		String chainId = config.getChainId();
		String coin_privateKey = config.getCoin_privateKey();
		String createUserPrivateKey = config.getCreateUserPrivateKey();
		String createUserPublicKey = config.getCreateUserPublicKey();
		String ledgerId = config.getLedgerId();
		String mchId = config.getMchId();
		String nodeId = config.getNodeId();
		
		if (StringUtils.isBlank(chainId)) {
			lessName.append("配置文件中的联盟链id不能为空，");
		}
		if (StringUtils.isBlank(coin_privateKey)) {
			lessName.append("配置文件中的账本id尚未被解析，");
		}
		if (StringUtils.isBlank(createUserPrivateKey)) {
			lessName.append("配置文件中的用户私钥不能为空，");
		}
		if (StringUtils.isBlank(createUserPublicKey)) {
			lessName.append("配置文件中的用户公钥不能为空，");
		}
		if (StringUtils.isBlank(ledgerId)) {
			lessName.append("配置文件中的账本id不能为空，");
		}
		if (StringUtils.isBlank(mchId)) {
			lessName.append("配置文件中的机构id不能为空，");
		}
		if (StringUtils.isBlank(nodeId)) {
			lessName.append("配置文件中的节点id不能为空，");
		}
		if (StringUtils.isNotBlank(lessName.toString())) {
			String string = new ErrorMessage(Integer.valueOf(StatusCode.CONFIG_NOT_SET), StatusCode.CONFIG_NOT_SET_MESSAGE, lessName.toString()).toJsonString();
			throw new ServiceException(string);
		}
		try {
			TrustSDK.checkPairKey(createUserPrivateKey, createUserPublicKey);
		} catch (TrustSDKException e) {
			String string = new ErrorMessage(Integer.valueOf(StatusCode.PAIR_KEY_ERROR), StatusCode.PAIR_KEY_ERROR_MESSAGE, lessName.toString()).toJsonString();
			throw new ServiceException(string);
		}
	}

	private void setProperties(String key, String name) {
		OutputStream output = null;
		try {
			String path = new ClassPathResource("/config.properties").getFile().getAbsolutePath();

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
			String path = new ClassPathResource("/config.properties").getFile().getAbsolutePath();
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
}
