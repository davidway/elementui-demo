package com.blockchain.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.blockchain.util.ConfigUtils;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value="userForm的json")
public class UserFormDTO  extends BaseDto{
	@ApiModelProperty(value="名称",required=true)
	@NotEmpty(message = "名称不能为空")
	private String name;
	@NotEmpty(message = "id不能为空")
	@ApiModelProperty(value="用户id",required=true)
	@Length(max=16,min=1,message="id的长度限制在1-16之间")
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
