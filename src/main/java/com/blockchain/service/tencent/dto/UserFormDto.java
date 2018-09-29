package com.blockchain.service.tencent.dto;

import org.hibernate.validator.constraints.NotEmpty;

import com.blockchain.validate.group.EthValidateGroup;
import com.blockchain.validate.group.TencentValidateGroup;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="userForm的json")
public class UserFormDto {
	@ApiModelProperty(value="名称",required=true)
	@NotEmpty(message = "名称不能为空",groups=TencentValidateGroup.class)
	private String name;
	@NotEmpty(message = "id不能为空",groups=TencentValidateGroup.class)
	@ApiModelProperty(value="用户id",required=true)
	private String id;

	@NotEmpty(message = "密码",groups=EthValidateGroup.class)
	@ApiModelProperty(value="密码",required=true)
	private String password;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserFormDto [name=" + name + ", id=" + id + ", password=" + password + "]";
	}



}
