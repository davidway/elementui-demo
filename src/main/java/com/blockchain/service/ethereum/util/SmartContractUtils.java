package com.blockchain.service.ethereum.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;

public class SmartContractUtils {
	public static final Integer DECIMALS = 18;

	public static String genereateSignSmartContractMethodAndParam(String dstAccount, Long amount, String methodName) {
		List<Type> inputParameters = new ArrayList<>();
		List<TypeReference<?>> outputParameters = new ArrayList<>();

	
		Uint256 tokenValue = new Uint256(BigDecimal.valueOf(amount).multiply(BigDecimal.TEN.pow(DECIMALS)).toBigInteger());
		if (StringUtils.isNotBlank(dstAccount)) {
			Address dstAddress = new Address(dstAccount);
			inputParameters.add(dstAddress);
		}

		inputParameters.add(tokenValue);
		TypeReference<Bool> typeReference = new TypeReference<Bool>() {
		};
		outputParameters.add(typeReference);
		Function function = new Function(methodName, inputParameters, outputParameters);
		String data = FunctionEncoder.encode(function);
		return data;
	}

}
