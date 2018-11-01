package com.blockchain.service.ethereum.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.web3j.tx.ChainId;

public class ChainUtil {

	static Logger logger = Logger.getLogger(ChainUtil.class);
	static Properties prop = new Properties();

	public static byte getChainId() {
		return Byte.valueOf(getByte(getProperties("chainId").trim()));
	}

	private static byte getByte(String properties) {
		/*
		 * # public static final byte NONE = -1; # public static final byte
		 * MAINNET = 1; # public static final byte EXPANSE_MAINNET = 2; # public
		 * static final byte ROPSTEN = 3; # public static final byte RINKEBY =
		 * 4; # public static final byte ROOTSTOCK_MAINNET = 30; # public static
		 * final byte ROOTSTOCK_TESTNET = 31; # public static final byte KOVAN =
		 * 42; # public static final byte ETHEREUM_CLASSIC_MAINNET = 61; #
		 * public static final byte ETHEREUM_CLASSIC_TESTNET = 62;
		 */
		switch (properties) {
		case "NONE":

			return ChainId.NONE;
		case "MAINNET":

			return ChainId.MAINNET;
		case "EXPANSE_MAINNET":

			return ChainId.EXPANSE_MAINNET;
		case "ROPSTEN":

			return ChainId.ROPSTEN;
		case "ROOTSTOCK_MAINNET":

			return ChainId.ROOTSTOCK_MAINNET;

		case "ROOTSTOCK_TESTNET":

			return ChainId.ROOTSTOCK_TESTNET;
		case "ETHEREUM_CLASSIC_MAINNET":

			return ChainId.ETHEREUM_CLASSIC_MAINNET;
		case "KOVAN":

			return ChainId.KOVAN;
		case "ETHEREUM_CLASSIC_TESTNET":

			return ChainId.ETHEREUM_CLASSIC_TESTNET;

		default:
			return ChainId.NONE;
		}
	}

	private static String getProperties(String name) {
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
}
