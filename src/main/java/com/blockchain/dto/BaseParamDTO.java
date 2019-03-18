package com.blockchain.dto;


import org.apache.commons.codec.binary.Base64;

import org.apache.commons.codec.digest.DigestUtils;

public class BaseParamDTO {
	public static String mchId = "";
	public static String seqNo = "" + System.currentTimeMillis();
	public static String create_user_publicKey = "";
	public static String create_user_privateKey = "";
	public static String afterTrustSql = "";
	public static String radomUserId = "";

	public static String user_public_key = "";
	public static String user_private_key = "";
	public static String user_id = "";
	public static String user_account_address = "";

	public static String second_user_id = "";
	public static String second_user_public_key = "+IfwHuxEqv+WlQ2S";
	public static String second_user_private_key = "/E=";
	public static String second_user_account_address = "";

	public static String third_user_id = "";
	public static String third_user_public_key = "/ULFg6oRFrz9XbcIi6Lb/TSa1Wzc";
	public static String third_user_private_key = "=";
	public static String third_user_account_address = "";

	public static String create_user_account_address = "";
	public static String version = "1.0";
	public static String signType = "";
	public static String nodeId = "";
	public static String chainId = "";
	public static String leadgerId = "";
	public static String coin_privateKey = Base64.encodeBase64String(DigestUtils.sha256(leadgerId));
	/**
	 * 发行币种的银行的账户
	 */
	public static String coin_Account = "12gRQNAefaADjQeEfw9gHhpqXJG47F9y1C";
	/**
	 * coinId
	 * */

	public static String user_coin_assetId = "27tJTnoxAjGRLrVh5U5gvZSHZDLTgPkidaSCneFuaDE2YMV";

}
