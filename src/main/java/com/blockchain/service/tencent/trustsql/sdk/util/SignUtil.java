package com.blockchain.service.tencent.trustsql.sdk.util;

/**
 * Project Name:trustsql-membercenter
 * File Name:SignUtil.java
 * Package Name:com.tencent.trustsql.membercenter.util
 * Date:2017年9月5日下午8:13:33
 * Copyright (c) 2017, Tencent All Rights Reserved.
 *
*/

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.beanutils.PropertyUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

/**
 * ClassName:SignUtil <br/>
 * Date:     2017年9月5日 下午8:13:33 <br/>
 * @author   ronyyang
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class SignUtil {
	
	public static String genSignSrc(Object obj) throws Exception {
		Map<String, Object> map = new TreeMap<String, Object>();
		PropertyDescriptor pd[] = PropertyUtils.getPropertyDescriptors(obj);
		for(int i = 0; i < pd.length; i++) {
			if(pd[i].getReadMethod() != null && pd[i].getWriteMethod() != null) {
				Object value = PropertyUtils.getProperty(obj, pd[i].getName());
				if("sign".equals(pd[i].getName())) {
					continue;
				}
				if(value != null) {
					map.put(pd[i].getName(), value);
				}
			}
		}
		return getCheckString(map);
	}
	
	public static String getCheckString(Map<String, Object> map) throws Exception {
		StringBuffer content = new StringBuffer();
		boolean first = true;
		Set<String> keySet = map.keySet();
		Iterator<String> iter = keySet.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (map.get(key) == null) {
				continue;
			}
			Object v = map.get(key);
			if (map.get(key) instanceof String) {
				String s = (String) map.get(key);
				if (s.length() <= 0) {
					continue;
				}
			} else if (!isBaseDataType(map.get(key).getClass())) {
				JSONObject js = JSONObject.parseObject(map.get(key).toString());
				String json = js.toString();
				v = json;
			}

			if (!first) {
				content.append("&").append(key).append("=").append(v);
			} else {
				content.append(key).append("=").append(v);
			}
			first = false;
		}
		return content.toString();
	}
	
	private static boolean isBaseDataType(Class clazz) throws Exception {
		return (clazz.equals(String.class) || clazz.equals(Integer.class) || clazz.equals(Byte.class)
				|| clazz.equals(Long.class) || clazz.equals(Double.class) || clazz.equals(Float.class)
				|| clazz.equals(Character.class) || clazz.equals(Short.class) || clazz.equals(BigDecimal.class)
				|| clazz.equals(BigInteger.class) || clazz.equals(Boolean.class) || clazz.equals(Date.class) || clazz
					.isPrimitive());
	}

}

