package com.blockchain.service.ethereum.dto;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="EthUserFormDto的json")
public class EthUserFormDto {

	@ApiModelProperty(value="密码",required=true)
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserFormDTO [password=" + password + ", getPassword()=" + getPassword() + "]";
	}


	




}
