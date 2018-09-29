package com.blockchain.service.tencent.util;


import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class DealJsonUtil {
	private final   Logger log = Logger.getLogger(this.getClass());
	/***   
     * @param Json 
     * @return StringJson 将json对象中为null的值转换成""，并转换成json字符串返回
     */  
	public  String nullToStringJson(JSONObject json){	
		String beforeJosn;
		String afterJosn = null;		
		beforeJosn = JSON.toJSONString(json, SerializerFeature.WriteMapNullValue);
	
		 if(beforeJosn.contains(":null")){
			 afterJosn = beforeJosn.replaceAll(":null", ":\"\"");
		 }else{
			 afterJosn = beforeJosn;
		 }
		
		return afterJosn;		
	}	
}
