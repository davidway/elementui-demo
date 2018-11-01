package com.blockchain.service.ethereum.vo;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;


@ApiModel(description="以太坊余额返回",value="以太坊余额返回json数据")
public class EthAndTokenAssetVo {
	@ApiModelProperty("以太币余额")
	private String ethereumBalance;

	@ApiModelProperty("代币余额")
	private String tokenBalance;
	public String getEthereumBalance() {
		return ethereumBalance;
	}
	public void setEthereumBalance(String ethereumBalance) {
		this.ethereumBalance = ethereumBalance;
	}
	public String getTokenBalance() {
		return tokenBalance;
	}
	public void setTokenBalance(String tokenBalance) {
		this.tokenBalance = tokenBalance;
	}
	
	
}
