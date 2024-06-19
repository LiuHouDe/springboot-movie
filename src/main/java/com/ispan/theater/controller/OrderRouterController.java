package com.ispan.theater.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import com.ispan.theater.repository.OrderRepository;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OrderRouterController {
	@Autowired
	OrderRepository orderRepository;
	
	@PostMapping("/order-redirect")
	@Transactional
	public String redirectFront(HttpServletRequest request) {
		if("1".equals(request.getParameter("RtnCode"))) {
			orderRepository.setOrderConditionByPaymentNo(request.getParameter("MerchantTradeNo"));
			orderRepository.setUserConsumptionECPay(request.getParameter("MerchantTradeNo"));
		}
		return "redirect:"+"https://starburst-cinema.xyz/order/ecpay-complete?MerchantTradeNo="+request.getParameter("MerchantTradeNo");
	}

//	@PostMapping("/ecpayResult")
//	public String test(HttpServletRequest request) {
//		System.out.println(request);
//		return "test";
//	}
	
}
