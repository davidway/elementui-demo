package com.blockchain.util;



import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.validate.group.ValidateGroup;

public class ValidatorUtil {
	
	//自动validate
	public static void validate(BindingResult bindingResult) throws ServiceException {
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
			
			throw new ServiceException().data(sb).pos("检查参数是否为空").errorCode(StatusCode.PARAM_ERROR).errorMessage(StatusCode.PARAM_ERROR_MESSAGE);
		}
	}
	
	//代码形式的validate
		public  <T> void validate(T t) throws ServiceException{
 		    StringBuffer sb = new StringBuffer();
		    
	      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	 
	      Validator validator = factory.getValidator();

	      Set<ConstraintViolation<T>> globalValidate = validator.validate(t);

	      
	      
	      if (globalValidate.size() > 0) {
	          for (ConstraintViolation<T> violation : globalValidate) {
	             sb.append(violation.getMessage());
	          }
	      } 
	      
	      if ( StringUtils.isNotBlank(sb)){
				
				throw new ServiceException().data(sb).pos("检查参数是否为空").errorCode(StatusCode.PARAM_ERROR).errorMessage(StatusCode.PARAM_ERROR_MESSAGE);
			}
		}
	//代码形式的validate
	public  <T> void validate(T t,Class<? extends ValidateGroup> validateGroupClass) throws ServiceException{
	    StringBuffer sb = new StringBuffer();
	    
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
 
      Validator validator = factory.getValidator();

      Set<ConstraintViolation<T>> constraintViolations = validator.validate(t,validateGroupClass);
      

      if (constraintViolations.size() > 0) {
          for (ConstraintViolation<T> violation : constraintViolations) {
             sb.append(violation.getMessage());
          }
      } 
      
      
      if ( StringUtils.isNotBlank(sb)){
			
			throw new ServiceException().data(sb).pos("检查参数是否为空").errorCode(StatusCode.PARAM_ERROR).errorMessage(StatusCode.PARAM_ERROR_MESSAGE);
		}
	}

}
