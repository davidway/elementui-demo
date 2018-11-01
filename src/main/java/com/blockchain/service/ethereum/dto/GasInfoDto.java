package com.blockchain.service.ethereum.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(description="gasInfo的dto",value="gasInfo的dto")
public class GasInfoDto {
	

	@NotEmpty(message="转出方不能为空")
		private String srcAccount;

	@NotEmpty(message="转入方不能为空")
		private String dstAccount;

		@NotNull(message="金额不能为空")
		private Long amount;
		@NotNull(message="方法类型不能为空")
		private Integer[] methodType;
		public String getSrcAccount() {
			return srcAccount;
		}
		public void setSrcAccount(String srcAccount) {
			this.srcAccount = srcAccount;
		}
		public String getDstAccount() {
			return dstAccount;
		}
		public void setDstAccount(String dstAccount) {
			this.dstAccount = dstAccount;
		}
		public Integer[] getMethodType() {
			return methodType;
		}
		public void setMethodType(Integer[] methodType) {
			this.methodType = methodType;
		}
		public Long getAmount() {
			return amount;
		}
		public void setAmount(Long amount) {
			this.amount = amount;
		}
		
		
		
}
