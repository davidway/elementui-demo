package com.blockchain.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class  JSONUtils{

	public static void prettyPrint(String applyResultString) {
	
		String result = JSON.toJSONString(JSON.parse(applyResultString),true);
		System.out.println(result);
	}

}
