package com.blockchain.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.blockchain.exception.ParameterErrorException;

public class ValidatorUtil {

	public static void validate(BindingResult bindingResult) throws ParameterErrorException {
		boolean first =true;
		StringBuffer sb = new StringBuffer("");
		if (bindingResult.hasErrors()) {

			for (ObjectError objectError : bindingResult.getAllErrors()) {
				String string = objectError.getDefaultMessage();
				if (first){
					sb.append(string);
					first=false;
				}else{
					sb.append(","+string);
				}
			}
		}
		if ( StringUtils.isNotBlank(sb)){
			throw new ParameterErrorException(sb);
		}
		
	}

}
