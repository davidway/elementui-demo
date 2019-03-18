package com.blockchain.exception;

public class ParameterErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParameterErrorException(StringBuffer sb) {
		super(sb.toString());
	}

	public ParameterErrorException(String string) {
		super(string.toString());
	}

}
