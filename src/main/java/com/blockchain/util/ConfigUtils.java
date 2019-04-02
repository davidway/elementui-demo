package com.blockchain.util;

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

import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.tencent.trustsql.sdk.TrustSDK;
import com.tencent.trustsql.sdk.exception.TrustSDKException;

@Deprecated
/**
 * 
 * 版权所有：2019-微三云 
 * 项目名称：blockchain     
 *  
 * 类描述：  
 * 类名称：com.blockchain.util.ConfigUtils       
 * 创建人：weekend   
 * 创建时间：2019年3月29日 上午10:40:01     
 * 修改人：  
 * 修改时间：2019年3月29日 上午10:40:01     
 * 修改备注：已经改为ConfigDto，由Saas平台下 由应用系统（客户端）发送请求配置信息   
 * @version   V1.0
 */
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
	private String host;

	public String getCreateUserPrivateKey() {
		this.createUserPrivateKey = getProperties("createUserPrivateKey");

		return this.createUserPrivateKey;
	}

	public void setCreateUserPrivateKey(String createUserPrivateKey) {
		this.createUserPrivateKey = createUserPrivateKey;

		setProperties("createUserPrivateKey", createUserPrivateKey);
	}

	public String getCreateUserPublicKey() {
		this.createUserPublicKey = getProperties("createUserPublicKey");

		return this.createUserPublicKey;
	}

	public void setCreateUserPublicKey(String createUserPublicKey) {
		this.createUserPublicKey = createUserPublicKey;

		setProperties("createUserPublicKey", createUserPublicKey);
	}

	public String getChainId() {
		this.chainId = getProperties("chainId");

		return this.chainId;
	}

	public void setChainId(String chainId) {
		this.chainId = chainId;

		setProperties("chainId", chainId);
	}

	public String getNodeId() {
		this.nodeId = getProperties("nodeId");

		return this.nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;

		setProperties("nodeId", nodeId);
	}

	public String getLedgerId() {
		this.ledgerId = getProperties("ledgerId");

		return this.ledgerId;
	}

	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
		setCoin_privateKey(this.ledgerId);
		setProperties("ledgerId", ledgerId);
	}

	public String getMchId() {
		this.mchId = getProperties("mchId");

		return this.mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;

		setProperties("mchId", mchId);
	}

	public String getCoin_privateKey() {
		this.coin_privateKey = getProperties("coin_privateKey");

		return this.coin_privateKey;
	}

	public void setCoin_privateKey(String ledgerId) {
		this.coin_privateKey = Base64.encodeBase64String(DigestUtils.sha256(ledgerId));

		setProperties("coin_privateKey", this.coin_privateKey);
	}

	public static void check() throws ServiceException {
		ConfigUtils configUtils = new ConfigUtils();
		String chainId = configUtils.getChainId();
		// String coin_privateKey = configUtils.getCoin_privateKey();
		String createUserPublicKey = configUtils.getCreateUserPublicKey();
		String createUserPrivateKey = configUtils.getCreateUserPrivateKey();
		// String ledgerId = configUtils.getLedgerId();
		String nodeId = configUtils.getNodeId();
		String mchId = configUtils.getMchId();
		StringBuffer lessName = new StringBuffer();

		if (StringUtils.isBlank(chainId)) {
			lessName.append("配置文件中的联盟链id不能为空，");
		}
		/*
		 * if (StringUtils.isBlank(coin_privateKey)) {
		 * lessName.append("配置文件中的账本id尚未被解析，"); }
		 */
		if (StringUtils.isBlank(createUserPrivateKey)) {
			lessName.append("配置文件中的用户私钥不能为空，");
		}
		if (StringUtils.isBlank(createUserPublicKey)) {
			lessName.append("配置文件中的用户公钥不能为空，");
		}
		/*
		 * if (StringUtils.isBlank(ledgerId)) {
		 * lessName.append("配置文件中的账本id不能为空，"); }
		 */
		if (StringUtils.isBlank(mchId)) {
			lessName.append("配置文件中的机构id不能为空，");
		}
		if (StringUtils.isBlank(nodeId)) {
			lessName.append("配置文件中的节点id不能为空，");
		}
		if (StringUtils.isNotBlank(lessName.toString())) {

			throw new ServiceException().errorCode(StatusCode.CONFIG_NOT_SET).errorMessage(StatusCode.CONFIG_NOT_SET_MESSAGE);
		}
		try {
			TrustSDK.checkPairKey(createUserPrivateKey, createUserPublicKey);
		} catch (TrustSDKException e) {

			throw new ServiceException().errorCode(StatusCode.PAIR_KEY_ERROR).errorMessage(StatusCode.PAIR_KEY_ERROR_MESSAGE);
		}
	}

	private void setProperties(String key, String name) {
		OutputStream output = null;
		try {
			String path = new ClassPathResource("../main-resources/config.properties").getFile().getAbsolutePath();

			output = new FileOutputStream(path);
			prop.setProperty(key, name);
			prop.store(output, "configTest");
		} catch (IOException io) {
			logger.error(io);
			io.printStackTrace();
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					logger.error("IO錯誤{}", e);
					e.printStackTrace();
				}
			}
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					logger.error("IO錯誤{}", e);
					e.printStackTrace();
				}
			}
		}
	}

	private String getProperties(String name) {
		InputStream in = null;
		String result = "";
		try {
			String path = new ClassPathResource("../main-resources/config.properties").getFile().getAbsolutePath();
			in = new FileInputStream(path);
			prop.load(in);
			result = getProperty(prop, name);
			return result;
		} catch (IOException io) {
			logger.error(io);
			io.printStackTrace();
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("IO錯誤{}", e);
					e.printStackTrace();
				}
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("IO錯誤{}", e);
					e.printStackTrace();
				}
			}
		}
		return result;

	}

	private static String getProperty(Properties prop, String key) {
		String value = prop.getProperty(key);
		if (value != null) {
			return value = value.trim();
		}
		return value;
	}

	public void setHost(String host) {
		this.host = host;

		setProperties("host", host);

	}

	public String getHost() {
		this.host = getProperties("host");

		return this.host;
	}

}
