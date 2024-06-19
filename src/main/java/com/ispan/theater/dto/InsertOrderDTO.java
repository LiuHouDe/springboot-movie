package com.ispan.theater.dto;

import java.util.List;

public class InsertOrderDTO {

	private List<Integer> ticketId;
	private Integer userId;
	private Integer movieId;
	private String paymentOptions;
	
	public InsertOrderDTO() {
		super();
	}

	public List<Integer> getTicketId() {
		return ticketId;
	}

	public void setTicketId(List<Integer> ticketId) {
		this.ticketId = ticketId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getMovieId() {
		return movieId;
	}

	public void setMovieId(Integer movieId) {
		this.movieId = movieId;
	}

	public String getPaymentOptions() {
		return paymentOptions;
	}

	public void setPaymentOptions(String paymentOptions) {
		this.paymentOptions = paymentOptions;
	}

	@Override
	public String toString() {
		return "InsertOrderDTO [ticketId=" + ticketId + ", userId=" + userId + ", movieId=" + movieId
				+ ", paymentOptions=" + paymentOptions + "]";
	}

}
