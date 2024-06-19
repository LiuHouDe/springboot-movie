package com.ispan.theater.listener;

import org.springframework.context.ApplicationEvent;

public class OrderConditionEventV2 extends ApplicationEvent{
	
	private Integer userId;
	
    public OrderConditionEventV2(Object source, Integer userId) {
		super(source);
		this.userId = userId;
	}
    
	public Integer getUserId() {
		return userId;
	}
}
