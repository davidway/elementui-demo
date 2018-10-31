package com.blockchain.util;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

public abstract class BeanUtils extends org.springframework.beans.BeanUtils {

	public static void copyProperties(Object source, Object target) throws BeansException {
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");
		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						// 这里判断以下value是否为空 当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等
						if (value != null) {
							Method writeMethod = targetPd.getWriteMethod();
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						}
					} catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target", ex);
					}
				}
			}
		}
	}
  
	public static Map<String, Object> compareProperties(Object source, Object target) throws BeansException {
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");
		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		Map<String, Object> map = new HashMap<String, Object>();
		for (PropertyDescriptor targetPd : targetPds) {
			PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
			if (sourcePd != null && sourcePd.getReadMethod() != null) {
				try {
					Method readMethod = sourcePd.getReadMethod();
					if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
						readMethod.setAccessible(true);
					}
					Object s_value = readMethod.invoke(source);
					Object t_value = readMethod.invoke(target);
					if(s_value instanceof String && s_value.toString() == ""){
						s_value = null;
					}
					if (s_value != null) {
						if (!s_value.equals(t_value)) {
							Map<String, Object> map1 = new HashMap<String, Object>();
							map1.put("old", t_value==null?"空":t_value);
							map1.put("new", s_value);
							map.put(targetPd.getName(), map1);
						}
					}
				} catch (Throwable ex) {
					throw new FatalBeanException("Could not copy properties from source to target", ex);
				}
			}
		}
		return map;
	}
	
	public static void setValue(Object target, String name, Object value) throws BeansException {
		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		for (PropertyDescriptor targetPd : targetPds) {
			if(targetPd.getName().equals(name)){
				Method writeMethod = targetPd.getWriteMethod();
				if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
					writeMethod.setAccessible(true);
				}
				try {
					writeMethod.invoke(target, value);
				} catch (Throwable ex) {
					throw new FatalBeanException("Could not copy setValue into target", ex);
				}
			}
		}
	}

	public static void saveProperties(Properties p, String name) {
		try {
			URL url = BeanUtils.class.getClassLoader().getResource("/");
			FileOutputStream s = new FileOutputStream(url.getPath()+File.separator+name);
			p.store(s, null);
			s.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static Properties loadProperties(String name) {
		InputStream is = BeanUtils.class.getClassLoader().getResourceAsStream(name);
		Properties p = new Properties();
		try {
			if(is!=null){
				p.load(is);
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

}