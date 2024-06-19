package com.ispan.theater.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderConditionPublisher {
	 @Autowired
	 private ApplicationEventPublisher applicationEventPublisher;
	 
	    public void publish(String message) {
	        // [2]使用publishEvent方法发布事件
	        applicationEventPublisher.publishEvent(new OrderConditionEvent(this, message));
	    }
	    
	    public void publishV2(Integer userId) {
	        // [2]使用publishEvent方法发布事件
	        applicationEventPublisher.publishEvent(new OrderConditionEventV2(this, userId));
	    }
	    
}
