package com.ispan.theater.dto;



public class ErrorDto {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    
	public ErrorDto() {
		super();
	}

	public ErrorDto(String timestamp, int status, String error, String message) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "ErrorDto [timestamp=" + timestamp + ", status=" + status + ", error=" + error + ", message=" + message
				+ "]";
	}
	
}
