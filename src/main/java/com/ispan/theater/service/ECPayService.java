package com.ispan.theater.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ispan.theater.domain.Order;
import com.ispan.theater.util.DatetimeConverter;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import ecpay.payment.integration.domain.DoActionObj;
import ecpay.payment.integration.domain.QueryTradeInfoObj;

@Service
public class ECPayService {
	public String ecpayCheckout(Order order,Integer quantity) {
		AllInOne all=new AllInOne("");
		AioCheckOutALL obj = new AioCheckOutALL();
//		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
		obj.setMerchantTradeNo(order.getPaymentNo());
		obj.setTotalAmount(order.getOrderAmount().toString().substring(0,order.getOrderAmount().toString().indexOf("."))+"");
		obj.setMerchantTradeDate(DatetimeConverter.createSqlDatetimeECPay(order.getCreateDate()));
		obj.setTradeDesc("此次共買了"+quantity+"張電影票");
		obj.setItemName("電影票");
		obj.setReturnURL("https://backendapp-l6hua254tq-de.a.run.app/ecpayResult");
		obj.setOrderResultURL("https://backendapp-l6hua254tq-de.a.run.app/order-redirect");
//		obj.setOrderResultURL("http://httpbin.org/post");

		obj.setNeedExtraPaidInfo("Y");
		String form = all.aioCheckOut(obj, null);
		System.out.println(form);
		String temp =form.substring(0, form.indexOf("<script"))+"</form>";
		System.out.println(temp);
		return temp;
	}

	public String postQueryTradeInfo(String merchantTradeNo){
		AllInOne all=new AllInOne("");
		QueryTradeInfoObj obj = new QueryTradeInfoObj();
		obj.setMerchantTradeNo(merchantTradeNo);
		return all.queryTradeInfo(obj);
	}
	
	public String postDoAction(String merchantTradeNo,String totalAmount,String tradeNo){
		AllInOne all=new AllInOne("");
		DoActionObj obj = new DoActionObj();
		obj.setMerchantTradeNo(merchantTradeNo);
		obj.setTotalAmount(totalAmount);
		obj.setTradeNo(tradeNo);
		obj.setAction("C");
		return all.doAction(obj);
	}
	
}
