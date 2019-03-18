package com.blockchain.vo;

public class UserAccountFormVO {
	String public_key;
	String user_id;
	public String getPublic_key() {
		return public_key;
	}
	public void setPublic_key(String public_key) {
		this.public_key = public_key;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	@Override
	public String toString() {
		return "UserAccountForm [public_key=" + public_key + ", user_id=" + user_id + "]";
	}
	
	
}
