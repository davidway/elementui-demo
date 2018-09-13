package com.blockchain.assets.swagger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 读取配置文件
 * 
 * @author zhaojing
 * 
 */
public class SwaggerProperties {
	static Logger logger = Logger.getLogger(SwaggerProperties.class);
	static Properties prop = new Properties();
	public static String isOpen;

	public static final String OPEN = "1";
	public static final String CLOSE = "0";
	static InputStream in=null;
	static {
		try {
			 in = SwaggerProperties.class.getClassLoader()
					.getResourceAsStream("../main-resources/swagger.properties");
			prop.load(in);
			isOpen = getProperty(prop, "is_open");
		}
		catch (IOException e) {
			logger.error(e);
		}
		finally{
			try {
				in.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

	private static String getProperty(Properties prop, String key) {
		String value = prop.getProperty(key);
		if (value != null) {
			return value = value.trim();
		}
		return value;
	}

	/** Prevent instantiation */
	private SwaggerProperties() {
	}

}
