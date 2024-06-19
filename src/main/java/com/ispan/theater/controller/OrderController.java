package com.ispan.theater.controller;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.theater.dao.OrderDaoImpl;
import com.ispan.theater.domain.Order;
import com.ispan.theater.dto.InsertOrderDTO;
import com.ispan.theater.exception.OrderException;
import com.ispan.theater.listener.OrderConditionPublisher;
import com.ispan.theater.repository.OrderRepository;
import com.ispan.theater.repository.TicketRepository;
import com.ispan.theater.service.CinemaService;
import com.ispan.theater.service.ECPayService;
import com.ispan.theater.service.LinePayService;
import com.ispan.theater.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="/order")
@CrossOrigin
public class OrderController {
	@Autowired
	OrderService orderService;
	@Autowired
	TicketRepository ticketRepository;
	@Autowired
	CinemaService cinemaService;
	@Autowired
	LinePayService linePayService;
	@Autowired
	ECPayService ecPayService;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	OrderConditionPublisher orderConditionPublisher;
	@Autowired
	OrderDaoImpl orderDaoImpl;
	@GetMapping("/movie/order")
//	@Cacheable(cacheNames = "Order",key="#id")
	public String getOrderById(@RequestParam(name="id")Integer id) {
		JSONObject orderJson=new JSONObject();
		Order order=orderService.findOrderByOrderId(id);
		if(order==null) {
			return orderJson.put("success",false).toString();
		}
//		System.out.println(order.getOrderDetails());
		return orderJson.put("id",order.getId()).put("createDate", order.getCreateDate()).put("modifyDate", order.getModifyDate()).
				put("orderAmount", order.getOrderAmount()).put("movieId", order.getMovie().getId()).put("userId", order.getUser().getId()).put("success", true).toString();
	}
	
	@GetMapping("/movie/orders")
//	@Cacheable(cacheNames = "Orders")
	public String getOrders() {
		List<Order> orders=orderService.getOrders();
		System.out.println(orders);
		JSONObject orderJson=new JSONObject();
		if(orders.size()==0) {
			return orderJson.put("success",false).toString();
		}
		JSONArray array=new JSONArray();
		orders.stream().forEach((order)->{
			JSONObject temp=new JSONObject();
			temp.put("id", order.getId()).put("createDate", order.getCreateDate()).
			put("modifyDate",order.getModifyDate()).put("orderAmount", order.getOrderAmount());
			array.put(temp);
		});
		return orderJson.put("orders", array).put("success", true).toString();
	}
	
	@GetMapping("/movie/findAllCinema")
	public String findAllCinema(HttpServletRequest request) {
		System.out.println("Authorization="+request.getHeader("Authorization"));
		return new JSONObject().put("allCinemaName", cinemaService.findAllCinemaName()).toString(); 
	}
	@GetMapping("/movie/findCinemaData")
	public String findCinemaData() {
		return new JSONObject().put("allCinemaName", cinemaService.findAllCinema()).toString();
	}
	
	@GetMapping("/movie/findMovie")
	public String findMovie(@RequestParam("cinemaId")Integer cinemaId) {
		return new JSONObject().put("movies",orderService.findMovieByScreening(cinemaId)).toString();
	}
	
	@GetMapping("/movie/dates")
	public String findScreeningByDate(@RequestParam("cinemaId")Integer cinemaId,@RequestParam("movieId")Integer movieId) {
		return new JSONObject().put("dates", orderService.findScreeningByDate(cinemaId,movieId)).toString();
	}
	
	@GetMapping("/movie/times")
	public String findScreeningByTime(@RequestParam("cinemaId")Integer cinemaId,@RequestParam("Date")String Date,@RequestParam("movieId")Integer movieId) {
		return new JSONObject().put("times", orderService.findScreeningByTime(cinemaId,Date,movieId)).toString();
	}
	
	@GetMapping("/movie/tickets")
	public String findTickets(@RequestParam("screeningId")Integer screeningId) {
		orderConditionPublisher.publish(":場次"+screeningId+"被查詢");
		return new JSONObject().put("tickets", orderService.ticketList(screeningId)).toString();
	}
	
	@PostMapping("/movie/booking")
	public String booking(@RequestBody InsertOrderDTO insertOrderDto) {
		System.out.println(insertOrderDto);
		String json=null;
		synchronized (this) {
			json = orderService.createOrder(insertOrderDto);
		}
		System.out.println(json);
		return json;
	}

	@GetMapping("/movie/confirm/linePayConfirm")
	public String LinePayConfirm(@RequestParam("transactionId")String transactionId,@RequestParam("orderId")Integer orderId) {
		System.out.println(transactionId+","+orderId);
		return orderService.orderCompleted(transactionId,orderId);
	}
	
	@GetMapping("/movie/ecPayConfirm")
	public String ECPayConfirm(@RequestParam("MerchantTradeNo")String merchantTradeNo) {
		return new JSONObject().put("Order",orderRepository.orderCompletedByECPay(merchantTradeNo)).toString();
	}
	
	
	@GetMapping("/movie/getOrder")
	public String getOrder(@RequestParam("userId")Integer userId,@RequestParam("page")Integer page) {
		orderConditionPublisher.publishV2(userId);
		return orderService.getOrder(userId, page);
	}
	
	@GetMapping("/movie/getOrderDetail")
	public String getOrderDetail(@RequestParam("orderId")Integer orderId) {
		return orderService.getOrderDetail(orderId);
	}
	
	@GetMapping("/movie/getOrderBackStage")
	public String getOrderBackStage(@RequestParam("page")Integer page) {
		return orderService.getOrderBackStage(page);
	}
	
	@GetMapping("/movie/deleteOrder")
	public String refund(@RequestParam("orderId")Integer orderId) {	return orderService.refund(orderId);	}
	
	
//	@GetMapping("/movie/test")
//	@Transactional
//	public String test(@RequestParam("orderId")String orderId) {
//		orderRepository.setUserConsumptionECPay(orderId);
//		return "Success";
//	}
	
	@PostMapping("/movie/getOrderCondition")
	public String test(@RequestBody Map<String,String> requestParameters) {
		return orderDaoImpl.multiConditionFindMovie(requestParameters);
	}
	
//	@GetMapping("/movie/test")
//	public String test() {
//		throw new OrderException(901,"場次已結束！無法退款！");
//	}
}


