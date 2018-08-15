package com.blockchain.DTO;


import org.apache.commons.codec.binary.Base64;

import org.apache.commons.codec.digest.DigestUtils;

public class BaseParamDTO {
	public static String mchId = "gb433b4b8de489629";
	public static String seqNo = "QHtMEDMBVN0nnsSRuUY" + System.currentTimeMillis();
	public static String create_user_publicKey = "ApC68FZmMEgDulnnyyOs8r6YZhI2sYvFLiPgR3hAm5cE";
	public static String create_user_privateKey = "WR083F5ueI+7C5VRwh0ykM2vVFcEhk4WCKH42yhZDHU=";
	public static String afterTrustSql = "MEQCIHM6Pid9bqzPMuSxDSjCroZXiBQ7xXCVYD6I2nYDtgaFAiAVdwKbehilwpyJiKMxq0ZcYCRQbe2VZUkgfo5K9894RQ==";
	public static String radomUserId = String.valueOf(Math.random());

	public static String user_public_key = "A3ysUQtO8vQ9Hvw43JBRPDNXv5IOZ6ACOVHOTo8z4848";
	public static String user_private_key = "1vmoaK5gFh83DeekkJK5InrK3Tdoirye9cBc2IQ1MIAo";
	public static String user_id = "weiqiang123";
	public static String user_account_address = "1PuSJUg9cwQxikAQbkuCkKGwPPaPtNENMs";

	public static String second_user_id = "weiqiang124";
	public static String second_user_public_key = "ArlVRtQWko4jEU4VDHvRCls5nIRf+IfwHuxEqv+WlQ2S";
	public static String second_user_private_key = "3oj3PfcQcQOV7r77cqIAvjXTzAAQAlCuqHGtGbeD7/E=";
	public static String second_user_account_address = "12UXDtKxSujmzek4GLtDZa7mmr9hkFy4dB";

	public static String third_user_id = "weiqiang125";
	public static String third_user_public_key = "AvTHGz78Ee5dlOAV/ULFg6oRFrz9XbcIi6Lb/TSa1Wzc";
	public static String third_user_private_key = "LtECQbyoNxkql4uJgY4forbKCoIn5a9GNN09SeuL22E=";
	public static String third_user_account_address = "16P6oH51PjDb79iMu1xYpYsqhXXhnEWmjv";

	public static String create_user_account_address = "19gnKJ5WKxJ6ifbzsAX37gRtB4nxFa5sY5";
	public static String version = "1.0";
	public static String signType = "ECDSA";
	public static String nodeId = "nd7pmrpira3p6qk55f";
	public static String chainId = "chkpmqytr367uwgfju";
	public static String leadgerId = "ldj9xey32i9mq76mvk";
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
