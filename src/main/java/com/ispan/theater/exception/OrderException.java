package com.ispan.theater.exception;

@SuppressWarnings("serial")
public class OrderException extends RuntimeException {

	private Integer errorCode;

	private String errorMessage;

	public OrderException() {
		super();
	}

	public OrderException(Integer errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
