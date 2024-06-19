package com.ispan.theater.service;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LinePayServiceTest {
	@Autowired
	LinePayService linePayService;
//    @Test
	public void test1() {
//		System.out.println(linePayService.test().get("info").get("paymentUrl").get("web"));
		System.out.println(linePayService.confirm("2024051702120888110", null));
	}
	
//	@Test
	public void test2() {
		//1165已退款 0000成功
		System.out.println(linePayService.refund("2024052202124471410").get("returnCode"));
		//[0;39m{returnCode=0000, returnMessage=Success., info={refundTransactionId=2024052302124506311, refundTransactionDate=2024-05-22T15:31:56Z}}
	}
	

}
