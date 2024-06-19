package com.ispan.theater.linepay;

public class RefundData {
	
	String refundAmount;
	
	public RefundData(String refundAmount) {
		super();
		this.refundAmount = refundAmount;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	
}
