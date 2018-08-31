package com.blockchain.DTO;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="userForm的json")
public class UserFormDTO {
	@ApiModelProperty(value="名称",required=true)
	@NotEmpty(message = "名称不能为空")
	private String name;
	@NotEmpty(message = "id不能为空")
	@ApiModelProperty(value="用户id",required=true)
	private String id;


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

	@Override
	public String toString() {
		return "UserForm [name=" + name + ", id=" + id + "]";
	}



}
