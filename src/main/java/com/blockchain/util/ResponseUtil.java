package com.blockchain.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.service.tencent.dto.ConfigPropertiesFormDto;
import com.blockchain.service.tencent.vo.PhpSystemJsonContentVo;

public class ResponseUtil {
	static DealJsonUtil dealJsonUtil = new DealJsonUtil();

	public static void echo(HttpServletResponse response, String obj) {
		try {
			response.setContentType("application/json;charset=UTF-8");  
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(dealJsonUtil.nullToStringJson(JSONObject.parseObject(obj)) );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void echo(HttpServletResponse response, JSONObject obj) {
		try {
			response.setContentType("application/json;charset=UTF-8");  
			response.setCharacterEncoding("utf-8");
			// response.getWriter().print(obj.toString());//这种方式当参数的value为空时，会丢失对应的key值
			response.getWriter().print(dealJsonUtil.nullToStringJson(obj));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void successEcho(HttpServletResponse response,PhpSystemJsonContentVo phpSystemJsonContentVo, Object  o) {
		String jsonString="";
		
		phpSystemJsonContentVo.setData( o);
		jsonString = JSON.toJSONString(phpSystemJsonContentVo);
		ResponseUtil.echo(response, jsonString);
	}
}
