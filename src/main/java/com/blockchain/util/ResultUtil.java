package com.blockchain.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;

public class ResultUtil {

	private static final Logger log = Logger.getLogger(ResultUtil.class);

	public static void checkResultIfSuccess(String pos, String resultString) throws ServiceException {
		if (StringUtils.isBlank(resultString)) {
			throw new ServiceException().errorCode(StatusCode.SYSTEM_UNKOWN_ERROR).errorMessage("调用SDK网络失败，请检查网络");
		}
		JSONObject applyObject = JSONObject.parseObject(resultString);
		Integer retcode = applyObject.getInteger("retcode");

		if (retcode.equals(83590142)) {

			Integer errorCode = StatusCode.APPLY_THREAD_ERROR;
			throw new ServiceException().errorCode(errorCode).errorMessage(applyObject.getString("retmsg"));
		} else if (retcode != 0 && retcode.equals(83590142) == false) {
			Integer errorCode = StatusCode.SYSTEM_UNKOWN_ERROR;
			throw new ServiceException().pos(pos).errorCode(errorCode).errorMessage(applyObject.getString("retmsg"));
		} else {
			log.debug(pos + "成功！结果为" + applyObject.toJSONString());
		}
	}

	public static void checkSubmitResultIfSuccess(String pos, String submitParamString, String submitResultString) throws ServiceException {
		if (StringUtils.isBlank(submitResultString)) {
			throw new ServiceException().errorCode(StatusCode.SYSTEM_UNKOWN_ERROR).errorMessage("调用SDK网络失败，请检查网络");
		}

		JSONObject submitResultObject = JSONObject.parseObject(submitResultString);

		Integer retcode = submitResultObject.getInteger("retcode");
		if (retcode.equals(83590142)) {
			
			Integer errorCode = StatusCode.SUBMIT_THREAD_ERROR;
			throw new ServiceException().errorCode(errorCode).errorMessage(submitResultObject.getString("retmsg")).data(submitResultObject);
		
		} else if (retcode != 0 && retcode.equals(83590142) == false) {
			
			Integer errorCode = StatusCode.SYSTEM_UNKOWN_ERROR;
			throw new ServiceException().errorCode(errorCode).errorMessage(submitResultObject.getString("retmsg"));

	
		} else {
			log.debug(pos + "成功！结果为" + submitParamString);
		}
	}

}
