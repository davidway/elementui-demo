package com.blockchain.service.tencent.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

public class MyHttpUtils {

	public static String getIp(HttpServletRequest request) {
		
		String serverName = request.getServerName(); // 服务器名
		String ipString="";
		try {
			InetAddress ip = InetAddress.getByName(serverName); // 先通过服务器名解析
			String ipAddr = request.getRemoteAddr(); // 获取服务器IP
			ipString= ip == null ? ipAddr : ip.getHostAddress(); // 如服务器名解析不出则使用remoteAddr

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ipString;
	}

}
