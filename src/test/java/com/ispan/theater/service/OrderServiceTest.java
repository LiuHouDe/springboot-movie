package com.ispan.theater.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ispan.theater.domain.Order;
import com.ispan.theater.exception.OrderException;
import com.ispan.theater.repository.OrderRepository;

@SpringBootTest
public class OrderServiceTest {
	@Autowired
	OrderService os;
	@Autowired
	OrderRepository or;
	ExecutorService executor=Executors.newFixedThreadPool(100);
	CountDownLatch countDownLatch=new CountDownLatch(100);
	
//	@Test
//	@Transactional
	public void find() {
		Order order=os.findOrderByUserId(3);
//		System.out.println(order);
	}
	
//	@Test
	public void lockTest() throws InterruptedException {
		for(int i=0;i<10;i++) {
			executor.execute(()->{
			Order order=os.findOrderByUserId(3);
			synchronized (this) {
				if(order.getOrderAmount()==1) {
					return;
				}
				os.updeteOrderAmount(order);
				countDownLatch.countDown();
			}
			});
		}
		countDownLatch.await();
		executor.shutdown();
	}
//	@Test
//	@Transactional
	public void test1() {
//		System.out.println(os.findOrder(11).toString());
		//1043
		System.out.println(new JSONObject().put("Order", or.orderCompleted(72)).toString());
	}
	
//	@Test
	public void test2() {
		or.getOrderByUser(3,(2-1)*10).forEach(map->System.out.println(map.entrySet()));
	}
	
//	@Test
//	public void test3() {
//		System.out.println(or.orderTotalByUserId(3).get("order_total"));
//	}
	
	
//	  @Test
		public void test4() {
		  os.refund(1061);
		}
		
//	   @Test
//	   @Transactional
	   public void test5() {
		or.setUserConsumptionECPay("aaa4ff026f254301a5c6");
	   }
	   
//	   @Test
	   public void test6() {
		   throw new OrderException(HttpStatus.SC_BAD_REQUEST,"已有座位被售出，請重新選擇！");
	   }
	   
//	   @Test
	   public void test7() {
		   System.out.println(os.getOrderBackStage(1));
	   }
	   
	   @Test
	   public void test8() {
		   System.out.println(or.findOrderConditionCurrentDate(212));
	   }	
		
}


