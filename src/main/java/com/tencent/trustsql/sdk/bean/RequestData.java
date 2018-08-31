/**
 * Project Name:trustsql-membercenter
 * File Name:RequestData.java
 * Package Name:com.tencent.trustsql.membercenter.entity
 * Date:2017年9月5日下午4:46:36
 * Copyright (c) 2017, Tencent All Rights Reserved.
 *
*/

package com.tencent.trustsql.sdk.bean;


/**
 * ClassName:RequestData <br/>
 * Date: 2017年9月5日 下午4:46:36 <br/>
 * 
 * @author ronyyang
 * @version
 * @since JDK 1.7
 * @see
 */
public class RequestData {

	private String mch_id;
	private String product_code;
	private String seq_no;
	private String type;
	private long time_stamp;
	private String sign;
	private String req_data;
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

	public String getSeq_no() {
		return seq_no;
	}

	public void setSeq_no(String seq_no) {
		this.seq_no = seq_no;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getTime_stamp() {
		return time_stamp;
	}

	public void setTime_stamp(long time_stamp) {
		this.time_stamp = time_stamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getReq_data() {
		return req_data;
	}

	public void setReq_data(String req_data) {
		this.req_data = req_data;
	}

}
