package com.ispan.theater.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.ispan.theater.service.OrderService;

@Component
public class OrderConditionListener implements ApplicationListener<OrderConditionEvent> {
	@Autowired
	OrderService orderService;
	@Override
	public void onApplicationEvent(OrderConditionEvent event) {
		System.out.println("-----監聽到事件"+event.getMessage()+"開始刪除未付款訂單-----");
		orderService.deleteOrder();
		System.out.println("-----事件"+event.getMessage()+"未付款訂單刪除完畢-----");
	}

}
