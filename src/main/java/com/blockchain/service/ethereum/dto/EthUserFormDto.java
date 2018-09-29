package com.blockchain.service.ethereum.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.wordnik.swagger.annotations.ApiParam;

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
