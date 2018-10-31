/**
 * Project Name:trustsql-membercenter
 * File Name:AccountCert.java
 * Package Name:com.tencent.trustsql.membercenter.entity
 * Date:2017年9月5日下午2:29:45
 * Copyright (c) 2017, Tencent All Rights Reserved.
 *
*/

package com.tencent.trustsql.sdk.bean;

/**
 * ClassName:AccountCert <br/>
 * Date: 2017年9月5日 下午2:29:45 <br/>
 * 
 * @author ronyyang
 * @version
 * @since JDK 1.7
 * @see
 */
public class AccountCert {

	String mch_id; // mch_id 机构号
	String product_code; // product_code 产品号
	private String user_id;// Fuser_id_crypted 业务方 userId
	private String account_address;// Faccount_address_crypted
	private String public_key;// Fpublic_key_crypted
	private String begin_time;
	private String end_time;
	private String state;
	private int page;
	private int limit;
	
	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getAccount_address() {
		return account_address;
	}

	public void setAccount_address(String account_address) {
		this.account_address = account_address;
	}

	public String getPublic_key() {
		return public_key;
	}

	public void setPublic_key(String public_key) {
		this.public_key = public_key;
	}
	public String getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
