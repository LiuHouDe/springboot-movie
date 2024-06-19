package com.ispan.theater.listener;

import org.springframework.context.ApplicationEvent;

public class OrderConditionEvent extends ApplicationEvent {
	
	private String message;	
	
	private Integer userId;

	public OrderConditionEvent(Object source, String message) {
		super(source);
		this.message=message;
	}

	public OrderConditionEvent(Object source, Integer userId) {
		super(source);
		this.userId = userId;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getMessage() {
        return message;
    }

}
