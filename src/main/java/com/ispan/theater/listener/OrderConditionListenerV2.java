package com.ispan.theater.listener;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.ispan.theater.service.OrderService;

@Component
public class OrderConditionListenerV2 implements ApplicationListener<OrderConditionEventV2> {
	@Autowired
	OrderService orderService;
	@Override
	public void onApplicationEvent(OrderConditionEventV2 event) {
		System.out.println("-----監聽到用戶編號"+event.getUserId()+"開始刪除未付款訂單-----");
		orderService.deleteOrderV2(event.getUserId());
		System.out.println("-----用戶編號"+event.getUserId()+"未付款訂單刪除完畢-----");
	}

}
