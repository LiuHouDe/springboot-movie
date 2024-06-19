package com.ispan.theater.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ECPayServiceTest {
	@Autowired
	ECPayService ecPayService;
//	@Test	
//	public void test1() {
//		String result=ecPayService.postQueryTradeInfo();
//		System.out.println(result);
//		System.out.println(result.substring(result.indexOf("&TradeNo")+9, result.indexOf("&",result.indexOf("&TradeNo")+1)));
//		System.out.println(result.substring(result.indexOf("&amount")+8, result.indexOf("&",result.indexOf("&amount")+1)));
//	}
//	@Test
//	public void test2() {
		//成功時RtnMsg=Succeeded、RtnCode=1
        //MerchantID=3002607&Merchant=3002607&MerchantTradeNo=8cb2cdc074334fc7aa11&TradeNo=2405221032549143&RtnCode=1&RtnMsg=Succeeded.
//		String result=ecPayService.postDoAction();
//		System.out.println(ecPayService.postDoAction());
//		System.out.println(result.substring(result.indexOf("&RtnCode")+9, result.indexOf("&",result.indexOf("&RtnCode")+1)));
//	}
}

