package com.ispan.theater.dto;

public class PaymentRequest {
    private Double total;
    private String currency;

    public PaymentRequest() {
    }

    public PaymentRequest(Double total, String currency) {
        this.total = total;
        this.currency = currency;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

